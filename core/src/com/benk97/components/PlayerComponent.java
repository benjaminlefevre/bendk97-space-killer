package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.components.PlayerComponent.PowerLevel.*;


public class PlayerComponent implements Component, Pool.Poolable {

    public enum PowerLevel {
        NORMAL, DOUBLE, TRIPLE
    }

    public long fireDelay = FIRE_DELAY;
    public int enemiesKilled = 0;
    public int howManyLifesLosed = 0;
    private int score = 0;
    private int highscore = 0;
    public int lives = LIVES;
    public int bombs = BOMBS;
    public PowerLevel powerLevel = NORMAL;

    @Override
    public void reset() {
        howManyLifesLosed = 0;
        score = 0;
        enemiesKilled = 0;
        lives = LIVES;
        bombs = BOMBS;
        powerLevel = NORMAL;
        fireDelay = FIRE_DELAY;
    }

    public String getScore() {
        return String.format("%7s", String.valueOf(score)).replace(' ', '0');
    }

    public String getHighccore() {
        return String.format("%7s", String.valueOf(highscore)).replace(' ', '0');
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
        if (powerLevel.equals(NORMAL)) {
            powerLevel = DOUBLE;
        } else if (powerLevel.equals(DOUBLE)) {
            powerLevel = TRIPLE;
        } else if (fireDelay == FIRE_DELAY) {
            fireDelay = FIRE_DELAY_FAST;
        } else if (fireDelay == FIRE_DELAY_FAST) {
            fireDelay = FIRE_DELAY_VERY_FAST;
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
