/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class InvulnerableComponent implements Component, Pool.Poolable{
    public int nbItems = 1;
    @Override
    public void reset() {
        nbItems = 1;

    }
}
