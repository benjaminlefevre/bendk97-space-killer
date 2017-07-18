package com.benk97.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.benk97.Settings;
import com.benk97.SpaceKillerGame;
import com.benk97.assets.Assets;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;
import static com.benk97.assets.Assets.*;

public class MenuScreen extends HDScreen {

    SpriteBatch batcher;
    Image image;
    TextButton playButton;
    TextButtonStyle buttonStyle;
    TextButton highscoresButton;
    ImageButton soundOff;
    ImageButton soundOn;
    ImageButton musicOff;
    ImageButton musicOn;
    Table table;
    BitmapFont font;
    Stage stage;
    TextButton displayScores;

    public MenuScreen(final Assets assets, final SpaceKillerGame game) {
        super(game, assets);
        batcher = new SpriteBatch();
        stage = new Stage(viewport, batcher);
        image = new Image(assets.get(MENU_BGD));
        image.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.addActor(image);
        buttonStyle = new TextButtonStyle();
        buttonStyle.font = assets.get(FONT_SPACE_KILLER_MEDIUM);
        playButton = new TextButton("play", buttonStyle);
        playButton.setSize(200f, 75f);
        highscoresButton = new TextButton("scores", buttonStyle);
        highscoresButton.setSize(300f, 75f);

        stage.addActor(playButton);
        stage.addActor(highscoresButton);
        playButton.setPosition(SCREEN_WIDTH / 4f, SCREEN_HEIGHT / 2f);
        highscoresButton.setPosition(SCREEN_WIDTH / 6f, SCREEN_HEIGHT / 2f - 100f);
        Gdx.input.setInputProcessor(stage);
        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(MENU_CLICK);
                assets.get(MENU_MUSIC).stop();
                dispose();
                game.goToScreen(Level1Screen.class);
                return true;
            }
        });
        highscoresButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(MENU_CLICK);
                playButton.remove();
                highscoresButton.remove();
                displayScores = new TextButton(Settings.getHighscoreString(), buttonStyle);
                displayScores.setSize(300f, 375f);
                displayScores.setPosition(SCREEN_WIDTH / 7f, 75f);
                displayScores.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        assets.playSound(MENU_CLICK);
                        displayScores.remove();
                        stage.addActor(playButton);
                        stage.addActor(highscoresButton);
                        return true;
                    }
                });
                stage.addActor(displayScores);
                return true;
            }
        });


        TextureAtlas atlas = assets.get(MENU_ATLAS);
        table = new Table();
        TextureRegionDrawable drawable = new TextureRegionDrawable(atlas.findRegion("sound-off"));
        soundOff = new ImageButton(drawable);
        drawable = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("sound-on")));
        soundOn = new ImageButton(drawable);
        drawable = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("music-off")));
        musicOff = new ImageButton(drawable);
        drawable = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("music-on")));
        musicOn = new ImageButton(drawable);
        if (Settings.isSoundOn()) {
            table.add(soundOn).size(30f, 30f);
        } else {
            table.add(soundOff).size(30f, 30f);
        }
        if (Settings.isMusicOn()) {
            table.add(musicOn).size(30f, 30f);
        } else {
            table.add(musicOff).size(30f, 30f);
        }
        table.setPosition(SCREEN_WIDTH - 50f, 30f);
        stage.addActor(table);
        soundOn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(MENU_CLICK);
                Settings.setSoundOff();
                table.getCells().get(0).clearActor();
                table.getCells().get(0).setActor(soundOff);
                return true;
            }
        });
        soundOff.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(MENU_CLICK);
                Settings.setSoundOn();
                table.getCells().get(0).clearActor();
                table.getCells().get(0).setActor(soundOn);
                return true;
            }
        });
        musicOn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(MENU_CLICK);
                Settings.setMusicOff();
                assets.get(MENU_MUSIC).pause();
                table.getCells().get(1).clearActor();
                table.getCells().get(1).setActor(musicOff);
                return true;
            }
        });
        musicOff.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(MENU_CLICK);
                Settings.setMusicOn();
                assets.playMusic(MENU_MUSIC);
                table.getCells().get(1).clearActor();
                table.getCells().get(1).setActor(musicOn);
                return true;
            }
        });


        assets.playMusic(MENU_MUSIC);
        font = assets.get(FONT_SPACE_KILLER_LARGE);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.setProjectionMatrix(viewport.getCamera().combined);
        stage.draw();
        batcher.begin();
        font.draw(batcher, "SPACE", SCREEN_WIDTH / 5f - 15f, SCREEN_HEIGHT * 3 / 4 + 50f);
        font.draw(batcher, "KILLER", SCREEN_WIDTH / 5f - 40f, SCREEN_HEIGHT * 3 / 4);
        batcher.end();
    }


    @Override
    public void dispose() {
        batcher.dispose();
        assets.unloadResources(this.getClass());
        stage.dispose();
    }
}
