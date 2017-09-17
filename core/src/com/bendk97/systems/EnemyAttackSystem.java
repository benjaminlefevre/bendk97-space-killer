package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.RandomXS128;
import com.bendk97.components.Mappers;

import java.util.Random;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class EnemyAttackSystem extends IteratingSystem {

    private com.bendk97.entities.EntityFactory entityFactory;
    private Random random = new RandomXS128();
    private Family player = Family.one(com.bendk97.components.PlayerComponent.class).exclude(com.bendk97.components.PauseComponent.class).get();

    public EnemyAttackSystem(int priority, com.bendk97.entities.EntityFactory entityFactory) {
        super(Family.all(com.bendk97.components.EnemyComponent.class).exclude(com.bendk97.components.BossComponent.class).get(), priority);
        this.entityFactory = entityFactory;
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ImmutableArray<Entity> playerEntity = getEngine().getEntitiesFor(player);
        if (playerEntity.size() == 0) {
            return;
        }
        com.bendk97.components.EnemyComponent enemy = com.bendk97.components.Mappers.enemy.get(entity);
        if (enemy.canAttack() && isVisible(entity) && random.nextInt() % enemy.probabilityAttack == 0) {
            entityFactory.createEnemyFire(entity, playerEntity.first());
            enemy.attackCapacity--;
        }
    }

    private boolean isVisible(Entity entity) {
        com.bendk97.components.PositionComponent position = com.bendk97.components.Mappers.position.get(entity);
        com.bendk97.components.EnemyComponent enemy = com.bendk97.components.Mappers.enemy.get(entity);
        com.bendk97.components.SpriteComponent sprite = Mappers.sprite.get(entity);
        return (position.x >= 0
                && position.x <= SCREEN_WIDTH - sprite.sprite.getWidth()
                && position.y <= SCREEN_HEIGHT - sprite.sprite.getHeight()
                && (position.y >= 150f || (position.y >= 0 && enemy.isTank)));
    }
}
