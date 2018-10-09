/*
 * Developed by Benjamin Lefèvre
 * Last modified 10/10/18 07:50
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.player;

import com.bendk97.components.PlayerComponent.PowerLevel;
import com.bendk97.screens.levels.Levels;

public class PlayerDataBuilder {
    private Levels level;
    private int numberOfContinue;
    private long fireDelay;
    private long fireDelaySide;
    private int enemiesKilled;
    private int laserShipKilled;
    private int howManyLivesLost;
    private int score;
    private int lives;
    private int bombs;
    private PowerLevel powerLevel;

    public PlayerDataBuilder atLevel(Levels level) {
        this.level = level;
        return this;
    }

    public PlayerDataBuilder withNumberOfContinue(int numberOfContinue) {
        this.numberOfContinue = numberOfContinue;
        return this;
    }

    public PlayerDataBuilder withWeaponPower(PowerLevel powerLevel, long fireDelay, long fireDelaySide) {
        this.fireDelay = fireDelay;
        this.fireDelaySide = fireDelaySide;
        this.powerLevel = powerLevel;
        return this;
    }


    public PlayerDataBuilder enemiesKilled(int enemiesKilled, int laserShipKilled) {
        this.enemiesKilled = enemiesKilled;
        this.laserShipKilled = laserShipKilled;
        return this;
    }


    public PlayerDataBuilder gameStats(int lives, int bombs, int score, int howManyLivesLost) {
        this.score = score;
        this.lives = lives;
        this.bombs = bombs;
        this.howManyLivesLost = howManyLivesLost;
        return this;
    }

    public PlayerData createPlayerData() {
        PlayerData playerData = new PlayerData();
        playerData.level = level;
        playerData.numberOfContinue = numberOfContinue;
        playerData.fireDelay = fireDelay;
        playerData.fireDelaySide = fireDelaySide;
        playerData.enemiesKilled = enemiesKilled;
        playerData.laserShipKilled = laserShipKilled;
        playerData.howManyLivesLost = howManyLivesLost;
        playerData.score = score;
        playerData.lives = lives;
        playerData.bombs = bombs;
        playerData.powerLevel = powerLevel;
        return playerData;
    }
}