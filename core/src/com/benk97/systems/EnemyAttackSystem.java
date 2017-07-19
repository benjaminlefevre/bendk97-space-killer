package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.benk97.components.*;
import com.benk97.entities.EntityFactory;

import java.util.Random;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class EnemyAttackSystem extends IteratingSystem {

    private EntityFactory entityFactory;
    private Random random = new Random(System.currentTimeMillis());
    private Family player = Family.one(PlayerComponent.class).get();

    public EnemyAttackSystem(int priority, EntityFactory entityFactory) {
        super(Family.all(EnemyComponent.class).get(), priority);
        this.entityFactory = entityFactory;
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (Mappers.enemy.get(entity).canAttack && isVisible(entity) && random.nextInt() % 200 == 0) {
            entityFactory.createEnemyFire(entity, getEngine().getEntitiesFor(player).first());
            Mappers.enemy.get(entity).canAttack = false;
        }
    }

    private boolean isVisible(Entity entity) {
        PositionComponent position = Mappers.position.get(entity);
        SpriteComponent sprite = Mappers.sprite.get(entity);
        return (position.x >= 0
                && position.x <= SCREEN_WIDTH - sprite.sprite.getWidth()
                && position.y <= SCREEN_HEIGHT - sprite.sprite.getHeight()
                && position.y >= 150f);
    }
}
