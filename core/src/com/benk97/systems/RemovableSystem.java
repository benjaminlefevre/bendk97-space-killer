package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.benk97.components.*;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class RemovableSystem extends IteratingSystem {
    PooledEngine engine;

    public RemovableSystem(PooledEngine engine) {
        super(Family.all(RemovableComponent.class, PositionComponent.class, VelocityComponent.class).get(), 5);
        this.engine = engine;
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.position.get(entity);
        SpriteComponent sprite = Mappers.sprite.get(entity);
        if (position.x + sprite.sprite.getWidth() < 0
                || position.x > SCREEN_WIDTH
                || position.y > SCREEN_HEIGHT
                || position.y + sprite.sprite.getHeight() < 0) {
            engine.removeEntity(entity);
        }
    }
}
