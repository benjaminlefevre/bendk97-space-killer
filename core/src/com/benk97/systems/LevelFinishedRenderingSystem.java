package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.benk97.assets.Assets;
import com.benk97.components.LeveLFinishedComponent;
import com.benk97.components.PlayerComponent;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.assets.Assets.FONT_SPACE_KILLER_MEDIUM;

public class LevelFinishedRenderingSystem extends IteratingSystem {
    private SpriteBatch batcher;
    private BitmapFont mediumFont;

    private Assets assets;
    private ShapeRenderer shapeRenderer;

    public LevelFinishedRenderingSystem(SpriteBatch batcher, Assets assets, int priority) {
        super(Family.all(PlayerComponent.class, LeveLFinishedComponent.class).get(), priority);
        this.assets = assets;
        this.batcher = batcher;
        this.mediumFont = assets.get(FONT_SPACE_KILLER_MEDIUM);
        this.mediumFont.setColor(Color.WHITE);
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        mediumFont.draw(batcher, "level 1\n\nfinished", 5f, 4 * SCREEN_HEIGHT / 5f);
        mediumFont.draw(batcher, "start\n\nagain", 5f, 4 * SCREEN_HEIGHT / 5f - 200f);
    }
}
