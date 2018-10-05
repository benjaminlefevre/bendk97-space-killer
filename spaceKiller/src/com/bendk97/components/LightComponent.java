/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import box2dLight.PointLight;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class LightComponent implements Component, Pool.Poolable {
    public PointLight light;
    public Pool<PointLight> lights;

    @Override
    public void reset() {
        if (light != null) {
            light.setActive(false);
            lights.free(light);
        }
    }
}
