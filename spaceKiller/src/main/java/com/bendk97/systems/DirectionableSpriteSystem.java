/*
 * Developed by Benjamin Lefèvre
 * Last modified 02/11/18 09:32
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.bendk97.components.DirectionableComponent;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.SpriteComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;

import static com.bendk97.pools.GamePools.poolVector2;

public class DirectionableSpriteSystem extends IteratingSystem {

    public DirectionableSpriteSystem(int priority) {
        super(Family.all(DirectionableComponent.class).get(),priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = ComponentMapperHelper.position.get(entity);
        SpriteComponent spriteComponent = ComponentMapperHelper.sprite.get(entity);
        Vector2 origin = poolVector2.getVector2(0, -1);
        Vector2 direction =  poolVector2.getVector2(positionComponent.x()-positionComponent.previousX(),
                positionComponent.y()-positionComponent.previousY());
        spriteComponent.sprite.setRotation(-direction.angle(origin));
        poolVector2.free(origin);
        poolVector2.free(direction);
    }
}
