package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.benk97.assets.Assets;
import com.benk97.components.GameOverComponent;
import com.benk97.components.Mappers;
import com.benk97.components.PlayerComponent;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.*;

public class GameOverRenderingSystem extends IteratingSystem {
    public static final String GAME_OVER = "GAME\nOVER";
    public static final String SCORE = "score\n\n";
    public static final String HIGHSCORE = "highscore\n\n";
    private SpriteBatch batcher;
    private BitmapFont largeFont;
    private BitmapFont mediumFont;

    private Assets assets;
    private ShapeRenderer shapeRenderer;
    private Camera camera;

    private Sprite extraLife, playAgain, home, share;

    public GameOverRenderingSystem(SpriteBatch batcher, Camera camera, Assets assets, int priority) {
        super(Family.all(PlayerComponent.class, GameOverComponent.class).get(), priority);
        this.assets = assets;
        this.camera = camera;
        this.batcher = batcher;
        this.largeFont = assets.get(FONT_SPACE_KILLER_LARGE);
        this.largeFont.setColor(Color.RED);
        this.mediumFont = assets.get(FONT_SPACE_KILLER_MEDIUM);
        this.mediumFont.setColor(Color.WHITE);
        this.shapeRenderer = new ShapeRenderer();
        this.playAgain = assets.get(GFX_LEVEL_ALL_ATLAS_NOMASK).createSprite("icon-playagain");
        this.home = assets.get(GFX_LEVEL_ALL_ATLAS_NOMASK).createSprite("icon-home");
        this.share = assets.get(GFX_LEVEL_ALL_ATLAS_NOMASK).createSprite("icon-share");
        this.extraLife = assets.get(GFX_LEVEL_ALL_ATLAS_NOMASK).createSprite("icon-extralife");
        this.playAgain.setPosition(PLAY_X, PLAY_Y);
        this.home.setPosition(HOME_X, HOME_Y);
        this.share.setPosition(SHARE_X, SHARE_Y);
        this.extraLife.setPosition(EXTRA_X, EXTRA_Y);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        batcher.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.6f));
        shapeRenderer.rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batcher.begin();
        largeFont.draw(batcher, GAME_OVER, SCREEN_WIDTH / 4f - 10f, SCREEN_HEIGHT - 100f);
        mediumFont.draw(batcher, SCORE + Mappers.player.get(entity).getScore(), 20f, SCREEN_HEIGHT / 2f + 30f);
        mediumFont.draw(batcher, HIGHSCORE + Mappers.player.get(entity).getHighccore(), 10f, SCREEN_HEIGHT / 2f - 130f);
        if (Mappers.player.get(entity).rewardAds > 0) {
            extraLife.draw(batcher);
        }
        playAgain.draw(batcher);
        home.draw(batcher);
        share.draw(batcher);
    }
}
