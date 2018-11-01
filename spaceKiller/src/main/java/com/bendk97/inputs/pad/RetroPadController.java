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

import static com.bendk97.pools.GamePools.poolVector3;

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
        Vector3 worldTouch = poolVector3.getVector3(screenX, screenY, 0f);
        worldTouch = camera.unproject(worldTouch);
        touchDragged(screenX, screenY, pointer);
        if (fireButton.contains(worldTouch.x, worldTouch.y)) {
            listener.fire();
        } else if (bombButton.contains(worldTouch.x, worldTouch.y)) {
            listener.dropBomb();
        }
        poolVector3.free(worldTouch);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 worldTouch = poolVector3.getVector3(screenX, screenY, 0f);
        worldTouch = camera.unproject(worldTouch);
        if (!fireButton.contains(worldTouch.x, worldTouch.y)) {
            listener.stop();
        }
        poolVector3.free(worldTouch);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 worldTouch = poolVector3.getVector3(screenX, screenY, 0f);
        worldTouch = camera.unproject(worldTouch);
        moveShip(worldTouch);
        poolVector3.free(worldTouch);
        return true;
    }

}
