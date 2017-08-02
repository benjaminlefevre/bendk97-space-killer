package com.benk97.inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.benk97.screens.LevelScreen;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;


public class PauseInputProcessor implements InputProcessor {

    protected Camera camera;
    protected LevelScreen screen;
    protected Rectangle resume;
    protected Rectangle quit;

    public PauseInputProcessor(Camera camera, LevelScreen screen) {
        this.camera = camera;
        this.screen = screen;
        this.resume = new Rectangle(50f, SCREEN_HEIGHT * 2f / 3f, 300f, 35f);
        this.quit = new Rectangle(100f, SCREEN_HEIGHT / 2f, 150f, 35f);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            return true;
        }
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
        if (resume.contains(worldTouch.x, worldTouch.y)) {
            screen.resumeGame();
        } else if (quit.contains(worldTouch.x, worldTouch.y)) {
            screen.quitGame();
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
