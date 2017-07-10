package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;

public class AnimationComponent implements Component, Pool.Poolable {
    public IntMap<Animation<Sprite>> animations = new IntMap<Animation<Sprite>>();

    @Override
    public void reset() {
        animations = new IntMap<Animation<Sprite>>();
    }
}
