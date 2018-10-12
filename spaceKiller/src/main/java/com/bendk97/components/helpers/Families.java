/*
 * Developed by Benjamin Lefèvre
 * Last modified 12/10/18 18:28
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components.helpers;

import com.badlogic.ashley.core.Family;
import com.bendk97.components.*;

public class Families {
    public static final Family playerBullet = Family.one(PlayerBulletComponent.class).get();
    public static final Family enemyBullet = Family.one(EnemyBulletComponent.class).get();
    public static final Family shieldUp = Family.one(ShieldUpComponent.class).get();
    public static final Family powerUp = Family.one(PowerUpComponent.class).get();
    public static final Family bombUp = Family.one(BombUpComponent.class).get();
    public static final Family enemyBodies = Family.all(EnemyComponent.class).exclude(GroundEnemyComponent.class).get();
    public static final Family enemies = Family.all(EnemyComponent.class).get();
    public static final Family playerVulnerable = Family.one(PlayerComponent.class).exclude(InvulnerableComponent.class, GameOverComponent.class).get();
    public static final Family player = Family.one(PlayerComponent.class).exclude(GameOverComponent.class).get();
    public static final Family shield = Family.one(ShieldComponent.class).get();

}