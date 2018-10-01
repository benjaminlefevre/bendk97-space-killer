/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.inputs;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bendk97.screens.LevelScreen;

public class GestureHandler extends GestureAdapter {
    private final LevelScreen levelScreen;
    private final Camera camera;

    public GestureHandler(LevelScreen screen, Camera camera) {
        this.levelScreen = screen;
        this.camera = camera;
    }


    @Override
    public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        Vector3 d1 = camera.unproject(new Vector3(initialPointer1,0f));
        Vector3 d2 = camera.unproject(new Vector3(initialPointer2,0f));
        Vector3 d3 = camera.unproject(new Vector3(pointer1,0f));
        Vector3 d4 = camera.unproject(new Vector3(pointer2,0f));
        if(d1.dst(d2)>150 && d3.dst(d4)<100){
            levelScreen.pause();
            return true;
        }else {
            return false;
        }

    }

}
