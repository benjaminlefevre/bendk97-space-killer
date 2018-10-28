/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class RemovableComponent implements Pool.Poolable, Component {

    public boolean hasDuration = false;
    public float duration = 0f;
    public float elapseTime = 0f;

    @Override
    public void reset() {
        hasDuration = false;
        duration = 0f;
        elapseTime = 0f;
    }

    public void setDuration(float duration) {
        this.hasDuration = true;
        this.duration = duration;
    }
}
