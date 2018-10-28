/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 22:06
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.StringBuilder;
import com.bendk97.player.PlayerData;
import com.bendk97.player.PlayerDataBuilder;
import com.bendk97.screens.levels.Level;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.components.PlayerComponent.PowerLevel.*;
import static com.bendk97.screens.levels.Level.Level1;


public class PlayerComponent implements Component, Pool.Poolable {

    public int enemiesCountLevel = 0;
    public int enemiesKilledLevel = 0;
    public long fireDelay = FIRE_DELAY;
    public long fireDelaySide = FIRE_DELAY_SIDE;
    public int enemiesKilled = 0;
    public int laserShipKilled = 0;
    public int howManyLivesLost = 0;
    private int score = 0;
    private StringBuilder scoreStr = new StringBuilder();
    private int high_score = 0;
    private StringBuilder highScoreStr = new StringBuilder();
    public int lives = LIVES;
    public int bombs = BOMBS;
    public PowerLevel powerLevel = NORMAL;
    public int numberOfContinue = NUMBER_OF_CONTINUE;
    public Level level = Level1;
    public float secondScript = -3;

    public void enemyKilled() {
        enemiesKilledLevel++;
        enemiesKilled++;
    }

    public enum PowerLevel {
        NORMAL(Constants.BLUE_FIRE, "bullet"),
        DOUBLE(Constants.BLUE_FIRE, "bullet2"),
        TRIPLE(Constants.BLUE_FIRE, "bullet3"),
        TRIPLE_SIDE(Constants.BLUE_FIRE, "bullet3", "bulletLeft1", "bulletRight1"),
        TRIPLE_FAST(Constants.GREEN_FIRE, "bullet4", "bulletLeft2", "bulletRight2"),
        TRIPLE_VERY_FAST(Constants.ORANGE_FIRE, "bullet5", "bulletLeft3", "bulletRight3");

        public final Color color;
        public final String bulletRegionName;
        public String bulletLeftSidedRegionName;
        public String bulletRightSidedRegionName;

        PowerLevel(Color color, String bulletRegionName) {
            this.color = color;
            this.bulletRegionName = bulletRegionName;
        }

        PowerLevel(Color color, String bulletRegionName, String left, String right) {
            this.color = color;
            this.bulletRegionName = bulletRegionName;
            this.bulletLeftSidedRegionName = left;
            this.bulletRightSidedRegionName = right;
        }

        private static class Constants {
            static final Color BLUE_FIRE = new Color(43f / 255f, 197f / 255f, 205f / 255f, 0.6f);
            static final Color GREEN_FIRE = new Color(3f / 255f, 255f / 255f, 136f / 255f, 0.6f);
            static final Color ORANGE_FIRE = new Color(255f / 255f, 120f / 255f, 0f, 0.6f);
        }
    }

    public PlayerData copyPlayerData() {
        return new PlayerDataBuilder()
                .atLevel(level)
                .withNumberOfContinue(numberOfContinue)
                .withWeaponPower(powerLevel, fireDelay, fireDelaySide)
                .enemiesKilled(enemiesKilled, laserShipKilled)
                .gameStats(lives, bombs, score, howManyLivesLost)
                .createPlayerData();
    }

    @Override
    public void reset() {
        secondScript = -3;
        level = Level1;
        numberOfContinue = NUMBER_OF_CONTINUE;
        howManyLivesLost = 0;
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

    public StringBuilder getScore() {
        scoreStr.setLength(0);
        return scoreStr.append(score, 7);
    }

    public StringBuilder getHighScoreFormatted() {
        highScoreStr.setLength(0);
        return highScoreStr.append(high_score, 7);
    }

    public void newCredit() {
        lives = LIVES;
        bombs = BOMBS;
        this.score = 0;
        numberOfContinue--;
    }

    public boolean updateScore(int points) {
        int oldScore = score;
        score += points;
        if (score > high_score) {
            high_score = score;
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
        howManyLivesLost++;
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
            default:
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

    public boolean isHighScore() {
        return score == high_score;
    }

    public void setHighScore(int highScore) {
        this.high_score = highScore;
    }
}
