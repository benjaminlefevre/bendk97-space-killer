/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.bendk97.components.*;
import com.bendk97.components.helpers.ComponentMapperHelper;

import static com.bendk97.pools.GamePools.poolVector2;

public class FollowPlayerSystem extends IteratingSystem {

    private final Family player = Family.one(PlayerComponent.class).get();

    public FollowPlayerSystem(int priority) {
        super(Family.all(FollowPlayerComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        FollowPlayerComponent followPlayerComponent = ComponentMapperHelper.follow.get(entity);
        if (followPlayerComponent.rotate) {
            followByRotation(entity);
        } else {
            followPlayerComponent.lastMove += deltaTime;
            if (followPlayerComponent.lastMove > 0.5) {
                followPlayerComponent.lastMove = 0;
                PositionComponent playerPosition = ComponentMapperHelper.position.get(getEngine().getEntitiesFor(player).first());
                PositionComponent entityPosition = ComponentMapperHelper.position.get(entity);
                VelocityComponent velocityComponent = ComponentMapperHelper.velocity.get(entity);
                float diff = entityPosition.x() - playerPosition.x();
                if (Math.abs(diff) < 1) {
                    velocityComponent.x = 0;
                } else {
                    velocityComponent.x = -Math.signum(diff) * ComponentMapperHelper.follow.get(entity).velocity;
                }
            }
        }
    }

    private void followByRotation(Entity entity) {
        SpriteComponent spriteComponent = ComponentMapperHelper.sprite.get(entity);
        PositionComponent positionComponent = ComponentMapperHelper.position.get(entity);
        PositionComponent playerPosition = ComponentMapperHelper.position.get(getEngine().getEntitiesFor(player).first());
        Vector2 v1 = poolVector2.getVector2(0,-1);
        Vector2 v2 = poolVector2.getVector2(playerPosition.x()-positionComponent.x(), playerPosition.y()-positionComponent.y());
        float angle = v2.angle(v1);
        poolVector2.free(v1, v2);
        spriteComponent.sprite.setRotation(-angle);
    }

}