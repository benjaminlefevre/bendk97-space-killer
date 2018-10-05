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

public class ShieldSystem extends IteratingSystem {

    private final Entity player;

    public ShieldSystem(int priority, Entity player) {
        super(Family.all(ShieldComponent.class).exclude(GameOverComponent.class).get(), priority);
        this.player = player;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = Mappers.position.get(entity);
        PositionComponent playerPosition = Mappers.position.get(player);
        SpriteComponent spriteComponent = Mappers.sprite.get(entity);
        SpriteComponent playerSprite = Mappers.sprite.get(player);
        positionComponent.setPosition(playerPosition.x - (spriteComponent.sprite.getWidth() - playerSprite.sprite.getWidth()) / 2f,
                playerPosition.y - (spriteComponent.sprite.getHeight() - playerSprite.sprite.getHeight()) / 2f);
    }

}