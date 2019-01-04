/*
 * Developed by Benjamin Lefèvre
 * Last modified 08/10/18 08:06
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bendk97.Settings;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.GameAssets;
import com.bendk97.components.*;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.components.texts.TextComponent;
import com.bendk97.entities.EntityFactory;
import com.bendk97.inputs.GameOverTouchInputProcessor;
import com.bendk97.inputs.GestureHandler;
import com.bendk97.inputs.PauseInputProcessor;
import com.bendk97.inputs.pad.RetroPadController;
import com.bendk97.inputs.pad.VirtualPadController;
import com.bendk97.listeners.PlayerListener;
import com.bendk97.listeners.impl.CollisionListenerImpl;
import com.bendk97.listeners.impl.InputListenerImpl;
import com.bendk97.listeners.impl.PlayerListenerImpl;
import com.bendk97.player.PlayerData;
import com.bendk97.pools.GamePools;
import com.bendk97.screens.levels.scripting.LevelScript;
import com.bendk97.screens.levels.utils.ScreenShake;
import com.bendk97.screens.menu.MenuScreen;
import com.bendk97.systems.*;
import com.bendk97.systems.collision.CollisionSystem;
import com.bendk97.systems.screen.GameOverRenderingSystem;
import com.bendk97.systems.screen.PauseRenderingSystem;
import com.bendk97.timer.PausableTimer;
import com.bendk97.tweens.*;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.MotionBlur;
import com.bitfire.utils.ShaderLoader;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.components.helpers.ComponentMapperHelper.*;
import static com.bendk97.google.Achievement.*;
import static com.bendk97.pools.GamePools.poolSprite;
import static com.bendk97.screens.levels.Level.getLevelScript;
import static com.bendk97.screens.levels.Level.nextLevelAfter;
import static com.bendk97.tweens.SpriteComponentTweenAccessor.ALPHA;

public abstract class LevelScreen extends ScreenAdapter {

    private float time;
    private InputMultiplexer inputProcessor = null;
    private Music music = null;
    private final LevelScript levelScript;
    private final Viewport viewport;
    public SpriteBatch batcher;
    private final Viewport viewportHUD;
    protected final OrthographicCamera camera;
    private final OrthographicCamera cameraHUD;
    private SpriteBatch batcherHUD;
    protected final PooledEngine engine;
    private final EntityFactory entityFactory;
    private final TweenManager tweenManager;
    private final GameAssets assets;
    private final SpaceKillerGame game;
    private final Entity player;
    private World world;
    private RayHandler rayHandler;
    protected final boolean fxLightEnabled;
    private PostProcessor postProcessor;

    private PlayerListenerImpl playerListener;
    private State state = State.RUNNING;


    public enum State {
        PAUSED, RUNNING, SCRIPT_PAUSED

    }

    public LevelScreen(final GameAssets assets, final SpaceKillerGame game) {
        this(assets, game, null);
    }

    protected LevelScreen(final GameAssets assets, final SpaceKillerGame game, SpriteBatch defaultBatcher) {
        if (defaultBatcher == null) {
            this.initBatchers();
        } else {
            this.batcherHUD = defaultBatcher;
            this.batcher = defaultBatcher;
        }
        PausableTimer.instance().stop();
        PausableTimer.instance().start();
        this.game = game;
        this.fxLightEnabled = Settings.isLightFXEnabled();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.cameraHUD = new OrthographicCamera();
        viewportHUD = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, cameraHUD);
        cameraHUD.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.assets = assets;
        this.tweenManager = new TweenManager();
        ScreenShake screenShake = new ScreenShake(tweenManager, camera);
        engine = new PooledEngine(POOL_INIT, POOL_MAX, POOL_INIT, POOL_MAX);
        engineListeners();
        if (fxLightEnabled) {
            initRayLightEffects(camera);
        }
        entityFactory = new EntityFactory(game, engine, assets, tweenManager, rayHandler, screenShake, level());
        player = entityFactory.playerEntityFactory.createEntityPlayer(level());
        SnapshotArray<Entity> lives = entityFactory.playerEntityFactory.createEntityPlayerLives(player);
        SnapshotArray<Entity> bombs = entityFactory.playerEntityFactory.createEntityPlayerBombs(player);
        createSystems(player, lives, bombs, batcher, screenShake);
        registerTweensAccessor();
        registerPostProcessingEffects();
        this.levelScript = getLevelScript(level(), this, assets, entityFactory, tweenManager, player, engine);
        time = -5;
    }

    protected abstract Level level();

    private void engineListeners() {
        engine.addEntityListener(new EntityListener() {
            static final String ENTITIES_ADDED = "entity added";
            static final String ENTITY_REMOVED = "entity removed";
            static final String ENTITIES = "entities: ";
            StringBuilder sb = new StringBuilder();

            @Override
            public void entityAdded(Entity entity) {
                if (DEBUG) {
                    sb.setLength(0);
                    Gdx.app.log(ENTITIES_ADDED,
                            sb.append(ENTITIES).append(engine.getEntities().size()).toString());
                }
            }

            @Override
            public void entityRemoved(Entity entity) {
                if (DEBUG) {
                    sb.setLength(0);
                    Gdx.app.log(ENTITY_REMOVED,
                            sb.append(ENTITIES).append(engine.getEntities().size()).toString());
                }
                AnimationComponent animationComponent = ComponentMapperHelper.animation.get(entity);
                if (animationComponent != null) {
                    for (Animation<Sprite> animation : animationComponent.animations.values()) {
                        for (int i = 0; i < animation.getKeyFrames().length; ++i) {
                            poolSprite.free(animation.getKeyFrames()[i]);
                        }
                    }
                } else {
                    SpriteComponent spriteComponent = sprite.get(entity);
                    if (spriteComponent != null) {
                        poolSprite.free(spriteComponent.sprite);
                    }
                }
            }
        });
    }

    private void initBatchers() {
        this.batcher = new SpriteBatch();
        this.batcherHUD = new SpriteBatch();
    }

    private void initRayLightEffects(OrthographicCamera camera) {
        world = new World(new Vector2(0, 0), false);
        rayHandler = new RayHandler(world, Gdx.graphics.getWidth() / 16, Gdx.graphics
                .getHeight() / 16);
        rayHandler.setShadows(false);
        rayHandler.setCombinedMatrix(camera);
    }

    public void currentMusic(Music music) {
        this.music = music;
    }

    public void nextLevel() {
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        playerComponent.level = nextLevelAfter(playerComponent.level);
        PlayerData playerData = playerComponent.copyPlayerData();
        Sprite screenshot = this.takeScreenshot(Gdx.graphics.getDeltaTime(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        switch (level()) {
            case Level2:
                game.playServices.unlockAchievement(KILL_BOSS_2);
                game.goToScreen(Level3Screen.class, playerData, screenshot);
                break;
            case Level3:
                game.playServices.unlockAchievement(KILL_BOSS_3);
                game.goToScreen(Level1Screen.class, playerData, screenshot);
                break;
            case Level1:
            default:
                game.playServices.unlockAchievement(KILL_BOSS);
                game.goToScreen(Level2Screen.class, playerData, screenshot);
                break;
        }
    }

    public InputProcessor getGameOverInputProcessor() {
        return new GameOverTouchInputProcessor(cameraHUD, game, assets, player);
    }

    private InputProcessor getPauseInputProcessor() {
        return new PauseInputProcessor(cameraHUD, this);
    }

    public void continueWithExtraLife() {
        final PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        player.remove(GameOverComponent.class);
        this.time = playerComponent.secondScript;
        this.resumeScripting();
        playerListener.updateLivesAndBombsAfterContinue(player);
        ComponentMapperHelper.position.get(player).setXY(PLAYER_ORIGIN_X, PLAYER_ORIGIN_Y);
        sprite.get(player).sprite.setPosition(PLAYER_ORIGIN_X, PLAYER_ORIGIN_Y);
        Gdx.input.setInputProcessor(inputProcessor);
        makeEntityInvulnerable(player);
    }

    public void makeEntityInvulnerable(Entity entity) {
        entityFactory.playerEntityFactory.addInvulnerableComponent(entity);
        Timeline.createSequence()
                .push(Tween.to(sprite.get(entity), ALPHA, 0.2f).target(0.2f))
                .push(Tween.to(sprite.get(entity), ALPHA, 0.2f).target(1f))
                .repeat(10, 0f)
                .setCallback((i, baseTween) -> {
                    if (i == TweenCallback.COMPLETE) {
                        entityFactory.playerEntityFactory.removeInvulnerableComponent(entity);
                    }
                })
                .start(tweenManager);
    }

    public float getCurrentTimeScript() {
        return time;
    }


    private void registerPostProcessingEffects() {
        if (Gdx.app.getType() == Application.ApplicationType.HeadlessDesktop) {
            return;
        }
        ShaderLoader.BasePath = "shaders/files/";
        postProcessor = new PostProcessor(false, false, Gdx.app.getType() == Application.ApplicationType.Desktop);
        MotionBlur motionBlur = new MotionBlur();
        motionBlur.setBlurOpacity(0.5f);
        postProcessor.addEffect(motionBlur);
    }

    private void registerTweensAccessor() {
        Tween.registerAccessor(Sprite.class, new SpriteTweenAccessor());
        Tween.registerAccessor(SpriteComponent.class, new SpriteComponentTweenAccessor());
        Tween.registerAccessor(TextComponent.class, new TextComponentTweenAccessor());
        Tween.registerAccessor(PositionComponent.class, new PositionComponentTweenAccessor());
        Tween.registerAccessor(VelocityComponent.class, new VelocityComponentTweenAccessor());
        Tween.registerAccessor(OrthographicCamera.class, new CameraTweenAccessor());
        Tween.registerAccessor(BitmapFontCache.class, new BitmapFontCacheTweenAccessor());
        if (fxLightEnabled) {
            Tween.registerAccessor(ConeLight.class, new ConeLightTweenAccessor());
        }
    }

    protected void createSystems(Entity player, SnapshotArray<Entity> lives, SnapshotArray<Entity> bombs, SpriteBatch batcher,
                                 ScreenShake screenShake) {
        playerListener = new PlayerListenerImpl(assets, entityFactory, lives, bombs, screenShake, this);
        engine.addSystem(playerListener);
        engine.addSystem(createInputHandlerSystem(player, playerListener));
        CollisionListenerImpl collisionListener = new CollisionListenerImpl(tweenManager, screenShake, assets, entityFactory, playerListener, this);
        engine.addSystem(collisionListener);
        engine.addSystem(new AnimationSystem(0));
        engine.addSystem(new BombExplosionSystem(0, collisionListener, player, tweenManager));
        engine.addSystem(new StateSystem(1));
        engine.addSystem(new MovementPlayerSystem(2, camera));
        engine.addSystem(new MovementSystem(2));
        engine.addSystem(new ShieldSystem(3, player));
        // RENDERING
        engine.addSystem(new BatcherBeginSystem(viewport, batcher, 4));
        engine.addSystem(new BackgroundRenderingSystem(batcher, 5));
        engine.addSystem(new DirectionableSpriteSystem(5));
        engine.addSystem(new DynamicEntitiesRenderingSystem(batcher, 6));
        engine.addSystem(new ScoreSquadronSystem(6, assets, batcher));
        engine.addSystem(new BatcherEndSystem(batcher, 7));
        engine.addSystem(new BatcherHUDBeginSystem(viewportHUD, batcherHUD, 8));
        engine.addSystem(new StaticEntitiesRenderingSystem(batcherHUD, 9));
        engine.addSystem(new TextHUDRenderingSystem(batcherHUD, assets, 11));
        engine.addSystem(new StatusHealthRenderingSystem(batcherHUD, assets, 10));
        engine.addSystem(new GameOverRenderingSystem(batcherHUD, cameraHUD, assets, 10));
        engine.addSystem(new PauseRenderingSystem(batcherHUD, cameraHUD, assets, 10));
        engine.addSystem(new LevelFinishedRenderingSystem(batcherHUD, assets, level(), 10));
        if (DEBUG) {
            engine.addSystem(new DebugStatsSystem(this, batcherHUD, 11));
        }
        engine.addSystem(new BatcherHUDEndSystem(batcherHUD, 12));
        // END RENDERING
        engine.addSystem(new CollisionSystem(collisionListener, viewport, batcher, 13));
        engine.addSystem(new TankAttackSystem(13));
        engine.addSystem(new EnemyAttackSystem(14, entityFactory));
        engine.addSystem(new BossAttackSystem(14, entityFactory));
        engine.addSystem(new SquadronSystem(level(), 15, entityFactory, player, playerListener));
        engine.addSystem(new RemovableSystem(16, tweenManager));
    }


    private InputListenerImpl createInputHandlerSystem(Entity player, PlayerListener playerListener) {
        // input
        inputProcessor = new InputMultiplexer();
        inputProcessor.addProcessor(new GestureDetector(new GestureHandler(this, cameraHUD)));

        InputListenerImpl inputListener = new InputListenerImpl(player, playerListener, entityFactory, assets, Settings.isVirtualPad());
        Entity bombButton = entityFactory.playerEntityFactory.createEntityBombButton(0.2f, BOMB_X, BOMB_Y);
        if (!Settings.isVirtualPad()) {
            Entity fireButton = entityFactory.playerEntityFactory.createEntityFireButton(0.2f, FIRE_X, FIRE_Y);
            Entity padController = entityFactory.playerEntityFactory.createEntitiesPadController(0.2f, 1.4f, PAD_X, PAD_Y);
            // define touch area as rectangles
            Sprite padSprite = sprite.get(padController).sprite;
            float heightTouch = padSprite.getHeight() * 1.2f / 3f;
            float widthTouch = padSprite.getWidth() * 1.2f / 3f;
            Rectangle[] squareTouchesDirection = new Rectangle[8];
            squareTouchesDirection[0] = new Rectangle(PAD_X, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
            squareTouchesDirection[1] = new Rectangle(PAD_X + widthTouch, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
            squareTouchesDirection[2] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y + 2 * heightTouch, widthTouch, heightTouch);
            squareTouchesDirection[3] = new Rectangle(PAD_X, PAD_Y + heightTouch, widthTouch, heightTouch);
            squareTouchesDirection[4] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y + heightTouch, widthTouch, heightTouch);
            squareTouchesDirection[5] = new Rectangle(PAD_X, PAD_Y, widthTouch, heightTouch);
            squareTouchesDirection[6] = new Rectangle(PAD_X + widthTouch, PAD_Y, widthTouch, heightTouch);
            squareTouchesDirection[7] = new Rectangle(PAD_X + 2 * widthTouch, PAD_Y, widthTouch, heightTouch);

            inputProcessor.addProcessor(new RetroPadController(this, inputListener, cameraHUD, squareTouchesDirection,
                    sprite.get(fireButton).getBounds(),
                    sprite.get(bombButton).getBounds()));

        } else {
            sprite.get(bombButton).sprite.setY(BOMB_Y_VIRTUAL);
            inputProcessor.addProcessor(new VirtualPadController(this, inputListener, cameraHUD, player,
                    sprite.get(bombButton).getBounds()));

        }
        Gdx.input.setInputProcessor(inputProcessor);
        return inputListener;
    }

    @Override
    public void render(float delta) {
        float deltaState = state.equals(State.PAUSED) ? 0f : delta;
        updateScriptLevel(deltaState);
        tweenManager.update(deltaState);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        postProcessor.capture();
        engine.update(deltaState);
        postProcessor.render();
        if (fxLightEnabled) {
            rayHandler.updateAndRender();
        }
    }


    public Sprite takeScreenshot(float delta, int width, int height) {
        FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        buffer.begin();
        this.resize(width, height);
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.update(delta);
        buffer.end();
        Sprite sprite = poolSprite.getSprite(buffer.getColorBufferTexture());
        sprite.flip(false, true);
        return sprite;
    }

    private void updateScriptLevel(float delta) {
        if (State.SCRIPT_PAUSED.equals(state)) {
            return;
        }
        int timeBefore = (int) Math.floor(time);
        time += delta;
        int newTime = (int) Math.floor(time);
        if (newTime > timeBefore) {
            levelScript.script(newTime);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        viewportHUD.update(width, height, true);
    }

    @Override
    public void pause() {
        if (gameOver.get(player) == null
                && pause.get(player) == null) {
            state = State.PAUSED;
            Gdx.input.setInputProcessor(this.getPauseInputProcessor());
            if (music != null) {
                music.pause();
            }
            PausableTimer.pause();
            player.add(engine.createComponent(PauseComponent.class));
        }
    }

    public void resumeGame() {
        state = State.RUNNING;
        Gdx.input.setInputProcessor(inputProcessor);
        if (music != null) {
            music.play();
        }
        PausableTimer.resume();
        player.remove(PauseComponent.class);
        postProcessor.rebind();
    }

    public void pauseScripting() {
        state = State.SCRIPT_PAUSED;
    }

    public void resumeScripting() {
        state = State.RUNNING;
    }

    public void quitGame() {
        game.goToScreen(MenuScreen.class);
    }

    @Override
    public void dispose() {
        PausableTimer.instance().stop();
        batcher.dispose();
        batcherHUD.dispose();
        entityFactory.dispose();
        if (fxLightEnabled) {
            rayHandler.dispose();
            world.dispose();
        }
        engine.removeAllEntities();
        engine.clearPools();
        removeSystemsEngine();
        GamePools.clearPools();
        postProcessor.dispose();
        tweenManager.killAll();
        tweenManager.update(0);
        Texture.clearAllTextures(Gdx.app);

    }

    private void removeSystemsEngine() {
        engine.removeSystem(engine.getSystem(PlayerListenerImpl.class));
        engine.removeSystem(engine.getSystem(InputListenerImpl.class));
        engine.removeSystem(engine.getSystem(CollisionListenerImpl.class));
        engine.removeSystem(engine.getSystem(AnimationSystem.class));
        engine.removeSystem(engine.getSystem(BombExplosionSystem.class));
        engine.removeSystem(engine.getSystem(StateSystem.class));
        engine.removeSystem(engine.getSystem(MovementPlayerSystem.class));
        engine.removeSystem(engine.getSystem(MovementSystem.class));
        engine.removeSystem(engine.getSystem(ShieldSystem.class));
        // RENDERING
        engine.removeSystem(engine.getSystem(BatcherBeginSystem.class));
        engine.removeSystem(engine.getSystem(BackgroundRenderingSystem.class));
        engine.removeSystem(engine.getSystem(DirectionableSpriteSystem.class));
        engine.removeSystem(engine.getSystem(DynamicEntitiesRenderingSystem.class));
        engine.removeSystem(engine.getSystem(ScoreSquadronSystem.class));
        engine.removeSystem(engine.getSystem(BatcherEndSystem.class));
        engine.removeSystem(engine.getSystem(BatcherHUDBeginSystem.class));
        engine.removeSystem(engine.getSystem(StaticEntitiesRenderingSystem.class));
        engine.removeSystem(engine.getSystem(TextHUDRenderingSystem.class));
        engine.removeSystem(engine.getSystem(StatusHealthRenderingSystem.class));
        engine.removeSystem(engine.getSystem(GameOverRenderingSystem.class));
        engine.removeSystem(engine.getSystem(PauseRenderingSystem.class));
        engine.removeSystem(engine.getSystem(LevelFinishedRenderingSystem.class));
        if (DEBUG) {
            engine.removeSystem(engine.getSystem(DebugStatsSystem.class));
        }
        engine.removeSystem(engine.getSystem(BatcherHUDEndSystem.class));
        // END RENDERING
        engine.removeSystem(engine.getSystem(CollisionSystem.class));
        engine.removeSystem(engine.getSystem(TankAttackSystem.class));
        engine.removeSystem(engine.getSystem(EnemyAttackSystem.class));
        engine.removeSystem(engine.getSystem(BossAttackSystem.class));
        engine.removeSystem(engine.getSystem(SquadronSystem.class));
        engine.removeSystem(engine.getSystem(RemovableSystem.class));
    }

    public void submitScore(int scoreInt) {
        game.playServices.submitScore(scoreInt);
    }

    public void checkAchievements(Entity player) {
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        if (playerComponent.enemiesKilled == 50) {
            game.playServices.unlockAchievement(KILL_50_ENEMIES);
        } else if (playerComponent.enemiesKilled == 100) {
            game.playServices.unlockAchievement(KILL_100_ENEMIES);
        } else if (playerComponent.enemiesKilled == 500) {
            game.playServices.unlockAchievement(KILL_500_ENEMIES);
        }
        if (playerComponent.laserShipKilled == 1) {
            game.playServices.unlockAchievement(KILL_LASER_SHIP);
        } else if (playerComponent.laserShipKilled == 5) {
            game.playServices.unlockAchievement(KILL_5_LASER_SHIPS);
        }
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public boolean isFxLightEnabled() {
        return fxLightEnabled;
    }
}
