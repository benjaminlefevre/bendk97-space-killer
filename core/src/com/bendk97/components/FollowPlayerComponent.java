/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class FollowPlayerComponent implements Component, Pool.Poolable{
    public float velocity = 0f;
    public float lastMove = 100f;
    public boolean rotate = false;
    @Override
    public void reset() {
        velocity = 0f;
        lastMove = 100f;
        rotate = false;
    }
}
