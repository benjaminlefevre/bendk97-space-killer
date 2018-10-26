/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.bendk97.components.SpriteComponent;

public class SpriteComponentTweenAccessor implements TweenAccessor<SpriteComponent> {

    public final static int ALPHA = 0;

    @Override
    public int getValues(SpriteComponent sprite, int type, float[] returnValues) {
        switch (type) {
            case ALPHA:
                returnValues[0] = sprite.alpha;
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public void setValues(SpriteComponent sprite, int type, float[] newValues) {
        switch (type) {
            case ALPHA:
                sprite.alpha = newValues[0];
                break;
            default:
                break;
        }
    }
}
