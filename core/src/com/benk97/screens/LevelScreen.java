package com.benk97.screens;

import aurelienribon.tweenengine.*;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.RandomXS128;
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
import com.benk97.google.Achievement;
import com.benk97.inputs.*;
import com.benk97.listeners.PlayerListener;
import com.benk97.listeners.impl.CollisionListenerImpl;
import com.benk97.listeners.impl.InputListenerImpl;
import com.benk97.listeners.impl.PlayerListenerImpl;
import com.benk97.mask.SpriteMaskFactory;
import com.benk97.player.PlayerData;
import com.benk97.systems.*;
import com.benk97.timer.PausableTimer;
import com.benk97.tweens.CameraTween;
import com.benk97.tweens.PositionComponentAccessor;
import com.benk97.tweens.SpriteComponentAccessor;
import com.benk97.tweens.VelocityComponentAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.*;
import static com.benk97.entities.SquadronFactory.*;
import static com.benk97.google.Achievement.*;
import static com.benk97.screens.LevelScreen.Level.*;
import static com.benk97.tweens.SpriteComponentAccessor.ALPHA;

public abstract class LevelScreen extends ScreenAdapter {

    protected float time = -1000f;
    protected Random random = new RandomXS128();
    InputMultiplexer inputProcessor = null;
    protected Music music = null;


    public void nextLevel() {
        PlayerComponent playerComponent = Mappers.player.get(player);
        FrameBuffer screenshot = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        screenshot.begin();
        this.render(Gdx.graphics.getDeltaTime());
        screenshot.end();
        playerComponent.level = playerComponent.level.equals(Level1) ? Level2 : (playerComponent.level.equals(Level2) ? Level3 : Level1);
        PlayerData playerData = playerComponent.copyPlayerData();
        this.dispose();
        switch (level) {
            case Level1:
                game.playServices.unlockAchievement(Achievement.KILL_BOSS);
                game.goToScreen(Level2Screen.class, playerData, screenshot);
                break;
            case Level2:
                game.playServices.unlockAchievement(Achievement.KILL_BOSS_2);
                game.goToScreen(Level3Screen.class, playerData, screenshot);
                break;
            case Level3:
                game.playServices.unlockAchievement(Achievement.KILL_BOSS_3);
                game.goToScreen(Level1Screen.class, playerData, screenshot);
        }
    }

    public InputProcessor getGameOverInputProcessor() {
        return new GameOverTouchInputProcessor(cameraHUD, game, assets, player);
    }

    public InputProcessor getPauseInputProcessor() {
        return new PauseInputProcessor(cameraHUD, this);
    }

