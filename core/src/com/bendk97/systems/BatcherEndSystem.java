/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BatcherEndSystem extends EntitySystem {

    private SpriteBatch batcher;
    public BatcherEndSystem(SpriteBatch batcher, int priority) {
        super(priority);
        this.batcher = batcher;
    }

    @Override
    public void update(float deltaTime) {
        batcher.end();

    }
}
