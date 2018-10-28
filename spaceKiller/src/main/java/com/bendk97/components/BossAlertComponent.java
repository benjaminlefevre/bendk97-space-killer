/*
 * Developed by Benjamin Lefèvre
 * Last modified 28/10/18 12:33
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BossAlertComponent implements Component, Pool.Poolable {
    @Override
    public void reset() {
        // Nothing to reset, this component is stateless
    }
}
