    /*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.inputs;

    import com.badlogic.gdx.Input;
    import com.badlogic.gdx.InputAdapter;
    import com.badlogic.gdx.graphics.Camera;
    import com.badlogic.gdx.math.Rectangle;
    import com.badlogic.gdx.math.Vector3;


    public abstract class TouchInputProcessor extends InputAdapter {
    protected com.bendk97.listeners.InputListener listener;

    protected Camera camera;
    protected Rectangle[] squareTouches;
    protected Rectangle bombButton;
    protected com.bendk97.screens.LevelScreen screen;

    public TouchInputProcessor(com.bendk97.screens.LevelScreen screen, com.bendk97.listeners.InputListener inputListener, Camera camera, Rectangle bombButton) {
        this.listener = inputListener;
        this.screen = screen;
        this.camera = camera;
        this.bombButton = bombButton;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            screen.pause();
            return true;
        }
        return false;
    }

    protected void moveShip(Vector3 worldTouch) {
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
    }
}
