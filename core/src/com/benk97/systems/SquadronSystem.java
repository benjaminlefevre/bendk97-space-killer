package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.benk97.components.Mappers;
import com.benk97.components.SquadronComponent;
import com.benk97.entities.EntityFactory;

public class SquadronSystem extends IteratingSystem {

    private EntityFactory entityFactory;

    public SquadronSystem(int priority, EntityFactory entityFactory) {
        super(Family.all(SquadronComponent.class).get(), priority);
        this.entityFactory = entityFactory;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        SquadronComponent squadron = Mappers.squadron.get(entity);
        if (squadron.ships.isEmpty()) {
            getEngine().removeEntity(entity);
            if(squadron.powerUpAfterDestruction){
                entityFactory.createPowerUp(entity);
            }
        }
    }
}