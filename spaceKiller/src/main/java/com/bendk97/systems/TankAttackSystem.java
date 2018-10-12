/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.bendk97.components.PauseComponent;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.TankComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;

public class TankAttackSystem extends IteratingSystem {

    private final Family player = Family.one(PlayerComponent.class).exclude(PauseComponent.class).get();

    public TankAttackSystem(int priority) {
        super(Family.all(TankComponent.class).get(), priority);
    }


    @Override
    protected void processEntity(final Entity entity, float deltaTime) {
        ImmutableArray<Entity> playerEntity = getEngine().getEntitiesFor(player);
        if (playerEntity.size() == 0) {
            return;
        }
        final TankComponent tank = ComponentMapperHelper.tank.get(entity);
        if (tank.nbShootsBeforeLastReload >= tank.nbShoots) {
            if ((TimeUtils.millis() - tank.lastShoot) > tank.delayBetweenBursts) {
                tank.reRandomCharacteristics();
                tank.lastShoot = TimeUtils.millis() - 300;
            }
        } else if ((TimeUtils.millis() - tank.lastShoot) > tank.delayShoot) {
            tank.lastShoot = TimeUtils.millis();
            tank.nbShootsBeforeLastReload++;
            ComponentMapperHelper.enemy.get(entity).attackCapacity = 1;
        }

    }
}
