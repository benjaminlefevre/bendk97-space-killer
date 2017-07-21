package com.benk97.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.benk97.Settings;
import com.benk97.SpaceKillerGame;
import com.benk97.assets.Assets;
import com.benk97.components.Mappers;
import com.benk97.components.PositionComponent;
import com.benk97.components.SpriteComponent;
import com.benk97.components.VelocityComponent;
import com.benk97.entities.EntityFactory;
import com.benk97.entities.SquadronFactory;
import com.benk97.inputs.RetroPadController;
import com.benk97.inputs.TouchInputProcessor;
import com.benk97.inputs.VirtualPadController;
import com.benk97.listeners.impl.CollisionListenerImpl;
import com.benk97.listeners.impl.InputListenerImpl;
import com.benk97.listeners.impl.PlayerListenerImpl;
import com.benk97.systems.*;
import com.benk97.tweens.PositionComponentAccessor;
import com.benk97.tweens.SpriteComponentAccessor;
import com.benk97.tweens.VelocityComponentAccessor;

import static com.benk97.SpaceKillerGameConstants.*;

public class LevelScreen extends ScreenAdapter {

    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected PooledEngine engine;
    protected EntityFactory entityFactory;
    protected SquadronFactory squadronFactory;
    protected TweenManager tweenManager;
    public Assets assets;
    private SpriteBatch batcher;
    private ShapeRenderer shapeRenderer;
    private SpaceKillerGame game;

    public LevelScreen(Assets assets, SpaceKillerGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        viewport = new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.batcher = new SpriteBatch();
        if (DEBUG) {
            this.shapeRenderer = new ShapeRenderer();
            this.shapeRenderer.setProjectionMatrix(this.camera.combined);
            this.shapeRenderer.setColor(Color.GREEN);
        }
        this.assets = assets;
        this.tweenManager = new TweenManager();
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
        entityFactory = new EntityFactory(engine, assets, tweenManager);
        squadronFactory = new SquadronFactory(tweenManager, entityFactory, engine);
        Entity player = entityFactory.createEntityPlayer();
        Array<Entity> lives = entityFactory.createEntityPlayerLives(player);
        createSystems(player, lives, batcher);
        registerTweensAccessor();
    }

    private void registerTweensAccessor() {
        Tween.registerAccessor(SpriteComponent.class, new SpriteComponentAccessor());
        Tween.registerAccessor(PositionComponent.class, new PositionComponentAccessor());
        Tween.registerAccessor(VelocityComponent.class, new VelocityComponentAccessor());
    }

    private void createSystems(Entity player, Array<Entity> lives, SpriteBatch batcher) {
        engine.addSystem(createInputHandlerSystem(player));
        PlayerListenerImpl playerListener = new PlayerListenerImpl(assets, entityFactory, lives, tweenManager, this);
        engine.addSystem(playerListener);
        CollisionListenerImpl collisionListener = new CollisionListenerImpl(tweenManager, assets, entityFactory, playerListener, (Level1Screen) this);
        engine.addSystem(collisionListener);
        engine.addSystem(new AnimationSystem(0));
        engine.addSystem(new StateSystem(1));
        engine.addSystem(new MovementSystem(2));
        engine.addSystem(new BackgroundRenderingSystem(batcher, 3));
        engine.addSystem(new ShieldSystem(4, player));
        engine.addSystem(new DynamicEntitiesRenderingSystem(batcher, shapeRenderer, 4));
        engine.addSystem(new StaticEntitiesRenderingSystem(batcher, 5));
        engine.addSystem(new ScoresRenderingSystem(batcher, assets, player, 6));
        engine.addSystem(new GameOverRenderingSystem(batcher, camera, assets, 7));
        engine.addSystem(new LevelFinishedRenderingSystem(batcher, assets, 7));
        if (DEBUG) {
            engine.addSystem(new FPSDisplayRenderingSystem(batcher, 8));
        }
        engine.addSystem(new CollisionSystem(collisionListener, 9));
        engine.addSystem(new EnemyAttackSystem(10, entityFactory));
        engine.addSystem(new BossAttackSystem(10, entityFactory));
        engine.addSystem(new SquadronSystem(11, entityFactory, player, playerListener));
        engine.addSystem(new ScoreSquadronSystem(12, assets, batcher));
        engine.addSystem(new RemovableSystem(13));
    }


    private InputListenerImpl createInputHandlerSystem(Entity player) {
        // input
        TouchInputProcessor inputProcessor = null;
        InputListenerImpl inputListener = new InputListenerImpl(player, entityFactory, assets, this, Settings.isVirtualPad());
        if (!Settings.isVirtualPad()) {
            Entity fireButton = entityFactory.createEntityFireButton(0.2f, FIRE_X, FIRE_Y);
            Entity padController = entityFactory.createEntitiesPadController(0.2f, 1.4f, PAD_X, PAD_Y);
            // define touch area as rectangles
            Sprite padSprite = Mappers.sprite.get(padController).sprite;
            float heightTouch = padSprite.getHeight() * 1.2f / 3f, widthTouch = padSprite.getWidth() * 1.2f / 3f;
            Rectangle[] squareTouchesDirection = new Rectangle[8];
            squareTouchesDirection[0] = new Rectangle(PAD_X, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
            squareTouchesDirection[1] = new Rectangle(PAD_X + widthTouch, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
            squareTouchesDirection[2] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
            squareTouchesDirection[3] = new Rectangle(PAD_X, PAD_Y + heightTouch, widthTouch, heightTouch);
            squareTouchesDirection[4] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y + heightTouch, widthTouch, heightTouch);
            squareTouchesDirection[5] = new Rectangle(PAD_X, PAD_Y, widthTouch, heightTouch);
            squareTouchesDirection[6] = new Rectangle(PAD_X + widthTouch, PAD_Y, widthTouch, heightTouch);
            squareTouchesDirection[7] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y, widthTouch, heightTouch);

            inputProcessor = new RetroPadController(inputListener, camera, squareTouchesDirection, Mappers.sprite.get(fireButton).getBounds());

        } else {
            inputProcessor = new VirtualPadController(inputListener, camera, player);

        }
        Gdx.input.setInputProcessor(inputProcessor);
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

    public void goToMenu() {
        game.showAd(this);
    }

    @Override
    public void dispose() {
        batcher.dispose();
    }
}
