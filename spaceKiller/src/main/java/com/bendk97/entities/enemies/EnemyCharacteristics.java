/*
 * Developed by Benjamin Lefèvre
 * Last modified 13/10/18 11:36
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.enemies;

public class EnemyCharacteristics {

    public boolean canAttack;
    public int rateShoot;
    public int strength;
    public int attackCapacity;
    public float velocityBullet;
    public String atlasName;
    public int points;

    public EnemyCharacteristics setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
        return this;
    }

    public EnemyCharacteristics setRateShoot(int rateShoot) {
        this.rateShoot = rateShoot;
        return this;
    }

    public EnemyCharacteristics setStrength(int strength) {
        this.strength = strength;
        return this;
    }

    public EnemyCharacteristics setAttackCapacity(int attackCapacity) {
        this.attackCapacity = attackCapacity;
        return this;
    }

    public EnemyCharacteristics setVelocityBullet(float velocityBullet) {
        this.velocityBullet = velocityBullet;
        return this;
    }

    public EnemyCharacteristics setAtlasName(String atlasName) {
        this.atlasName = atlasName;
        return this;
    }

    public EnemyCharacteristics setPoints(int points) {
        this.points = points;
        return this;
    }
}
