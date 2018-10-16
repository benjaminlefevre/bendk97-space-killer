/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;
import com.bendk97.tweens.SpriteTween;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.bendk97.tweens.SpriteTween.ROTATION;
import static com.bendk97.tweens.SpriteTween.SCALE;


public class SplashScreen extends HDScreen {

    // A variable for tracking elapsed time for the animation
    private float stateTime;
    // Objects used
    private Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    private Sprite logo;
    private SpriteBatch spriteBatch;
    private final Actor fader = new Actor();
    private TweenManager tweenManager = new TweenManager();


    public SplashScreen(Assets assets, SpaceKillerGame game) {
        super(game, assets,1080,1920);
        assets.playMusic(Assets.SPLASH_MUSIC);
        initGraphics();
        initFader();
    }

    private void initGraphics() {
        TextureAtlas atlas = assets.get(Assets.SPLASH_ATLAS);
        // Initialize the Animation with the frame interval and array of frames
        walkAnimation = new Animation<>(0.025f, atlas.findRegions("human_running"), LOOP);

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        spriteBatch = new SpriteBatch();
        stateTime = 0f;
        logo = new Sprite(assets.get(Assets.SPLASH_TXT_LOGO));
        logo.setPosition(viewport.getWorldWidth() / 2f - logo.getWidth() / 2f,
                viewport.getWorldHeight() / 2f - logo.getHeight() / 2f);
        logo.setScale(0f);
        Tween.registerAccessor(Sprite.class, new SpriteTween());
        Tween.to(logo, ROTATION, 0.5f)
                .ease(Linear.INOUT).target(360f).repeat(3, 0f)
                .start(tweenManager);
        Tween.to(logo, SCALE, 1.5f)
                .ease(Linear.INOUT).target(1f, 1f)
                .start(tweenManager);

    }

    private void initFader() {
        fader.clearActions();
        //Assign the starting value
        fader.setColor(new Color(1f, 1f, 1f, 0f));
        // Fade in during 2 seconds
        fader.addAction(Actions.sequence(Actions.fadeIn(4f), Actions.fadeOut(1f)));
    }


    @Override
    public void resume() {
        initFader();
        stateTime = 0;
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float stateTimeBefore = stateTime;
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        tweenManager.update(delta);
        //Act updates the actions of an actor
        fader.act(Gdx.graphics.getDeltaTime());

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(currentFrame, ((viewport.getWorldWidth() / 3) * stateTime), 0, 150, 150);
        logo.setAlpha(fader.getColor().a);
        logo.draw(spriteBatch);
        spriteBatch.end();
        if(stateTime>5 && stateTimeBefore <= 5){
            this.dispose();
            game.goToScreen(MenuScreen.class);
        }
    }

    @Override
    public void dispose() { // SpriteBatches and Textures must always be disposed
        spriteBatch.dispose();
        tweenManager.killAll();
        assets.unloadResources(this.getClass());
    }

}
