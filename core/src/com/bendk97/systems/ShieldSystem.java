package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.bendk97.components.Mappers;

public class ShieldSystem extends IteratingSystem {

    private Entity player;

    public ShieldSystem(int priority, Entity player) {
        super(Family.all(com.bendk97.components.ShieldComponent.class).exclude(com.bendk97.components.GameOverComponent.class).get(), priority);
        this.player = player;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        com.bendk97.components.PositionComponent positionComponent = com.bendk97.components.Mappers.position.get(entity);
        com.bendk97.components.PositionComponent playerPosition = com.bendk97.components.Mappers.position.get(player);
        com.bendk97.components.SpriteComponent spriteComponent = com.bendk97.components.Mappers.sprite.get(entity);
        com.bendk97.components.SpriteComponent playerSprite = Mappers.sprite.get(player);
        positionComponent.setPosition(playerPosition.x - (spriteComponent.sprite.getWidth() - playerSprite.sprite.getWidth()) / 2f,
                playerPosition.y - (spriteComponent.sprite.getHeight() - playerSprite.sprite.getHeight()) / 2f);
    }

}