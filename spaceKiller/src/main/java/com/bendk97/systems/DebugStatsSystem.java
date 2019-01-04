/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.StringBuilder;
import com.bendk97.pools.BitmapFontHelper;
import com.bendk97.screens.levels.LevelScreen;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.pools.GamePools.getPoolStats;

public class DebugStatsSystem extends EntitySystem {

    private static final int FREQUENCY_POOL_STATS_MS = 5000;
    private static final int FREQUENCY_MEM_STATS_MS = 5000;
    private static final String SCRIPT = "Script: ";
    private static final String CURRENT = "Current: ";
    private static final String MIN = "Min: ";
    private static final String MAX = "Max: ";
    private static final String FPS = " fps";
    public static final String S = " s";
    private static final String NEWLINE = "\n";
    private static final String JAVA_HEAP = "Java Heap: ";
    private static final String NATIVE_HEAP = "Native Heap: ";
    private static final int BYTES_TO_MB = 1024 * 1024;
    private static final String MB = "MB";
    private static final String EMPTY = "??";
    private final SpriteBatch batcher;
    private final BitmapFontCache bitmapFont;
    private final LevelScreen screen;
    private final StringBuilder sb = new StringBuilder();

    private int minFps = 999;
    private int maxFps = 0;
    private long nativeHeap;
    private long javaHeap;
    private float poolStatsTimeCounter = 0;
    private float memoryStatsTimeCounter = 0;

    public DebugStatsSystem(LevelScreen screen, SpriteBatch batcher, int priority) {
        super(priority);
        this.screen = screen;
        this.batcher = batcher;
        bitmapFont = new BitmapFontCache(new BitmapFont());
        this.bitmapFont.getFont().getData().setScale(0.5f);
        this.nativeHeap = Gdx.app.getNativeHeap();
        this.javaHeap = Gdx.app.getJavaHeap();
    }

    @Override
    public void update(float deltaTime) {

        int currentFps = Gdx.graphics.getFramesPerSecond();
        updatePoolStats(deltaTime);
        updateMemoryStats(deltaTime);

        if (screen.getCurrentTimeScript() > 0) {
            if (currentFps > maxFps) {
                maxFps = currentFps;
            }
            if (currentFps < minFps) {
                minFps = currentFps;
            }
        }
        sb.setLength(0);
        sb.append(SCRIPT).append(Math.floor(screen.getCurrentTimeScript())).append(S).append(NEWLINE);
        sb.append(CURRENT).append(Gdx.graphics.getFramesPerSecond()).append(FPS).append(NEWLINE);
        sb.append(MIN).append(minFps == 999 ? EMPTY : minFps).append(FPS).append(NEWLINE);
        sb.append(MAX).append(maxFps).append(FPS).append(NEWLINE);
        sb.append(JAVA_HEAP).append(javaHeap / BYTES_TO_MB).append(MB).append(NEWLINE);
        sb.append(NATIVE_HEAP).append(nativeHeap /BYTES_TO_MB).append(MB).append(NEWLINE);
        drawText(165f, SCREEN_HEIGHT - 70f);
    }

    private void updateMemoryStats(float deltaTime) {
        memoryStatsTimeCounter += deltaTime * 1000f;
        if (memoryStatsTimeCounter >= FREQUENCY_MEM_STATS_MS) {
            memoryStatsTimeCounter = 0f;
            nativeHeap = Gdx.app.getNativeHeap();
            javaHeap = Gdx.app.getJavaHeap();
        }
    }

    private void updatePoolStats(float deltaTime) {
        poolStatsTimeCounter += deltaTime * 1000f;
        if (poolStatsTimeCounter >= FREQUENCY_POOL_STATS_MS) {
            poolStatsTimeCounter = 0f;
            Gdx.app.log("POOLS", getPoolStats());
        }
    }

    private void drawText(float x, float y) {
        BitmapFontHelper.drawText(batcher, bitmapFont, 0.6f, sb.toString(), x, y);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        bitmapFont.getFont().dispose();
    }
}
