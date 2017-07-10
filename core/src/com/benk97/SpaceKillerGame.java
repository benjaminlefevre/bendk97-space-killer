package com.benk97;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.benk97.assets.Assets;
import com.benk97.screens.Level1Screen;

public class SpaceKillerGame extends Game {
    private Assets assets = new Assets();

    public SpaceKillerGame() {
    }

    @Override
    public void create() {
        goToScreen(Level1Screen.class);
    }

    public void goToScreen(Class screen) {
        try {
            assets.loadResources(screen);
            this.setScreen((Screen) screen.getConstructor(Assets.class).newInstance(assets));
        } catch (Exception e) {
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
