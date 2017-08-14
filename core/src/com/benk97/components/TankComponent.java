package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

public class TankComponent implements Component, Pool.Poolable {

    public long lastShoot = TimeUtils.millis() - 300;
    public TankLevel level = TankLevel.EASY;
    public long nbShootsBeforeLastReload = 0;
    public long delayShoot = TankLevel.EASY.delayShoot;
    public int nbShoots = TankLevel.EASY.nbShoots;
    public long delayBetweenRafales = TankLevel.EASY.delayBetweenRafales;

    public enum TankLevel {
        EASY(2, 500, 1500, 300f), MEDIUM(3, 450, 1250, 325f), HARD(4, 400, 1000, 350f);
        public long delayShoot, delayBetweenRafales;
        public float bulletVelocity;
        public int nbShoots;
        public Random random = new RandomXS128();

        private TankLevel(int nbShoots, long delayShoot, long delayBetweenRafales, float bulletVelocity) {
            this.nbShoots = (nbShoots - 1) + random.nextInt(3);
            this.delayShoot = (delayShoot - 100) + (long) random.nextFloat() * 200;
            this.bulletVelocity = bulletVelocity;
            this.delayBetweenRafales = (delayBetweenRafales - 100) + (long) random.nextFloat() * 200;
        }
    }

    public void setLevel(TankLevel level) {
        delayShoot = level.delayShoot;
        delayBetweenRafales = level.delayBetweenRafales;
        nbShoots = level.nbShoots;
        this.level = level;
    }

    @Override
    public void reset() {
        lastShoot = TimeUtils.millis() - 300;
        level = TankLevel.EASY;
        delayShoot = TankLevel.EASY.delayShoot;
        nbShoots = TankLevel.EASY.nbShoots;
        delayBetweenRafales = TankLevel.EASY.delayBetweenRafales;
        nbShootsBeforeLastReload = 0;
    }

    public void reRandomCharacteristics() {
        setLevel(level);
        nbShootsBeforeLastReload = 0;
    }
}
