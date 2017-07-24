package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.benk97.components.BombExplosionComponent;
import com.benk97.components.EnemyBulletComponent;
import com.benk97.components.EnemyComponent;
import com.benk97.listeners.CollisionListener;

public class BombExplosionSystem extends IteratingSystem {

    private Family enemies = Family.one(EnemyComponent.class).get();
    private Family bullets = Family.one(EnemyBulletComponent.class).get();
    private CollisionListener collisionListener;
    private Entity player;

    public BombExplosionSystem(int priority, CollisionListener collisionListener, Entity player) {
        super(Family.all(BombExplosionComponent.class).get(), priority);
        this.collisionListener = collisionListener;
        this.player = player;
    }


    @Override
    protected void processEntity(final Entity entity, float deltaTime) {
        for(Entity bullet : getEngine().getEntitiesFor(bullets)){
            getEngine().removeEntity(bullet);
        }
        for (Entity enemy : getEngine().getEntitiesFor(enemies)) {
            collisionListener.enemyShootByExplosion(enemy, player);
        }
        getEngine().removeEntity(entity);
    }
}
