package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.benk97.components.StateComponent;

import static com.benk97.components.Mappers.state;

public class StateSystem extends IteratingSystem {

    public StateSystem() {
        super(Family.all(StateComponent.class).get(), 0);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        state.get(entity).time += deltaTime;
    }
}
