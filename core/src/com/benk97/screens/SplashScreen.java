package com.benk97.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.benk97.SpaceKillerGame;
import com.benk97.assets.Assets;

import static com.benk97.assets.Assets.SPLASH_MUSIC;


public class SplashScreen extends HDScreen {

    private static final int FRAME_COLS = 6, FRAME_ROWS = 5;
    Texture logoTexture;

    // A variable for tracking elapsed time for the animation
    float stateTime;
    // Objects used
    Animation<Sprite> walkAnimation; // Must declare frame type (TextureRegion)
    Texture walkSheet;
    SpriteBatch spriteBatch;
    SpriteBatch logo;
    Actor fader = new Actor();


    public SplashScreen(Assets assets, SpaceKillerGame game) {
        super(game, assets);
        assets.playMusic(SPLASH_MUSIC);
        initGraphics();
        initFader();
    }

    public void initGraphics() {
        // Load the sprite sheet as a Texture
        walkSheet = assets.get(Assets.SPLASH_TXT_HUMAN);
        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        Sprite[] walkFrames = new Sprite[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index] = new Sprite(tmp[i][j]);
                walkFrames[index++].setScale(0.5f);
            }
        }
        // Initialize the Animation with the frame interval and array of frames
        walkAnimation = new Animation<Sprite>(0.025f, walkFrames);

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        spriteBatch = new SpriteBatch();
        stateTime = 0f;
        logoTexture = assets.get(Assets.SPLASH_TXT_LOGO);
        logo = new SpriteBatch();
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
        spriteBatch.draw(currentFrame, ((VIEWPORT_WIDTH / 3) * stateTime), 0, 200, 200);
        spriteBatch.end();
        logo.setProjectionMatrix(viewport.getCamera().combined);
        logo.setColor(fader.getColor());
        logo.begin();
        logo.draw(logoTexture,
                VIEWPORT_WIDTH / 2 - logoTexture.getWidth() / 2,
                VIEWPORT_HEIGHT / 2 - logoTexture.getHeight() / 2);
        logo.end();
        if(stateTime>5 && stateTimeBefore <= 5){
            this.dispose();
            game.goToScreen(MenuScreen.class);
        }
    }

    @Override
    public void dispose() { // SpriteBatches and Textures must always be disposed
        spriteBatch.dispose();
        walkSheet.dispose();
        assets.unloadResources(this.getClass());
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
