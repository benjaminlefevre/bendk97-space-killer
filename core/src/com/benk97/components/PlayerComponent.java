package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;


public class PlayerComponent implements Component, Pool.Poolable{
    private int score = 0;
    private int highscore = 0;
    private int lives = 3;
    @Override
    public void reset() {
        score = 0;
        lives = 3;
    }

    public String getScore() {
        return String.format("%7s", String.valueOf(score)).replace(' ', '0');
    }
    public String getHighccore() {
        return String.format("%7s", String.valueOf(highscore)).replace(' ', '0');
    }


    public void updateScore(int points) {
        score += points;
        if(score>highscore){
            highscore = score;
        }
    }

    public void loseLife() {
        lives--;
    }
}
