/*
 * Developed by Benjamin Lefèvre
 * Last modified 14/10/18 00:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public abstract class GLDarkRenderingSystem extends IteratingSystem {

    private static final Color BLACK_60 = new Color(0, 0, 0, 0.6f);
    protected final SpriteBatch batcher;
    private final ShapeRenderer shapeRenderer;
    private final Camera camera;

    protected GLDarkRenderingSystem(Family family, SpriteBatch batcher, Camera camera, int priority) {
        super(family, priority);
        this.camera = camera;
        this.batcher = batcher;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        batcher.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(BLACK_60);
        shapeRenderer.rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batcher.begin();
    }
}
