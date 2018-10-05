/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraTween implements TweenAccessor<OrthographicCamera> {
    public final static int ZOOM = 0;
    public final static int X = 1;
    public final static int Y = 2;

    @Override
    public int getValues(OrthographicCamera target, int type, float[] returnValues) {
        switch (type) {
            case ZOOM:
                returnValues[0] = target.zoom;
                return 1;
            case X:
                returnValues[0] = target.position.x;
                return 1;
            case Y:
                returnValues[0] = target.position.y;
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public void setValues(OrthographicCamera target, int type, float[] newValues) {
        switch (type) {
            case ZOOM:
                target.zoom = newValues[0];
                break;
            case X:
                target.position.x = newValues[0];
                break;
            case Y:
                target.position.y = newValues[0];
                break;
            default:
                break;
        }
    }
}
