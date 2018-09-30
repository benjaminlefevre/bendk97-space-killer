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
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.TimeUtils;
import com.bendk97.components.Mappers;
import com.bendk97.components.PauseComponent;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.TankComponent;

import java.util.Random;

public class TankAttackSystem extends IteratingSystem {

    private Family player = Family.one(PlayerComponent.class).exclude(PauseComponent.class).get();
    private Random random = new RandomXS128();

    public TankAttackSystem(int priority) {
        super(Family.all(TankComponent.class).get(), priority);
    }


    @Override
    protected void processEntity(final Entity entity, float deltaTime) {
        ImmutableArray<Entity> playerEntity = getEngine().getEntitiesFor(player);
        if (playerEntity.size() == 0) {
            return;
        }
        final TankComponent tank = Mappers.tank.get(entity);
        if (tank.nbShootsBeforeLastReload >= tank.nbShoots) {
            if ((TimeUtils.millis() - tank.lastShoot) > tank.delayBetweenRafales) {
                tank.reRandomCharacteristics();
                tank.lastShoot = TimeUtils.millis() - 300;
            }
        } else if ((TimeUtils.millis() - tank.lastShoot) > tank.delayShoot) {
            tank.lastShoot = TimeUtils.millis();
            tank.nbShootsBeforeLastReload++;
            Mappers.enemy.get(entity).attackCapacity = 1;
        }

    }
}
