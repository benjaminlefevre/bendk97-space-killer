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
    public boolean powerUpAfterDestruction = false;
    public Vector2 lastKilledPosition;

    public void addEntities(Entity... entities){
        ships.addAll(Arrays.asList(entities));
    }

    public void removeEntity(Entity entity){
        PositionComponent position = Mappers.position.get(entity);
        lastKilledPosition = new Vector2(position.x, position.y);
        ships.remove(entity);
    }

    @Override
    public void reset() {
        powerUpAfterDestruction = false;
        ships = new ArrayList<Entity>();
        lastKilledPosition = null;
    }
}
