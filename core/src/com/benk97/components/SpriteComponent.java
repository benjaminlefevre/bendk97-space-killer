package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;

public class SpriteComponent implements Component, Pool.Poolable {

    public Sprite sprite;
    public boolean stayInBoundaries = false;

    @Override
    public void reset() {
        sprite = null;
    }

}
