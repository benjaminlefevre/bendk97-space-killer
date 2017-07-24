package com.benk97.components;

import box2dLight.Light;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class LightComponent implements Component, Pool.Poolable {
    public Light light;
    public Pool lights;

    @Override
    public void reset() {
        if (light != null) {
            light.setActive(false);
            lights.free(light);
        }
    }
}
