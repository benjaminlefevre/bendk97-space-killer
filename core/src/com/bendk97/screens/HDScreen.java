package com.bendk97.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;


public abstract class HDScreen extends ScreenAdapter {


    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected SpaceKillerGame game;
    protected Assets assets;

    public HDScreen(SpaceKillerGame game, Assets assets, float width, float height) {
        this.game = game;
        this.assets = assets;
        this.camera = new OrthographicCamera();
        viewport = new StretchViewport(width, height, camera);
        camera.setToOrtho(false, width, height);
    }

    public HDScreen(SpaceKillerGame game, Assets assets) {
        this(game, assets, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public SpaceKillerGame getGame() {
        return game;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


}
