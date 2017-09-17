package com.bendk97.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BatcherBeginSystem extends EntitySystem {

    private SpriteBatch batcher;
    private Viewport viewport;

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
