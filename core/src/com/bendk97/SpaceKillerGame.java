/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 22:06
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.bendk97.assets.Assets;
import com.bendk97.google.PlayServices;
import com.bendk97.player.PlayerData;
import com.bendk97.screens.LevelScreen;
import com.bendk97.screens.MenuScreen;
import com.bendk97.screens.SplashScreen;
import com.bendk97.share.IntentShare;

import java.io.File;

import static com.bendk97.SpaceKillerGameConstants.SKIP_SPLASH;
import static com.bendk97.screens.SocialScoreScreen.TEMP_DIRECTORY;

public class SpaceKillerGame extends Game {
    private final Assets assets = new Assets();
    public final PlayServices playServices;
    public Screen currentScreen;
    public PlayerData playerData;
    public final IntentShare intentShare;
    public final String gameVersion;

    public SpaceKillerGame(PlayServices playServices, IntentShare intentShare, String version) {
        this.playServices = playServices;
        this.gameVersion = version;
        this.intentShare = intentShare;
        if (SpaceKillerGameConstants.DEBUG) {
            GLProfiler profiler = new GLProfiler(Gdx.graphics);
            profiler.enable();
        }
    }

    private void cleanTempDirectory() {
        if (Gdx.files.isExternalStorageAvailable()) {
            final File directory = Gdx.files.external(TEMP_DIRECTORY).file();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (File file : directory.listFiles()) {

                            file.delete();
                        }
                    } catch (Exception e) {
                    }
                }
            }).start();
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

    public void goToScreen(Class screen) {
        goToScreen(screen, null, null);
    }

    public void goToScreen(Class screen, PlayerData playerData, FrameBuffer screenshot) {
        try {
            assets.loadResources(screen);
            this.playerData = playerData;
            currentScreen = (Screen) screen.getConstructor(Assets.class, SpaceKillerGame.class).newInstance(assets, this);
            if (screenshot != null) {
                this.setScreen(new com.bendk97.screens.TransitionScreen(screenshot, currentScreen, this));
            } else {
                this.setScreen(currentScreen);
            }
        } catch (Exception e) {
            Gdx.app.log("Guru Meditation", "error: " + e.getMessage());
            Gdx.app.exit();
        } finally {
            Runtime.getRuntime().gc();
        }
    }


    @Override
    public void dispose() {
    }

    public void signInSucceeded() {
        if (currentScreen instanceof MenuScreen) {
            signInFailed = false;
            ((MenuScreen) currentScreen).signInSucceeded();
        }
    }

    public boolean signInFailed = false;

    public void signInFailed() {
        if (currentScreen instanceof MenuScreen) {
            signInFailed = true;
            ((MenuScreen) currentScreen).signInFailed();
        }
    }

    public void continueWithExtraLife() {
        if (currentScreen instanceof LevelScreen) {
            ((LevelScreen) currentScreen).continueWithExtraLife();
        }
    }
}
