/*
 * Developed by Benjamin Lefèvre
 * Last modified 13/10/18 11:36
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.enemies;

public class EnemyCharacteristics {

    protected boolean canAttack;
    public boolean directionable;
    protected int rateShoot;
    protected int strength;
    protected int attackCapacity;
    protected float velocityBullet;
    protected String atlasName;
    public int points;

    protected EnemyCharacteristics setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
        return this;
    }

    protected EnemyCharacteristics directionable(boolean directionable) {
        this.directionable = directionable;
        return this;
    }

    protected EnemyCharacteristics setRateShoot(int rateShoot) {
        this.rateShoot = rateShoot;
        return this;
    }

    protected EnemyCharacteristics setStrength(int strength) {
        this.strength = strength;
        return this;
    }

    protected EnemyCharacteristics setAttackCapacity(int attackCapacity) {
        this.attackCapacity = attackCapacity;
        return this;
    }

    protected EnemyCharacteristics setVelocityBullet(float velocityBullet) {
        this.velocityBullet = velocityBullet;
        return this;
    }

    protected EnemyCharacteristics setAtlasName(String atlasName) {
        this.atlasName = atlasName;
        return this;
    }

    public EnemyCharacteristics setPoints(int points) {
        this.points = points;
        return this;
    }
}
