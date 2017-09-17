package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class VelocityComponent implements Component, Poolable {
    public float x = 0.0f, y = 0.0f;

    @Override
    public void reset() {
        x = 0.0f;
        y = 0.0f;
    }
}
