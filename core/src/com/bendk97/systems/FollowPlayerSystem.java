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
import com.bendk97.components.Mappers;

public class FollowPlayerSystem extends IteratingSystem {

    private Family player = Family.one(com.bendk97.components.PlayerComponent.class).get();

    public FollowPlayerSystem(int priority) {
        super(Family.all(com.bendk97.components.FollowPlayerComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        com.bendk97.components.FollowPlayerComponent followPlayerComponent = com.bendk97.components.Mappers.follow.get(entity);
        if (followPlayerComponent.rotate) {
            followByRotation(entity);
        } else {
            followPlayerComponent.lastMove += deltaTime;
            if (followPlayerComponent.lastMove > 0.5) {
                followPlayerComponent.lastMove = 0;
                com.bendk97.components.PositionComponent playerPosition = com.bendk97.components.Mappers.position.get(getEngine().getEntitiesFor(player).first());
                com.bendk97.components.PositionComponent entityPosition = com.bendk97.components.Mappers.position.get(entity);
                com.bendk97.components.VelocityComponent velocityComponent = com.bendk97.components.Mappers.velocity.get(entity);
                float diff = entityPosition.x - playerPosition.x;
                if (Math.abs(diff) < 1) {
                    velocityComponent.x = 0;
                } else {
                    velocityComponent.x = -Math.signum(diff) * com.bendk97.components.Mappers.follow.get(entity).velocity;
                }
            }
        }
    }

    private void followByRotation(Entity entity) {
        com.bendk97.components.SpriteComponent spriteComponent = com.bendk97.components.Mappers.sprite.get(entity);
        com.bendk97.components.PositionComponent positionComponent = com.bendk97.components.Mappers.position.get(entity);
        com.bendk97.components.PositionComponent playerPosition = Mappers.position.get(getEngine().getEntitiesFor(player).first());
        Vector2 v1 = new Vector2(0,-1);
        Vector2 v2 = new Vector2(playerPosition.x-positionComponent.x, playerPosition.y-positionComponent.y);
        float angle = v2.angle(v1);
        spriteComponent.sprite.setRotation(-angle);
    }

}