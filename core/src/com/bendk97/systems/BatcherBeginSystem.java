/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BatcherBeginSystem extends EntitySystem {

    private final SpriteBatch batcher;
    private final Viewport viewport;

    public BatcherBeginSystem(Viewport viewport, SpriteBatch batcher, int priority) {
        super(priority);
        this.batcher = batcher;
        this.viewport = viewport;
    }

    @Override
    public void update(float deltaTime) {
        viewport.apply();
        batcher.begin();
        batcher.setProjectionMatrix(viewport.getCamera().combined);

    }
}
