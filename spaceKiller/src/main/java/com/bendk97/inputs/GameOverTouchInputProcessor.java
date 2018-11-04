/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.inputs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.GameAssets;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.screens.SocialScoreScreen;
import com.bendk97.screens.levels.Level1Screen;
import com.bendk97.screens.menu.MenuScreen;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.pools.GamePools.poolVector3;


public class GameOverTouchInputProcessor extends InputAdapter {

    private final Camera camera;
    private final SpaceKillerGame game;
    private final Rectangle playAgain;
    private final Rectangle home;
    private final Rectangle share;
    private final Rectangle extraLife;
    private final GameAssets assets;
    private final Entity player;

    public GameOverTouchInputProcessor(Camera camera, SpaceKillerGame game, GameAssets assets, Entity player) {
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldTouch = poolVector3.getVector3(screenX, screenY, 0f);
        worldTouch = camera.unproject(worldTouch);
        if (playAgain.contains(worldTouch.x, worldTouch.y)) {
            game.goToScreen(Level1Screen.class);
        } else if (home.contains(worldTouch.x, worldTouch.y)) {
            game.goToScreen(MenuScreen.class);
        } else if (share.contains(worldTouch.x, worldTouch.y)) {
            SocialScoreScreen socialScoreScreen = new SocialScoreScreen(assets, game, ComponentMapperHelper.player.get(player).getScoreInt());
            String filePath = socialScoreScreen.takeScreenshot();
            if (filePath != null) {
                game.intentShare.shareScore(filePath);
            }
        } else if (extraLife.contains(worldTouch.x, worldTouch.y)) {
            game.continueWithExtraLife();
        }
        poolVector3.free(worldTouch);
        return true;
    }
}
