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

import static com.bendk97.pools.GamePools.getPoolStats;

public class DebugStatsSystem extends EntitySystem {

    private static final int FREQUENCY_POOL_STATS_MS = 5000;
    private static final String SCRIPT = "Script: ";
    private static final String CURRENT = "Current: ";
    private static final String MIN = "Min: ";
    private static final String MAX = "Max: ";
    private static final String FPS = " fps";
    public static final String S = " s";
    private static final String SPRITE = "Sprite";
    private static final String SPRITE_POOL_SIZE = "Sprite Pool Size: ";
    private final SpriteBatch batcher;
    private final BitmapFontCache bitmapFont;
    private final LevelScreen screen;
    private final StringBuilder sb = new StringBuilder();

    private int minFps = 999;
    private int maxFps = 0;
    private float deltaCount = 0;

    public DebugStatsSystem(LevelScreen screen, SpriteBatch batcher, int priority) {
        super(priority);
        this.screen = screen;
        this.batcher = batcher;
        bitmapFont = new BitmapFontCache(new BitmapFont());
        this.bitmapFont.getFont().getData().setScale(0.5f);
    }

    @Override
    public void update(float deltaTime) {
        int currentFps = Gdx.graphics.getFramesPerSecond();

        updatePoolStats(deltaTime);

        if (screen.getCurrentTimeScript() > 0) {
            if (currentFps > maxFps) {
                maxFps = currentFps;
            }
            if (currentFps < minFps) {
                minFps = currentFps;
            }
        }
        sb.setLength(0);
        sb.append(SCRIPT).append(Math.floor(screen.getCurrentTimeScript())).append(S);
        drawText(0f, 10f);
        sb.setLength(0);
        sb.append(CURRENT).append(Gdx.graphics.getFramesPerSecond()).append(FPS);
        drawText(75f, 10f);
        sb.setLength(0);
        sb.append(MIN).append(minFps).append(FPS);
        drawText(150f, 10f);
        sb.setLength(0);
        sb.append(MAX).append(maxFps).append(FPS);
        drawText(225f, 10f);
    }

    private void updatePoolStats(float deltaTime) {
        deltaCount += deltaTime * 1000f;
        if (deltaCount >= FREQUENCY_POOL_STATS_MS) {
            deltaCount = 0f;
            Gdx.app.log("POOLS", getPoolStats());
        }
    }

    private void drawText(float x, float y) {
        BitmapFontHelper.drawText(batcher, bitmapFont, sb.toString(), x, y);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        bitmapFont.getFont().dispose();
    }
}
