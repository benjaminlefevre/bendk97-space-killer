/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.listeners;

import com.badlogic.ashley.core.Entity;

public interface PlayerListener {
    void loseLive(Entity player);

    void updateScore(Entity player, Entity ennemy, int nbHits);

    void updateScore(Entity player, int score);

    void newBombObtained(Entity player);

    void dropBomb();
}
