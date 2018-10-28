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
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bendk97.assets.Assets;
import com.bendk97.components.LevelFinishedComponent;
import com.bendk97.components.PauseComponent;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.screens.levels.Level;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.pools.BitmapFontHelper.drawText;
import static com.bendk97.screens.levels.Level.Level2;
import static com.bendk97.screens.levels.Level.Level3;

public class LevelFinishedRenderingSystem extends IteratingSystem {
    private static final String LEVEL_1_FINISHED = "lvl 1 completed";
    private static final String LEVEL_2_FINISHED = "lvl 2 completed";
    private static final String LEVEL_3_FINISHED = "lvl 3 completed";
    private static final String ENEMIES_KILLED = "enemies killed";
    private static final String NEXT_NOT_IMPLEMENTED = "no level 4 yet";
    private static final String START_AGAIN = "start again";
    private static final String GO_NEXT = "go next level";
    private final SpriteBatch batcher;
    private final BitmapFontCache mediumFont;
    private final Level level;

    public LevelFinishedRenderingSystem(SpriteBatch batcher, Assets assets, Level level, int priority) {
        super(Family.all(PlayerComponent.class, LevelFinishedComponent.class).exclude(PauseComponent.class).get(), priority);
        this.level = level;
        this.batcher = batcher;
        this.mediumFont = assets.getFont(Assets.FONT_SPACE_KILLER_SMALLEST);
        this.mediumFont.setColor(Color.WHITE);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        drawText(batcher, mediumFont,
                level.equals(Level3) ? LEVEL_3_FINISHED :
                        level.equals(Level2) ? LEVEL_2_FINISHED : LEVEL_1_FINISHED, 10f, 4 * SCREEN_HEIGHT / 5f);
        PlayerComponent player = ComponentMapperHelper.player.get(entity);
        drawText(batcher, mediumFont, ENEMIES_KILLED, 10f, 4 * SCREEN_HEIGHT / 5f - 100f);
        drawText(batcher, mediumFont, player.enemiesKilledLevel + " out of " + player.enemiesCountLevel, 40f, 4 * SCREEN_HEIGHT / 5f - 150f);
        if (!level.equals(Level3)) {
            drawText(batcher, mediumFont, GO_NEXT, 10f, 4 * SCREEN_HEIGHT / 5f - 250f);
        } else {
            drawText(batcher, mediumFont, NEXT_NOT_IMPLEMENTED, 10f, 4 * SCREEN_HEIGHT / 5f - 250f);
            drawText(batcher, mediumFont, START_AGAIN, 10f, 4 * SCREEN_HEIGHT / 5f - 300f);

        }
    }
}
