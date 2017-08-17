package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.benk97.player.PlayerData;
import com.benk97.screens.LevelScreen;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.components.PlayerComponent.PowerLevel.*;


public class PlayerComponent implements Component, Pool.Poolable {

    public void enemyKilled() {
        enemiesKilledLevel++;
        enemiesKilled++;
    }

    public enum PowerLevel {
        NORMAL("bullet"), DOUBLE("bullet2"), TRIPLE("bullet3"), TRIPLE_SIDE("bullet3", "bulletLeft1", "bulletRight1"),
        TRIPLE_FAST("bullet4", "bulletLeft2", "bulletRight2"), TRIPLE_VERY_FAST("bullet5", "bulletLeft3", "bulletRight3");

        public String regionName;
        public String leftRegionName;
        public String rightRegionName;

        PowerLevel(String regionName) {
            this.regionName = regionName;
        }
        PowerLevel(String regionName, String left, String right) {
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
    public int rewardAds = EXTRA_LIVES_ADS;
    public LevelScreen.Level level = LevelScreen.Level.Level1;
    public float secondScript = -3;

    public PlayerData copyPlayerData() {
        return new PlayerData(level, secondScript, rewardAds, fireDelay, fireDelaySide, enemiesKilled, laserShipKilled, howManyLifesLosed, score, highscore, lives, bombs, powerLevel);
    }

    @Override
    public void reset() {
        secondScript = -3;
        level = LevelScreen.Level.Level1;
        rewardAds = EXTRA_LIVES_ADS;
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

    public String getHighccore() {
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
