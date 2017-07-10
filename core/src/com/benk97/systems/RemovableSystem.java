package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.benk97.components.*;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class RemovableSystem extends IteratingSystem {

    public RemovableSystem(int priority) {
        super(Family.all(RemovableComponent.class, PositionComponent.class, VelocityComponent.class).get(), priority);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.position.get(entity);
        SpriteComponent sprite = Mappers.sprite.get(entity);
        if (position.x + sprite.sprite.getWidth() < 0
                || position.x > SCREEN_WIDTH
                || position.y > SCREEN_HEIGHT
                || position.y + sprite.sprite.getHeight() < 0) {
            getEngine().removeEntity(entity);
        }
    }
}
