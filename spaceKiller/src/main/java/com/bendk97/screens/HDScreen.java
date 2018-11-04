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
import com.bendk97.assets.GameAssets;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;


public abstract class HDScreen extends ScreenAdapter {


    protected final Viewport viewport;
    protected final SpaceKillerGame game;
    protected final GameAssets assets;

    protected HDScreen(SpaceKillerGame game, GameAssets assets, float width, float height) {
        this.game = game;
        this.assets = assets;
        OrthographicCamera camera = new OrthographicCamera();
        viewport = new StretchViewport(width, height, camera);
        camera.setToOrtho(false, width, height);
    }

    protected HDScreen(SpaceKillerGame game, GameAssets assets) {
        this(game, assets, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


}
