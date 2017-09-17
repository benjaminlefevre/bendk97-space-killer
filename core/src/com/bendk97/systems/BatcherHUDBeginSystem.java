package com.bendk97.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BatcherHUDBeginSystem extends BatcherBeginSystem {

    public BatcherHUDBeginSystem(Viewport viewport, SpriteBatch batcher, int priority) {
        super(viewport, batcher, priority);
    }

    @Override
    public void update(float delta){
        super.update(delta);
    }
}
