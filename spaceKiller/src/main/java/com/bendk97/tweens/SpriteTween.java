/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteTween implements TweenAccessor<Sprite> {
    public final static int POS_XY = 0;
    public final static int ROTATION = 1;
    public final static int SCALE = 2;

    @Override
    public int getValues(Sprite target, int type, float[] returnValues) {
        switch (type) {
            case POS_XY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            case ROTATION:
                returnValues[0] = target.getRotation();
                return 1;
            case SCALE:
                returnValues[0] = target.getScaleX();
                returnValues[1] = target.getScaleY();
                return 2;
            default:
                return -1;
        }
    }

    @Override
    public void setValues(Sprite target, int type, float[] newValues) {
        switch (type) {
            case POS_XY:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                break;
            case ROTATION:
                target.setRotation(newValues[0]);
                break;
            case SCALE:
                target.setScale(newValues[0], newValues[1]);
                break;
            default:
                break;
        }
    }
}
