package com.benk97.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.benk97.components.DynamicSpriteComponent;
import com.benk97.components.PositionComponent;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class RemovableSystem extends IteratingSystem {
    PooledEngine engine;

    public RemovableSystem(PooledEngine engine, Family family) {
        super(family, 0);
        this.engine = engine;
    }

    ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    ComponentMapper<DynamicSpriteComponent> dm = ComponentMapper.getFor(DynamicSpriteComponent.class);

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = pm.get(entity);
        DynamicSpriteComponent sprite = dm.get(entity);
        if (position.x + sprite.getWidth() < 0
                || position.x > SCREEN_WIDTH
                || position.y > SCREEN_HEIGHT
                || position.y + sprite.getHeight() < 0) {
            engine.removeEntity(entity);
        }
    }
}
