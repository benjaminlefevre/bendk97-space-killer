/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 22:06
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.player;

import com.bendk97.components.PlayerComponent;
import com.bendk97.screens.LevelScreen.Level;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.components.PlayerComponent.PowerLevel.NORMAL;

/* use to pass player data between screens*/
public class PlayerData {
    public long fireDelay = FIRE_DELAY;
    public long fireDelaySide = FIRE_DELAY_SIDE;
    public int enemiesKilled = 0;
    public int laserShipKilled = 0;
    public int howManyLifesLosed = 0;
    public int score = 0;
    public int lives = LIVES;
    public int bombs = BOMBS;
    public PlayerComponent.PowerLevel powerLevel = NORMAL;
    public int numberOfContinue = 0;
    public final Level level;

    public PlayerData(Level level, int numberOfContinue, long fireDelay, long fireDelaySide,
                      int enemiesKilled, int laserShipKilled, int howManyLifesLosed, int score,
                      int lives, int bombs, PlayerComponent.PowerLevel powerLevel) {
        this.fireDelay = fireDelay;
        this.fireDelaySide = fireDelaySide;
        this.numberOfContinue = numberOfContinue;
        this.level = level;
        this.enemiesKilled = enemiesKilled;
        this.laserShipKilled = laserShipKilled;
        this.howManyLifesLosed = howManyLifesLosed;
        this.score = score;
        this.lives = lives;
        this.bombs = bombs;
        this.powerLevel = powerLevel;
    }
}
