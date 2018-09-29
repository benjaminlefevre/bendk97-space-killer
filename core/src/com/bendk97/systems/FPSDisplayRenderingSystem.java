/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class FPSDisplayRenderingSystem extends EntitySystem {

    private SpriteBatch batcher;
    private BitmapFont bitmapFont;
    private com.bendk97.screens.LevelScreen screen;

    public FPSDisplayRenderingSystem(com.bendk97.screens.LevelScreen screen, SpriteBatch batcher, int priority) {
        super(priority);
        this.screen = screen;
        this.batcher = batcher;
        this.bitmapFont = new BitmapFont();
        this.bitmapFont.getData().setScale(0.5f);
    }

    float currentTime = 0f;
    int currentFps = 0;

    @Override
    public void update(float deltaTime) {
        float oldTime = currentTime;
        currentTime += deltaTime;
        if (Math.floor(currentTime) > Math.floor(oldTime)) {
            currentFps = (int) (1 / Gdx.graphics.getRawDeltaTime());
        }
        bitmapFont.draw(batcher, Gdx.graphics.getFramesPerSecond() + " fps/avg", SCREEN_WIDTH - 80f, 10f);
        bitmapFont.draw(batcher, currentFps + " fps", SCREEN_WIDTH - 30f, 10f);
        bitmapFont.draw(batcher, Math.floor(screen.getCurrentTimeScript()) + " s", SCREEN_WIDTH - 150f, 10f);
    }
}