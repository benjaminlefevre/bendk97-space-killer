package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benk97.assets.Assets;
import com.benk97.components.Mappers;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.FONT_SPACE_KILLER;

public class ScoresRenderingSystem extends EntitySystem {
    private SpriteBatch batcher;
    private BitmapFont bitmapFont;
    private Assets assets;
    private Entity player;

    public ScoresRenderingSystem(SpriteBatch batcher, Assets assets, Entity player, int priority) {
        super(priority);
        this.assets = assets;
        this.player = player;
        this.batcher = batcher;
        this.bitmapFont = assets.get(FONT_SPACE_KILLER);
        bitmapFont.setColor(Color.WHITE);
    }

    @Override
    public void update(float deltaTime) {
        bitmapFont.draw(batcher, "SCORE", SCORE_X, SCORE_Y);
        bitmapFont.draw(batcher, Mappers.player.get(player).getScore(), SCORE_X - 10f, SCORE_Y - 20f);
        bitmapFont.draw(batcher, "LIVES", LIVES_X, LIVES_Y);
        bitmapFont.draw(batcher, "HIGH", HIGH_X, HIGH_Y);
        bitmapFont.draw(batcher, Mappers.player.get(player).getHighccore(), HIGH_X - 10f, HIGH_Y - 20f);
    }
}
