package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.RandomXS128;
import com.benk97.components.BossComponent;
import com.benk97.components.Mappers;
import com.benk97.components.PauseComponent;
import com.benk97.components.PlayerComponent;
import com.benk97.entities.EntityFactory;
import com.benk97.timer.PausableTimer;

import java.util.Random;

public class BossAttackSystem extends IteratingSystem {

    private EntityFactory entityFactory;
    private Family player = Family.one(PlayerComponent.class).exclude(PauseComponent.class).get();
    private Random random = new RandomXS128();

    public BossAttackSystem(int priority, EntityFactory entityFactory) {
        super(Family.all(BossComponent.class).get(), priority);
        this.entityFactory = entityFactory;
    }


    @Override
    protected void processEntity(final Entity entity, float deltaTime) {
        ImmutableArray<Entity> playerEntity = getEngine().getEntitiesFor(player);
        if (playerEntity.size() == 0) {
            return;
        }
        final BossComponent boss = Mappers.boss.get(entity);
        if (boss.pleaseFire1) {
            if (Mappers.enemy.get(entity).isDead()) {
                return;
            }
            entityFactory.createBossFire(entity, playerEntity.first());
            boss.pleaseFire1 = false;
            PausableTimer.schedule(new PausableTimer.Task() {
                @Override
                public void run() {
                    if (!Mappers.enemy.get(entity).isDead()) {
                        boss.pleaseFire1 = true;
                    }
                }
            }, boss.minTriggerFire1 + random.nextFloat() * 2f);
        }
        if (Mappers.boss.get(entity).pleaseFire2) {
            if (Mappers.enemy.get(entity).isDead()) {
                return;
            }
            entityFactory.createBossFire2(entity);
            boss.pleaseFire2 = false;
            PausableTimer.schedule(new PausableTimer.Task() {
                @Override
                public void run() {
                    if (!Mappers.enemy.get(entity).isDead()) {
                        boss.pleaseFire2 = true;
                    }
                }
            }, boss.minTriggerFire2 + random.nextFloat() * 2f);
        }
    }
}
