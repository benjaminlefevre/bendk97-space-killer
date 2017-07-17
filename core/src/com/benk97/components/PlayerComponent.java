package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import static com.benk97.SpaceKillerGameConstants.LIVES;
import static com.benk97.components.PlayerComponent.PowerLevel.*;


public class PlayerComponent implements Component, Pool.Poolable {
    public enum PowerLevel {
        NORMAL, DOUBLE, TRIPLE
    }
    private int score = 0;
    private int highscore = 0;
    public int lives = LIVES;
    public PowerLevel powerLevel = NORMAL;

    @Override
    public void reset() {
        score = 0;
        lives = LIVES;
        powerLevel = NORMAL;
    }

    public String getScore() {
        return String.format("%7s", String.valueOf(score)).replace(' ', '0');
    }

    public String getHighccore() {
        return String.format("%7s", String.valueOf(highscore)).replace(' ', '0');
    }


    public void updateScore(int points) {
        score += points;
        if (score > highscore) {
            highscore = score;
        }
    }

    public void loseLife() {
        lives--;
        powerLevel = NORMAL;
    }

    public void powerUp() {
        if(powerLevel.equals(NORMAL)){
            powerLevel = DOUBLE;
        } else if(powerLevel.equals(DOUBLE)){
            powerLevel = TRIPLE;
        } else {
            updateScore(1000);
        }
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public int getScoreInt() {
        return score;
    }

    public void setHighScore(int highScore) {
        this.highscore = highScore;
    }
}
