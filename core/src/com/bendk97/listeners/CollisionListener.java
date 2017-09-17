package com.bendk97.listeners;


import com.badlogic.ashley.core.Entity;

public interface CollisionListener {

    void enemyShoot(Entity ennemy, Entity player, Entity bullet);

    void playerHitByEnnemyBody(Entity player, Entity ennemy);

    void playerHitByEnnemyBullet(Entity player, Entity bullet);

    void playerPowerUp(Entity player, Entity powerUp);

    void bulletStoppedByShield(Entity bullet);

    void enemyShootByShield(Entity enemy, Entity shield);

    void playerShieldUp(Entity player, Entity shieldUp);

    void playerBombUp(Entity player, Entity bombUp);

    void enemyShootByExplosion(Entity enemy, Entity player);
}
