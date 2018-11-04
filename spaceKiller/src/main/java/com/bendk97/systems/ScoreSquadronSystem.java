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
import com.bendk97.components.PositionComponent;
import com.bendk97.components.ScoreSquadronComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;

import static com.bendk97.assets.GameAssets.FONT_SPACE_KILLER;
import static com.bendk97.pools.BitmapFontHelper.drawText;

public class ScoreSquadronSystem extends IteratingSystem {

    private final BitmapFontCache font;
    private final SpriteBatch batcher;

    public ScoreSquadronSystem(int priority, GameAssets assets, SpriteBatch batcher) {
        super(Family.all(ScoreSquadronComponent.class).get(), priority);
        this.font = assets.getFont(FONT_SPACE_KILLER);
        this.batcher = batcher;
    }

    @Override
    public void processEntity(final Entity entity, float deltaTime) {
        ScoreSquadronComponent squadron = ComponentMapperHelper.scoreSquadron.get(entity);
        PositionComponent position = ComponentMapperHelper.position.get(entity);
        font.setColor(Color.YELLOW);
        drawText(batcher, font, squadron.score.toString(), position.x(), position.y());
    }
}