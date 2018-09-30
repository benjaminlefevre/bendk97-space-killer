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
import com.badlogic.gdx.math.RandomXS128;
import com.bendk97.assets.Assets;
import com.bendk97.components.Mappers;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.ScoreSquadronComponent;

import java.util.Random;

public class ScoreSquadronSystem extends IteratingSystem {

    private BitmapFont font;
    private SpriteBatch batcher;

    public ScoreSquadronSystem(int priority, Assets assets, SpriteBatch batcher) {
        super(Family.all(ScoreSquadronComponent.class).get(), priority);
        this.font = assets.get(Assets.FONT_SPACE_KILLER);
        this.batcher = batcher;
    }

    private Random random = new RandomXS128();

    @Override
    public void processEntity(final Entity entity, float deltaTime) {
        ScoreSquadronComponent squadron = Mappers.scoreSquadron.get(entity);
        PositionComponent position = Mappers.position.get(entity);
        font.setColor(Color.YELLOW);
        font.draw(batcher, squadron.score + "", position.x, position.y);
    }
}