package com.benk97.listeners;


import com.badlogic.ashley.core.Entity;

public interface CollisionListener {

    void enemyShoot(Entity ennemy, Entity player, Entity bullet);

    void playerHitByEnnemyBody(Entity player, Entity ennemy);
}
