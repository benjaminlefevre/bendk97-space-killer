/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.bendk97.components.GameOverComponent;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class MovementSystem extends IteratingSystem {


    public MovementSystem(int priority) {
        super(Family.all(com.bendk97.components.PositionComponent.class, com.bendk97.components.VelocityComponent.class).exclude(GameOverComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        com.bendk97.components.PositionComponent position = com.bendk97.components.Mappers.position.get(entity);
        com.bendk97.components.VelocityComponent velocity = com.bendk97.components.Mappers.velocity.get(entity);
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
        com.bendk97.components.LightComponent light = com.bendk97.components.Mappers.light.get(entity);
        if(light!=null){
            Sprite sprite = com.bendk97.components.Mappers.sprite.get(entity).sprite;
            light.light.setPosition(position.x + sprite.getWidth() / 2f,
                    position.y + sprite.getHeight() / 2f);
        }
        // check if entity is a player, if yes, check play boundaries
        playerCannotGetOutBoundaries(entity, position);
    }

    private void playerCannotGetOutBoundaries(Entity entity, com.bendk97.components.PositionComponent position) {
        com.bendk97.components.SpriteComponent spriteComponent = com.bendk97.components.Mappers.sprite.get(entity);
        if (spriteComponent != null && spriteComponent.stayInBoundaries) {
            if (position.x < 0) {
                position.x = 0;
            } else if (position.x > SCREEN_WIDTH - spriteComponent.sprite.getWidth()) {
                position.x = SCREEN_WIDTH - spriteComponent.sprite.getWidth();
            }
            if (position.y < 0) {
                position.y = 0;
            } else if (position.y > SCREEN_HEIGHT - spriteComponent.sprite.getHeight()) {
                position.y = SCREEN_HEIGHT - spriteComponent.sprite.getHeight();
            }
        }
    }
}