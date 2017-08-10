package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

import static com.benk97.entities.EntityFactory.ENEMY_FIRE_CIRCLE;

public class EnemyComponent implements Component, Pool.Poolable {
    public int points = 0;
    private int lifeGauge = 1;
    private int lifeMax = 1;
    public int attackCapacity = 0;
    public int attackType = ENEMY_FIRE_CIRCLE;
    public boolean isLaserShip = false;
    public int probabilityAttack = 150;
    public boolean isBoss = false;
    public boolean isTank = false;
    public float bulletVelocity = 0f;
    public Entity squadron = null;

    @Override
    public void reset() {
        isLaserShip = false;
        probabilityAttack = 150;
        attackType = ENEMY_FIRE_CIRCLE;
        points = 0;
        isBoss = false;
        isTank = false;
        lifeGauge = 1;
        lifeMax = 1;
        attackCapacity = 0;
        squadron = null;
        bulletVelocity = 0f;
    }

    public boolean belongsToSquadron() {
        return squadron != null;
    }

    public boolean canAttack() {
        return attackCapacity > 0;
    }

    public void hit(int nbHits) {
        if (lifeGauge > 0) {
            lifeGauge -= nbHits;
        }
    }

    public void initLifeGauge(int lifePoints) {
        this.lifeMax = lifePoints;
        this.lifeGauge = lifePoints;
    }

    public int getLifeGauge() {
        return lifeGauge;
    }

    public float getRemainingLifeInPercent() {
        return lifeGauge / ((float) lifeMax);
    }

    public boolean isDead() {
        return lifeGauge <= 0;
    }
}
