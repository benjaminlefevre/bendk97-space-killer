/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.inputs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;
import com.bendk97.components.Mappers;
import com.bendk97.screens.Level1Screen;
import com.bendk97.screens.MenuScreen;
import com.bendk97.screens.SocialScoreScreen;

import static com.bendk97.SpaceKillerGameConstants.*;


public class GameOverTouchInputProcessor implements InputProcessor {

    private final Camera camera;
    private final com.bendk97.SpaceKillerGame game;
    private final Rectangle playAgain;
    private final Rectangle home;
    private final Rectangle share;
    private final Rectangle extraLife;
    private final Assets assets;
    private final Entity player;

    public GameOverTouchInputProcessor(Camera camera, SpaceKillerGame game, Assets assets, Entity player) {
        this.camera = camera;
        this.player = player;
        this.game = game;
        this.assets = assets;
        this.playAgain = new Rectangle(PLAY_X, PLAY_Y, ICON_SIZE, ICON_SIZE);
        this.home = new Rectangle(HOME_X, HOME_Y, ICON_SIZE, ICON_SIZE);
        this.share = new Rectangle(SHARE_X, SHARE_Y, ICON_SIZE, ICON_SIZE);
        this.extraLife = new Rectangle(EXTRA_X, EXTRA_Y, ICON_SIZE, ICON_SIZE);
    }

    @Override
    public boolean keyDown(int keycode) {
        return keycode == Input.Keys.BACK;
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldTouch = camera.unproject(new Vector3(screenX, screenY, 0f));
        if (playAgain.contains(worldTouch.x, worldTouch.y)) {
            game.currentScreen.dispose();
            game.goToScreen(Level1Screen.class);
        } else if (home.contains(worldTouch.x, worldTouch.y)) {
            game.currentScreen.dispose();
            game.goToScreen(MenuScreen.class);
        } else if (share.contains(worldTouch.x, worldTouch.y)) {
            SocialScoreScreen socialScoreScreen = new SocialScoreScreen(assets, game, Mappers.player.get(player).getScoreInt());
            String filePath = socialScoreScreen.takeScreenshot();
            if (filePath != null) {
                game.intentShare.shareScore(filePath);
            }
        } else if (extraLife.contains(worldTouch.x, worldTouch.y)) {
            game.continueWithExtraLife();
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
