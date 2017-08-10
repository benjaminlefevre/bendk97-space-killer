package com.benk97;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.benk97.ads.AdsController;
import com.benk97.assets.Assets;
import com.benk97.google.PlayServices;
import com.benk97.player.PlayerData;
import com.benk97.screens.*;
import com.benk97.share.IntentShare;

import java.io.File;

import static com.benk97.SpaceKillerGameConstants.SKIP_SPLASH;

public class SpaceKillerGame extends Game {
    private Assets assets = new Assets();
    private AdsController adsController;
    public PlayServices playServices;
    public Screen currentScreen;
    public PlayerData playerData;
    public IntentShare intentShare;

    public SpaceKillerGame(AdsController adsController, PlayServices playServices, IntentShare intentShare) {
        this.adsController = adsController;
        this.playServices = playServices;
        this.intentShare = intentShare;
    }

    public void askExtraLifeRewardWithAd() {
        adsController.showInterstitialAd();
    }

    private void cleanTempDirectory() {
        if (Gdx.files.isExternalStorageAvailable()) {
            final File directory = Gdx.files.external(SocialScoreScreen.TEMP_DIRECTORY).file();
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
                this.setScreen(new TransitionScreen(screenshot, currentScreen, this));
            } else {
                this.setScreen(currentScreen);
            }
        } catch (Exception e) {
            Gdx.app.log("Guru Meditation", "error: " + e.getMessage());
            Gdx.app.exit();
        }
    }


    @Override
    public void render() {
        super.render();
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
