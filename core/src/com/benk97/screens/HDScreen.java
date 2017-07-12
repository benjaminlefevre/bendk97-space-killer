package com.benk97.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.benk97.SpaceKillerGame;
import com.benk97.assets.Assets;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;


public abstract class HDScreen extends ScreenAdapter {

    public static final float VIEWPORT_WIDTH = SCREEN_WIDTH;
    public static final float VIEWPORT_HEIGHT = SCREEN_HEIGHT;
    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected SpaceKillerGame game;
    protected Assets assets;

    public HDScreen(SpaceKillerGame game, Assets assets) {
        this.game = game;
        this.assets = assets;
        this.camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    }

    public SpaceKillerGame getGame() {
        return game;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


}
