/*
 * Developed by Benjamin Lefèvre
 * Last modified 13/10/18 23:32
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.inputs.pad;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.bendk97.listeners.InputListener;
import com.bendk97.screens.levels.LevelScreen;

public class RetroPadController extends AbstractPadController {

    private final Rectangle fireButton;

    public RetroPadController(LevelScreen screen, InputListener inputListener, Camera camera, Rectangle[] squareTouches, Rectangle fireButton,
                              Rectangle bombButton) {
        super(screen, inputListener, camera, bombButton);
        this.squareTouches = squareTouches;
        this.fireButton = fireButton;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldTouch = camera.unproject(new Vector3(screenX, screenY, 0f));
        touchDragged(screenX, screenY, pointer);
        if (fireButton.contains(worldTouch.x, worldTouch.y)) {
            listener.fire();
        } else if (bombButton.contains(worldTouch.x, worldTouch.y)) {
            listener.dropBomb();
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 worldTouch = camera.unproject(new Vector3(screenX, screenY, 0f));
        if (!fireButton.contains(worldTouch.x, worldTouch.y)) {
            listener.stop();
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 worldTouch = camera.unproject(new Vector3(screenX, screenY, 0f));
        moveShip(worldTouch);
        return true;
    }

}
