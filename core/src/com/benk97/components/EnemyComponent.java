package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class EnemyComponent implements Component, Pool.Poolable {
    public int points = 0;
    public int lifeGauge = 1;
    public boolean canAttack = false;
    public boolean isBoss = false;
    public float bulletVelocity = 0f;
    public Entity squadron = null;

    @Override
    public void reset() {
        points = 0;
        isBoss = false;
        lifeGauge = 1;
        canAttack = false;
        squadron = null;
        bulletVelocity = 0f;
    }

    public void hit(int nbHits) {
        if (lifeGauge > 0) {
            lifeGauge -= nbHits;
        }
    }

    public boolean isDead() {
        return lifeGauge <= 0;
    }
}
