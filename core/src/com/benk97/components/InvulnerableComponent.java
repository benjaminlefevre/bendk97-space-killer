package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class InvulnerableComponent implements Component, Pool.Poolable{
    public int nbItems = 1;
    @Override
    public void reset() {
        nbItems = 1;

    }
}
