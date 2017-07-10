package com.benk97.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benk97.components.BackgroundComponent;
import com.benk97.components.DynamicSpriteComponent;
import com.benk97.components.PositionComponent;
import com.benk97.components.StaticSpriteComponent;

import static com.benk97.SpaceKillerGameConstants.*;

public class RenderingSystem extends EntitySystem {
    private Entity player;
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> staticEntities;
    private ImmutableArray<Entity> backgroundEntities;


    private ComponentMapper<DynamicSpriteComponent> dm = ComponentMapper.getFor(DynamicSpriteComponent.class);
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<StaticSpriteComponent> sm = ComponentMapper.getFor(StaticSpriteComponent.class);
    private ComponentMapper<BackgroundComponent> bm = ComponentMapper.getFor(BackgroundComponent.class);
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
        entities = engine.getEntitiesFor(Family.one(DynamicSpriteComponent.class).get());
        staticEntities = engine.getEntitiesFor(Family.all(StaticSpriteComponent.class).get());
        backgroundEntities = engine.getEntitiesFor(Family.all(BackgroundComponent.class).get());
    }


    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.setProjectionMatrix(camera.combined);
        // background
        batcher.begin();
        for (Entity entity : backgroundEntities) {
            BackgroundComponent backgroundComponent = bm.get(entity);
            PositionComponent positionComponent = pm.get(entity);
            batcher.draw(backgroundComponent.texture, 0, 0, (int) positionComponent.x, (int) positionComponent.y, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        // sprites
        for (Entity entity : entities) {
            PositionComponent position = pm.get(entity);
            DynamicSpriteComponent display = dm.get(entity);
            Sprite sprite = display.getSprite(deltaTime);
            if(sprite==null){
                Gdx.app.log("toto", sprite.toString());
            }
            sprite.setPosition(position.x, position.y);
            sprite.draw(batcher);
        }


        for (Entity entity : staticEntities) {
            StaticSpriteComponent staticSpriteComponent = sm.get(entity);
            staticSpriteComponent.getSprite(deltaTime).draw(batcher, staticSpriteComponent.alpha);
        }
        if (DEBUG) {
            bitmapFont.draw(batcher, Gdx.graphics.getFramesPerSecond() + " fps", 0f, SCREEN_HEIGHT - 10f);
        }
        batcher.end();


    }
}