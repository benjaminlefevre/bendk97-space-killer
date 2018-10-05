/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 22:06
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;
import com.bendk97.player.PlayerData;
import com.bendk97.screens.LevelScreen.Level;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.components.PlayerComponent.PowerLevel.*;


public class PlayerComponent implements Component, Pool.Poolable {

    public void enemyKilled() {
        enemiesKilledLevel++;
        enemiesKilled++;
    }

    public enum PowerLevel {
        NORMAL(new Color(43f/255f, 197f/255f, 205f/255f, 0.6f), "bullet"),
        DOUBLE(new Color(43f/255f, 197f/255f, 205f/255f, 0.6f), "bullet2"),
        TRIPLE(new Color(43f/255f, 197f/255f, 205f/255f, 0.6f), "bullet3"),
        TRIPLE_SIDE(new Color(43f/255f, 197f/255f, 205f/255f, 0.6f), "bullet3", "bulletLeft1", "bulletRight1"),
        TRIPLE_FAST(new Color(3f/255f, 255f/255f, 136f/255f, 0.6f), "bullet4", "bulletLeft2", "bulletRight2"),
        TRIPLE_VERY_FAST(new Color(255f/255f, 120f/255f, 0f, 0.6f),"bullet5", "bulletLeft3", "bulletRight3");

        public final Color color;
        public final String regionName;
        public String leftRegionName;
        public String rightRegionName;

        PowerLevel(Color color, String regionName) {
            this.color = color;
            this.regionName = regionName;
        }
        PowerLevel(Color color, String regionName, String left, String right) {
            this.color = color;
            this.regionName = regionName;
            this.leftRegionName = left;
            this.rightRegionName = right;
        }

    }

    public int enemiesCountLevel = 0;
    public int enemiesKilledLevel = 0;

    public long fireDelay = FIRE_DELAY;
    public long fireDelaySide = FIRE_DELAY_SIDE;
    public int enemiesKilled = 0;
    public int laserShipKilled = 0;
    public int howManyLifesLosed = 0;
    private int score = 0;
    private int highscore = 0;
    public int lives = LIVES;
    public int bombs = BOMBS;
    public PowerLevel powerLevel = NORMAL;
    public int numberOfContinue = NUMBER_OF_CONTINUE;
    public Level level = Level.Level1;
    public float secondScript = -3;

    public PlayerData copyPlayerData() {
        return new PlayerData(level, numberOfContinue, fireDelay, fireDelaySide, enemiesKilled, laserShipKilled, howManyLifesLosed, score, lives, bombs, powerLevel);
    }

    @Override
    public void reset() {
        secondScript = -3;
        level = Level.Level1;
        numberOfContinue = NUMBER_OF_CONTINUE;
        howManyLifesLosed = 0;
        score = 0;
        enemiesKilled = 0;
        laserShipKilled = 0;
        lives = LIVES;
        bombs = BOMBS;
        powerLevel = NORMAL;
        fireDelay = FIRE_DELAY;
        fireDelaySide = FIRE_DELAY_SIDE;
        enemiesCountLevel = 0;
        enemiesKilledLevel = 0;
    }

    public String getScore() {
        return String.format("%7s", String.valueOf(score)).replace(' ', '0');
    }

    public String getHighscore() {
        return String.format("%7s", String.valueOf(highscore)).replace(' ', '0');
    }

    public void resetScore() {
        this.score = 0;
    }

    public boolean updateScore(int points) {
        int oldScore = score;
        score += points;
        if (score > highscore) {
            highscore = score;
        }
        if (Math.floor(oldScore / NEW_LIFE) < Math.floor(score / NEW_LIFE)) {
            lives++;
            return true;
        } else {
            return false;
        }
    }

    public void loseLife() {
        lives--;
        howManyLifesLosed++;
        powerLevel = NORMAL;
        fireDelaySide = FIRE_DELAY_SIDE;
        fireDelay = FIRE_DELAY;
    }

    public boolean hasBombs() {
        return bombs > 0;
    }

    public void useBomb() {
        if (bombs == 0) {
            return;
        }
        bombs--;
    }

    public void powerUp() {
        switch (powerLevel) {
            case NORMAL:
                powerLevel = DOUBLE;
                break;
            case DOUBLE:
                powerLevel = TRIPLE;
                break;
            case TRIPLE:
                powerLevel = TRIPLE_SIDE;
                break;
            case TRIPLE_SIDE:
                fireDelay = FIRE_DELAY_FAST;
                fireDelaySide = FIRE_DELAY_SIDE_FAST;
                powerLevel = TRIPLE_FAST;
                break;
            case TRIPLE_FAST:
                fireDelay = FIRE_DELAY_VERY_FAST;
                fireDelaySide = FIRE_DELAY_SIDE_VERY_FAST;
                powerLevel = TRIPLE_VERY_FAST;
                break;
            case TRIPLE_VERY_FAST:
                updateScore(100);
                break;
        }
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public int getScoreInt() {
        return score;
    }

    public boolean isHighscore() {
        return score == highscore;
    }

    public void setHighScore(int highScore) {
        this.highscore = highScore;
    }
}
