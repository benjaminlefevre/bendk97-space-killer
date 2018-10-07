/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;


abstract class HDScreen extends ScreenAdapter {


    final Viewport viewport;
    final SpaceKillerGame game;
    final Assets assets;

    HDScreen(SpaceKillerGame game, Assets assets, float width, float height) {
        this.game = game;
        this.assets = assets;
        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(width, height, camera);
        camera.setToOrtho(false, width, height);
    }

    HDScreen(SpaceKillerGame game, Assets assets) {
        this(game, assets, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


}
