package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import static com.benk97.SpaceKillerGameConstants.ANIMATION_MAIN;

public class StateComponent implements Component, Pool.Poolable {
    private int state = ANIMATION_MAIN;
    public float time = 0.0f;

    public int get() {
        return state;
    }

    public void set(int newState) {
        state = newState;
        time = 0.0f;
    }

    @Override
    public void reset() {
        this.state = 0;
        this.time = 0.0f;
    }
}