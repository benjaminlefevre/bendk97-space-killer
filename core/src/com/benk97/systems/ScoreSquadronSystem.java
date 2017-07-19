package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benk97.assets.Assets;
import com.benk97.components.Mappers;
import com.benk97.components.PositionComponent;
import com.benk97.components.ScoreSquadronComponent;

import java.util.Random;

import static com.benk97.assets.Assets.FONT_SPACE_KILLER;

public class ScoreSquadronSystem extends IteratingSystem {

    private BitmapFont font;
    private SpriteBatch batcher;

    public ScoreSquadronSystem(int priority, Assets assets, SpriteBatch batcher) {
        super(Family.all(ScoreSquadronComponent.class).get(), priority);
        this.font = assets.get(FONT_SPACE_KILLER);
        this.batcher = batcher;
    }

    private Random random = new Random(System.currentTimeMillis());

    @Override
    public void processEntity(final Entity entity, float deltaTime) {
        ScoreSquadronComponent squadron = Mappers.scoreSquadron.get(entity);
        PositionComponent position = Mappers.position.get(entity);
        font.setColor(Color.YELLOW);
        font.draw(batcher, squadron.score + "", position.x, position.y);
    }
}