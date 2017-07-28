package com.benk97.player;

import com.benk97.components.PlayerComponent;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.components.PlayerComponent.PowerLevel.NORMAL;

/* use to pass player data between screens*/
public class PlayerData {
    public long fireDelay = FIRE_DELAY;
    public int enemiesKilled = 0;
    public int laserShipKilled = 0;
    public int howManyLifesLosed = 0;
    public int score = 0;
    private int highscore = 0;
    public int lives = LIVES;
    public int bombs = BOMBS;
    public PlayerComponent.PowerLevel powerLevel = NORMAL;

    public PlayerData(long fireDelay, int enemiesKilled, int laserShipKilled, int howManyLifesLosed, int score, int highscore, int lives, int bombs, PlayerComponent.PowerLevel powerLevel) {
        this.fireDelay = fireDelay;
        this.enemiesKilled = enemiesKilled;
        this.laserShipKilled = laserShipKilled;
        this.howManyLifesLosed = howManyLifesLosed;
        this.score = score;
        this.highscore = highscore;
        this.lives = lives;
        this.bombs = bombs;
        this.powerLevel = powerLevel;
    }
}
