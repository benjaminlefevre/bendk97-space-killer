package com.benk97.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import box2dLight.RayHandler;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.benk97.Settings;
import com.benk97.SpaceKillerGame;
import com.benk97.assets.Assets;
import com.benk97.components.*;
import com.benk97.entities.EntityFactory;
import com.benk97.entities.SquadronFactory;
import com.benk97.inputs.RetroPadController;
import com.benk97.inputs.TouchInputProcessor;
import com.benk97.inputs.VirtualPadController;
import com.benk97.listeners.PlayerListener;
import com.benk97.listeners.impl.CollisionListenerImpl;
import com.benk97.listeners.impl.InputListenerImpl;
import com.benk97.listeners.impl.PlayerListenerImpl;
import com.benk97.mask.SpriteMaskFactory;
import com.benk97.systems.*;
import com.benk97.tweens.PositionComponentAccessor;
import com.benk97.tweens.SpriteComponentAccessor;
import com.benk97.tweens.VelocityComponentAccessor;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.google.Achievement.*;

public class LevelScreen extends ScreenAdapter {

    protected Viewport viewport;
    protected OrthographicCamera camera;
    protected PooledEngine engine;
    protected EntityFactory entityFactory;
    protected SquadronFactory squadronFactory;
    protected TweenManager tweenManager;
    public Assets assets;
    private SpriteBatch batcher;
    protected SpaceKillerGame game;
    protected Entity player;
    protected SpriteMaskFactory spriteMaskFactory;

    private World world;
    private RayHandler rayHandler;

    public LevelScreen(Assets assets, SpaceKillerGame game) {
        this.game = game;
        this.spriteMaskFactory = new SpriteMaskFactory();
        this.camera = new OrthographicCamera();
        viewport = new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.batcher = new SpriteBatch();
        this.assets = assets;
        this.tweenManager = new TweenManager();
        engine = new PooledEngine();
        engine.addEntityListener(new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                if (DEBUG) {
                    Gdx.app.log("entity added", "size: " + engine.getEntities().size());
                }
            }

            @Override
            public void entityRemoved(Entity entity) {
                if (DEBUG) {
                    Gdx.app.log("entity removed", "size " + engine.getEntities().size());
                }
            }
        });
        if (FX) {
            world = new World(new Vector2(0, 0), false);
            rayHandler = new RayHandler(world);
            rayHandler.setShadows(false);
            rayHandler.setCombinedMatrix(camera);
        }
        entityFactory = new EntityFactory(engine, assets, tweenManager, rayHandler);
        squadronFactory = new SquadronFactory(tweenManager, entityFactory, engine);
        player = entityFactory.createEntityPlayer();
        Array<Entity> lives = entityFactory.createEntityPlayerLives(player);
        Array<Entity> bombs = entityFactory.createEntityPlayerBombs(player);
        createSystems(player, lives, bombs, batcher);
        registerTweensAccessor();
    }

    private void registerTweensAccessor() {
        Tween.registerAccessor(SpriteComponent.class, new SpriteComponentAccessor());
        Tween.registerAccessor(PositionComponent.class, new PositionComponentAccessor());
        Tween.registerAccessor(VelocityComponent.class, new VelocityComponentAccessor());
    }

    private void createSystems(Entity player, Array<Entity> lives, Array<Entity> bombs, SpriteBatch batcher) {
        PlayerListenerImpl playerListener = new PlayerListenerImpl(assets, entityFactory, lives, bombs, tweenManager, this);
        engine.addSystem(playerListener);
        engine.addSystem(createInputHandlerSystem(player, bombs, playerListener));
        CollisionListenerImpl collisionListener = new CollisionListenerImpl(tweenManager, assets, entityFactory, playerListener, (Level1Screen) this);
        engine.addSystem(collisionListener);
        engine.addSystem(new AnimationSystem(0));
        engine.addSystem(new BombExplosionSystem(0, collisionListener, player));
        engine.addSystem(new StateSystem(1));
        engine.addSystem(new MovementSystem(2));
        engine.addSystem(new BackgroundRenderingSystem(batcher, 3));
        engine.addSystem(new ShieldSystem(4, player));
        engine.addSystem(new DynamicEntitiesRenderingSystem(batcher, 4));
        engine.addSystem(new StaticEntitiesRenderingSystem(batcher, 6));
        engine.addSystem(new ScoresRenderingSystem(batcher, assets, player, 6));
        engine.addSystem(new GameOverRenderingSystem(batcher, camera, assets, 7));
        engine.addSystem(new LevelFinishedRenderingSystem(batcher, assets, 7));
        if (DEBUG) {
            engine.addSystem(new FPSDisplayRenderingSystem(batcher, 8));
        }
        engine.addSystem(new CollisionSystem(collisionListener, spriteMaskFactory, 9));
        engine.addSystem(new EnemyAttackSystem(10, entityFactory));
        engine.addSystem(new BossAttackSystem(10, entityFactory));
        engine.addSystem(new SquadronSystem(11, entityFactory, player, playerListener));
        engine.addSystem(new ScoreSquadronSystem(12, assets, batcher));
        engine.addSystem(new RemovableSystem(13));
    }


    private InputListenerImpl createInputHandlerSystem(Entity player, Array<Entity> bombs, PlayerListener playerListener) {
        // input
        TouchInputProcessor inputProcessor = null;
        InputListenerImpl inputListener = new InputListenerImpl(player, playerListener, entityFactory, assets, this, Settings.isVirtualPad());
        Entity bombButton = entityFactory.createEntityBombButton(0.2f, BOMB_X, BOMB_Y);
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

            inputProcessor = new RetroPadController(inputListener, camera, squareTouchesDirection, Mappers.sprite.get(fireButton).getBounds(),
                    Mappers.sprite.get(bombButton).getBounds());

        } else {
            Mappers.sprite.get(bombButton).sprite.setY(BOMB_Y_VIRTUAL);
            inputProcessor = new VirtualPadController(inputListener, camera, player, Mappers.sprite.get(bombButton).getBounds());

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
        if (FX) {
            rayHandler.updateAndRender();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void goToMenu() {
        game.showAd();
    }

    @Override
    public void dispose() {
        batcher.dispose();
        entityFactory.dispose();
        spriteMaskFactory.clear();
        if (FX) {
            rayHandler.dispose();
            world.dispose();
        }
    }

    public void submitScore(int scoreInt) {
        game.playServices.submitScore(scoreInt);
    }

    public void checkAchievements(Entity player) {
        PlayerComponent playerComponent = Mappers.player.get(player);
        if (playerComponent.enemiesKilled == 50) {
            game.playServices.unlockAchievement(KILL_50_ENEMIES);
        } else if (playerComponent.enemiesKilled == 100) {
            game.playServices.unlockAchievement(KILL_100_ENEMIES);
        } else if (playerComponent.enemiesKilled == 500) {
            game.playServices.unlockAchievement(KILL_500_ENEMIES);
        }
    }
}
