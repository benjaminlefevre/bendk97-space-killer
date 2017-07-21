package com.benk97;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.benk97.ads.AdsController;
import com.benk97.assets.Assets;
import com.benk97.screens.MenuScreen;
import com.benk97.screens.SplashScreen;

import static com.benk97.SpaceKillerGameConstants.SKIP_SPLASH;

public class SpaceKillerGame extends Game {
    private Assets assets = new Assets();
    private AdsController adsController;

    public SpaceKillerGame(AdsController adsController) {
        this.adsController = adsController;
    }

    public void showAd() {
        adsController.showInterstitialAd();
    }

    @Override
    public void create() {
        if (SKIP_SPLASH) {
            goToScreen(MenuScreen.class);
        } else {
            goToScreen(SplashScreen.class);
        }
    }

    public void goToScreen(Class screen) {
        try {
            assets.loadResources(screen);
            this.setScreen((Screen) screen.getConstructor(Assets.class, SpaceKillerGame.class).newInstance(assets, this));
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
}
