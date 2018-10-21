/*
 * Developed by Benjamin Lefèvre
 * Last modified 21/10/18 12:03
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.bendk97.components.LightComponent;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.VelocityComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;

public abstract class AbstractMovementSystem extends IteratingSystem {

    public AbstractMovementSystem(Family family, int priority) {
        super(family, priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = ComponentMapperHelper.position.get(entity);
        VelocityComponent velocity = ComponentMapperHelper.velocity.get(entity);
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
        LightComponent light = ComponentMapperHelper.light.get(entity);
        if (light != null) {
            Sprite sprite = ComponentMapperHelper.sprite.get(entity).sprite;
            light.light.setPosition(position.x + sprite.getWidth() / 2f,
                    position.y + sprite.getHeight() / 2f);
        }
    }
}