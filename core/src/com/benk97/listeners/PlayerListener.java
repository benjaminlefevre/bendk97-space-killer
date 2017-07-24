package com.benk97.listeners;

import com.badlogic.ashley.core.Entity;

public interface PlayerListener {
    void loseLive(Entity player);

    void updateScore(Entity player, Entity ennemy, int nbHits);

    void updateScore(Entity player, int score);

    void newBombObtained(Entity player);

    void dropBomb();
}
