/*
 * Developed by Benjamin Lefèvre
 * Last modified 30/10/18 22:51
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.pools;

import com.badlogic.gdx.math.Circle;

public class PoolCircle extends GamePool<Circle> {

    protected PoolCircle(int max) {
        super(Circle.class, max);
    }

    @Override
    public void reset(Circle circle) {
        circle.radius = 0f;
        circle.x = 0f;
        circle.y = 0f;
    }
}
