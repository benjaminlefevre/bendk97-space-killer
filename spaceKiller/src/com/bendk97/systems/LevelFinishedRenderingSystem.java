/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bendk97.assets.Assets;
import com.bendk97.components.LevelFinishedComponent;
import com.bendk97.components.Mappers;
import com.bendk97.components.PauseComponent;
import com.bendk97.components.PlayerComponent;
import com.bendk97.screens.LevelScreen.Level;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;

public class LevelFinishedRenderingSystem extends IteratingSystem {
    private static final String LEVEL_1_FINISHED = "lvl 1 completed";
    private static final String LEVEL_2_FINISHED = "lvl 2 completed";
    private static final String LEVEL_3_FINISHED = "lvl 3 completed";
    private static final String ENEMIES_KILLED = "enemies killed";
    private static final String NEXT_NOT_IMPLEMENTED = "no level 4 yet";
    private static final String START_AGAIN = "start again";
    private static final String GO_NEXT = "go next level";
    private final SpriteBatch batcher;
    private final BitmapFont mediumFont;
    private final Level level;

    public LevelFinishedRenderingSystem(SpriteBatch batcher, Assets assets, Level level, int priority) {
        super(Family.all(PlayerComponent.class, LevelFinishedComponent.class).exclude(PauseComponent.class).get(), priority);
        this.level = level;
        this.batcher = batcher;
        this.mediumFont = assets.get(Assets.FONT_SPACE_KILLER_SMALLEST);
        this.mediumFont.setColor(Color.WHITE);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        mediumFont.draw(batcher,
                level.equals(Level.Level3) ? LEVEL_3_FINISHED :
                        level.equals(Level.Level2) ? LEVEL_2_FINISHED : LEVEL_1_FINISHED, 10f, 4 * SCREEN_HEIGHT / 5f);
        PlayerComponent player = Mappers.player.get(entity);
        mediumFont.draw(batcher, ENEMIES_KILLED, 10f, 4 * SCREEN_HEIGHT / 5f - 100f);
        mediumFont.draw(batcher, player.enemiesKilledLevel + " out of " + player.enemiesCountLevel, 40f, 4 * SCREEN_HEIGHT / 5f - 150f);
        if (!level.equals(Level.Level3)) {
            mediumFont.draw(batcher, GO_NEXT, 10f, 4 * SCREEN_HEIGHT / 5f - 250f);
        } else {
            mediumFont.draw(batcher, NEXT_NOT_IMPLEMENTED, 10f, 4 * SCREEN_HEIGHT / 5f - 250f);
            mediumFont.draw(batcher, START_AGAIN, 10f, 4 * SCREEN_HEIGHT / 5f - 300f);

        }
    }
}
