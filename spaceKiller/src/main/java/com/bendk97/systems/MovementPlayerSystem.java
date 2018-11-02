/*
 * Developed by Benjamin Lefèvre
 * Last modified 21/10/18 11:50
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.bendk97.components.*;
import com.bendk97.components.helpers.ComponentMapperHelper;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class MovementPlayerSystem extends AbstractMovementSystem {

    private final OrthographicCamera camera;

    public MovementPlayerSystem(int priority, OrthographicCamera camera) {
        super(Family.all(PositionComponent.class, VelocityComponent.class, PlayerComponent.class)
                .exclude(GameOverComponent.class)
                .get(), priority);
        this.camera = camera;
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        super.processEntity(entity, deltaTime);
        playerCannotGetOutBoundaries(entity);
    }

    private void playerCannotGetOutBoundaries(Entity entity) {
        PositionComponent positionComponent = ComponentMapperHelper.position.get(entity);
        SpriteComponent spriteComponent = ComponentMapperHelper.sprite.get(entity);
        if (spriteComponent != null) {
            if (positionComponent.x() < -50) {
                positionComponent.setX(-50);
            } else if (positionComponent.x() > SCREEN_WIDTH - spriteComponent.sprite.getWidth() + 50) {
                positionComponent.setX(SCREEN_WIDTH - spriteComponent.sprite.getWidth() + 50);
            }
            if (positionComponent.y() < 0) {
                positionComponent.setY(0);
            } else if (positionComponent.y() > SCREEN_HEIGHT - spriteComponent.sprite.getHeight()) {
                positionComponent.setY(SCREEN_HEIGHT - spriteComponent.sprite.getHeight());
            }
            cameraFollows(positionComponent, spriteComponent);
        }
    }

    private void cameraFollows(PositionComponent positionComponent, SpriteComponent spriteComponent) {
        if (positionComponent.x() < 0) {
            camera.position.x = positionComponent.x() + SCREEN_WIDTH / 2;
        } else if (positionComponent.x() > SCREEN_WIDTH - spriteComponent.sprite.getWidth()) {
            camera.position.x = positionComponent.x() + spriteComponent.sprite.getWidth() - SCREEN_WIDTH / 2;
        } else {
            camera.position.x = SCREEN_WIDTH / 2;
        }
        camera.update();
    }
}