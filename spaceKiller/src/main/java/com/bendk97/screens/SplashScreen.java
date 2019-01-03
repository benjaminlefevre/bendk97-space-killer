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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.bendk97.SpaceKillerGame;
import com.bendk97.SpaceKillerGameConstants;
import com.bendk97.assets.GameAssets;
import com.bendk97.lightning.bolt.LightningBolt;
import com.bendk97.lightning.bolt.LightningBoltArt;
import com.bendk97.pools.GamePools;
import com.bendk97.screens.menu.MenuScreen;
import com.bendk97.tweens.SpriteTweenAccessor;

import java.util.Random;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line;
import static com.bendk97.assets.GameAssets.*;
import static com.bendk97.pools.GamePools.poolCircle;
import static com.bendk97.pools.GamePools.poolSprite;
import static com.bendk97.tweens.SpriteTweenAccessor.ROTATION;
import static com.bendk97.tweens.SpriteTweenAccessor.SCALE;


public final class SplashScreen extends HDScreen {

    private static final Circle copyright = poolCircle.obtain();
    public static final int SCREEN_WIDTH = 1080;
    public static final int SCREEN_HEIGHT = 1920;
    // A variable for tracking elapsed time for the animation
    private float stateTime;
    // Objects used
    private Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    private Sprite logo;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private BitmapFontCache font;
    private final Actor fader = new Actor();
    private TweenManager tweenManager = new TweenManager();
    private Array<LightningBolt> bolts = new Array<>();
    private LightningBoltArt boltArt;
    private final Random random = new RandomXS128();

    public SplashScreen(GameAssets assets, SpaceKillerGame game) {
        super(game, assets, SCREEN_WIDTH, SCREEN_HEIGHT);
        assets.playMusic(SPLASH_MUSIC);
        initGraphics();
        initFader();
    }

    private void initGraphics() {
        font = assets.getFont(FONT_SPLASH);
        copyright.set(SCREEN_WIDTH / 4.5f + 12f, SCREEN_HEIGHT / 6f -2f, 25f);
        font.setText(SpaceKillerGameConstants.COPYRIGHT, SCREEN_WIDTH / 4.5f, SCREEN_HEIGHT / 6f);
        TextureAtlas atlas = assets.get(SPLASH_ATLAS);
        // Initialize the Animation with the frame interval and array of frames
        walkAnimation = new Animation<>(0.025f, atlas.findRegions("human_running"), LOOP);
        shapeRenderer = new ShapeRenderer();
        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        spriteBatch = new SpriteBatch();
        stateTime = 0f;
        logo = poolSprite.getSprite(assets.get(SPLASH_TXT_LOGO));
        logo.setPosition(viewport.getWorldWidth() / 2f - logo.getWidth() / 2f,
                viewport.getWorldHeight() / 2f - logo.getHeight() / 2f);
        logo.setScale(0f);
        Tween.registerAccessor(Sprite.class, new SpriteTweenAccessor());
        Tween.to(logo, ROTATION, 0.5f)
                .ease(Linear.INOUT).target(360f).repeat(3, 0f)
                .start(tweenManager);
        Tween.to(logo, SCALE, 1.5f)
                .ease(Linear.INOUT).target(1f, 1f)
                .start(tweenManager);

        initBolts();
    }

    private void initBolts() {
        boltArt = new LightningBoltArt(assets, 12);
        createBolt();
        createBolt();
    }

    private void createBolt() {
        bolts.add(
                new LightningBolt(boltArt,
                        random.nextFloat() * SCREEN_WIDTH, random.nextFloat() * SCREEN_HEIGHT,
                        random.nextFloat() * SCREEN_WIDTH, random.nextFloat() * SCREEN_HEIGHT)
        );
    }

    private void initFader() {
        fader.clearActions();
        //Assign the starting value
        fader.setColor(Color.WHITE);
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
        Gdx.gl.glLineWidth(5);
        float stateTimeBefore = stateTime;
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        tweenManager.update(delta);
        //Act updates the actions of an actor
        fader.act(Gdx.graphics.getDeltaTime());

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        renderBolts();
        spriteBatch.draw(currentFrame, ((viewport.getWorldWidth() / 3) * stateTime), 0, 150, 150);
        logo.setAlpha(fader.getColor().a);
        logo.draw(spriteBatch);
        font.draw(spriteBatch);
        spriteBatch.end();
        shapeRenderer.begin(Line);
        shapeRenderer.circle(copyright.x, copyright.y, copyright.radius);
        shapeRenderer.end();
        if (stateTime > 5 && stateTimeBefore <= 5) {
            game.goToScreen(MenuScreen.class);
        }
    }

    private void renderBolts() {
        for (int i = 0; i < bolts.size; i++) {
            bolts.get(i).update();
        }
        for (int i = 0; i < bolts.size; i++) {
            bolts.get(i).draw(spriteBatch);
        }

        int count = bolts.size;
        for (int i = 0; i < count; i++) {
            if (bolts.get(i).isComplete()) {
                bolts.get(i).dispose();
                bolts.removeIndex(i);
                count--;
            }
        }
        if (random.nextInt(10) == 4) {
            createBolt();
        }
    }

    @Override
    public void dispose() { // SpriteBatches and Textures must always be disposed
        spriteBatch.dispose();
        shapeRenderer.dispose();
        poolCircle.free(copyright);
        tweenManager.killAll();
        Texture.clearAllTextures(Gdx.app);
        poolSprite.free(logo);
        for (int i = 0; i < bolts.size; ++i) {
            bolts.get(i).dispose();
        }
        GamePools.clearPools();
    }

}
