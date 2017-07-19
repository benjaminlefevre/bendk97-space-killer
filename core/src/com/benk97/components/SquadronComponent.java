package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SquadronComponent implements Component, Pool.Poolable {
    public List<Entity> ships = new ArrayList<Entity>();
    public int toShoot = 0;
    public boolean powerUpAfterDestruction = false;
    public boolean displayBonusSquadron = false;
    public int scoreBonus = 1000;
    public Vector2 lastKilledPosition;

    public void addEntities(Entity... entities){
        ships.addAll(Arrays.asList(entities));
        toShoot = entities.length;
    }

    public void removeEntity(Entity entity){
        PositionComponent position = Mappers.position.get(entity);
        lastKilledPosition = new Vector2(position.x, position.y);
        ships.remove(entity);
        toShoot--;
    }

    @Override
    public void reset() {
        powerUpAfterDestruction = false;
        ships = new ArrayList<Entity>();
        lastKilledPosition = null;
        displayBonusSquadron = false;
        scoreBonus = 1000;
        toShoot = 0;
    }
}
