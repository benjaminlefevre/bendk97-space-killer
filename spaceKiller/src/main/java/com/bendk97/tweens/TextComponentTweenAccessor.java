/*
 * Developed by Benjamin Lefèvre
 * Last modified 28/10/18 19:07
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.bendk97.components.texts.TextComponent;

public class TextComponentTweenAccessor implements TweenAccessor<TextComponent> {

    public final static int POSY = 0;

    @Override
    public int getValues(TextComponent text, int type, float[] returnValues) {
        switch (type) {
            case POSY:
                returnValues[0] = text.posY;
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public void setValues(TextComponent text, int type, float[] newValues) {
        switch (type) {
            case POSY:
                text.posY = newValues[0];
                break;
            default:
                break;
        }
    }
}
