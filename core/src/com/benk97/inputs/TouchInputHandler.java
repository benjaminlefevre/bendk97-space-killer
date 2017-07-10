package com.benk97.inputs;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class TouchInputHandler implements InputProcessor {
    private InputHandler listener;
    private Rectangle[] squareTouches;
    private Camera camera;

    public TouchInputHandler(InputHandler inputHandler, Camera camera, Rectangle[] squareTouches) {
        this.listener = inputHandler;
        this.camera = camera;
        this.squareTouches = squareTouches;
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
        if (squareTouches[0].contains(worldTouch.x, worldTouch.y)) {
            listener.goLeftTop();
        } else if (squareTouches[1].contains(worldTouch.x, worldTouch.y)) {
            listener.goTop();
        } else if (squareTouches[2].contains(worldTouch.x, worldTouch.y)) {
            listener.goRightTop();
        } else if (squareTouches[3].contains(worldTouch.x, worldTouch.y)) {
            listener.goLeft();
        } else if (squareTouches[4].contains(worldTouch.x, worldTouch.y)) {
            listener.goRight();
        } else if (squareTouches[5].contains(worldTouch.x, worldTouch.y)) {
            listener.goLeftDown();
        } else if (squareTouches[6].contains(worldTouch.x, worldTouch.y)) {
            listener.goDown();
        } else if (squareTouches[7].contains(worldTouch.x, worldTouch.y)) {
            listener.goRightBottom();
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        listener.stop();
        return true;
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
