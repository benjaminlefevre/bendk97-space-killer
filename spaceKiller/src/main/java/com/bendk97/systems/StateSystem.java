/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.bendk97.components.StateComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;

public class StateSystem extends IteratingSystem {

    public StateSystem(int priority) {
        super(Family.all(StateComponent.class).get(), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        ComponentMapperHelper.state.get(entity).time += deltaTime;
    }
}
