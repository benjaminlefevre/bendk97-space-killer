package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Timer;
import com.benk97.components.BossComponent;
import com.benk97.components.Mappers;
import com.benk97.components.PlayerComponent;
import com.benk97.entities.EntityFactory;

import java.util.Random;

public class BossAttackSystem extends IteratingSystem {

    private EntityFactory entityFactory;
    private Family player = Family.one(PlayerComponent.class).get();
    private Random random = new RandomXS128();

    public BossAttackSystem(int priority, EntityFactory entityFactory) {
        super(Family.all(BossComponent.class).get(), priority);
        this.entityFactory = entityFactory;
    }


    @Override
    protected void processEntity(final Entity entity, float deltaTime) {
        if (Mappers.boss.get(entity).pleaseFire) {
            if (Mappers.enemy.get(entity).isDead()) {
                return;
            }
            entityFactory.createBossFire(entity, getEngine().getEntitiesFor(player).first());
            Mappers.boss.get(entity).pleaseFire = false;
            new Timer().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    if (Mappers.boss.get(entity) != null && !Mappers.enemy.get(entity).isDead()) {
                        Mappers.boss.get(entity).pleaseFire = true;
                    }
                }
            }, 5f + random.nextFloat() * 2f);
        }
    }
}
