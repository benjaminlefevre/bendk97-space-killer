/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

public class TankComponent implements Component, Pool.Poolable {

    public long lastShoot = TimeUtils.millis() - 300;
    private TankLevel level = TankLevel.EASY;
    public long nbShootsBeforeLastReload = 0;
    public long delayShoot = TankLevel.EASY.delayShoot;
    public int nbShoots = TankLevel.EASY.nbShoots;
    public long delayBetweenBursts = TankLevel.EASY.delayBetweenBursts;

    public enum TankLevel {
        EASY(2, 500, 1500, 300f), MEDIUM(3, 450, 1250, 325f), HARD(4, 400, 1000, 350f);
        final long delayShoot;
        final long delayBetweenBursts;
        public final float bulletVelocity;
        final int nbShoots;
        final Random random = new RandomXS128();

        TankLevel(int nbShoots, long delayShoot, long delayBetweenBursts, float bulletVelocity) {
            this.nbShoots = (nbShoots - 1) + random.nextInt(3);
            this.delayShoot = (delayShoot - 100) + (long) random.nextFloat() * 200;
            this.bulletVelocity = bulletVelocity;
            this.delayBetweenBursts = (delayBetweenBursts - 100) + (long) random.nextFloat() * 200;
        }
    }

    public void setLevel(TankLevel level) {
        delayShoot = level.delayShoot;
        delayBetweenBursts = level.delayBetweenBursts;
        nbShoots = level.nbShoots;
        this.level = level;
    }

    @Override
    public void reset() {
        lastShoot = TimeUtils.millis() - 300;
        level = TankLevel.EASY;
        delayShoot = TankLevel.EASY.delayShoot;
        nbShoots = TankLevel.EASY.nbShoots;
        delayBetweenBursts = TankLevel.EASY.delayBetweenBursts;
        nbShootsBeforeLastReload = 0;
    }

    public void reRandomCharacteristics() {
        setLevel(level);
        nbShootsBeforeLastReload = 0;
    }
}
