/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;


public class SplashScreen extends HDScreen {

    // A variable for tracking elapsed time for the animation
    private float stateTime;
    // Objects used
    private Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    private Texture logo;
    private SpriteBatch spriteBatch;
    private SpriteBatch logoBatcher;
    private final Actor fader = new Actor();


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
        logo = assets.get(Assets.SPLASH_TXT_LOGO);
        logoBatcher = new SpriteBatch();
    }

    private void initFader() {
        fader.clearActions();
        //Assign the starting value
        fader.setColor(new Color(1f, 1f, 1f, 0f));
        // Fade in during 2 seconds
        fader.addAction(Actions.sequence(Actions.fadeIn(3f), Actions.fadeOut(3f)));
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

        //Act updates the actions of an actor
        fader.act(Gdx.graphics.getDeltaTime());

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(currentFrame, ((viewport.getWorldWidth() / 3) * stateTime), 0, 150, 150);
        spriteBatch.end();
        logoBatcher.setProjectionMatrix(viewport.getCamera().combined);
        logoBatcher.setColor(fader.getColor());
        logoBatcher.begin();
        logoBatcher.draw(logo,
                viewport.getWorldWidth() / 2f - logo.getWidth() / 2f,
                viewport.getWorldHeight() / 2f - logo.getHeight() / 2f);
        logoBatcher.end();
        if(stateTime>5 && stateTimeBefore <= 5){
            this.dispose();
            game.goToScreen(MenuScreen.class);
        }
    }

    @Override
    public void dispose() { // SpriteBatches and Textures must always be disposed
        spriteBatch.dispose();
        assets.unloadResources(this.getClass());
    }

}
