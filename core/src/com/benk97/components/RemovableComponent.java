package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class RemovableComponent implements Pool.Poolable, Component {

    public float elapseTime = 0f;

    @Override
    public void reset() {
        elapseTime = 0f;
    }
}
