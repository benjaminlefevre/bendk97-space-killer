/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.bendk97.Settings;
import com.bendk97.SpaceKillerGameConstants;
import com.bendk97.assets.Assets;

import static com.bendk97.SpaceKillerGameConstants.*;

public class MenuScreen extends HDScreen {

    private final SpriteBatch batcher;
    private final Image image;
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
    private final BitmapFont font;
    private final BitmapFont fontVersion;
    private final Stage stage;
    private TextButton displayScores;
    private final TextButton retroPad;
    private final TextButton virtualPad;
    private final TextButton lightFx;
    private final TextButton vibration;
    private final TextButton back;

    private final ImageButton leaderboard;
    private final ImageButton achievements;
    private final ImageButton leaderboard_off;
    private final ImageButton achievements_off;
    private final ImageButton gplay;
    private final ImageButton gplayOff;
    private ImageButton helpScreen;
    private ImageButton creditsScreen;


    public MenuScreen(final Assets assets, final com.bendk97.SpaceKillerGame game) {
        super(game, assets);
        if (!NO_GOOGLE && !game.signInFailed && !game.playServices.isSignedIn()) {
            game.playServices.signIn();
        }
        batcher = new SpriteBatch();
        stage = new Stage(viewport, batcher);
        image = new Image(assets.get(Assets.MENU_BGD));
        image.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.addActor(image);
        buttonStyle = new TextButtonStyle();
        buttonStyle.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        playButton = new TextButton("play", buttonStyle);
        playButton.setSize(200f, 75f);
        highscoresButton = new TextButton("scores", buttonStyle);
        highscoresButton.setSize(200f, 75f);
        controllerButton = new TextButton("settings", buttonStyle);
        controllerButton.setSize(200f, 75f);
        helpButton = new TextButton("help", buttonStyle);
        helpButton.setSize(200f, 75f);
        creditsButton = new TextButton("credits", buttonStyle);
        creditsButton.setSize(200f, 75f);


        final TextButtonStyle styleRetro = new TextButtonStyle();
        styleRetro.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        styleRetro.fontColor = !Settings.isVirtualPad() ? Color.YELLOW : Color.WHITE;
        retroPad = new TextButton("retro pad", styleRetro);

        TextButtonStyle styleVirtual = new TextButtonStyle();
        styleVirtual.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        styleVirtual.fontColor = Settings.isVirtualPad() ? Color.YELLOW : Color.WHITE;
        virtualPad = new TextButton("virtual pad\n\n   / autofire", styleVirtual);

        TextButtonStyle styleFX = new TextButtonStyle();
        styleFX.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        styleFX.fontColor = Settings.isLightFXEnabled() ? Color.YELLOW : Color.WHITE;
        lightFx = new TextButton("light fx", styleFX);

        TextButtonStyle styleVibration = new TextButtonStyle();
        styleVibration.font = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        styleVibration.fontColor = Settings.isVibrationEnabled() ? Color.YELLOW : Color.WHITE;
        vibration = new TextButton("vibration", styleVibration);


        back = new TextButton("back", buttonStyle);
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
                dispose();
                game.goToScreen(Level1Screen.class);
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
                displayScores = new TextButton(Settings.getHighscoreString(), buttonStyle);
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
                    retroPad.getStyle().fontColor = Color.YELLOW;
                    virtualPad.getStyle().fontColor = Color.WHITE;
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
                    virtualPad.getStyle().fontColor = Color.YELLOW;
                    retroPad.getStyle().fontColor = Color.WHITE;

                }
                return true;
            }
        });

        lightFx.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                Settings.changeLightFXEnabled();
                lightFx.getStyle().fontColor = Settings.isLightFXEnabled() ? Color.YELLOW : Color.WHITE;
                return true;
            }
        });

        vibration.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                assets.playSound(Assets.MENU_CLICK);
                Settings.changeVibrationEnabled();
                vibration.getStyle().fontColor = Settings.isVibrationEnabled() ? Color.YELLOW : Color.WHITE;
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
        leaderboard = new ImageButton((new TextureRegionDrawable(atlas.findRegion("leaderboard"))));
        leaderboard_off = new ImageButton((new TextureRegionDrawable(atlas.findRegion("leaderboard_off"))));
        achievements.setPosition(150f, 10f);
        leaderboard.setPosition(200f, 10f);
        achievements_off.setPosition(150f, 10f);
        leaderboard_off.setPosition(200f, 10f);
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
        leaderboard.addListener(new

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
            stage.addActor(leaderboard);
        } else {
            stage.addActor(gplayOff);
            stage.addActor(achievements_off);
            stage.addActor(leaderboard_off);
        }

        table = new Table();

        helpScreen = new ImageButton(new TextureRegionDrawable(atlas.findRegion("help")));
        helpScreen.setColor(new Color(1f, 1f, 1f, 0.6f));
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
        creditsScreen.setColor(new Color(1f, 1f, 1f, 0.6f));
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
        font = assets.get(Assets.FONT_SPACE_KILLER_LARGE);
        fontVersion = new BitmapFont();
        fontVersion.setColor(Color.WHITE);
        fontVersion.getData().setScale(0.5f);

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
        font.draw(batcher, "SPACE", SCREEN_WIDTH / 5f - 15f, SCREEN_HEIGHT * 3 / 4 + 100f);
        font.draw(batcher, "KILLER", SCREEN_WIDTH / 5f - 40f, SCREEN_HEIGHT * 3 / 4 + 50f);
        fontVersion.draw(batcher, SpaceKillerGameConstants.GAME_VERSION, 10f, 20f);
        if (!game.playServices.isSignedIn()) {
            fontVersion.draw(batcher, " Google Play\nPlease sign in", 90f, 60f);
        }
        batcher.end();
    }


    @Override
    public void dispose() {
        batcher.dispose();
        assets.unloadResources(this.getClass());
        stage.dispose();
    }

    public void signInSucceeded() {
        stage.addActor(gplay);
        stage.addActor(leaderboard);
        stage.addActor(achievements);
        gplayOff.remove();
        achievements_off.remove();
        leaderboard_off.remove();

    }

    public void signInFailed() {
        stage.addActor(gplayOff);
        stage.addActor(leaderboard_off);
        stage.addActor(achievements_off);
        achievements.remove();
        leaderboard.remove();
        gplay.remove();
    }


}
