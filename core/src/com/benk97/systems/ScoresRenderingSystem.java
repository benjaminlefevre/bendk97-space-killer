package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benk97.assets.Assets;
import com.benk97.components.Mappers;
import com.benk97.components.PlayerComponent;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.FONT_SPACE_KILLER;

public class ScoresRenderingSystem extends IteratingSystem {
    public static final String SCORE = "SCORE";
    public static final String LIVES = "LIVES";
    public static final String HIGH = "HIGH";
    private SpriteBatch batcher;
    private BitmapFont bitmapFont;

    public ScoresRenderingSystem(SpriteBatch batcher, Assets assets, int priority) {
        super(Family.one(PlayerComponent.class).get() ,priority);
        this.batcher = batcher;
        this.bitmapFont = assets.get(FONT_SPACE_KILLER);
    }

    @Override
    protected void processEntity(Entity player, float deltaTime) {
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.draw(batcher, SCORE, SCORE_X, SCORE_Y);
        bitmapFont.draw(batcher, Mappers.player.get(player).getScore(), SCORE_X - 10f, SCORE_Y - 20f);
        bitmapFont.draw(batcher, LIVES, LIVES_X, LIVES_Y);
        bitmapFont.draw(batcher, HIGH, HIGH_X, HIGH_Y);
        bitmapFont.draw(batcher, Mappers.player.get(player).getHighccore(), HIGH_X - 10f, HIGH_Y - 20f);
    }
}
