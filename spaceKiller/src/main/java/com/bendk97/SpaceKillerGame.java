/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 22:06
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;
import com.bendk97.assets.GameAssets;
import com.bendk97.google.PlayServices;
import com.bendk97.player.PlayerData;
import com.bendk97.screens.SplashScreen;
import com.bendk97.screens.levels.LevelScreen;
import com.bendk97.screens.levels.utils.TransitionScreen;
import com.bendk97.screens.menu.MenuScreen;
import com.bendk97.share.IntentShare;

import java.io.File;

import static com.bendk97.SpaceKillerGameConstants.SKIP_SPLASH;
import static com.bendk97.screens.SocialScoreScreen.TEMP_DIRECTORY;

public class SpaceKillerGame extends Game implements Disposable {
    private static final String INTENT_FILES = "intent files";
    private static final String WAS_UNABLE_TO_CLEAN_TEMP_DIRECTORY = "was unable to clean temp directory";
    private final GameAssets assets = new GameAssets();
    public final PlayServices playServices;
    public PlayerData playerData;
    public final IntentShare intentShare;
    public boolean signInFailed = false;


    public SpaceKillerGame(PlayServices playServices, IntentShare intentShare) {
        this.playServices = playServices;
        this.intentShare = intentShare;
    }

    private void cleanTempDirectory() {
        if (Gdx.files.isExternalStorageAvailable()) {
            final File[] tempDirectoryFiles = Gdx.files.external(TEMP_DIRECTORY).file().listFiles();
            if (tempDirectoryFiles != null) {
                new Thread(() -> {
                    try {
                        for (File file : tempDirectoryFiles) {
                            if (!file.delete()) {
                                Gdx.app.log(INTENT_FILES, WAS_UNABLE_TO_CLEAN_TEMP_DIRECTORY);
                            }
                        }

                    } catch (Exception e) {
                        Gdx.app.log(INTENT_FILES, WAS_UNABLE_TO_CLEAN_TEMP_DIRECTORY);
                    }
                }).start();
            }
        }
    }

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        cleanTempDirectory();
        if (SKIP_SPLASH) {
            goToScreen(MenuScreen.class);
        } else {
            goToScreen(SplashScreen.class);
        }
    }

    public void goToScreen(Class<? extends Screen> newScreen) {
        goToScreen(newScreen, null, null);
    }

    public void goToScreen(Class<? extends Screen> newScreen, PlayerData playerData, Sprite previousScreenSprite) {
        try {
            if(this.getScreen() != null) {
                screen.dispose();
            }
            assets.loadResources(screen != null ? screen.getClass() : null, newScreen);
            this.playerData = playerData;
            //noinspection JavaReflectionMemberAccess
            Screen nextScreen = newScreen.getConstructor(GameAssets.class, SpaceKillerGame.class).newInstance(assets, this);
            if (previousScreenSprite != null && LevelScreen.class.isAssignableFrom(newScreen)) {
                LevelScreen nextLevelScreen = (LevelScreen) nextScreen;
                Sprite nextScreenSprite = nextLevelScreen.takeScreenshot(Gdx.graphics.getDeltaTime(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                this.setScreen(new TransitionScreen(previousScreenSprite, nextScreenSprite, nextLevelScreen, this));
            } else {
                this.setScreen(nextScreen);
            }
        } catch (Exception e) {
            Gdx.app.log("Guru Meditation", "error: " + e.getMessage(), e);
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        // Nothing to dispose here ... Really ? huh
    }

    public void signInSucceeded() {
        if (screen instanceof MenuScreen) {
            signInFailed = false;
            ((MenuScreen) screen).signInSucceeded();
        }
    }

    public void signInFailed() {
        if (screen instanceof MenuScreen) {
            signInFailed = true;
            ((MenuScreen) screen).signInFailed();
        }
    }

    public void continueWithExtraLife() {
        if (screen instanceof LevelScreen) {
            ((LevelScreen) screen).continueWithExtraLife();
        }
    }
}
