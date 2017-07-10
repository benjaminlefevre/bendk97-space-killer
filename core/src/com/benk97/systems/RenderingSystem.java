package com.benk97.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benk97.components.*;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.components.Mappers.background;

public class RenderingSystem extends EntitySystem {
    private Entity player;
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> staticEntities;
    private ImmutableArray<Entity> backgroundEntities;

    private SpriteBatch batcher;
    private OrthographicCamera camera;
    private BitmapFont bitmapFont;

    public RenderingSystem(OrthographicCamera camera, Entity player) {
        super(2);
        this.player = player;
        this.batcher = new SpriteBatch();
        this.camera = camera;
        if (DEBUG) {
            bitmapFont = new BitmapFont();
            bitmapFont.getData().setScale(0.5f);
        }
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.one(SpriteComponent.class).get());
        staticEntities = engine.getEntitiesFor(Family.all(StaticSpriteComponent.class).get());
        backgroundEntities = engine.getEntitiesFor(Family.all(BackgroundComponent.class).get());
    }


    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // background
        batcher.begin();
        batcher.setProjectionMatrix(camera.combined);

        for (Entity entity : backgroundEntities) {
            BackgroundComponent backgroundComponent = background.get(entity);
            PositionComponent positionComponent = Mappers.position.get(entity);
            batcher.draw(backgroundComponent.texture, 0, 0, (int) positionComponent.x, (int) positionComponent.y, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        // sprites
        for (Entity entity : entities) {
            PositionComponent position = Mappers.position.get(entity);
            SpriteComponent spriteComponent = Mappers.sprite.get(entity);
            Sprite sprite = spriteComponent.sprite;
            sprite.setPosition(position.x, position.y);
            sprite.draw(batcher);
        }
        if (DEBUG) {
            bitmapFont.draw(batcher, Gdx.graphics.getFramesPerSecond() + " fps", 0f, SCREEN_HEIGHT - 10f);
        }

        batcher.setProjectionMatrix(camera.combined);
        for (Entity entity : staticEntities) {
            StaticSpriteComponent staticSpriteComponent = Mappers.staticSprite.get(entity);
            staticSpriteComponent.sprite.draw(batcher, staticSpriteComponent.alpha);
        }

        batcher.end();
    }
}