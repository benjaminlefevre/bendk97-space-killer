/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 22:06
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.player;

import com.bendk97.components.PlayerComponent;
import com.bendk97.screens.levels.Level;

/* use to pass player data between screens*/
public final class PlayerData {
    public  long fireDelay;
    public  long fireDelaySide;
    public  int enemiesKilled;
    public  int laserShipKilled;
    public  int howManyLivesLost;
    public  int score;
    public  int lives;
    public  int bombs;
    public  PlayerComponent.PowerLevel powerLevel;
    public  int numberOfContinue;
    public Level level;
}
