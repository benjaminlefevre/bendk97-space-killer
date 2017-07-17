package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class EnemyComponent implements Component, Pool.Poolable {
    public int points = 0;
    public boolean canAttack = false;
    public Entity squadron = null;

    @Override
    public void reset() {
        points = 0;
        canAttack = false;
        squadron = null;
    }
}
