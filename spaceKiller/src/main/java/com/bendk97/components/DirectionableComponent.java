/*
 * Developed by Benjamin Lefèvre
 * Last modified 02/11/18 08:48
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DirectionableComponent implements Component, Pool.Poolable {
    @Override
    public void reset() {
        // nothing to reset
    }
}
