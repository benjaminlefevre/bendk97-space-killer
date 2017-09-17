package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.bendk97.components.PlayerBulletComponent;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class RemovableSystem extends IteratingSystem {

    public RemovableSystem(int priority) {
        super(Family.all(com.bendk97.components.RemovableComponent.class, com.bendk97.components.PositionComponent.class, com.bendk97.components.VelocityComponent.class).get(), priority);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        com.bendk97.components.RemovableComponent removableComponent = com.bendk97.components.Mappers.removable.get(entity);
        com.bendk97.components.PositionComponent position = com.bendk97.components.Mappers.position.get(entity);
        com.bendk97.components.SpriteComponent sprite = com.bendk97.components.Mappers.sprite.get(entity);
        removableComponent.elapseTime += deltaTime;
        if ((removableComponent.elapseTime >= 2.0f || entity.getComponent(PlayerBulletComponent.class) != null) &&
                (position.x + sprite.sprite.getWidth() < 0
                        || position.x > SCREEN_WIDTH
                        || position.y > SCREEN_HEIGHT
                        || position.y + sprite.sprite.getHeight() < 0)) {
            getEngine().removeEntity(entity);
        }
    }
}
