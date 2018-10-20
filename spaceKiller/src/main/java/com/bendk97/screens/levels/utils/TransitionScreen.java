/*
 * Developed by Benjamin Lefèvre
 * Last modified 07/10/18 18:34
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels.utils;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bendk97.SpaceKillerGame;
import com.bendk97.screens.levels.LevelScreen;
import com.bendk97.tweens.SpriteTween;

public class TransitionScreen extends ScreenAdapter {
    private final SpaceKillerGame game;
    private final LevelScreen nextScreen;

    private SpriteBatch spriteBatch;
    private TweenManager manager;

    private Sprite currentScreenSprite;
    private Sprite nextScreenSprite;

    @Override
    public void dispose() {
        currentScreenSprite.getTexture().dispose();
        nextScreenSprite.getTexture().dispose();
        spriteBatch.dispose();
        Texture.clearAllTextures(Gdx.app);
    }

    public TransitionScreen(Sprite currentScreenSprite, Sprite nextScreenSprite, LevelScreen nextScreen, SpaceKillerGame game) {
        this.currentScreenSprite = currentScreenSprite;
        this.nextScreenSprite = nextScreenSprite;
        this.nextScreen = nextScreen;
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

        TweenCallback backgroundAnimationTweenComplete = (type, source) -> {
            dispose();
            game.setScreen(nextScreen);
        };

        currentScreenSprite.setPosition(0, 0);
        nextScreenSprite.setPosition(Gdx.graphics.getWidth(), 0);

        Tween.to(nextScreenSprite, SpriteTween.POS_XY, 2.5f)
                .target(0, 0)
                .setCallback(backgroundAnimationTweenComplete)
                .setCallbackTriggers(TweenCallback.COMPLETE)
                .start(manager);
    }
}