/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.bendk97.components.helpers.ComponentMapperHelper;

import static com.bendk97.pools.GamePools.poolVector2;

public class SquadronComponent implements Component, Pool.Poolable {
    public Array<Entity> ships = new Array<>();
    public int toShoot = 0;
    public boolean powerUpAfterDestruction = false;
    public boolean displayBonusSquadron = false;
    public int scoreBonus = 1000;
    public Vector2 lastKilledPosition;

    public SquadronComponent() {
        lastKilledPosition = poolVector2.obtain();
        lastKilledPosition.set(10f, 10f);
    }

    public void addEntities(Entity... entities){
        ships.addAll(entities);
        toShoot = entities.length;
    }

    public void removeEntity(Entity entity){
        PositionComponent position = ComponentMapperHelper.position.get(entity);
        lastKilledPosition.set(position.x(), position.y());
        ships.removeValue(entity, true);
        toShoot--;
    }

    @Override
    public void reset() {
        powerUpAfterDestruction = false;
        ships = new Array<>();
        lastKilledPosition.set(10f, 10f);
        displayBonusSquadron = false;
        scoreBonus = 1000;
        toShoot = 0;
    }
}