    public void continueWithExtraLife() {
        final PlayerComponent playerComponent = Mappers.player.get(player);
        player.remove(GameOverComponent.class);
        ((LevelScreen) game.currentScreen).startLevel(playerComponent.secondScript);
        playerComponent.lives = LIVES;
        playerComponent.bombs = BOMBS;
        playerComponent.resetScore();
        playerListener.updateLivesAndBombsAfterContinue(player);
        playerComponent.rewardAds--;
        Mappers.position.get(player).setPosition(PLAYER_ORIGIN_X, PLAYER_ORIGIN_Y);
        Mappers.sprite.get(player).sprite.setPosition(PLAYER_ORIGIN_X, PLAYER_ORIGIN_Y);
        entityFactory.addInvulnerableComponent(player);
        Gdx.input.setInputProcessor(inputProcessor);
        Timeline.createSequence()
                .push(Tween.to(Mappers.sprite.get(player), ALPHA, 0.2f).target(0f))
                .push(Tween.to(Mappers.sprite.get(player), ALPHA, 0.2f).target(1f))
                .repeat(10, 0f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        if (i == TweenCallback.COMPLETE) {
                            entityFactory.removeInvulnerableComponent(player);
                        }
                    }
                })
                .start(tweenManager);
    }

    public float getCurrentTimeScript() {
        return time;
    }

    public enum Level {
        Level1(GFX_LEVEL1_ATLAS_MASK), Level2(GFX_LEVEL2_ATLAS_MASK), Level3(GFX_LEVEL3_ATLAS_MASK);
        AssetDescriptor<TextureAtlas> sprites;

        Level(AssetDescriptor<TextureAtlas> sprites) {
            this.sprites = sprites;
        }

        public AssetDescriptor<TextureAtlas> getSprites() {
            return sprites;
        }
    }

    protected Viewport viewport;
    protected OrthographicCamera camera;
    private SpriteBatch batcher;
    protected Viewport viewportHUD;
    protected OrthographicCamera cameraHUD;
    private SpriteBatch batcherHUD;
    protected PooledEngine engine;
    protected EntityFactory entityFactory;
    protected SquadronFactory squadronFactory;
    protected TweenManager tweenManager;
    public Assets assets;
    protected SpaceKillerGame game;
    protected Entity player;
    protected SpriteMaskFactory spriteMaskFactory;
    protected ScreenShake screenShake;

    private World world;
    private RayHandler rayHandler;
    private boolean fxLightEnabled = false;

    private Level level;

    public LevelScreen(Assets assets, SpaceKillerGame game, Level level) {
        PausableTimer.instance().stop();
        PausableTimer.instance().start();
        this.level = level;
        this.game = game;
        this.fxLightEnabled = Settings.isLightFXEnabled();
        this.spriteMaskFactory = new SpriteMaskFactory();
        this.camera = new OrthographicCamera();
        viewport = new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.batcher = new SpriteBatch();
        this.cameraHUD = new OrthographicCamera();
        viewportHUD = new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT, cameraHUD);
        cameraHUD.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.batcherHUD = new SpriteBatch();
        this.assets = assets;
        this.tweenManager = new TweenManager();
        screenShake = new ScreenShake(tweenManager, camera);
        engine = new PooledEngine(50, 150, 50, 150);
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
        if (fxLightEnabled) {
            world = new World(new Vector2(0, 0), false);
            rayHandler = new RayHandler(world,Gdx.graphics.getWidth() / 16, Gdx.graphics
                    .getHeight() / 16);
            rayHandler.setShadows(false);
            rayHandler.setCombinedMatrix(camera);
        }
        entityFactory = new EntityFactory(game, engine, assets, tweenManager, rayHandler, screenShake, level);
        squadronFactory = new SquadronFactory(tweenManager, entityFactory, camera, engine);
        player = entityFactory.createEntityPlayer(level);
        Array<Entity> lives = entityFactory.createEntityPlayerLives(player);
        Array<Entity> bombs = entityFactory.createEntityPlayerBombs(player);
        createSystems(player, lives, bombs, batcher, screenShake);
        registerTweensAccessor();
    }

    private void registerTweensAccessor() {
        Tween.registerAccessor(SpriteComponent.class, new SpriteComponentAccessor());
        Tween.registerAccessor(PositionComponent.class, new PositionComponentAccessor());
        Tween.registerAccessor(VelocityComponent.class, new VelocityComponentAccessor());
        Tween.registerAccessor(OrthographicCamera.class, new CameraTween());
    }

    PlayerListenerImpl playerListener;

    protected void createSystems(Entity player, Array<Entity> lives, Array<Entity> bombs, SpriteBatch batcher, ScreenShake screenShake) {
        playerListener = new PlayerListenerImpl(game, assets, entityFactory, lives, bombs, tweenManager, screenShake, this);
        engine.addSystem(playerListener);
        engine.addSystem(createInputHandlerSystem(player, playerListener));
        CollisionListenerImpl collisionListener = new CollisionListenerImpl(tweenManager, screenShake, assets, entityFactory, playerListener, this);
        engine.addSystem(collisionListener);
        engine.addSystem(new AnimationSystem(0));
        engine.addSystem(new BombExplosionSystem(0, collisionListener, player));
        engine.addSystem(new StateSystem(1));
        engine.addSystem(new MovementSystem(camera, 2));
        engine.addSystem(new ShieldSystem(3, player));
        // RENDERING
        engine.addSystem(new BatcherBeginSystem(viewport, batcher, 4));
        engine.addSystem(new BackgroundRenderingSystem(batcher, 5));
        engine.addSystem(new DynamicEntitiesRenderingSystem(batcher, 6));
        engine.addSystem(new ScoreSquadronSystem(6, assets, batcher));
        engine.addSystem(new BatcherEndSystem(viewport, batcher, 7));
        engine.addSystem(new BatcherHUDBeginSystem(viewportHUD, batcherHUD, 8));
        engine.addSystem(new StaticEntitiesRenderingSystem(batcherHUD, 9));
        engine.addSystem(new ScoresRenderingSystem(batcherHUD, assets, 11));
        engine.addSystem(new GameOverRenderingSystem(batcherHUD, cameraHUD, assets, 10));
        engine.addSystem(new PauseRenderingSystem(batcherHUD, cameraHUD, assets, 10));
        engine.addSystem(new LevelFinishedRenderingSystem(batcherHUD, assets, level, 10));
        if (DEBUG) {
            engine.addSystem(new FPSDisplayRenderingSystem(this, batcherHUD, 11));
        }
        engine.addSystem(new BatcherHUDEndSystem(viewportHUD, batcherHUD, 12));
        // END RENDERING
        engine.addSystem(new CollisionSystem(collisionListener, spriteMaskFactory, 13));
        engine.addSystem(new TankAttackSystem(13));
        engine.addSystem(new EnemyAttackSystem(14, entityFactory));
        engine.addSystem(new BossAttackSystem(14, entityFactory));
        engine.addSystem(new SquadronSystem(level, 15, entityFactory, player, playerListener));
        engine.addSystem(new RemovableSystem(16));
    }


    private InputListenerImpl createInputHandlerSystem(Entity player, PlayerListener playerListener) {
        // input
        inputProcessor = new InputMultiplexer();
        inputProcessor.addProcessor(new GestureDetector(new GestureHandler(this, cameraHUD)));

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

            inputProcessor.addProcessor(new RetroPadController(this, inputListener, cameraHUD, squareTouchesDirection, Mappers.sprite.get(fireButton).getBounds(),
                    Mappers.sprite.get(bombButton).getBounds()));

        } else {
            Mappers.sprite.get(bombButton).sprite.setY(BOMB_Y_VIRTUAL);
            inputProcessor.addProcessor(new VirtualPadController(this, inputListener, cameraHUD, player, Mappers.sprite.get(bombButton).getBounds()));

        }
        Gdx.input.setInputProcessor(inputProcessor);
        return inputListener;
    }

    @Override
    public void render(float delta) {
        float deltaState = state.equals(State.RUNNING) ? delta : 0f;
        updateScriptLevel(deltaState);
        tweenManager.update(deltaState);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.update(deltaState);
        if (fxLightEnabled) {
            rayHandler.updateAndRender();
        }
    }

    private void updateScriptLevel(float delta) {
        int timeBefore = (int) Math.floor(time);
        time += delta;
        int newTime = (int) Math.floor(time);
        if (newTime > timeBefore) {
            script(newTime);
        }
    }

    protected abstract void script(int second);


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        viewportHUD.update(width, height, true);
    }

    public enum State {
        PAUSED, RUNNING
    }

    protected State state = State.RUNNING;

    protected void playMusic(AssetDescriptor<Music> musicDesc, float volume) {
        music = assets.playMusic(musicDesc, volume);
    }

    @Override
    public void pause() {
        if (player.getComponent(GameOverComponent.class) == null
                && player.getComponent(PauseComponent.class) == null) {
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
    }

    public void quitGame() {
        this.dispose();
        game.goToScreen(MenuScreen.class);
    }

    @Override
    public void dispose() {
        PausableTimer.instance().stop();
        batcher.dispose();
        batcherHUD.dispose();
        entityFactory.dispose();
        spriteMaskFactory.clear();
        if (fxLightEnabled) {
            rayHandler.dispose();
            world.dispose();
        }
        assets.unloadResources(this.getClass());
        engine.clearPools();
        engine.removeAllEntities();
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
        if (playerComponent.laserShipKilled == 1) {
            game.playServices.unlockAchievement(KILL_LASER_SHIP);
        } else if (playerComponent.laserShipKilled == 5) {
            game.playServices.unlockAchievement(KILL_5_LASER_SHIPS);
        }
    }

    protected Texture getRandomMist() {
        int randomMist = random.nextInt(7);
        return getMist(randomMist);
    }

    protected Texture getMist(int mistType) {
        switch (mistType) {
            case 0:
                return assets.get(GFX_BGD_MIST7);
            case 1:
                return assets.get(GFX_BGD_MIST1);
            case 2:
                return assets.get(GFX_BGD_MIST2);
            case 3:
                return assets.get(GFX_BGD_MIST3);
            case 4:
                return assets.get(GFX_BGD_MIST4);
            case 5:
                return assets.get(GFX_BGD_MIST5);
            case 6:
                return assets.get(GFX_BGD_MIST6);
        }
        return null;
    }

    public int getRandomAsteroidType() {
        return 999 + random.nextInt(2);
    }

    public int getRandomHouseType() {
        return 1500 + random.nextInt(9);
    }

    class ScriptItem {
        int typeShip;
        int typeSquadron;
        float velocity;
        int number;
        Object[] params;
        boolean powerUp;
        boolean displayBonus;
        int bonus;
        int rateShoot;
        float bulletVelocity;

        public ScriptItem(int typeShip, int typeSquadron, float velocity, int number, boolean powerUp, boolean displayBonus, int bonus, int rateShoot, float velocityBullet, Object... params) {
            this.typeShip = typeShip;
            this.rateShoot = rateShoot;
            this.typeSquadron = typeSquadron;
            this.velocity = velocity;
            this.number = number;
            this.params = params;
            this.powerUp = powerUp;
            this.bonus = bonus;
            this.displayBonus = displayBonus;
            this.bulletVelocity = velocityBullet;
        }

        public ScriptItem(int typeShip, int typeSquadron, float velocity, int number, boolean powerUp, boolean displayBonus, int bonus, float velocityBullet, Object... params) {
            this(typeShip, typeSquadron, velocity, number, powerUp, displayBonus, bonus, STANDARD_RATE_SHOOT, velocityBullet, params);
        }

        public void execute() {
            squadronFactory.createSquadron(typeShip, typeSquadron, velocity, number, powerUp, displayBonus, bonus, bulletVelocity, rateShoot, params);
            if (Mappers.levelFinished.get(player) == null) {
                Mappers.player.get(player).enemiesCountLevel += number;
            }
        }

    }

    protected List<ScriptItem> randomSpawnEnemies(int nbSpawns, float velocity, int rateShoot, float bulletVelocity, int bonus, int minEnemies, int maxEnemies, Boolean comingFromLeft) {
        List<ScriptItem> list = new ArrayList<ScriptItem>(nbSpawns);
        for (int i = 0; i < nbSpawns; ++i) {
            int randomMoveType = getRandomMoveType();
            int number = randomMoveType == ARROW_DOWN || randomMoveType == ARROW_UP ? 7 : minEnemies + random.nextInt(maxEnemies - minEnemies + 1);
            list.add(
                    new ScriptItem(
                            getRandomShipType(),
                            randomMoveType,
                            velocity,
                            number,
                            false, true,
                            number * bonus,
                            rateShoot,
                            bulletVelocity,
                            getRandomMoveParams(randomMoveType, comingFromLeft == null ? random.nextBoolean() : comingFromLeft)
                    ));
        }
        return list;
    }

    private Object[] getRandomMoveParams(int randomMoveType, boolean comingFromLeft) {
        float direction = comingFromLeft ? 1f : -1f;
        int leftOrRight = comingFromLeft ? 0 : 1;
        switch (randomMoveType) {
            case INFINITE_CIRCLE:
                return null;
            case ARROW_DOWN:
            case ARROW_UP:
                return null;
            case LINEAR_X:
                return new Object[]{-direction * SHIP_WIDTH + leftOrRight * SCREEN_WIDTH, 2f / 3f * SCREEN_HEIGHT + random.nextFloat() * SCREEN_HEIGHT / 12f, direction};
            case LINEAR_Y:
                return new Object[]{1f / 5f * SCREEN_WIDTH + random.nextFloat() * 3 * SCREEN_WIDTH / 5f, SCREEN_HEIGHT};
            case LINEAR_XY:
                return new Object[]{0f + leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_WIDTH - leftOrRight * SCREEN_WIDTH, 0f};
            case SEMI_CIRCLE:
                return new Object[]{0f, SCREEN_HEIGHT};
            case BEZIER_SPLINE:
                if (random.nextBoolean()) {
                    return new Object[]{
                            new Vector2(0f + leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                            new Vector2(SCREEN_WIDTH - leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                            new Vector2(SCREEN_WIDTH - leftOrRight * SCREEN_WIDTH, 0f),
                            new Vector2(0f + leftOrRight * SCREEN_WIDTH, 0f)};
                } else {
                    return new Object[]{
                            new Vector2(-11 * SHIP_WIDTH + leftOrRight * (SCREEN_WIDTH + 11 * SHIP_WIDTH), SCREEN_HEIGHT / 2f),
                            new Vector2(0f + leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                            new Vector2(SCREEN_WIDTH - leftOrRight * (SCREEN_WIDTH + 2 * SHIP_WIDTH), SCREEN_HEIGHT),
                            new Vector2(SCREEN_WIDTH - leftOrRight * (SCREEN_WIDTH + 6 * SHIP_WIDTH), SCREEN_HEIGHT / 2f)};
                }
            case CATMULL_ROM_SPLINE:
                return new Object[]{
                        new Vector2(0f + leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                        new Vector2(SCREEN_WIDTH * (0.8f - leftOrRight * 0.6f), 3 * SCREEN_HEIGHT / 4f),
                        new Vector2(SCREEN_WIDTH * (0.2f + leftOrRight * 0.6f), 2 * SCREEN_HEIGHT / 4f),
                        new Vector2(SCREEN_WIDTH * (0.8f - leftOrRight * 0.6f), SCREEN_HEIGHT / 4f),
                        new Vector2(SCREEN_WIDTH * (0.2f + leftOrRight * 0.6f), -SCREEN_HEIGHT / 4f),
                        new Vector2(SCREEN_WIDTH * (0.8f - leftOrRight * 0.6f), -2 * SCREEN_HEIGHT / 4f)
                };


        }
        return null;
    }

    public int getRandomShipType() {
        return random.nextInt(6);
    }

    protected int getRandomMoveType() {
        return random.nextInt(8);
    }

    protected void startLevel(float time) {
        this.time = time;
        initSpawns();
    }

    protected abstract void initSpawns();


}
