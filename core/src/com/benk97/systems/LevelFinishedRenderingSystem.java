package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benk97.assets.Assets;
import com.benk97.components.LeveLFinishedComponent;
import com.benk97.components.Mappers;
import com.benk97.components.PauseComponent;
import com.benk97.components.PlayerComponent;
import com.benk97.screens.LevelScreen;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.assets.Assets.FONT_SPACE_KILLER_SMALLEST;

public class LevelFinishedRenderingSystem extends IteratingSystem {
    public static final String LEVEL_1_FINISHED = "lvl 1 completed";
    public static final String LEVEL_2_FINISHED = "lvl 2 completed";
    public static final String LEVEL_3_FINISHED = "lvl 3 completed";
    public static final String ENEMIES_KILLED = "enemies killed";
    public static final String NEXT_NOT_IMPLEMNTED = "no level 4 yet";
    public static final String START_AGAIN = "start again";
    public static final String GO_NEXT = "go next level";
    private SpriteBatch batcher;
    private BitmapFont mediumFont;
    private LevelScreen.Level level;

    public LevelFinishedRenderingSystem(SpriteBatch batcher, Assets assets, LevelScreen.Level level, int priority) {
        super(Family.all(PlayerComponent.class, LeveLFinishedComponent.class).exclude(PauseComponent.class).get(), priority);
        this.level = level;
        this.batcher = batcher;
        this.mediumFont = assets.get(FONT_SPACE_KILLER_SMALLEST);
        this.mediumFont.setColor(Color.WHITE);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        mediumFont.draw(batcher,
                level.equals(LevelScreen.Level.Level3) ? LEVEL_3_FINISHED :
                        level.equals(LevelScreen.Level.Level2) ? LEVEL_2_FINISHED : LEVEL_1_FINISHED, 10f, 4 * SCREEN_HEIGHT / 5f);
        PlayerComponent player = Mappers.player.get(entity);
        mediumFont.draw(batcher, ENEMIES_KILLED, 10f, 4 * SCREEN_HEIGHT / 5f - 100f);
        mediumFont.draw(batcher, player.enemiesKilledLevel + " out of " + player.enemiesCountLevel, 40f, 4 * SCREEN_HEIGHT / 5f - 150f);
        if (!level.equals(LevelScreen.Level.Level3)) {
            mediumFont.draw(batcher, GO_NEXT, 10f, 4 * SCREEN_HEIGHT / 5f - 250f);
        } else {
            mediumFont.draw(batcher, NEXT_NOT_IMPLEMNTED, 10f, 4 * SCREEN_HEIGHT / 5f - 250f);
            mediumFont.draw(batcher, START_AGAIN, 10f, 4 * SCREEN_HEIGHT / 5f - 300f);

        }
    }
}
