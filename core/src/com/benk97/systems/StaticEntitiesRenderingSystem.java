package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benk97.components.Mappers;
import com.benk97.components.PositionComponent;
import com.benk97.components.SpriteComponent;

public class StaticEntitiesRenderingSystem extends IteratingSystem {
    private SpriteBatch batcher;

    public StaticEntitiesRenderingSystem(SpriteBatch batcher, int priority) {
        super(Family.all(SpriteComponent.class).exclude(PositionComponent.class).get(), priority);
        this.batcher = batcher;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SpriteComponent staticSpriteComponent = Mappers.sprite.get(entity);
        staticSpriteComponent.sprite.draw(batcher, staticSpriteComponent.alpha);
    }
}
