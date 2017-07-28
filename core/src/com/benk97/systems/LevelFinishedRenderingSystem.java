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
import com.benk97.screens.LevelScreen;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.assets.Assets.FONT_SPACE_KILLER_MEDIUM;

public class LevelFinishedRenderingSystem extends IteratingSystem {
    public static final String LEVEL_1_FINISHED = "level 1\n\nfinished";
    public static final String LEVEL_2_FINISHED = "level 2\n\nfinished";
    public static final String NEXT_NOT_IMPLEMNTED ="no level\n\n3 yet";
    public static final String START_AGAIN = "start\n\n  again";
    public static final String GO_NEXT = "go next\n\n   level";
    private SpriteBatch batcher;
    private BitmapFont mediumFont;
    private LevelScreen.Level level;
    private Assets assets;
    private ShapeRenderer shapeRenderer;

    public LevelFinishedRenderingSystem(SpriteBatch batcher, Assets assets, LevelScreen.Level level, int priority) {
        super(Family.all(PlayerComponent.class, LeveLFinishedComponent.class).get(), priority);
        this.level = level;
        this.assets = assets;
        this.batcher = batcher;
        this.mediumFont = assets.get(FONT_SPACE_KILLER_MEDIUM);
        this.mediumFont.setColor(Color.WHITE);
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        mediumFont.draw(batcher, level.equals(LevelScreen.Level.Level2) ? LEVEL_2_FINISHED :
                LEVEL_1_FINISHED, 5f, 4 * SCREEN_HEIGHT / 5f);
        if(level.equals(LevelScreen.Level.Level1)) {
            mediumFont.draw(batcher, GO_NEXT, 5f, 4 * SCREEN_HEIGHT / 5f - 200f);
        }else {
            mediumFont.draw(batcher, NEXT_NOT_IMPLEMNTED, 5f, 4 * SCREEN_HEIGHT / 5f - 200f);
            mediumFont.draw(batcher, START_AGAIN, 5f, 4 * SCREEN_HEIGHT / 5f - 400f);

        }
    }
}
