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
import com.bendk97.assets.GameAssets;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.components.texts.BossAlertComponent;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.GameAssets.FONT_SPACE_KILLER;
import static com.bendk97.pools.BitmapFontHelper.drawText;

public class TextHUDRenderingSystem extends IteratingSystem {
    private static final String SCORE = "SCORE";
    private static final String LIVES = "LIVES";
    private static final String HIGH = "HIGH";
    private final SpriteBatch batcher;
    private final BitmapFontCache bitmapFont;

    public TextHUDRenderingSystem(SpriteBatch batcher, GameAssets assets, int priority) {
        super(Family.one(PlayerComponent.class).get(), priority);
        this.batcher = batcher;
        this.bitmapFont = assets.getFont(FONT_SPACE_KILLER);
    }

    @Override
    protected void processEntity(Entity player, float deltaTime) {
        bitmapFont.setColor(Color.WHITE);
        drawText(batcher, bitmapFont, SCORE, SCORE_X, SCORE_Y);
        drawText(batcher, bitmapFont, SCORE, SCORE_X, SCORE_Y);
        drawText(batcher, bitmapFont, ComponentMapperHelper.player.get(player).getScore().toString(), SCORE_X - 10f, SCORE_Y - 20f);
        drawText(batcher, bitmapFont, LIVES, LIVES_X, LIVES_Y);
        drawText(batcher, bitmapFont, HIGH, HIGH_X, HIGH_Y);
        drawText(batcher, bitmapFont, ComponentMapperHelper.player.get(player).getHighScoreFormatted().toString(), HIGH_X - 10f, HIGH_Y - 20f);
        displayEventTexts(player);
    }

    private void displayEventTexts(Entity player) {
        BossAlertComponent bossAlertComponent = ComponentMapperHelper.bossAlert.get(player);
        if (bossAlertComponent != null) {
            drawText(batcher, bossAlertComponent.font, bossAlertComponent.text, bossAlertComponent.posX, bossAlertComponent.posY);
        }
    }
}
