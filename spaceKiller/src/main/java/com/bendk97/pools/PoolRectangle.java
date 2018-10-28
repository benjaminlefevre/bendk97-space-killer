/*
 * Developed by Benjamin Lefèvre
 * Last modified 30/10/18 22:53
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.pools;

import com.badlogic.gdx.math.Rectangle;

public class PoolRectangle extends GamePool<Rectangle> {

    protected PoolRectangle(int max) {
        super(Rectangle.class, max);
    }

    @Override
    public void reset(Rectangle rectangle) {
        rectangle.set(0, 0, 0, 0);
    }
}
