package com.benk97.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.benk97.assets.Assets;
import com.benk97.components.Mappers;
import com.benk97.components.SpriteComponent;
import com.benk97.entities.EntityFactory;
import com.benk97.inputs.TouchInputProcessor;
import com.benk97.listeners.impl.CollisionListenerImpl;
import com.benk97.listeners.impl.InputListenerImpl;
import com.benk97.listeners.impl.PlayerListenerImpl;
import com.benk97.systems.*;
import com.benk97.tweens.SpriteComponentAccessor;

import static com.benk97.SpaceKillerGameConstants.*;

public class LevelScreen extends ScreenAdapter {

    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected PooledEngine engine;
    protected EntityFactory entityFactory;
    protected TweenManager tweenManager;
    public Assets assets;
    private SpriteBatch batcher;


    public LevelScreen(Assets assets) {
        this.batcher = new SpriteBatch();
        this.assets = assets;
        this.tweenManager = new TweenManager();
        this.camera = new OrthographicCamera();
        viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        engine = new PooledEngine();
        engine.addEntityListener(new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                Gdx.app.log("entity added", "size: " + engine.getEntities().size());
            }

            @Override
            public void entityRemoved(Entity entity) {
                Gdx.app.log("entity removed", "size " + engine.getEntities().size());
            }
        });
        entityFactory = new EntityFactory(engine, assets);
        Entity player = entityFactory.createEntityPlayer();
        Array<Entity> lives = entityFactory.createEntityPlayerLives(player);
        createSystems(player, lives, batcher);
        registerTweensAccessor();
    }

    private void registerTweensAccessor() {
        Tween.registerAccessor(SpriteComponent.class, new SpriteComponentAccessor());
    }

    private void createSystems(Entity player, Array<Entity> lives, SpriteBatch batcher) {
        engine.addSystem(createInputHandlerSystem(player));
        PlayerListenerImpl playerListener = new PlayerListenerImpl(entityFactory, lives, tweenManager);
        engine.addSystem(playerListener);
        CollisionListenerImpl collisionListener = new CollisionListenerImpl(assets, entityFactory, playerListener);
        engine.addSystem(collisionListener);
        engine.addSystem(new AnimationSystem(0));
        engine.addSystem(new StateSystem(1));
        engine.addSystem(new MovementSystem(2));
        engine.addSystem(new BackgroundRenderingSystem(batcher, 3));
        engine.addSystem(new DynamicEntitiesRenderingSystem(batcher, 4));
        engine.addSystem(new StaticEntitiesRenderingSystem(batcher, 5));
        engine.addSystem(new ScoresRenderingSystem(batcher, assets, player, 6));
        engine.addSystem(new GameOverRenderingSystem(batcher, camera, assets, 7));
        if (DEBUG) {
            engine.addSystem(new FPSDisplayRenderingSystem(batcher, 8));
        }
        engine.addSystem(new CollisionSystem(collisionListener, 9));
        engine.addSystem(new RemovableSystem(10));
    }


    private InputListenerImpl createInputHandlerSystem(Entity player) {
        Entity fireButton = entityFactory.createEntityFireButton(0.2f, FIRE_X, FIRE_Y);
        Entity padController = entityFactory.createEntitiesPadController(0.2f, PAD_X, PAD_Y);
        // define touch area as rectangles
        Sprite padSprite = Mappers.sprite.get(padController).sprite;
        float heightTouch = padSprite.getHeight() / 3f, widthTouch = padSprite.getWidth() / 3f;
        Rectangle[] squareTouchesDirection = new Rectangle[8];
        squareTouchesDirection[0] = new Rectangle(PAD_X, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[1] = new Rectangle(PAD_X + widthTouch, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[2] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[3] = new Rectangle(PAD_X, PAD_Y + heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[4] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y + heightTouch, widthTouch, heightTouch);
        squareTouchesDirection[5] = new Rectangle(PAD_X, PAD_Y, widthTouch, heightTouch);
        squareTouchesDirection[6] = new Rectangle(PAD_X + widthTouch, PAD_Y, widthTouch, heightTouch);
        squareTouchesDirection[7] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y, widthTouch, heightTouch);
        // input
        InputListenerImpl inputListener = new InputListenerImpl(player, entityFactory, assets);
        TouchInputProcessor touchInputProcessor = new TouchInputProcessor(inputListener, camera, squareTouchesDirection, Mappers.sprite.get(fireButton).getBounds());
        Gdx.input.setInputProcessor(touchInputProcessor);
        return inputListener;
    }


    @Override
    public void render(float delta) {
        tweenManager.update(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        batcher.setProjectionMatrix(camera.combined);
        engine.update(delta);
        batcher.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
