/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bendk97.SpaceKillerGame;
import com.bendk97.tweens.SpriteTween;

public class TransitionScreen extends ScreenAdapter {
    public final static float screenWidth = Gdx.graphics.getWidth();
    public final static float screenHeight = Gdx.graphics.getHeight();

    private SpaceKillerGame game;
    private com.bendk97.assets.Assets assets;
    private Screen next;

    private FrameBuffer currentBuffer;
    private FrameBuffer nextBuffer;

    private SpriteBatch spriteBatch;
    private TweenManager manager;

    private Sprite currentScreenSprite;
    private Sprite nextScreenSprite;

    @Override
    public void dispose() {
        spriteBatch.dispose();
        currentBuffer.dispose();
        nextBuffer.dispose();
    }

    public TransitionScreen(FrameBuffer current, Screen next, SpaceKillerGame game) {
        this.currentBuffer = current;
        this.next = next;
        this.game = game;
    }

    @Override
    public void render(float delta) {
        manager.update(Gdx.graphics.getDeltaTime());
        spriteBatch.begin();
        currentScreenSprite.draw(spriteBatch);
        nextScreenSprite.draw(spriteBatch);
        spriteBatch.end();

    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        manager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteTween());

        TweenCallback backgroundAnimationTweenComplete = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                dispose();
                game.setScreen(next);
            }
        };

        nextBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int) screenWidth, (int) screenHeight, false);
        nextBuffer.begin();
        next.resize((int) screenWidth, (int) screenHeight);
        next.render(Gdx.graphics.getDeltaTime());
        nextBuffer.end();
        nextScreenSprite = new Sprite(nextBuffer.getColorBufferTexture());
        nextScreenSprite.setPosition(screenWidth, 0);
        nextScreenSprite.flip(false, true);

        currentScreenSprite = new Sprite(currentBuffer.getColorBufferTexture());
        currentScreenSprite.setPosition(0, 0);
        currentScreenSprite.flip(false, true);
        Tween.to(nextScreenSprite, SpriteTween.POS_XY, 1.0f)
                .target(0, 0)
                .setCallback(backgroundAnimationTweenComplete)
                .setCallbackTriggers(TweenCallback.COMPLETE)
                .start(manager);
    }
}