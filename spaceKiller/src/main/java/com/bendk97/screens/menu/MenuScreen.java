/*
 * Developed by Benjamin Lefèvre
 * Last modified 03/11/18 23:12
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.bendk97.Settings;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;
import com.bendk97.screens.HDScreen;
import com.bendk97.screens.levels.Level1Screen;
import com.google.common.collect.Sets;

import java.util.Set;

import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.graphics.Color.YELLOW;
import static com.bendk97.Settings.getHighScoreString;
import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.pools.BitmapFontHelper.drawText;

public final class MenuScreen extends HDScreen {

    private static final String SPACE = "SPACE";
    private static final String KILLER = "KILLER";
    private static final String GOOGLE_PLAY_PLEASE_SIGN_IN = " Google Play\nPlease sign in";
    private static final String PLAY = "play";
    private static final String SCORES = "scores";
    private static final String SETTINGS = "settings";
    private static final String HELP = "help";
    private static final String CREDITS = "credits";
    private static final String RETRO_PAD = "retro pad";
    private static final String VIRTUAL_PAD_AUTOFIRE = "virtual pad\n\n   / autofire";
    private static final String LIGHT_FX = "light fx";
    private static final String VIBRATION = "vibration";
    private static final String BACK = "back";
    protected static final Color WHITE_ALPHA_60 = new Color(1f, 1f, 1f, 0.6f);
    private SpriteBatch batcher;
    private final String gameVersion;
    private final TextureAtlas atlas;
    // fonts
    private BitmapFontCache font;
    private BitmapFontCache fontVersion;
    protected final Stage stage;
    // Google icons
    private ImageButton leaderBoard;
    private ImageButton achievements;
    private ImageButton leaderBoard_off;
    private ImageButton achievements_off;
    private ImageButton gplay;
    private ImageButton gplayOff;

    public MenuScreen(final Assets assets, final SpaceKillerGame game) {
        this(assets, game, null);
    }

    /* for test purposes */
    protected MenuScreen(final Assets assets, final SpaceKillerGame game, final SpriteBatch providedBatcher) {
        super(game, assets);
        initBatcher(providedBatcher);
        this.gameVersion = VERSION;
        if (!NO_GOOGLE && !game.signInFailed && !game.playServices.isSignedIn()) {
            game.playServices.signIn();
        }
        atlas = assets.get(Assets.MENU_ATLAS);
        stage = new Stage(viewport, batcher);
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);

        initBackground(assets);
        initFonts();
        assets.playMusic(Assets.MENU_MUSIC);
        Gdx.input.setInputProcessor(stage);
        setupMainMenu(buttonStyle);
        setupSoundsSettings(assets);
        setupGoogleSettings(assets, game);
    }

    private void initBatcher(SpriteBatch batcher) {
        if (batcher != null) {
            this.batcher = batcher;
        } else {
            this.batcher = new SpriteBatch();
        }
    }

    private void initBackground(Assets assets) {
        Image image = new Image(assets.get(Assets.MENU_BGD));
        image.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.addActor(image);
    }

    private void initFonts() {
        font = assets.getFont(Assets.FONT_SPACE_KILLER_LARGE);
        fontVersion = new BitmapFontCache(new BitmapFont());
        fontVersion.setColor(YELLOW);
        fontVersion.getFont().getData().setScale(0.5f);
    }

    private void setupSoundsSettings(Assets assets) {
        Table table = new Table();
        TextureRegionDrawable drawable = new TextureRegionDrawable(atlas.findRegion("sound-off"));
        ImageButton soundOff = new ImageButton(drawable);

        drawable = new TextureRegionDrawable(atlas.findRegion("sound-on"));
        ImageButton soundOn = new ImageButton(drawable);

        drawable = new TextureRegionDrawable(atlas.findRegion("music-off"));
        ImageButton musicOff = new ImageButton(drawable);

        drawable = new TextureRegionDrawable(atlas.findRegion("music-on"));
        ImageButton musicOn = new ImageButton(drawable);
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
                assets.playSound(Assets.MENU_CLICK);
                Settings.setSoundOff();
                table.getCells().get(0).clearActor();
                table.getCells().get(0).setActor(soundOff);
                return true;
            }
        });
        soundOff.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                Settings.setSoundOn();
                table.getCells().get(0).clearActor();
                table.getCells().get(0).setActor(soundOn);
                return true;
            }
        });
        musicOn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                Settings.setMusicOff();
                assets.get(Assets.MENU_MUSIC).pause();
                table.getCells().get(1).clearActor();
                table.getCells().get(1).setActor(musicOff);
                return true;
            }
        });
        musicOff.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                Settings.setMusicOn();
                assets.playMusic(Assets.MENU_MUSIC);
                table.getCells().get(1).clearActor();
                table.getCells().get(1).setActor(musicOn);
                return true;
            }
        });
    }

    private void setupGoogleSettings(Assets assets, SpaceKillerGame game) {
        gplay = new ImageButton(new TextureRegionDrawable(atlas.findRegion("gplay")));
        gplayOff = new ImageButton(new TextureRegionDrawable(atlas.findRegion("gplay_off")));
        achievements = new ImageButton(new TextureRegionDrawable(atlas.findRegion("achievements")));
        achievements_off = new ImageButton(new TextureRegionDrawable(atlas.findRegion("achievements_off")));
        leaderBoard = new ImageButton((new TextureRegionDrawable(atlas.findRegion("leaderboard"))));
        leaderBoard_off = new ImageButton((new TextureRegionDrawable(atlas.findRegion("leaderboard_off"))));
        achievements.setPosition(150f, 10f);
        leaderBoard.setPosition(200f, 10f);
        achievements_off.setPosition(150f, 10f);
        leaderBoard_off.setPosition(200f, 10f);
        gplay.setPosition(90f, 0f);
        gplayOff.setPosition(90f, 0f);

        gplayOff.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                game.playServices.signIn();
                return true;
            }

        });
        gplay.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                game.playServices.rateGame();
                return true;
            }

        });
        achievements.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                game.playServices.showAchievement();
                return true;
            }

        });
        leaderBoard.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                game.playServices.showScore();
                return true;
            }

        });
        if (game.playServices.isSignedIn()) {
            stage.addActor(gplay);
            stage.addActor(achievements);
            stage.addActor(leaderBoard);
        } else {
            stage.addActor(gplayOff);
            stage.addActor(achievements_off);
            stage.addActor(leaderBoard_off);
        }
    }

    private void setupMainMenu(TextButtonStyle buttonStyle) {
        TextButton playButton = createTextButton(PLAY, buttonStyle,
                SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2 + 75, 200, 75);
        TextButton highscoresButton = createTextButton(SCORES, buttonStyle,
                SCREEN_WIDTH / 4f, SCREEN_HEIGHT / 2f, 200, 75);
        TextButton settingsButton = createTextButton(SETTINGS, buttonStyle,
                SCREEN_WIDTH / 4f, SCREEN_HEIGHT / 2f - 75f, 200, 75);
        TextButton helpButton = createTextButton(HELP, buttonStyle,
                SCREEN_WIDTH / 4f, SCREEN_HEIGHT / 2f - 150f, 200, 75);
        TextButton creditsButton = createTextButton(CREDITS, buttonStyle,
                SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2 - 225, 200, 75);
        Set<TextButton> mainMenuButtons = Sets.newHashSet(playButton, highscoresButton, settingsButton, helpButton, creditsButton);

        addButtons(mainMenuButtons);

        onClickPlayButton(playButton);
        onClickHighscoresButton(highscoresButton, buttonStyle, mainMenuButtons);
        onClickSettingsButton(settingsButton, buttonStyle, mainMenuButtons);
        onClickHelpButton(helpButton, mainMenuButtons);
        onClickCreditsButton(creditsButton, mainMenuButtons);
    }

    private void onClickCreditsButton(TextButton creditsButton, Set<TextButton> mainMenuButtons) {
        ImageButton creditsScreen = new ImageButton(new TextureRegionDrawable(atlas.findRegion("credits")));
        creditsScreen.setColor(WHITE_ALPHA_60);
        creditsScreen.setPosition(5f, 100f);
        goBackToMainMenuWhenClick(creditsScreen, mainMenuButtons);
        actionWhenClick(creditsButton, mainMenuButtons, creditsScreen);
    }

    private void onClickHelpButton(TextButton helpButton, Set<TextButton> mainMenuButtons) {
        ImageButton helpScreen = new ImageButton(new TextureRegionDrawable(atlas.findRegion("help")));
        helpScreen.setColor(WHITE_ALPHA_60);
        helpScreen.setPosition(5f, 100f);
        goBackToMainMenuWhenClick(helpScreen, mainMenuButtons);
        actionWhenClick(helpButton, mainMenuButtons, helpScreen);
    }

    private void onClickPlayButton(TextButton playButton) {
        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                assets.get(Assets.MENU_MUSIC).stop();
                game.goToScreen(Level1Screen.class);
                return true;
            }
        });
    }

    private void onClickHighscoresButton(TextButton highscoresButton, TextButtonStyle buttonStyle, Set<TextButton> mainMenuButtons) {
        TextButton displayScores = createTextButton(getHighScoreString(), buttonStyle,
                SCREEN_WIDTH / 7, 75, 300, 375);
        goBackToMainMenuWhenClick(displayScores, mainMenuButtons);
        actionWhenClick(highscoresButton, mainMenuButtons, displayScores);
    }

    private void actionWhenClick(TextButton textButton, Set<TextButton> mainMenuButtons, Button button) {
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
                assets.playSound(Assets.MENU_CLICK);
                removeButtons(mainMenuButtons);
                stage.addActor(button);
                return true;
            }
        });
    }

    private void goBackToMainMenuWhenClick(Button backButton, Set<? extends Button> mainMenuButtons) {
        replaceButtonsByNewOnesOnClick(backButton, Sets.newHashSet(backButton), mainMenuButtons);
    }

    private void replaceButtonsByNewOnesOnClick(Button backButton, Set<? extends Button> buttonsToRemove, Set<? extends Button> mainMenuButtons) {
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int b) {
                assets.playSound(Assets.MENU_CLICK);
                removeButtons(buttonsToRemove);
                addButtons(mainMenuButtons);
                return true;
            }
        });
    }

    private void onClickSettingsButton(TextButton settingsButton, TextButtonStyle buttonStyle, Set<TextButton> mainMenuButtons) {
        TextButton back = createTextButton(BACK, buttonStyle, 100, 75, 200, 75);

        TextButtonStyle styleRetro = new TextButtonStyle();
        styleRetro.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        styleRetro.fontColor = !Settings.isVirtualPad() ? YELLOW : WHITE;
        TextButton retroPad = createTextButton(RETRO_PAD, styleRetro, 100, 450, 200, 75);

        TextButtonStyle styleVirtual = new TextButtonStyle();
        styleVirtual.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        styleVirtual.fontColor = Settings.isVirtualPad() ? YELLOW : WHITE;
        TextButton virtualPad = createTextButton(VIRTUAL_PAD_AUTOFIRE, styleVirtual, 100, 300, 200, 150);

        TextButtonStyle styleFX = new TextButtonStyle();
        styleFX.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        styleFX.fontColor = Settings.isLightFXEnabled() ? YELLOW : WHITE;
        TextButton lightFx = createTextButton(LIGHT_FX, styleFX, 100, 225, 200, 75);

        TextButtonStyle styleVibration = new TextButtonStyle();
        styleVibration.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        styleVibration.fontColor = Settings.isVibrationEnabled() ? YELLOW : WHITE;
        TextButton vibration = createTextButton(VIBRATION, styleVibration, 100, 150, 200, 75);

        Set<TextButton> settingsButtons = Sets.newHashSet(back, retroPad, virtualPad, lightFx, vibration);

        replaceButtonsByNewOnesOnClick(settingsButton, mainMenuButtons, settingsButtons);

        onClickRetroPadButton(retroPad, virtualPad);
        onClickVirtualPadButton(virtualPad, retroPad);
        onClickLightFXButton(lightFx);
        onClickVibrationButton(vibration);
        onClickBackButton(back, mainMenuButtons, settingsButtons);

    }

    private void onClickBackButton(TextButton back, Set<TextButton> mainMenuButtons, Set<TextButton> settingsButtons) {
        replaceButtonsByNewOnesOnClick(back, settingsButtons, mainMenuButtons);
    }

    private void onClickVibrationButton(TextButton vibration) {
        vibration.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                Settings.changeVibrationEnabled();
                vibration.getStyle().fontColor = Settings.isVibrationEnabled() ? YELLOW : WHITE;
                return true;
            }
        });
    }

    private void onClickLightFXButton(TextButton lightFx) {
        lightFx.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                Settings.changeLightFXEnabled();
                lightFx.getStyle().fontColor = Settings.isLightFXEnabled() ? YELLOW : WHITE;
                return true;
            }
        });
    }

    private void onClickVirtualPadButton(TextButton virtualPad, TextButton retroPad) {
        virtualPad.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!Settings.isVirtualPad()) {
                    assets.playSound(Assets.MENU_CLICK);
                    Settings.setVirtualPad();
                    virtualPad.getStyle().fontColor = YELLOW;
                    retroPad.getStyle().fontColor = WHITE;

                }
                return true;
            }
        });
    }

    private void onClickRetroPadButton(TextButton retroPad, TextButton virtualPad) {
        retroPad.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Settings.isVirtualPad()) {
                    assets.playSound(Assets.MENU_CLICK);
                    Settings.setRetroPad();
                    retroPad.getStyle().fontColor = YELLOW;
                    virtualPad.getStyle().fontColor = WHITE;
                }
                return true;
            }
        });
    }

    private TextButton createTextButton(String text, TextButtonStyle buttonStyle, float x, float y, float width, float height) {
        TextButton playButton = new TextButton(text, buttonStyle);
        playButton.setSize(width, height);
        playButton.setPosition(x, y);
        return playButton;
    }

    private void removeButtons(Set<? extends Button> buttons) {
        for (Button button : buttons) {
            button.remove();
        }
    }

    private void addButtons(Set<? extends Button> buttons) {
        for (Button button : buttons) {
            stage.addActor(button);
        }
    }

    @Override
    public void show() {
        // Nothing to show lol
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.setProjectionMatrix(viewport.getCamera().combined);
        stage.draw();
        batcher.begin();
        drawText(batcher, font, SPACE, SCREEN_WIDTH / 5f - 15f, SCREEN_HEIGHT * 3 / 4 + 100f);
        drawText(batcher, font, KILLER, SCREEN_WIDTH / 5f - 40f, SCREEN_HEIGHT * 3 / 4 + 50f);
        drawText(batcher, fontVersion, gameVersion, 10f, 20f);
        if (!game.playServices.isSignedIn()) {
            fontVersion.setColor(WHITE);
            drawText(batcher, fontVersion, GOOGLE_PLAY_PLEASE_SIGN_IN, 90f, 60f);
            fontVersion.setColor(YELLOW);
        }
        batcher.end();
    }


    @Override
    public void dispose() {
        batcher.dispose();
        fontVersion.getFont().dispose();
        stage.dispose();
        Texture.clearAllTextures(Gdx.app);
    }

    public void signInSucceeded() {
        displayImageButtons(gplay, leaderBoard, achievements);
        hideImageButtons(gplayOff, leaderBoard_off, achievements_off);
    }

    public void signInFailed() {
        displayImageButtons(gplayOff, leaderBoard_off, achievements_off);
        hideImageButtons(gplay, leaderBoard, achievements);
    }

    private void displayImageButtons(ImageButton... imageButtons) {
        for (ImageButton imageButton : imageButtons) {
            stage.addActor(imageButton);
        }
    }

    private void hideImageButtons(ImageButton... imageButtons) {
        for (ImageButton imageButton : imageButtons) {
            imageButton.remove();
        }
    }
}
