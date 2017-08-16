package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.benk97.components.*;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;
import static com.benk97.components.Mappers.sprite;

public class MovementSystem extends IteratingSystem {

    private OrthographicCamera camera;

    public MovementSystem(OrthographicCamera camera, int priority) {
        super(Family.all(PositionComponent.class, VelocityComponent.class).exclude(GameOverComponent.class).get(), priority);
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.position.get(entity);
        VelocityComponent velocity = Mappers.velocity.get(entity);
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
        // check if entity is a player, if yes, check play boundaries
        playerCannotGetOutBoundaries(entity, position);
//        if (Mappers.player.get(entity) != null) {
//            float camX = camera.position.x;
//            float camY = camera.position.y;
//
//            Vector2 dimCam = new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT);
//            dimCam.scl(camera.zoom / 2);
//            Vector2 camMin = new Vector2(camX, camY).sub(dimCam);
//            Vector2 camMax = new Vector2(camX, camY).add(dimCam);
//            if (position.x < camMin.x) {
//                camera.position.x -= camMin.x - position.x;
//            } else if ((position.x + Mappers.sprite.get(entity).sprite.getWidth()) > camMax.x) {
//                camera.position.x += (position.x + Mappers.sprite.get(entity).sprite.getWidth()) - camMax.x;
//            } else if(camMin.x<0){
//                camera.position.x -= camMin.x;
//            } else if(camMax.x>SCREEN_WIDTH){
//                camera.position.x -= (camMax.x - SCREEN_WIDTH);
//            }
//            if(camMax.y != SCREEN_HEIGHT){
//                camera.position.y += (SCREEN_HEIGHT-camMax.y);
//            }
//
//        }
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