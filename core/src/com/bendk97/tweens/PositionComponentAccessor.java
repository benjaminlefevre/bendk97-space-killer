/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.bendk97.components.PositionComponent;

public class PositionComponentAccessor implements TweenAccessor<com.bendk97.components.PositionComponent> {
    public final static int POSITION_X = 0;
    public final static int POSITION_Y = 1;
    public final static int POSITION_XY = 2;

    @Override
    public int getValues(PositionComponent target, int type, float[] returnValues) {
        switch (type) {
            case POSITION_X:
                returnValues[0] = target.x;
                return 1;
            case POSITION_Y:
                returnValues[0] = target.y;
                return 1;
            case POSITION_XY:
                returnValues[0] = target.x;
                returnValues[1] = target.y;
                return 2;
            default:
                return -1;
        }
    }

    @Override
    public void setValues(PositionComponent target, int type, float[] newValues) {
        switch (type) {
            case POSITION_X:
                target.x = newValues[0];
                break;
            case POSITION_Y:
                target.y = newValues[0];
                break;
            case POSITION_XY:
                target.x = newValues[0];
                target.y = newValues[1];
                break;
            default:
                break;
        }
    }
}
