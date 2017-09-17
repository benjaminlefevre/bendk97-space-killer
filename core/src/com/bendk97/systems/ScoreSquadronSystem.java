package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.RandomXS128;

import java.util.Random;

public class ScoreSquadronSystem extends IteratingSystem {

    private BitmapFont font;
    private SpriteBatch batcher;

    public ScoreSquadronSystem(int priority, com.bendk97.assets.Assets assets, SpriteBatch batcher) {
        super(Family.all(com.bendk97.components.ScoreSquadronComponent.class).get(), priority);
        this.font = assets.get(com.bendk97.assets.Assets.FONT_SPACE_KILLER);
        this.batcher = batcher;
    }

    private Random random = new RandomXS128();

    @Override
    public void processEntity(final Entity entity, float deltaTime) {
        com.bendk97.components.ScoreSquadronComponent squadron = com.bendk97.components.Mappers.scoreSquadron.get(entity);
        com.bendk97.components.PositionComponent position = com.bendk97.components.Mappers.position.get(entity);
        font.setColor(Color.YELLOW);
        font.draw(batcher, squadron.score + "", position.x, position.y);
    }
}