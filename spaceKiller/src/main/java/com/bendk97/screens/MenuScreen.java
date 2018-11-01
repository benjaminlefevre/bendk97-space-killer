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
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.bendk97.Settings;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;

import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.graphics.Color.YELLOW;
import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.pools.BitmapFontHelper.drawText;
import static com.bendk97.screens.levels.Level.Level1;

public class MenuScreen extends HDScreen {

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
    private static final Color WHITE_ALPHA_60 = new Color(1f, 1f, 1f, 0.6f);
    private final SpriteBatch batcher;
    private final String gameVersion;
    private final TextButton playButton;
    private final TextButtonStyle buttonStyle;
    private final TextButton highscoresButton;
    private final TextButton controllerButton;
    private final TextButton helpButton;
    private final TextButton creditsButton;
    private final ImageButton soundOff;
    private final ImageButton soundOn;
    private final ImageButton musicOff;
    private final ImageButton musicOn;
    private final Table table;
    private final BitmapFontCache font;
    private final BitmapFontCache fontVersion;
    private final Stage stage;
    private TextButton displayScores;
    private final TextButton retroPad;
    private final TextButton virtualPad;
    private final TextButton lightFx;
    private final TextButton vibration;
    private final TextButton back;

    private final ImageButton leaderBoard;
    private final ImageButton achievements;
    private final ImageButton leaderBoard_off;
    private final ImageButton achievements_off;
    private final ImageButton gplay;
    private final ImageButton gplayOff;
    private ImageButton helpScreen;
    private ImageButton creditsScreen;


