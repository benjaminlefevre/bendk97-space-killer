package com.benk97.listeners;

import com.badlogic.ashley.core.Entity;

public interface PlayerListener {
    void loseLive(Entity player);

    void updateScore(Entity player, Entity ennemy);

    void updateScore(Entity player, int score);
}
