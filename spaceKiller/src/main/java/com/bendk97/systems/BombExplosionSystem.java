/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.bendk97.components.BombExplosionComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.components.helpers.Families;
import com.bendk97.listeners.CollisionListener;


public class BombExplosionSystem extends IteratingSystem {

    private final CollisionListener collisionListener;
    private final Entity player;
    private final TweenManager tweenManager;

    public BombExplosionSystem(int priority, CollisionListener collisionListener, Entity player, TweenManager tweenManager) {
        super(Family.all(BombExplosionComponent.class).get(), priority);
        this.collisionListener = collisionListener;
        this.player = player;
        this.tweenManager = tweenManager;
    }


    @Override
    protected void processEntity(final Entity entity, float deltaTime) {
        for(Entity bullet : getEngine().getEntitiesFor(Families.enemyBullet)){
            getEngine().removeEntity(bullet);
            tweenManager.killTarget(ComponentMapperHelper.sprite.get(bullet));

        }
        for (Entity enemy : getEngine().getEntitiesFor(Families.enemies)) {
            collisionListener.enemyShootByExplosion(enemy, player);
        }
        getEngine().removeEntity(entity);
    }
}
