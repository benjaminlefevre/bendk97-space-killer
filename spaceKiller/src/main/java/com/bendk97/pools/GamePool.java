/*
 * Developed by Benjamin Lefèvre
 * Last modified 30/10/18 22:38
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.pools;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.StringBuilder;

public abstract class GamePool<T> {
    private static final String SPACE = " ";
    private static final String FREE = "[size=";
    private static final String MAX = ", max=";
    private static final String PEAK = ", peak=";
    private static final String ALIVE = ", alive=";
    private static final String END = "]\n";
    private final StringBuilder sb = new StringBuilder();
    private final Pool<T> pool;
    private final Class<T> klass;
    private int alive = 0;

    public GamePool(Class<T> klass, int max) {
        this.pool = Pools.get(klass, max);
        this.klass = klass;
    }

    public T obtain() {
        alive++;
        return pool.obtain();
    }

    public void free(T object) {
        alive--;
        pool.free(object);
        reset(object);
    }

    public void clear() {
        pool.clear();
        alive = 0;
        pool.peak = 0;
    }

    public abstract void reset(T pooledObject);

    public String getPoolStats() {
        sb.setLength(0);
        return sb.append(klass.getSimpleName())
                .append(SPACE)
                .append(FREE)
                .append(pool.getFree())
                .append(MAX)
                .append(pool.max)
                .append(PEAK)
                .append(pool.peak)
                .append(ALIVE)
                .append(alive)
                .append(END).toString();
    }
}
