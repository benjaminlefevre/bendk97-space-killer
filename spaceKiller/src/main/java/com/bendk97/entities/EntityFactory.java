/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 22:06
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.Linear;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.bendk97.Settings;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;
import com.bendk97.components.*;
import com.bendk97.components.TankComponent.TankLevel;
import com.bendk97.screens.LevelScreen;
import com.bendk97.screens.ScreenShake;
import com.bendk97.timer.PausableTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP_PINGPONG;
import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.Assets.*;
import static com.bendk97.components.Mappers.position;
import static com.bendk97.components.Mappers.sprite;
import static com.bendk97.tweens.PositionComponentAccessor.POSITION_XY;
import static com.bendk97.tweens.PositionComponentAccessor.POSITION_Y;
import static com.bendk97.tweens.SpriteComponentAccessor.ALPHA;

public class EntityFactory implements Disposable {

    final static int SOUCOUPE = 0;
    private final static int SHIP_1 = 1;
    private final static int SHIP_2 = 2;
    private final static int SHIP_3 = 3;
    private final static int SHIP_4 = 4;
    private final static int SHIP_5 = 5;

    public final static int SHIP_LV3_1 = 6;
    private final static int SHIP_LV3_2 = 7;
    private final static int SHIP_LV3_3 = 8;
    private final static int SHIP_LV3_4 = 9;
    private final static int SHIP_LV3_5 = 10;
    private final static int SHIP_LV3_6 = 11;
    private final static int SHIP_LV3_7 = 12;
    private final static int SHIP_LV3_8 = 13;
    public final static int NB_SHIP_LV3 = 8;

    final static int SHIP_LV2_LASER_SHIP1 = 50;
    private final static int SHIP_LV2_LASER_SHIP2 = 51;
    private final static int SHIP_LV2_LASER_SHIP3 = 52;
    private final static int SHIP_LV2_LASER_SHIP4 = 53;
    final static int NB_SHIP_LV2_LASER_SHIP = 4;


    public final static int BOSS_LEVEL_1 = 100;
    public final static int BOSS_LEVEL_2 = 101;
    public final static int BOSS_LEVEL_3 = 102;
    final static int ASTEROID_1 = 999;
    final static int ASTEROID_2 = 1000;
    final static int HOUSE_1 = 1500;
    final static int HOUSE_2 = 1501;
    final static int HOUSE_3 = 1502;
    final static int HOUSE_4 = 1503;
    final static int HOUSE_5 = 1504;
    final static int HOUSE_6 = 1505;
    final static int HOUSE_7 = 1506;
    final static int HOUSE_8 = 1507;
    final static int HOUSE_9 = 1508;

    private final PooledEngine engine;
    private final Assets assets;
    private final TweenManager tweenManager;
    private final ScreenShake screenShake;
    private final TextureAtlas atlasNoMask;
    private final TextureAtlas atlasMask;
    private final RayHandler rayHandler;
    private final Pool<PointLight> lightPool = new Pool<PointLight>() {
        @Override
        protected PointLight newObject() {
            PointLight light = new PointLight(rayHandler, 5);
            light.setXray(true);
            return light;
        }
    };
    private final Random random = new RandomXS128();
    private final SpaceKillerGame game;


    public EntityFactory(SpaceKillerGame game, PooledEngine engine, Assets assets, TweenManager tweenManager, RayHandler rayHandler,
                         ScreenShake screenShake, LevelScreen.Level level) {
        this.engine = engine;
        this.screenShake = screenShake;
        this.game = game;
        this.rayHandler = rayHandler;
        if (rayHandler != null) {
            Array<PointLight> poolObjects = new Array<PointLight>(30);
            for (int i = 0; i < 50; ++i) {
                PointLight light = lightPool.obtain();
                light.setActive(false);
                poolObjects.add(light);
            }
            lightPool.freeAll(poolObjects);
        }
        this.assets = assets;
        this.tweenManager = tweenManager;
        this.atlasNoMask = assets.get(GFX_LEVEL_ALL_ATLAS_NO_MASK);
        this.atlasMask = assets.get(level.getSprites());
    }


    public Entity createBackground(Texture texture, float velocity) {
        return createBackground(texture, 0, velocity);
    }

    public Entity createBackground(Texture texture, int zIndex, float velocity) {
        Entity background = engine.createEntity();
        BackgroundComponent component = engine.createComponent(BackgroundComponent.class);
        component.setTexture(texture);
        component.zIndex = zIndex;
        background.add(component);
        background.add(engine.createComponent(PositionComponent.class));
        background.add(engine.createComponent(VelocityComponent.class));
        background.getComponent(VelocityComponent.class).y = velocity;
        engine.addEntity(background);
        return background;
    }

    public void createForeground(Texture texture, float velocity) {
        Entity foreground = engine.createEntity();
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.setTexture(new Sprite(texture), 1.0f, 0, 1.0f);
        component.zIndex = 100;
        foreground.add(component);
        foreground.add(engine.createComponent(PositionComponent.class));
        foreground.add(engine.createComponent(VelocityComponent.class));
        foreground.add(engine.createComponent(RemovableComponent.class));
        foreground.getComponent(PositionComponent.class).setPosition(0f, SCREEN_HEIGHT + 20f);
        foreground.getComponent(VelocityComponent.class).y = -velocity;
        engine.addEntity(foreground);
    }


    public void createPlayerFire(Entity player) {
        Entity bullet = engine.createEntity();
        bullet.add(engine.createComponent(PlayerBulletComponent.class));
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
        PlayerComponent playerComponent = Mappers.player.get(player);
        spriteComponent.sprite = new Sprite(atlasMask.findRegion(playerComponent.powerLevel.regionName));
        bullet.add(spriteComponent);
        bullet.add(engine.createComponent(RemovableComponent.class));
        engine.addEntity(bullet);
        PositionComponent playerPosition = position.get(player);
        positionComponent.x = playerPosition.x + Mappers.sprite.get(player).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f;
        positionComponent.y = playerPosition.y + sprite.get(player).sprite.getHeight();
        velocityComponent.y = PLAYER_BULLET_VELOCITY;
        if (rayHandler != null) {
            createLight(bullet, playerComponent.powerLevel.color, spriteComponent.sprite.getWidth() * 7f);
        }
    }

