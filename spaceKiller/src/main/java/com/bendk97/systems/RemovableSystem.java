/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.RemovableComponent;
import com.bendk97.components.SpriteComponent;
import com.bendk97.components.VelocityComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;

import static com.bendk97.SpaceKillerGameConstants.*;

public class RemovableSystem extends IteratingSystem {

    public RemovableSystem(int priority) {
        super(Family.all(RemovableComponent.class, PositionComponent.class, VelocityComponent.class).get(), priority);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        RemovableComponent removableComponent = ComponentMapperHelper.removable.get(entity);
        if (removableComponent.hasDuration) {
            checkIfDurationIsOver(entity, deltaTime, removableComponent);
        } else {
            PositionComponent position = ComponentMapperHelper.position.get(entity);
            SpriteComponent sprite = ComponentMapperHelper.sprite.get(entity);
            if (position.x + sprite.sprite.getWidth() < -OFFSET_WIDTH
                    || position.x > SCREEN_WIDTH + OFFSET_WIDTH
                    || position.y > SCREEN_HEIGHT
                    || position.y + sprite.sprite.getHeight() < 0) {
                getEngine().removeEntity(entity);
            }
        }
    }

    private void checkIfDurationIsOver(Entity entity, float deltaTime, RemovableComponent removableComponent) {
        removableComponent.elapseTime += deltaTime;
        if (removableComponent.elapseTime > removableComponent.duration) {
            getEngine().removeEntity(entity);
        }
    }
}
