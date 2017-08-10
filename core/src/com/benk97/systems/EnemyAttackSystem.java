package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.RandomXS128;
import com.benk97.components.*;
import com.benk97.entities.EntityFactory;

import java.util.Random;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class EnemyAttackSystem extends IteratingSystem {

    private EntityFactory entityFactory;
    private Random random = new RandomXS128();
    private Family player = Family.one(PlayerComponent.class).exclude(PauseComponent.class).get();

    public EnemyAttackSystem(int priority, EntityFactory entityFactory) {
        super(Family.all(EnemyComponent.class).exclude(BossComponent.class).get(), priority);
        this.entityFactory = entityFactory;
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ImmutableArray<Entity> playerEntity = getEngine().getEntitiesFor(player);
        if (playerEntity.size() == 0) {
            return;
        }
        EnemyComponent enemy = Mappers.enemy.get(entity);
        if (enemy.canAttack() && isVisible(entity) && random.nextInt() % enemy.probabilityAttack == 0) {
            entityFactory.createEnemyFire(entity, playerEntity.first());
            enemy.attackCapacity--;
        }
    }

    private boolean isVisible(Entity entity) {
        PositionComponent position = Mappers.position.get(entity);
        EnemyComponent enemy = Mappers.enemy.get(entity);
        SpriteComponent sprite = Mappers.sprite.get(entity);
        return (position.x >= 0
                && position.x <= SCREEN_WIDTH - sprite.sprite.getWidth()
                && position.y <= SCREEN_HEIGHT - sprite.sprite.getHeight()
                && (position.y >= 150f || (position.y >= 0 && enemy.isTank)));
    }
}