    public void createPlayerFireSide(Entity player) {
        createPlayerLeftFire(player);
        createPlayerRightFire(player);
    }

    private void createPlayerLeftFire(Entity player) {
        Entity bullet = engine.createEntity();
        bullet.add(engine.createComponent(PlayerBulletComponent.class));
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
        PlayerComponent playerComponent = Mappers.player.get(player);
        spriteComponent.sprite = new Sprite(atlasMask.findRegion(playerComponent.powerLevel.leftRegionName));
        bullet.add(spriteComponent);
        bullet.add(engine.createComponent(RemovableComponent.class));
        engine.addEntity(bullet);
        PositionComponent playerPosition = position.get(player);
        positionComponent.x = playerPosition.x - spriteComponent.sprite.getWidth();
        positionComponent.y = playerPosition.y + sprite.get(player).sprite.getHeight();
        Vector2 direction = new Vector2(0, 1);
        direction.rotate(35f);
        direction.nor();
        direction.scl(PLAYER_BULLET_VELOCITY * 1.5f);
        velocityComponent.y = direction.y;
        velocityComponent.x = direction.x;
        if (rayHandler != null) {
            createLight(bullet, playerComponent.powerLevel.color, spriteComponent.sprite.getWidth() * 7f);
        }
    }


    private void createPlayerRightFire(Entity player) {
        Entity bullet = engine.createEntity();
        bullet.add(engine.createComponent(PlayerBulletComponent.class));
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
        PlayerComponent playerComponent = Mappers.player.get(player);
        spriteComponent.sprite = new Sprite(atlasMask.findRegion(playerComponent.powerLevel.rightRegionName));
        bullet.add(spriteComponent);
        bullet.add(engine.createComponent(RemovableComponent.class));
        engine.addEntity(bullet);
        PositionComponent playerPosition = position.get(player);
        positionComponent.x = playerPosition.x + sprite.get(player).sprite.getWidth();
        positionComponent.y = playerPosition.y + sprite.get(player).sprite.getHeight();
        Vector2 direction = new Vector2(0, 1);
        direction.rotate(-35f);
        direction.nor();
        direction.scl(PLAYER_BULLET_VELOCITY * 1.5f);
        velocityComponent.y = direction.y;
        velocityComponent.x = direction.x;
        if (rayHandler != null) {
            createLight(bullet, playerComponent.powerLevel.color, spriteComponent.sprite.getWidth() * 7f);
        }
    }

