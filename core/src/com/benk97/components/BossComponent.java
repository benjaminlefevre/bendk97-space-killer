package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BossComponent implements Component, Pool.Poolable {

    public boolean pleaseFire = false;

    @Override
    public void reset() {
        pleaseFire = true;
    }
}
