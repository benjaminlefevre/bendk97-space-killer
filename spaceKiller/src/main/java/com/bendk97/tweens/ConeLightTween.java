/*
 * Developed by Benjamin Lefèvre
 * Last modified 16/10/18 18:31
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import box2dLight.ConeLight;

public class ConeLightTween implements TweenAccessor<ConeLight> {
    public final static int DISTANCE = 0;
    public final static int POSITION = 1;


    @Override
    public int getValues(ConeLight target, int type, float[] returnValues) {
        switch (type) {
            case DISTANCE:
                returnValues[0] = target.getDistance();
                return 1;
            case POSITION:
                returnValues[0] = target.getPosition().x;
                returnValues[1] = target.getPosition().y;
                return 2;
            default:
                return -1;
        }
    }

    @Override
    public void setValues(ConeLight target, int type, float[] newValues) {
        switch (type) {
            case DISTANCE:
                target.setDistance(newValues[0]);
                break;
            case POSITION:
                target.setPosition(newValues[0], newValues[1]);
                break;
            default:
                break;
        }
    }
}
