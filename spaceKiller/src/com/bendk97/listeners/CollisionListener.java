/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.listeners;


import com.badlogic.ashley.core.Entity;

public interface CollisionListener {

    void enemyShoot(Entity enemy, Entity player, Entity bullet);

    void playerHitByEnemyBody(Entity player);

    void playerHitByEnemyBullet(Entity player, Entity bullet);

    void playerPowerUp(Entity player, Entity powerUp);

    void bulletStoppedByShield(Entity bullet);

    void enemyShootByShield(Entity enemy);

    void playerShieldUp(Entity player, Entity shieldUp);

    void playerBombUp(Entity player, Entity bombUp);

    void enemyShootByExplosion(Entity enemy, Entity player);
}