    public MenuScreen(final Assets assets, final SpaceKillerGame game) {
        super(game, assets);
        this.gameVersion = VERSION;
        if (!NO_GOOGLE && !game.signInFailed && !game.playServices.isSignedIn()) {
            game.playServices.signIn();
        }
        batcher = new SpriteBatch();
        stage = new Stage(viewport, batcher);
        Image image = new Image(assets.get(Assets.MENU_BGD));
        image.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.addActor(image);
        buttonStyle = new TextButtonStyle();
        buttonStyle.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        playButton = new TextButton(PLAY, buttonStyle);
        playButton.setSize(200f, 75f);
        highscoresButton = new TextButton(SCORES, buttonStyle);
        highscoresButton.setSize(200f, 75f);
        controllerButton = new TextButton(SETTINGS, buttonStyle);
        controllerButton.setSize(200f, 75f);
        helpButton = new TextButton(HELP, buttonStyle);
        helpButton.setSize(200f, 75f);
        creditsButton = new TextButton(CREDITS, buttonStyle);
        creditsButton.setSize(200f, 75f);


        final TextButtonStyle styleRetro = new TextButtonStyle();
        styleRetro.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        styleRetro.fontColor = !Settings.isVirtualPad() ? YELLOW : WHITE;
        retroPad = new TextButton(RETRO_PAD, styleRetro);

        TextButtonStyle styleVirtual = new TextButtonStyle();
        styleVirtual.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        styleVirtual.fontColor = Settings.isVirtualPad() ? YELLOW : WHITE;
        virtualPad = new TextButton(VIRTUAL_PAD_AUTOFIRE, styleVirtual);

        TextButtonStyle styleFX = new TextButtonStyle();
        styleFX.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        styleFX.fontColor = Settings.isLightFXEnabled() ? YELLOW : WHITE;
        lightFx = new TextButton(LIGHT_FX, styleFX);

        TextButtonStyle styleVibration = new TextButtonStyle();
        styleVibration.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        styleVibration.fontColor = Settings.isVibrationEnabled() ? YELLOW : WHITE;
        vibration = new TextButton(VIBRATION, styleVibration);


        back = new TextButton(BACK, buttonStyle);
        retroPad.setSize(200f, 75f);
        virtualPad.setSize(200f, 150f);
        back.setSize(200f, 75f);
        lightFx.setSize(200f, 75f);
        vibration.setSize(200f, 75f);
        retroPad.setPosition(100f, 450f);
        virtualPad.setPosition(100f, 300f);
        lightFx.setPosition(100f, 225f);
        vibration.setPosition(100f, 150f);
        back.setPosition(100f, 75f);


        stage.addActor(playButton);
        stage.addActor(highscoresButton);
        stage.addActor(controllerButton);
        stage.addActor(helpButton);
        stage.addActor(creditsButton);
        playButton.setPosition(SCREEN_WIDTH / 4f, SCREEN_HEIGHT / 2f + 75f);
        highscoresButton.setPosition(SCREEN_WIDTH / 4f, SCREEN_HEIGHT / 2f);
        controllerButton.setPosition(SCREEN_WIDTH / 4f, SCREEN_HEIGHT / 2f - 75f);
        helpButton.setPosition(SCREEN_WIDTH / 4f, SCREEN_HEIGHT / 2f - 150f);
        creditsButton.setPosition(SCREEN_WIDTH / 4f, SCREEN_HEIGHT / 2f - 225f);
        Gdx.input.setInputProcessor(stage);
        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                assets.get(Assets.MENU_MUSIC).stop();
                game.goToLevelScreen(Level1);
                return true;
            }
        });
        highscoresButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                playButton.remove();
                highscoresButton.remove();
                controllerButton.remove();
                helpButton.remove();
                creditsButton.remove();
                displayScores = new TextButton(Settings.getHighScoreString(), buttonStyle);
                displayScores.setSize(300f, 375f);
                displayScores.setPosition(SCREEN_WIDTH / 7f, 75f);
                displayScores.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        assets.playSound(Assets.MENU_CLICK);
                        displayScores.remove();
                        stage.addActor(playButton);
                        stage.addActor(highscoresButton);
                        stage.addActor(controllerButton);
                        stage.addActor(helpButton);
                        stage.addActor(creditsButton);
                        return true;
                    }
                });
                stage.addActor(displayScores);
                return true;
            }
        });

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

        lightFx.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                Settings.changeLightFXEnabled();
                lightFx.getStyle().fontColor = Settings.isLightFXEnabled() ? YELLOW : WHITE;
                return true;
            }
        });

        vibration.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                Settings.changeVibrationEnabled();
                vibration.getStyle().fontColor = Settings.isVibrationEnabled() ? YELLOW : WHITE;
                return true;
            }
        });

        back.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.addActor(playButton);
                stage.addActor(highscoresButton);
                stage.addActor(controllerButton);
                stage.addActor(helpButton);
                stage.addActor(creditsButton);
                retroPad.remove();
                virtualPad.remove();
                lightFx.remove();
                vibration.remove();
                back.remove();
                return true;
            }
        });


        controllerButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                playButton.remove();
                highscoresButton.remove();
                controllerButton.remove();
                helpButton.remove();
                creditsButton.remove();

                stage.addActor(virtualPad);
                stage.addActor(retroPad);
                stage.addActor(back);
                stage.addActor(lightFx);
                stage.addActor(vibration);
                return true;
            }
        });

        helpButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                playButton.remove();
                highscoresButton.remove();
                controllerButton.remove();
                helpButton.remove();
                creditsButton.remove();
                stage.addActor(helpScreen);
                return true;
            }
        });

        creditsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                playButton.remove();
                highscoresButton.remove();
                controllerButton.remove();
                helpButton.remove();
                creditsButton.remove();
                stage.addActor(creditsScreen);
                return true;
            }
        });

        TextureAtlas atlas = assets.get(Assets.MENU_ATLAS);
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
        gplayOff.addListener(new

                                     InputListener() {
                                         @Override
                                         public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                             assets.playSound(Assets.MENU_CLICK);
                                             game.playServices.signIn();
                                             return true;
                                         }

                                     });
        gplay.addListener(new

                                  InputListener() {
                                      @Override
                                      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                          assets.playSound(Assets.MENU_CLICK);
                                          game.playServices.rateGame();
                                          return true;
                                      }

                                  });
        achievements.addListener(new

                                         InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 assets.playSound(Assets.MENU_CLICK);
                                                 game.playServices.showAchievement();
                                                 return true;
                                             }

                                         });
        leaderBoard.addListener(new

                                        InputListener() {
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

        table = new Table();

        helpScreen = new ImageButton(new TextureRegionDrawable(atlas.findRegion("help")));
        helpScreen.setColor(WHITE_ALPHA_60);
        helpScreen.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                helpScreen.remove();
                stage.addActor(playButton);
                stage.addActor(highscoresButton);
                stage.addActor(helpButton);
                stage.addActor(controllerButton);
                stage.addActor(creditsButton);
                return true;
            }
        });
        helpScreen.setPosition(5f, 100f);

        creditsScreen = new ImageButton(new TextureRegionDrawable(atlas.findRegion("credits")));
        creditsScreen.setColor(WHITE_ALPHA_60);
        creditsScreen.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                creditsScreen.remove();
                stage.addActor(playButton);
                stage.addActor(highscoresButton);
                stage.addActor(helpButton);
                stage.addActor(controllerButton);
                stage.addActor(creditsButton);
                return true;
            }
        });
        creditsScreen.setPosition(5f, 100f);

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
        soundOn.addListener(new

                                    InputListener() {
                                        @Override
                                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                            assets.playSound(Assets.MENU_CLICK);
                                            Settings.setSoundOff();
                                            table.getCells().get(0).clearActor();
                                            table.getCells().get(0).setActor(soundOff);
                                            return true;
                                        }
                                    });
        soundOff.addListener(new

                                     InputListener() {
                                         @Override
                                         public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                             assets.playSound(Assets.MENU_CLICK);
                                             Settings.setSoundOn();
                                             table.getCells().get(0).clearActor();
                                             table.getCells().get(0).setActor(soundOn);
                                             return true;
                                         }
                                     });
        musicOn.addListener(new

                                    InputListener() {
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
        musicOff.addListener(new

                                     InputListener() {
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


        assets.playMusic(Assets.MENU_MUSIC);
        font = assets.getFont(Assets.FONT_SPACE_KILLER_LARGE);
        fontVersion = new BitmapFontCache(new BitmapFont());
        fontVersion.setColor(YELLOW);
        fontVersion.getFont().getData().setScale(0.5f);

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
        drawText(batcher, font, SPACE, SCREEN_WIDTH/ 5f - 15f, SCREEN_HEIGHT * 3 / 4 + 100f);
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
        assets.unloadResources(this.getClass());
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
