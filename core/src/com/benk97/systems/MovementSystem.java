package com.benk97.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.benk97.components.DynamicSpriteComponent;
import com.benk97.components.PositionComponent;
import com.benk97.components.VelocityComponent;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class MovementSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<DynamicSpriteComponent> dm = ComponentMapper.getFor(DynamicSpriteComponent.class);


    public MovementSystem(Family family) {
        super(family, 1);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = pm.get(entity);
        VelocityComponent velocity = vm.get(entity);
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
        // check if entity is a player, if yes, check play boundaries
        playerCannotGetOutBoundaries(entity, position);
    }

    private void playerCannotGetOutBoundaries(Entity entity, PositionComponent position) {

        DynamicSpriteComponent dynamicSpriteComponent = dm.get(entity);
        if (dynamicSpriteComponent != null && dynamicSpriteComponent.stayInBoundaries) {
            if (position.x < 0) {
                position.x = 0;
            } else if (position.x > SCREEN_WIDTH - dynamicSpriteComponent.getWidth()) {
                position.x = SCREEN_WIDTH - dynamicSpriteComponent.getWidth();
            }
            if (position.y < 0) {
                position.y = 0;
            } else if (position.y > SCREEN_HEIGHT - dynamicSpriteComponent.getHeight()) {
                position.y = SCREEN_HEIGHT - dynamicSpriteComponent.getHeight();
            }
        }
    }
}