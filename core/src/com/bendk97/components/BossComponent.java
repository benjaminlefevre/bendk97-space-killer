/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BossComponent implements Component, Pool.Poolable {

    public boolean pleaseFire1 = false;
    public int minTriggerFire1 = 5;
    public float velocityFire1 = 0;
    public boolean pleaseFire2 = false;
    public int minTriggerFire2 = 5;
    public float velocityFire2 = 0;
    @Override
    public void reset() {
        pleaseFire1 = false;
        pleaseFire2 = false;
        minTriggerFire2 = 5;
        minTriggerFire1 = 5;
        velocityFire1 = 0;
        velocityFire2 = 0;
    }
}
