/*
 * Developed by Benjamin Lefèvre
 * Last modified 26/10/18 20:47
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class BitmapFontTweenAccessor implements TweenAccessor<BitmapFont> {

    public final static int ALPHA = 0;

    @Override
    public int getValues(BitmapFont font, int type, float[] returnValues) {
        switch (type) {
            case ALPHA:
                returnValues[0] = font.getColor().a;
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public void setValues(BitmapFont font, int type, float[] newValues) {
        switch (type) {
            case ALPHA:
                font.getColor().a = newValues[0];
                break;
            default:
                break;
        }
    }
}
