/*
 * Developed by Benjamin Lefèvre
 * Last modified 08/10/18 20:20
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels.utils;

import com.badlogic.ashley.core.Entity;
import com.bendk97.components.Mappers;
import com.bendk97.entities.enemies.SquadronFactory;

public class ScriptItemExecutor {
    private final SquadronFactory squadronFactory;
    private final Entity player;

    public ScriptItemExecutor(SquadronFactory squadronFactory, Entity player) {
        this.squadronFactory = squadronFactory;
        this.player = player;
    }


    public void execute(ScriptItem scriptItem) {
        squadronFactory.createSquadron(scriptItem);
        if (Mappers.levelFinished.get(player) == null) {
            Mappers.player.get(player).enemiesCountLevel += scriptItem.number;
        }
    }
}
