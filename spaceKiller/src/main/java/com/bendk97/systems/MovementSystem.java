/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Family;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.VelocityComponent;

public class MovementSystem extends AbstractMovementSystem {

    public MovementSystem(int priority) {
        super(Family.all(PositionComponent.class, VelocityComponent.class)
                .exclude(PlayerComponent.class)
                .get(), priority);
    }
}