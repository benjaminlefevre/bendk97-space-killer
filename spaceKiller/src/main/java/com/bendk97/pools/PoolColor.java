/*
 * Developed by Benjamin Lefèvre
 * Last modified 01/11/18 14:07
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.pools;

import com.badlogic.gdx.graphics.Color;

public class PoolColor extends GamePool<Color> {

    protected PoolColor(int max) {
        super(Color.class, max);
    }

    @Override
    public void reset(Color color) {
        // nothing special to do, color will be initizialed juster after obtain
    }

    public Color getColor(Color color) {
        Color newColor = obtain();
        newColor.set(color);
        return newColor;
    }
}
