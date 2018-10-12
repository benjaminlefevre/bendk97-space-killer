/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.ShieldComponent;
import com.bendk97.components.SpriteComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;

public class ShieldSystem extends IteratingSystem {

    private final Entity player;

    public ShieldSystem(int priority, Entity player) {
        super(Family.all(ShieldComponent.class).exclude(GameOverComponent.class).get(), priority);
        this.player = player;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent positionComponent = ComponentMapperHelper.position.get(entity);
        PositionComponent playerPosition = ComponentMapperHelper.position.get(player);
        SpriteComponent spriteComponent = ComponentMapperHelper.sprite.get(entity);
        SpriteComponent playerSprite = ComponentMapperHelper.sprite.get(player);
        positionComponent.setPosition(playerPosition.x - (spriteComponent.sprite.getWidth() - playerSprite.sprite.getWidth()) / 2f,
                playerPosition.y - (spriteComponent.sprite.getHeight() - playerSprite.sprite.getHeight()) / 2f);
    }

}