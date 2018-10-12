/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.bendk97.components.*;
import com.bendk97.components.helpers.ComponentMapperHelper;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class RemovableSystem extends IteratingSystem {

    public RemovableSystem(int priority) {
        super(Family.all(RemovableComponent.class, PositionComponent.class, VelocityComponent.class).get(), priority);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        RemovableComponent removableComponent = ComponentMapperHelper.removable.get(entity);
        PositionComponent position = ComponentMapperHelper.position.get(entity);
        SpriteComponent sprite = ComponentMapperHelper.sprite.get(entity);
        removableComponent.elapseTime += deltaTime;
        if ((removableComponent.elapseTime >= 2.0f || entity.getComponent(PlayerBulletComponent.class) != null) &&
                (position.x + sprite.sprite.getWidth() < 0
                        || position.x > SCREEN_WIDTH
                        || position.y > SCREEN_HEIGHT
                        || position.y + sprite.sprite.getHeight() < 0)) {
            getEngine().removeEntity(entity);
        }
    }
}
