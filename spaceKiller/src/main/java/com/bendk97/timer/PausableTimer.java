/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.timer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Executes tasks in the future on the main loop thread.
 *
 * @author Nathan Sweet
 */
public class PausableTimer {
    private static final Array<PausableTimer> instances = new Array<>(1);
    private static TimerThread thread;
    static private final int CANCELLED = -1;
    private final Array<Task> tasks = new Array<>(false, 8);


    /**
     * Timer instance for general application wide usage. Static methods on {@link PausableTimer} make convenient use of this instance.
     */
    private static PausableTimer instance = new PausableTimer();

    static public PausableTimer instance() {
        if (instance == null) {
            instance = new PausableTimer();
        }
        return instance;
    }

    private PausableTimer() {
        start();
    }

    /**
     * Schedules a task to occur once as soon as possible, but not sooner than the start of the next frame.
     */
    private Task postTask(Task task) {
        return scheduleTask(task, 0, 0, 0);
    }

    /**
     * Schedules a task to occur once after the specified delay.
     */
    private void scheduleTask(Task task, float delaySeconds) {
        scheduleTask(task, delaySeconds, 0, 0);
    }

    /**
     * Schedules a task to occur once after the specified delay and then a number of additional times at the specified
     * interval.
     */
    private Task scheduleTask(Task task, float delaySeconds, float intervalSeconds, int repeatCount) {
        if (task.repeatCount != CANCELLED)
            throw new IllegalArgumentException("The same task may not be scheduled twice.");
        task.executeTimeMillis = System.nanoTime() / 1000000 + (long) (delaySeconds * 1000);
        task.intervalMillis = (long) (intervalSeconds * 1000);
        task.repeatCount = repeatCount;
        synchronized (this) {
            tasks.add(task);
        }
        wake();

        return task;
    }

    public static void pause() {
        synchronized (instances) {
            instances.removeValue(instance(), true);
            thread.pause();
        }
    }

    public static void resume() {
        synchronized (instances) {
            instances.add(instance());
            thread.resume();
        }
    }

    /**
     * Starts the timer if it was stopped.
     */
    public void start() {
        synchronized (instances) {
            if (instances.contains(this, true)) return;
            instances.add(this);
            if (thread == null) thread = new TimerThread();
            wake();
        }
    }

    /**
     * Stops the timer, tasks will not be executed and time that passes will not be applied to the task delays.
     */
    public void stop() {
        synchronized (instances) {
            instances.removeValue(this, true);
            if (thread != null) {
                thread.dispose();
                thread = null;
            }
        }
    }

    /**
     * Cancels all tasks.
     */
    public void clear() {
        synchronized (this) {
            for (int i = 0, n = tasks.size; i < n; i++)
                tasks.get(i).cancel();
            tasks.clear();
        }
    }

    /**
     * Returns true if the timer has no tasks in the queue. Note that this can change at any time. Synchronize on the timer
     * instance to prevent tasks being added, removed, or updated.
     */
    public boolean isEmpty() {
        synchronized (this) {
            return tasks.size == 0;
        }
    }

    private long update(long timeMillis, long waitMillisecond) {
        long waitMillis = waitMillisecond;
        synchronized (this) {
            for (int i = 0, n = tasks.size; i < n; i++) {
                Task task = tasks.get(i);
                if (task.executeTimeMillis > timeMillis) {
                    waitMillis = Math.min(waitMillis, task.executeTimeMillis - timeMillis);
                    continue;
                }
                if (task.repeatCount != CANCELLED) {
                    if (task.repeatCount == 0) task.repeatCount = CANCELLED;
                    task.app.postRunnable(task);
                }
                if (task.repeatCount == CANCELLED) {
                    tasks.removeIndex(i);
                    i--;
                    n--;
                } else {
                    task.executeTimeMillis = timeMillis + task.intervalMillis;
                    waitMillis = Math.min(waitMillis, task.intervalMillis);
                    if (task.repeatCount > 0) task.repeatCount--;
                }
            }
        }
        return waitMillis;
    }

    /**
     * Adds the specified delay to all tasks.
     */
    private void delay(long delayMillis) {
        synchronized (this) {
            for (int i = 0, n = tasks.size; i < n; i++) {
                Task task = tasks.get(i);
                task.executeTimeMillis += delayMillis;
            }
        }
    }

    private static void wake() {
        synchronized (instances) {
            instances.notifyAll();
        }
    }

    /**
     * Schedules a task on {@link #instance}.
     *
     * @see #postTask(Task)
     */
    static public Task post(Task task) {
        return instance().postTask(task);
    }

    /**
     * Schedules a task on {@link #instance}.
     *
     * @see #scheduleTask(Task, float)
     */
    static public void schedule(Task task, float delaySeconds) {
        instance().scheduleTask(task, delaySeconds);
    }

    /**
     * Runnable with a cancel method.
     *
     * @author Nathan Sweet
     * @see PausableTimer
     */
    static abstract public class Task implements Runnable {
        long executeTimeMillis;
        long intervalMillis;
        int repeatCount = CANCELLED;
        final Application app;

        public Task() {
            app = Gdx.app; // Need to store the app when the task was created for multiple LwjglAWTCanvas.
            if (app == null) throw new IllegalStateException("Gdx.app not available.");
        }

        /**
         * If this is the last time the task will be ran or the task is first cancelled, it may be scheduled again in this
         * method.
         */
        abstract public void run();

        /**
         * Cancels the task. It will not be executed until it is scheduled again. This method can be called at any time.
         */
        private synchronized void cancel() {
            executeTimeMillis = 0;
            repeatCount = CANCELLED;
        }

    }

    /**
     * Manages the single timer thread. Stops thread on libgdx application pause and dispose, starts thread on resume.
     *
     * @author Nathan Sweet
     */
    static class TimerThread implements Runnable {
        private Files files;
        private long pauseMillis;

        TimerThread() {
            resume();
        }

        public void run() {
            while (true) {
                synchronized (instances) {
                    if (files != Gdx.files) return;

                    long timeMillis = System.nanoTime() / 1000000;
                    long waitMillis = 5000;
                    for (int i = 0, n = instances.size; i < n; i++) {
                        try {
                            waitMillis = instances.get(i).update(timeMillis, waitMillis);
                        } catch (Throwable ex) {
                            throw new GdxRuntimeException("Task failed: " + instances.get(i).getClass().getName(), ex);
                        }
                    }

                    if (files != Gdx.files) return;

                    try {
                        if (waitMillis > 0) instances.wait(waitMillis);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }

        void resume() {
            long delayMillis = System.nanoTime() / 1000000 - pauseMillis;
            synchronized (instances) {
                for (int i = 0, n = instances.size; i < n; i++) {
                    instances.get(i).delay(delayMillis);
                }
            }
            files = Gdx.files;
            Thread t = new Thread(this, "Timer");
            t.setDaemon(true);
            t.start();
        }

        private void pause() {
            pauseMillis = System.nanoTime() / 1000000;
            synchronized (instances) {
                files = null;
                wake();
            }
        }

        private void dispose() {
            pause();
            instances.clear();
            instance = null;
        }
    }
}
