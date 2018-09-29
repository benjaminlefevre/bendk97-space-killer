/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.bendk97.assets.Assets;
import com.bendk97.components.PauseComponent;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class PauseRenderingSystem extends IteratingSystem {
    public static final String RESUME = "resume";
    public static final String QUIT = "quit";
    private SpriteBatch batcher;
    private BitmapFont mediumFont;

    private ShapeRenderer shapeRenderer;
    private Camera camera;

    public PauseRenderingSystem(SpriteBatch batcher, Camera camera, Assets assets, int priority) {
        super(Family.all(PauseComponent.class).get(), priority);
        this.camera = camera;
        this.batcher = batcher;
        this.mediumFont = assets.get(Assets.FONT_SPACE_KILLER_MEDIUM);
        this.mediumFont.setColor(Color.WHITE);
        this.shapeRenderer = new ShapeRenderer();
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
        mediumFont.draw(batcher, RESUME, 50f, SCREEN_HEIGHT *2f/3f);
        mediumFont.draw(batcher, QUIT, 100f, SCREEN_HEIGHT *1f/2f);

    }
}
