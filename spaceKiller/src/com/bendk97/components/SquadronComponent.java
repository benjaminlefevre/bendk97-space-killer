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

public class SquadronComponent implements Component, Pool.Poolable {
    public Array<Entity> ships = new Array<Entity>();
    public int toShoot = 0;
    public boolean powerUpAfterDestruction = false;
    public boolean displayBonusSquadron = false;
    public int scoreBonus = 1000;
    public Vector2 lastKilledPosition;

    public void addEntities(Entity... entities){
        ships.addAll(entities);
        toShoot = entities.length;
    }

    public void removeEntity(Entity entity){
        PositionComponent position = Mappers.position.get(entity);
        lastKilledPosition = new Vector2(position.x, position.y);
        ships.removeValue(entity, true);
        toShoot--;
    }

    @Override
    public void reset() {
        powerUpAfterDestruction = false;
        ships = new Array<Entity>();
        lastKilledPosition = null;
        displayBonusSquadron = false;
        scoreBonus = 1000;
        toShoot = 0;
    }
}