    public void createPlayerBomb(Entity player) {
        final Entity bomb = engine.createEntity();
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        bomb.add(positionComponent);
        SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(FRAME_DURATION, atlasNoMask.createSprites("bomb"), LOOP));
        spriteComponent.sprite = animationComponent.animations.get(ANIMATION_MAIN).getKeyFrame(0);
        bomb.add(spriteComponent);
        bomb.add(animationComponent);
        bomb.add(engine.createComponent(StateComponent.class));
        engine.addEntity(bomb);
        PositionComponent playerPosition = position.get(player);
        positionComponent.x = playerPosition.x + Mappers.sprite.get(player).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f;
        positionComponent.y = playerPosition.y + Mappers.sprite.get(player).sprite.getHeight() >= SCREEN_HEIGHT * 3f / 4f ?
                playerPosition.y : playerPosition.y + sprite.get(player).sprite.getHeight();
        Tween.to(positionComponent, POSITION_XY, 0.6f).ease(Linear.INOUT)
                .target(SCREEN_WIDTH / 2f - spriteComponent.sprite.getWidth() / 2f, SCREEN_HEIGHT * 3f / 4f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int event, BaseTween<?> baseTween) {
                        if (event == COMPLETE) {
                            createBombExplosion(bomb);
                            engine.removeEntity(bomb);
                        }
                    }
                })
                .start(tweenManager);
    }

    private void createBombExplosion(Entity bomb) {
        final Entity bombExplosion = engine.createEntity();
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        bombExplosion.add(positionComponent);
        final SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(FRAME_DURATION_BOMB_EXPLOSION,
                atlasNoMask.createSprites("bomb_explosion"), LOOP_PINGPONG));
        spriteComponent.sprite = atlasNoMask.createSprite("bomb_explosion", 6);
        spriteComponent.zIndex = 100;
        bombExplosion.add(spriteComponent);
        bombExplosion.add(animationComponent);
        bombExplosion.add(engine.createComponent(StateComponent.class));
        engine.addEntity(bombExplosion);
        assets.playSound(SOUND_BOMB_EXPLOSION);
        PositionComponent bombPosition = position.get(bomb);
        positionComponent.x = bombPosition.x - spriteComponent.sprite.getWidth() / 2f;
        positionComponent.y = bombPosition.y - spriteComponent.sprite.getHeight() / 2f;
        Tween.to(positionComponent, POSITION_Y, 0.7f).ease(Linear.INOUT)
                .target(bombPosition.y - spriteComponent.sprite.getHeight() / 2f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int event, BaseTween<?> baseTween) {
                        if (event == COMPLETE) {
                            bombExplosion.add(engine.createComponent(BombExplosionComponent.class));
                        }
                    }
                })
                .start(tweenManager);
        screenShake.shake(20f, 1f, false);
        PausableTimer.schedule(new PausableTimer.Task() {
            @Override
            public void run() {
                if (rayHandler != null) {
                    createLight(bombExplosion, new Color(1f, 1f, 1f, 0.8f), spriteComponent.sprite.getHeight() * 20f);
                }
            }
        }, 0.6f);
    }

    private void createLight(Entity entity) {
        createLight(entity, new Color(0.5f, 0f, 0f, 0.3f),
                Mappers.sprite.get(entity).sprite.getHeight() * 20f);
    }

    private void createLight(Entity entity, Color color, float distance) {
        SpriteComponent sprite = Mappers.sprite.get(entity);
        PositionComponent position = Mappers.position.get(entity);
        LightComponent lightComponent = engine.createComponent(LightComponent.class);
        PointLight light = lightPool.obtain();
        light.setActive(true);
        light.setColor(color);
        light.setDistance(distance);
        light.setPosition(position.x + sprite.sprite.getWidth() / 2f,
                position.y + sprite.sprite.getHeight() / 2f);
        lightComponent.light = light;
        lightComponent.lights = lightPool;
        entity.add(lightComponent);
    }

    public final static int ENEMY_FIRE_CIRCLE = 1;
    private final static int ENEMY_FIRE_LASER = 2;

    public void createEnemyFire(Entity enemy, Entity player) {
        switch (Mappers.enemy.get(enemy).attackType) {
            case ENEMY_FIRE_LASER:
                createEnemyFireLaser(enemy);
                break;
            case ENEMY_FIRE_CIRCLE:
            default:
            createEnemyFireCircle(enemy, player);
            break;
        }
    }

    private void createEnemyFireLaser(Entity enemy) {
        PositionComponent position = Mappers.position.get(enemy);
        Sprite sprite = Mappers.sprite.get(enemy).sprite;
        createEnemyFireLaser(position.x + sprite.getWidth() / 2f, position.y, Mappers.enemy.get(enemy).bulletVelocity);
    }

    private void createEnemyFireLaser(float posX, float posY, float velocity) {
        assets.playSound(SOUND_FIRE_ENEMY);
        Entity bullet = engine.createEntity();
        bullet.add(engine.createComponent(EnemyBulletComponent.class));
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
        spriteComponent.sprite = new Sprite(atlasMask.findRegion("laser"));
        bullet.add(spriteComponent);
        bullet.add(engine.createComponent(RemovableComponent.class));
        engine.addEntity(bullet);
        positionComponent.x = posX - spriteComponent.sprite.getWidth() / 2f;
        positionComponent.y = posY - spriteComponent.sprite.getHeight() / 2f;

        Vector2 directionBullet = new Vector2(0f, -1);
        directionBullet.scl(velocity);
        velocityComponent.x = 0;
        velocityComponent.y = directionBullet.y;
    }


    private void createEnemyFireCircle(Entity enemy, Entity player) {
        assets.playSound(SOUND_FIRE_ENEMY);
        Entity bullet = engine.createEntity();
        bullet.add(engine.createComponent(EnemyBulletComponent.class));
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
        spriteComponent.sprite = new Sprite(atlasMask.findRegion("bulletEnemy"));
        bullet.add(spriteComponent);
        bullet.add(engine.createComponent(RemovableComponent.class));
        engine.addEntity(bullet);
        PositionComponent playerPosition = position.get(player);
        PositionComponent enemyPosition = position.get(enemy);
        EnemyComponent enemyComponent = Mappers.enemy.get(enemy);
        if (enemyComponent.isBoss) {
            positionComponent.x = enemyPosition.x + Mappers.sprite.get(enemy).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f;
            positionComponent.y = enemyPosition.y + sprite.get(enemy).sprite.getHeight() * 3f / 4f;
        } else if (enemyComponent.isTank) {
            Sprite tank = sprite.get(enemy).sprite;
            float rotation = tank.getRotation();
            Vector2 pos = new Vector2(enemyPosition.x + Mappers.sprite.get(enemy).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f,
                    enemyPosition.y + 32f);
            Vector2 angle = new Vector2(0, -1);
            angle.rotate(rotation);
            angle.nor();
            angle.scl(new Vector2(32, 32));
            angle.add(pos);
            positionComponent.x = angle.x;
            positionComponent.y = angle.y;
        } else {
            positionComponent.x = enemyPosition.x + Mappers.sprite.get(enemy).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f;
            positionComponent.y = enemyPosition.y + sprite.get(enemy).sprite.getHeight();
        }
        Vector2 directionBullet = new Vector2(playerPosition.x - positionComponent.x, playerPosition.y - positionComponent.y);
        directionBullet.nor();
        if (!enemyComponent.isTank) {
            directionBullet.rotate(-10 + random.nextFloat() * 20f);
        }
        directionBullet.scl(Mappers.enemy.get(enemy).bulletVelocity);
        velocityComponent.x = directionBullet.x;
        velocityComponent.y = directionBullet.y;
    }

    public void createBossFire(final Entity boss, final Entity player) {
        int type = random.nextInt(3);
        LevelScreen.Level level = Mappers.player.get(player).level;
        if (type == 0) {
            final int bullets = level.equals(LevelScreen.Level.Level3) ? 20 : 10;
            float delay = level.equals(LevelScreen.Level.Level3) ? 0.1f : 0.2f;
            for (int i = 0; i < bullets; ++i) {
                PausableTimer.schedule(new PausableTimer.Task() {
                    @Override
                    public void run() {
                        createEnemyFire(boss, player);
                    }
                }, 0f + delay * i);
            }
        } else if (type == 1 || level.equals(LevelScreen.Level.Level1)
                || Mappers.position.get(boss).x < 0 || Mappers.position.get(boss).x > SCREEN_WIDTH * 3f / 4f) {
            createBossFireCircle(boss, false);
            if (level.equals(LevelScreen.Level.Level3)) {
                createBossFireCircle(boss, true);
            }
        } else {
            PositionComponent position = Mappers.position.get(boss);
            BossComponent bossComponent = Mappers.boss.get(boss);
            createEnemyFireLaser(position.x + 160f, position.y + 170f, bossComponent.velocityFire2);
            createEnemyFireLaser(position.x + 195f, position.y + 170f, bossComponent.velocityFire2);
            if (level.equals(LevelScreen.Level.Level3)) {
                createEnemyFireLaser(position.x + 160f, position.y + 170f, -bossComponent.velocityFire2);
                createEnemyFireLaser(position.x + 195f, position.y + 170f, -bossComponent.velocityFire2);

            }
        }
    }

    public void createBossFire2(Entity boss) {
        PositionComponent position = Mappers.position.get(boss);
        BossComponent bossComponent = Mappers.boss.get(boss);
        createEnemyFireLaser(position.x + 12f, position.y + 186f, bossComponent.velocityFire2);
        createEnemyFireLaser(position.x + 342f, position.y + 186f, bossComponent.velocityFire2);
    }

    private void createBossFireCircle(Entity boss, boolean yUp) {
        Array<Entity> bullets = new Array<Entity>(10);
        assets.playSound(SOUND_FIRE_ENEMY);
        for (int i = 0; i < 12; ++i) {
            Entity bullet = engine.createEntity();

            bullet.add(engine.createComponent(EnemyBulletComponent.class));

            PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
            bullet.add(positionComponent);

            VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
            bullet.add(velocityComponent);

            SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
            spriteComponent.sprite = new Sprite(atlasMask.findRegion("bulletEnemy"));
            bullet.add(spriteComponent);

            bullet.add(engine.createComponent(RemovableComponent.class));

            PositionComponent enemyPosition = position.get(boss);
            positionComponent.x = enemyPosition.x + Mappers.sprite.get(boss).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f;
            positionComponent.y = enemyPosition.y + Mappers.sprite.get(boss).sprite.getHeight() / 4f;

            bullets.add(bullet);
            engine.addEntity(bullet);
        }
        float rotation = yUp ? 35f : -35f;
        for (int i = 0; i < bullets.size; ++i) {
            Vector2 directionBullet = new Vector2(1f, 0f);
            directionBullet.setAngle(rotation);
            rotation -= yUp ? -10f : 10f;
            directionBullet.scl(Mappers.boss.get(boss).velocityFire1);
            VelocityComponent velocityComponent = Mappers.velocity.get(bullets.get(i));
            velocityComponent.x = directionBullet.x;
            velocityComponent.y = directionBullet.y;
        }
    }


    public void createPowerUp(Entity squadron) {
        final Entity powerUp = engine.createEntity();
        powerUp.add(engine.createComponent(PowerUpComponent.class));
        PositionComponent position = engine.createComponent(PositionComponent.class);
        powerUp.add(position);
        powerUp.add(engine.createComponent(VelocityComponent.class));
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        Array<Sprite> sprites = atlasMask.createSprites("power-up");
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(FRAME_DURATION_POWER_UP, sprites, LOOP));
        powerUp.add(animationComponent);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = sprites.get(0);
        powerUp.add(component);
        position.x = Mappers.squadron.get(squadron).lastKilledPosition.x;
        position.y = Mappers.squadron.get(squadron).lastKilledPosition.y;
        powerUp.add(engine.createComponent(StateComponent.class));
        Timeline.createSequence()
                .beginParallel()
                .push(Tween.to(position, POSITION_Y, 8f).ease(Linear.INOUT).target(50f))
                .push(Tween.to(component, ALPHA, 0.5f).delay(4f).ease(Linear.INOUT).target(0f).repeat(8, 0f))
                .end()
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        if (i == TweenCallback.COMPLETE) {
                            engine.removeEntity(powerUp);
                        }
                    }
                })
                .start(tweenManager);
        engine.addEntity(powerUp);
    }

    public void createShieldUp(Entity squadron) {
        final Entity shieldUp = engine.createEntity();
        shieldUp.add(engine.createComponent(ShieldUpComponent.class));
        PositionComponent position = engine.createComponent(PositionComponent.class);
        shieldUp.add(position);
        shieldUp.add(engine.createComponent(VelocityComponent.class));
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        Array<Sprite> sprites = atlasMask.createSprites("shieldup");
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(FRAME_DURATION_POWER_UP, sprites, LOOP));
        shieldUp.add(animationComponent);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = sprites.get(0);
        shieldUp.add(component);
        position.x = Mappers.squadron.get(squadron).lastKilledPosition.x;
        position.y = Mappers.squadron.get(squadron).lastKilledPosition.y;
        shieldUp.add(engine.createComponent(StateComponent.class));
        Timeline.createSequence()
                .beginParallel()
                .push(Tween.to(position, POSITION_Y, 8f).ease(Linear.INOUT).target(50f))
                .push(Tween.to(component, ALPHA, 0.5f).delay(4f).ease(Linear.INOUT).target(0f).repeat(8, 0f))
                .end()
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        if (i == TweenCallback.COMPLETE) {
                            engine.removeEntity(shieldUp);
                        }
                    }
                })
                .start(tweenManager);
        engine.addEntity(shieldUp);
    }

    public void createBombUp(Entity squadron) {
        final Entity bombUp = engine.createEntity();
        bombUp.add(engine.createComponent(BombUpComponent.class));
        PositionComponent position = engine.createComponent(PositionComponent.class);
        bombUp.add(position);
        bombUp.add(engine.createComponent(VelocityComponent.class));
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = new Sprite(atlasMask.findRegion("bombUp"));
        bombUp.add(component);
        position.x = Mappers.squadron.get(squadron).lastKilledPosition.x;
        position.y = Mappers.squadron.get(squadron).lastKilledPosition.y;
        Timeline.createSequence()
                .beginParallel()
                .push(Tween.to(position, POSITION_Y, 8f).ease(Linear.INOUT).target(50f))
                .push(Tween.to(component, ALPHA, 0.5f).delay(4f).ease(Linear.INOUT).target(0f).repeat(8, 0f))
                .end()
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        if (i == TweenCallback.COMPLETE) {
                            engine.removeEntity(bombUp);
                        }
                    }
                })
                .start(tweenManager);
        engine.addEntity(bombUp);
    }

    public Entity createLaserShip(int type, Float velocity, float bulletVelocity, int rateShoot, int gaugeLife, int points, boolean fromLeft) {
        String atlasRegion = null;
        switch (type) {
            case SHIP_LV2_LASER_SHIP1:
                atlasRegion = "staticEnemy1";
                break;
            case SHIP_LV2_LASER_SHIP2:
                atlasRegion = "staticEnemy2";
                break;
            case SHIP_LV2_LASER_SHIP3:
                atlasRegion = "staticEnemy3";
                break;
            case SHIP_LV2_LASER_SHIP4:
                atlasRegion = "staticEnemy4";
                break;
            case SHIP_LV3_1:
                atlasRegion = "lark";
                break;
            case SHIP_LV3_2:
                atlasRegion = "stab";
                break;
            case SHIP_LV3_3:
                atlasRegion = "squid";
                break;
            case SHIP_LV3_4:
                atlasRegion = "bug";
                break;
            case SHIP_LV3_5:
                atlasRegion = "swarmer";
                break;
            case SHIP_LV3_6:
                atlasRegion = "stingray";
                break;
            case SHIP_LV3_7:
                atlasRegion = "fish";
                break;
            case SHIP_LV3_8:
            default:
                atlasRegion = "podfish";
                break;
        }
        return createLaserShip(atlasRegion, velocity, bulletVelocity, rateShoot, gaugeLife, points, fromLeft);
    }

    private Entity createLaserShip(String atlasRegion, Float velocity, float bulletVelocity, int rateShoot, int gaugeLife, int points, boolean fromLeft) {
        Entity enemy = engine.createEntity();
        engine.addEntity(enemy);
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        enemy.add(positionComponent);
        if (velocity != null) {
            VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
            velocityComponent.x = velocity;
            enemy.add(velocityComponent);
            FollowPlayerComponent followPlayerComponent = engine.createComponent(FollowPlayerComponent.class);
            enemy.add(followPlayerComponent);
            followPlayerComponent.velocity = velocity;

        }
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        Array<Sprite> sprites = atlasMask.createSprites(atlasRegion);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(FRAME_DURATION, sprites, LOOP_PINGPONG));
        enemy.add(animationComponent);
        SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
        enemy.add(spriteComponent);
        spriteComponent.sprite = sprites.get(0);
        spriteComponent.zIndex = 20;
        positionComponent.x = fromLeft ? -spriteComponent.sprite.getWidth() : SCREEN_WIDTH;
        positionComponent.y = SCREEN_HEIGHT - spriteComponent.sprite.getHeight();
        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        enemyComponent.initLifeGauge(gaugeLife);
        enemyComponent.points = points;
        enemyComponent.bulletVelocity = bulletVelocity;
        enemyComponent.attackCapacity = Integer.MAX_VALUE;
        enemyComponent.probabilityAttack = rateShoot;
        enemyComponent.attackType = ENEMY_FIRE_LASER;
        enemyComponent.isLaserShip = true;
        enemy.add(enemyComponent);
        enemy.add(engine.createComponent(StateComponent.class));
        return enemy;
    }


    public List<Entity> createTank(TankLevel level, int gauge, int points) {
        List<Entity> entities = new ArrayList<Entity>();
        Entity tankCannon = engine.createEntity();
        final EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        enemyComponent.points = points;
        enemyComponent.isTank = true;
        enemyComponent.initLifeGauge(gauge);
        enemyComponent.probabilityAttack = 1;
        enemyComponent.bulletVelocity = level.bulletVelocity;
        enemyComponent.attackCapacity = 0;
        tankCannon.add(enemyComponent);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        tankCannon.add(position);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = atlasMask.createSprite("tankCannon");
        component.sprite.setOrigin(32f, 46f);
        component.zIndex = -5;
        tankCannon.add(component);
        tankCannon.add(engine.createComponent(GroundEnemyComponent.class));
        FollowPlayerComponent followPlayerComponent = engine.createComponent(FollowPlayerComponent.class);
        followPlayerComponent.rotate = true;
        tankCannon.add(followPlayerComponent);
        TankComponent tankComponent = engine.createComponent(TankComponent.class);
        tankComponent.setLevel(level);
        tankCannon.add(tankComponent);
        engine.addEntity(tankCannon);
        Entity tankBody = engine.createEntity();
        tankBody.add(engine.createComponent(PositionComponent.class));
        SpriteComponent sprite = engine.createComponent(SpriteComponent.class);
        sprite.sprite = atlasMask.createSprite("tankBody");
        sprite.zIndex = -6;
        tankBody.add(sprite);
        tankBody.add(engine.createComponent(GroundEnemyComponent.class));
        engine.addEntity(tankBody);

        entities.add(tankBody);
        entities.add(tankCannon);
        return entities;
    }

    private Entity createEnemy(Entity squadron, boolean canAttack, int rateShoot, float velocityBullet, String atlasName, int points,
                               int strength, float frameDuration, PlayMode animationType, int attackCapacity) {
        Entity enemy = engine.createEntity();
        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        enemyComponent.points = points;
        enemyComponent.initLifeGauge(strength);
        enemyComponent.probabilityAttack = rateShoot;
        enemyComponent.bulletVelocity = velocityBullet;
        enemyComponent.attackCapacity = canAttack ? attackCapacity : 0;
        if (squadron != null) {
            enemyComponent.squadron = squadron;
        }
        enemy.add(enemyComponent);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        enemy.add(position);
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        Array<Sprite> sprites = atlasMask.createSprites(atlasName);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(frameDuration, sprites, animationType));
        enemy.add(animationComponent);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = sprites.get(0);
        enemy.add(component);
        enemy.add(engine.createComponent(StateComponent.class));
        engine.addEntity(enemy);
        return enemy;
    }

    public Entity createEnemySoucoupe(Entity squadron, boolean canAttack, float velocityBullet) {
        return createEnemy(squadron, canAttack, STANDARD_RATE_SHOOT, velocityBullet, "soucoupe", 100, 1, FRAME_DURATION, LOOP, 1);
    }


    public Entity createEnemyShip(Entity squadron, boolean canAttack, float velocityBullet, int rateShoot, int enemyType) {
        String atlasRegion = "enemy";
        int points = 200;
        int strength = 1;
        int attackCapacity = 1;
        PlayMode playMode = LOOP;
        float frameDuration = FRAME_DURATION;
        switch (enemyType) {
            case SHIP_1:
                atlasRegion = "enemy";
                break;
            case SHIP_2:
                atlasRegion = "enemy2";
                break;
            case SHIP_3:
                atlasRegion = "enemy3";
                frameDuration = FRAME_DURATION_ENEMY_3;
                break;
            case SHIP_4:
                atlasRegion = "enemy4";
                break;
            case SHIP_5:
                atlasRegion = "enemy5";
                playMode = LOOP_PINGPONG;
                break;
            case SHIP_LV3_1:
                atlasRegion = "lark";
                playMode = LOOP_PINGPONG;
                attackCapacity = 3;
                strength = 2;
                points = 250;
                break;
            case SHIP_LV3_2:
                atlasRegion = "stab";
                playMode = LOOP_PINGPONG;
                attackCapacity = 2;
                strength = 1;
                points = 200;
                break;
            case SHIP_LV3_3:
                atlasRegion = "squid";
                playMode = LOOP_PINGPONG;
                attackCapacity = 3;
                strength = 2;
                points = 250;
                break;
            case SHIP_LV3_4:
                atlasRegion = "bug";
                playMode = LOOP_PINGPONG;
                attackCapacity = 2;
                strength = 1;
                points = 200;
                break;
            case SHIP_LV3_5:
                atlasRegion = "swarmer";
                playMode = LOOP_PINGPONG;
                attackCapacity = 2;
                strength = 1;
                points = 250;
                break;
            case SHIP_LV3_6:
                atlasRegion = "stingray";
                playMode = LOOP_PINGPONG;
                attackCapacity = 2;
                strength = 1;
                points = 200;
                break;
            case SHIP_LV3_7:
                atlasRegion = "fish";
                playMode = LOOP_PINGPONG;
                attackCapacity = 2;
                strength = 2;
                points = 250;
                break;
            case SHIP_LV3_8:
            default:
                atlasRegion = "podfish";
                playMode = LOOP_PINGPONG;
                attackCapacity = 2;
                strength = 1;
                points = 200;
                break;
        }
        return createEnemy(squadron, canAttack, rateShoot, velocityBullet, atlasRegion, points, strength, frameDuration, playMode, attackCapacity);
    }


    public Entity createBoss(Entity squadron, float velocityBullet, float velocityCircle) {
        final Entity enemy = engine.createEntity();
        BossComponent bossComponent = engine.createComponent(BossComponent.class);
        bossComponent.velocityFire1 = velocityCircle;
        enemy.add(bossComponent);
        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        enemyComponent.points = 50;
        enemyComponent.isBoss = true;
        enemyComponent.initLifeGauge(BOSS_LEVEL1_GAUGE);
        enemyComponent.bulletVelocity = velocityBullet;
        enemyComponent.attackCapacity = Integer.MAX_VALUE;
        if (squadron != null) {
            enemyComponent.squadron = squadron;
        }
        enemy.add(enemyComponent);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        enemy.add(position);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = atlasMask.createSprite("boss-level1");
        enemy.add(component);
        engine.addEntity(enemy);
        PausableTimer.schedule(new PausableTimer.Task() {
            @Override
            public void run() {
                Mappers.boss.get(enemy).pleaseFire1 = true;
            }
        }, 5f);
        return enemy;
    }

    public Entity createBoss2(Entity squadron, float velocityBullet, float velocityFireCircle, float velocityBullet2) {
        final Entity enemy = engine.createEntity();
        BossComponent boss = engine.createComponent(BossComponent.class);
        enemy.add(boss);
        boss.minTriggerFire1 = 4;
        boss.minTriggerFire2 = 7;
        boss.velocityFire1 = velocityFireCircle;
        boss.velocityFire2 = velocityBullet2;
        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        enemyComponent.points = 50;
        enemyComponent.isBoss = true;
        enemyComponent.initLifeGauge(BOSS_LEVEL2_GAUGE);
        enemyComponent.bulletVelocity = velocityBullet;
        enemyComponent.attackCapacity = Integer.MAX_VALUE;
        if (squadron != null) {
            enemyComponent.squadron = squadron;
        }
        enemy.add(enemyComponent);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        enemy.add(position);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = atlasMask.createSprite("boss");
        enemy.add(component);
        engine.addEntity(enemy);
        PausableTimer.schedule(new PausableTimer.Task() {
            @Override
            public void run() {
                Mappers.boss.get(enemy).pleaseFire1 = true;
            }
        }, 5f);
        PausableTimer.schedule(new PausableTimer.Task() {
            @Override
            public void run() {
                Mappers.boss.get(enemy).pleaseFire2 = true;
            }
        }, 2f);

        return enemy;
    }

    public Entity createBoss3(Entity squadron, float velocityBullet, float velocityBulletFireCircle, float velocityBullet2) {
        final Entity enemy = engine.createEntity();
        BossComponent boss = engine.createComponent(BossComponent.class);
        enemy.add(boss);
        boss.minTriggerFire1 = 3;
        boss.minTriggerFire2 = 7;
        boss.velocityFire1 = velocityBulletFireCircle;
        boss.velocityFire2 = velocityBullet2;
        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        enemyComponent.points = 50;
        enemyComponent.isBoss = true;
        enemyComponent.initLifeGauge(BOSS_LEVEL3_GAUGE);
        enemyComponent.bulletVelocity = velocityBullet;
        enemyComponent.attackCapacity = Integer.MAX_VALUE;
        if (squadron != null) {
            enemyComponent.squadron = squadron;
        }
        enemy.add(enemyComponent);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        enemy.add(position);

        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        Array<Sprite> sprites = atlasMask.createSprites("boss3");
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(0.075f, (Sprite[]) sprites.toArray(Sprite.class)));
        animationComponent.animations.get(ANIMATION_MAIN).setPlayMode(LOOP_PINGPONG);
        enemy.add(animationComponent);

        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = sprites.get(0);
        enemy.add(component);
        engine.addEntity(enemy);
        PausableTimer.schedule(new PausableTimer.Task() {
            @Override
            public void run() {
                Mappers.boss.get(enemy).pleaseFire1 = true;
            }
        }, 5f);
        enemy.add(engine.createComponent(StateComponent.class));
        return enemy;
    }


    public Entity createAsteroid(Entity squadron, int asteroid) {
        Entity enemy = engine.createEntity();
        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        enemyComponent.points = 50;
        if (squadron != null) {
            enemyComponent.squadron = squadron;
        }
        enemy.add(enemyComponent);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        enemy.add(position);
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        String asteroidSprite = asteroid == ASTEROID_1 ? "asteroid" : "asteroid2";
        Array<Sprite> sprites = atlasMask.createSprites(asteroidSprite);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(FRAME_DURATION, sprites, LOOP));
        enemy.add(animationComponent);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = sprites.get(0);
        component.zIndex = -99;
        enemy.add(component);
        enemy.add(engine.createComponent(StateComponent.class));
        engine.addEntity(enemy);
        return enemy;
    }


    public Array<Entity> createHouse(Entity squadron, int houseType) {
        Array<Entity> entities = new Array<Entity>();
        Entity house = engine.createEntity();
        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        enemyComponent.points = 50;
        if (squadron != null) {
            enemyComponent.squadron = squadron;
        }
        house.add(enemyComponent);
        house.add(engine.createComponent(PositionComponent.class));
        house.add(engine.createComponent(GroundEnemyComponent.class));
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = atlasMask.createSprite("house-" + (houseType - HOUSE_1 + 1));
        component.zIndex = -10;
        house.add(component);
        engine.addEntity(house);

        Entity houseDestroyed = engine.createEntity();
        houseDestroyed.add(engine.createComponent(PositionComponent.class));
        houseDestroyed.add(engine.createComponent(GroundEnemyComponent.class));
        component = engine.createComponent(SpriteComponent.class);
        component.sprite = atlasMask.createSprite("house-" + (houseType - HOUSE_1 + 1) + "_destroyed");
        component.zIndex = -11;
        houseDestroyed.add(component);
        engine.addEntity(houseDestroyed);

        entities.add(house);
        entities.add(houseDestroyed);
        return entities;
    }


    public Entity createEntityPlayer(LevelScreen.Level level) {
        Entity player = engine.createEntity();
        if (engine.getEntitiesFor(Family.one(PlayerComponent.class).get()).size() > 0) {
            throw new IllegalArgumentException("A player entity already exists!");
        }
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        playerComponent.setHighScore(Settings.getHighscore());
        if (game.playerData != null) {
            playerComponent.bombs = game.playerData.bombs;
            playerComponent.howManyLivesLost = game.playerData.howManyLivesLost;
            playerComponent.enemiesKilled = game.playerData.enemiesKilled;
            playerComponent.laserShipKilled = game.playerData.laserShipKilled;
            playerComponent.fireDelay = game.playerData.fireDelay;
            playerComponent.fireDelaySide = game.playerData.fireDelaySide;
            playerComponent.updateScore(game.playerData.score);
            playerComponent.lives = game.playerData.lives;
            playerComponent.powerLevel = game.playerData.powerLevel;
            playerComponent.numberOfContinue = game.playerData.numberOfContinue;
            playerComponent.level = game.playerData.level;
        } else {
            playerComponent.level = level;
        }
        player.add(playerComponent);
        player.add(engine.createComponent(PositionComponent.class));
        player.add(engine.createComponent(VelocityComponent.class));
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        Array<Sprite> spritesMAIN = new Array<Sprite>(2);
        spritesMAIN.add(atlasMask.createSprite("player", 1));
        spritesMAIN.add(atlasMask.createSprite("player", 2));
        Array<Sprite> spritesLEFT = new Array<Sprite>(2);
        spritesLEFT.add(atlasMask.createSprite("player", 0));
        spritesLEFT.add(atlasMask.createSprite("player", 3));
        Array<Sprite> spritesRIGHT = new Array<Sprite>(2);
        spritesRIGHT.add(atlasMask.createSprite("player", 0));
        spritesRIGHT.add(atlasMask.createSprite("player", 3));
        spritesRIGHT.get(0).flip(true, false);
        spritesRIGHT.get(1).flip(true, false);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(FRAME_DURATION, spritesMAIN, LOOP));
        animationComponent.animations.put(GO_LEFT, new Animation<Sprite>(FRAME_DURATION, spritesLEFT, LOOP));
        animationComponent.animations.put(GO_RIGHT, new Animation<Sprite>(FRAME_DURATION, spritesRIGHT, LOOP));
        player.add(animationComponent);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = spritesMAIN.get(0);
        component.zIndex = 99;
        component.stayInBoundaries = true;
        player.add(component);
        player.add(engine.createComponent(StateComponent.class));
        engine.addEntity(player);
        Mappers.position.get(player).setPosition(PLAYER_ORIGIN_X, PLAYER_ORIGIN_Y);
        return player;
    }

    public void createShield(final Entity player) {
        final Entity shield = engine.createEntity();
        PositionComponent playerPosition = Mappers.position.get(player);
        addInvulnerableComponent(player);
        SpriteComponent playerSprite = Mappers.sprite.get(player);
        SpriteComponent spriteComponent = engine.createComponent(SpriteComponent.class);
        spriteComponent.zIndex = 99;
        spriteComponent.sprite = atlasMask.createSprite("shield");
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        positionComponent.setPosition(playerPosition.x - (spriteComponent.sprite.getWidth() - playerSprite.sprite.getWidth()) / 2f,
                playerPosition.y - (spriteComponent.sprite.getHeight() - playerSprite.sprite.getHeight()) / 2f);
        shield.add(positionComponent);
        shield.add(spriteComponent);
        shield.add(engine.createComponent(ShieldComponent.class));
        engine.addEntity(shield);
        Timeline.createSequence().beginSequence()
                .delay(5f)
                .push(Tween.to(spriteComponent, ALPHA, 0.2f).target(0.2f))
                .push(Tween.to(spriteComponent, ALPHA, 0.2f).target(0.8f))
                .repeat(5, 0f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        if (i == TweenCallback.COMPLETE) {
                            engine.removeEntity(shield);
                            removeInvulnerableComponent(player);
                        }
                    }
                })
                .start(tweenManager);
    }

    public SnapshotArray<Entity> createEntityPlayerLives(Entity player) {
        PlayerComponent playerComponent = Mappers.player.get(player);
        SnapshotArray<Entity> entities = new SnapshotArray<Entity>(true, playerComponent.lives, Entity.class);
        for (int i = 0; i < playerComponent.lives - 1; ++i) {
            Entity life = engine.createEntity();
            SpriteComponent component = engine.createComponent(SpriteComponent.class);
            Sprite sprite = atlasMask.createSprite("player", 1);
            component.setTexture(sprite, 1f, 0f, 0.5f);
            component.setPosition(LIVES_X + 20f * i, LIVES_Y - sprite.getHeight());
            life.add(component);
            engine.addEntity(life);
            entities.add(life);
        }
        return entities;
    }

    public SnapshotArray<Entity> createEntityPlayerBombs(Entity player) {
        PlayerComponent playerComponent = Mappers.player.get(player);
        SnapshotArray<Entity> entities = new SnapshotArray<Entity>(true, playerComponent.bombs, Entity.class);
        for (int i = 0; i < playerComponent.bombs; ++i) {
            Entity bomb = engine.createEntity();
            SpriteComponent component = engine.createComponent(SpriteComponent.class);
            Sprite sprite = atlasNoMask.createSprite("bomb", 1);
            component.zIndex = 100;
            component.setTexture(sprite, 1f, 0f, 1f);
            component.setPosition(BOMB_STOCK_X - 22f * i, BOMB_STOCK_Y);
            bomb.add(component);
            engine.addEntity(bomb);
            entities.add(bomb);
        }
        return entities;
    }

    public Entity createEntityExploding(float x, float y) {
        Entity explosion = engine.createEntity();
        PositionComponent position = engine.createComponent(PositionComponent.class);
        explosion.add(position);
        position.setPosition(x, y);
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        Array<Sprite> sprites = atlasNoMask.createSprites("explosion");
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<Sprite>(FRAME_DURATION_EXPLOSION, sprites, PlayMode.NORMAL));
        explosion.add(animationComponent);
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.sprite = animationComponent.animations.get(ANIMATION_MAIN).getKeyFrame(0);
        component.zIndex = 100;
        explosion.add(component);
        explosion.add(engine.createComponent(StateComponent.class));
        engine.addEntity(explosion);
        //
        if (rayHandler != null) {
            createLight(explosion);
        }
        return explosion;
    }

    public void createBossExploding(final Entity enemy) {
        final SpriteComponent sprite = Mappers.sprite.get(enemy);
        for (int i = 0; i < 50; ++i) {
            PausableTimer.schedule(new PausableTimer.Task() {
                @Override
                public void run() {
                    assets.playSound(SOUND_EXPLOSION);
                    PositionComponent position = Mappers.position.get(enemy);
                    if (position != null) {
                        createEntityExploding(position.x + random.nextFloat() * sprite.sprite.getWidth(),
                                position.y + random.nextFloat() * sprite.sprite.getHeight());
                    }
                }
            }, i * 0.1f);
        }
        if (rayHandler != null) {
            createLight(enemy, new Color(0.7f, 0f, 0f, 0.4f), sprite.sprite.getHeight() * 20f);
        }
    }

    public Entity createEntityFireButton(float alpha, float posX, float posY) {
        Entity entity = engine.createEntity();
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.setTexture(atlasNoMask.createSprite("fire_button"), alpha, 0, 1f);
        component.zIndex = 100;
        component.setPosition(posX, posY);
        entity.add(component);
        engine.addEntity(entity);
        return entity;
    }

    public Entity createEntityBombButton(float alpha, float posX, float posY) {
        Entity entity = engine.createEntity();
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.zIndex = 100;
        component.setTexture(atlasNoMask.createSprite("bomb_button"), alpha, 0, 1f);
        component.setPosition(posX, posY);
        entity.add(component);
        engine.addEntity(entity);
        return entity;

    }

    public Entity createEntitiesPadController(float alpha, float scale, float posX, float posY) {
        Entity pad = engine.createEntity();
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.zIndex = 100;
        component.setTexture(atlasNoMask.createSprite("pad"), alpha, 0f, scale);
        component.setPosition(posX, posY);
        pad.add(component);
        engine.addEntity(pad);
        return pad;
    }

    public Entity createSquadron(boolean powerUp, boolean displayScoreBonus, int bonus) {
        Entity squadron = engine.createEntity();
        SquadronComponent squadronComponent = engine.createComponent(SquadronComponent.class);
        squadronComponent.powerUpAfterDestruction = powerUp;
        squadronComponent.displayBonusSquadron = displayScoreBonus;
        squadronComponent.scoreBonus = bonus;
        squadron.add(squadronComponent);
        engine.addEntity(squadron);
        return squadron;
    }

    public Entity createScoreSquadron(Entity squadron) {
        Entity scoreSquadron = engine.createEntity();
        ScoreSquadronComponent score = engine.createComponent(ScoreSquadronComponent.class);
        PositionComponent position = engine.createComponent(PositionComponent.class);
        SquadronComponent squadronComponent = Mappers.squadron.get(squadron);
        score.score = squadronComponent.scoreBonus + "";
        position.x = squadronComponent.lastKilledPosition.x;
        position.y = squadronComponent.lastKilledPosition.y;
        if (position.x < SCREEN_WIDTH) {
            position.x = SCREEN_WIDTH / 2f;
        }
        if (position.x >= SCREEN_WIDTH - 20f) {
            position.x -= 50f;
        }
        if (position.y > SCREEN_HEIGHT) {
            position.y = SCREEN_HEIGHT * 0.8f;
        }
        scoreSquadron.add(position);
        scoreSquadron.add(score);
        engine.addEntity(scoreSquadron);
        return scoreSquadron;
    }

    @Override
    public void dispose() {
        lightPool.clear();
    }

    public void addInvulnerableComponent(Entity player) {
        InvulnerableComponent invulnerableComponent = Mappers.invulnerable.get(player);
        if (invulnerableComponent != null) {
            invulnerableComponent.nbItems++;
        } else {
            player.add(engine.createComponent(InvulnerableComponent.class));
        }
    }

    public void removeInvulnerableComponent(Entity player) {
        InvulnerableComponent invulnerableComponent = Mappers.invulnerable.get(player);
        invulnerableComponent.nbItems--;
        if (invulnerableComponent.nbItems == 0) {
            player.remove(InvulnerableComponent.class);
        }
    }

}
