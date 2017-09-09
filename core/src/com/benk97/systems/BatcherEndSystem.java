package com.benk97.systems;

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
