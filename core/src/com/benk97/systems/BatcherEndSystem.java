package com.benk97.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BatcherEndSystem extends EntitySystem {

    private SpriteBatch batcher;
    private Viewport viewport;

    public BatcherEndSystem(Viewport viewport, SpriteBatch batcher, int priority) {
        super(priority);
        this.batcher = batcher;
        this.viewport = viewport;
    }

    @Override
    public void update(float deltaTime) {
        batcher.end();

    }
}
