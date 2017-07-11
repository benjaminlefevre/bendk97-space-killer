package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.benk97.components.*;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;
import static com.benk97.components.Mappers.sprite;

public class MovementSystem extends IteratingSystem {


    public MovementSystem(int priority) {
        super(Family.all(PositionComponent.class, VelocityComponent.class).exclude(GameOverComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.position.get(entity);
        VelocityComponent velocity = Mappers.velocity.get(entity);
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
        // check if entity is a player, if yes, check play boundaries
        playerCannotGetOutBoundaries(entity, position);
    }

    private void playerCannotGetOutBoundaries(Entity entity, PositionComponent position) {
        SpriteComponent spriteComponent = sprite.get(entity);
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