package com.benk97.inputs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.benk97.SpaceKillerGame;
import com.benk97.assets.Assets;
import com.benk97.components.Mappers;
import com.benk97.screens.Level1Screen;
import com.benk97.screens.MenuScreen;
import com.benk97.screens.SocialScoreScreen;

import static com.benk97.SpaceKillerGameConstants.*;


public class GameOverTouchInputProcessor implements InputProcessor {

    protected Camera camera;
    protected SpaceKillerGame game;
    protected Rectangle playAgain;
    protected Rectangle home;
    protected Rectangle share;
    protected Assets assets;
    protected Entity player;

    public GameOverTouchInputProcessor(Camera camera, SpaceKillerGame game, Assets assets, Entity player) {
        this.camera = camera;
        this.player = player;
        this.game = game;
        this.assets = assets;
        this.playAgain = new Rectangle(PLAY_X, PLAY_Y, ICON_SIZE, ICON_SIZE);
        this.home = new Rectangle(HOME_X, HOME_Y, ICON_SIZE, ICON_SIZE);
        this.share = new Rectangle(SHARE_X, SHARE_Y, ICON_SIZE, ICON_SIZE);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
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
