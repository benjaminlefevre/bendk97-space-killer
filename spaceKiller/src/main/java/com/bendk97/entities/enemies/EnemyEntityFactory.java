/*
 * Developed by Benjamin Lefèvre
 * Last modified 11/10/18 23:28
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.enemies;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;
import com.bendk97.components.*;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.screens.levels.Level;
import com.bendk97.timer.PausableTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP_PINGPONG;
import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.Assets.SOUND_EXPLOSION;
import static com.bendk97.entities.EntityFactoryIds.*;

public class EnemyEntityFactory {

    private final EntityFactory entityFactory;
    public SoloEnemyFactory soloEnemyFactory;
    public SquadronFactory squadronFactory;
    private final Random random = new RandomXS128();


    public EnemyEntityFactory(EntityFactory entityFactory, Level level, OrthographicCamera camera) {
        this.entityFactory = entityFactory;
        this.soloEnemyFactory = new SoloEnemyFactory(entityFactory, level);
        this.squadronFactory = new SquadronFactory(entityFactory, camera);
    }

    protected Entity createLaserShip(int type, Float velocity, float bulletVelocity, int rateShoot, int gaugeLife, int points, boolean fromLeft) {
        String atlasRegion;
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
        Entity enemy = entityFactory.engine.createEntity();
        entityFactory.engine.addEntity(enemy);
        PositionComponent positionComponent = entityFactory.engine.createComponent(PositionComponent.class);
        enemy.add(positionComponent);
        if (velocity != null) {
            VelocityComponent velocityComponent = entityFactory.engine.createComponent(VelocityComponent.class);
            velocityComponent.x = velocity;
            enemy.add(velocityComponent);
            FollowPlayerComponent followPlayerComponent = entityFactory.engine.createComponent(FollowPlayerComponent.class);
            enemy.add(followPlayerComponent);
            followPlayerComponent.velocity = velocity;

        }
        AnimationComponent animationComponent = entityFactory.engine.createComponent(AnimationComponent.class);
        Array<Sprite> sprites = entityFactory.levelAtlas.createSprites(atlasRegion);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<>(FRAME_DURATION, sprites, LOOP_PINGPONG));
        enemy.add(animationComponent);
        SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        enemy.add(spriteComponent);
        spriteComponent.sprite = sprites.get(0);
        spriteComponent.pixelPerfectCollision = true;
        spriteComponent.zIndex = 20;
        positionComponent.x = fromLeft ? -spriteComponent.sprite.getWidth() : SCREEN_WIDTH;
        positionComponent.y = SCREEN_HEIGHT - spriteComponent.sprite.getHeight();
        EnemyComponent enemyComponent = entityFactory.engine.createComponent(EnemyComponent.class);
        enemyComponent.initLifeGauge(gaugeLife);
        enemyComponent.points = points;
        enemyComponent.bulletVelocity = bulletVelocity;
        enemyComponent.attackCapacity = Integer.MAX_VALUE;
        enemyComponent.probabilityAttack = rateShoot;
        enemyComponent.attackType = ENEMY_FIRE_LASER;
        enemyComponent.isLaserShip = true;
        enemy.add(enemyComponent);
        enemy.add(entityFactory.engine.createComponent(StateComponent.class));
        return enemy;
    }


    public List<Entity> createTank(TankComponent.TankLevel level, int gauge, int points) {
        List<Entity> entities = new ArrayList<>();
        Entity tankCannon = entityFactory.engine.createEntity();
        final EnemyComponent enemyComponent = entityFactory.engine.createComponent(EnemyComponent.class);
        enemyComponent.points = points;
        enemyComponent.isTank = true;
        enemyComponent.initLifeGauge(gauge);
        enemyComponent.probabilityAttack = 1;
        enemyComponent.bulletVelocity = level.bulletVelocity;
        enemyComponent.attackCapacity = 0;
        tankCannon.add(enemyComponent);
        PositionComponent position = entityFactory.engine.createComponent(PositionComponent.class);
        tankCannon.add(position);
        SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.sprite = entityFactory.levelAtlas.createSprite("tankCannon");
        component.sprite.setOrigin(32f, 46f);
        component.zIndex = -5;
        tankCannon.add(component);
        tankCannon.add(entityFactory.engine.createComponent(GroundEnemyComponent.class));
        FollowPlayerComponent followPlayerComponent = entityFactory.engine.createComponent(FollowPlayerComponent.class);
        followPlayerComponent.rotate = true;
        tankCannon.add(followPlayerComponent);
        TankComponent tankComponent = entityFactory.engine.createComponent(TankComponent.class);
        tankComponent.setLevel(level);
        tankCannon.add(tankComponent);
        entityFactory.engine.addEntity(tankCannon);
        Entity tankBody = entityFactory.engine.createEntity();
        tankBody.add(entityFactory.engine.createComponent(PositionComponent.class));
        SpriteComponent sprite = entityFactory.engine.createComponent(SpriteComponent.class);
        sprite.pixelPerfectCollision = true;
        sprite.sprite = entityFactory.levelAtlas.createSprite("tankBody");
        sprite.zIndex = -6;
        tankBody.add(sprite);
        tankBody.add(entityFactory.engine.createComponent(GroundEnemyComponent.class));
        entityFactory.engine.addEntity(tankBody);

        entities.add(tankBody);
        entities.add(tankCannon);
        return entities;
    }

    private Entity createEnemy(Entity squadron, EnemyCharacteristics characteristics, float frameDuration, Animation.PlayMode animationType) {
        Entity enemy = entityFactory.engine.createEntity();
        EnemyComponent enemyComponent = entityFactory.engine.createComponent(EnemyComponent.class);
        enemyComponent.points = characteristics.points;
        enemyComponent.initLifeGauge(characteristics.strength);
        enemyComponent.probabilityAttack = characteristics.rateShoot;
        enemyComponent.bulletVelocity = characteristics.velocityBullet;
        enemyComponent.attackCapacity = characteristics.canAttack ? characteristics.attackCapacity : 0;
        if (squadron != null) {
            enemyComponent.squadron = squadron;
        }
        enemy.add(enemyComponent);
        PositionComponent position = entityFactory.engine.createComponent(PositionComponent.class);
        enemy.add(position);
        AnimationComponent animationComponent = entityFactory.engine.createComponent(AnimationComponent.class);
        Array<Sprite> sprites = entityFactory.levelAtlas.createSprites(characteristics.atlasName);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<>(frameDuration, sprites, animationType));
        enemy.add(animationComponent);
        SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.sprite = sprites.get(0);
        enemy.add(component);
        enemy.add(entityFactory.engine.createComponent(StateComponent.class));
        entityFactory.engine.addEntity(enemy);
        return enemy;
    }

    public Entity createEnemySoucoupe(Entity squadron, boolean canAttack, float velocityBullet) {
        EnemyCharacteristics characteristics = new EnemyCharacteristics()
                .setAtlasName("soucoupe")
                .setCanAttack(canAttack)
                .setRateShoot(STANDARD_RATE_SHOOT)
                .setVelocityBullet(velocityBullet)
                .setPoints(100)
                .setStrength(1)
                .setAttackCapacity(1);

        return createEnemy(squadron, characteristics, FRAME_DURATION, LOOP);
    }


    public Entity createEnemyShip(Entity squadron, boolean canAttack, float velocityBullet, int rateShoot, int enemyType) {
        String atlasRegion;
        int points = 200;
        int strength = 1;
        int attackCapacity = 1;
        Animation.PlayMode playMode = LOOP;
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
        EnemyCharacteristics characteristics = new EnemyCharacteristics()
                .setAtlasName(atlasRegion)
                .setCanAttack(canAttack)
                .setRateShoot(rateShoot)
                .setVelocityBullet(velocityBullet)
                .setPoints(points)
                .setStrength(strength)
                .setAttackCapacity(attackCapacity);
        return createEnemy(squadron, characteristics, frameDuration, playMode);
    }


    public Entity createBoss(Entity squadron, float velocityBullet, float velocityCircle) {
        Entity boss = entityFactory.engine.createEntity();
        BossComponent bossComponent = entityFactory.engine.createComponent(BossComponent.class);
        bossComponent.velocityFire1 = velocityCircle;
        boss.add(bossComponent);
        addStatusHealthBarTo(boss);
        EnemyComponent enemyComponent = entityFactory.engine.createComponent(EnemyComponent.class);
        enemyComponent.points = 50;
        enemyComponent.isBoss = true;
        enemyComponent.initLifeGauge(BOSS_LEVEL1_GAUGE);
        enemyComponent.bulletVelocity = velocityBullet;
        enemyComponent.attackCapacity = Integer.MAX_VALUE;
        if (squadron != null) {
            enemyComponent.squadron = squadron;
        }
        boss.add(enemyComponent);
        PositionComponent position = entityFactory.engine.createComponent(PositionComponent.class);
        boss.add(position);
        SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        spriteComponent.pixelPerfectCollision = true;
        spriteComponent.sprite = entityFactory.levelAtlas.createSprite("boss-level1");
        boss.add(spriteComponent);
        entityFactory.engine.addEntity(boss);
        PausableTimer.schedule(new PausableTimer.Task() {
            @Override
            public void run() {
                ComponentMapperHelper.boss.get(boss).pleaseFire1 = true;
            }
        }, 5f);
        return boss;
    }

    public Entity createBoss2(Entity squadron, float velocityBullet, float velocityFireCircle, float velocityBullet2) {
        Entity boss = entityFactory.engine.createEntity();
        BossComponent bossComponent = entityFactory.engine.createComponent(BossComponent.class);
        boss.add(bossComponent);
        bossComponent.minTriggerFire1 = 4;
        bossComponent.minTriggerFire2 = 7;
        bossComponent.velocityFire1 = velocityFireCircle;
        bossComponent.velocityFire2 = velocityBullet2;
        addStatusHealthBarTo(boss);
        EnemyComponent enemyComponent = entityFactory.engine.createComponent(EnemyComponent.class);
        enemyComponent.points = 50;
        enemyComponent.isBoss = true;
        enemyComponent.initLifeGauge(BOSS_LEVEL2_GAUGE);
        enemyComponent.bulletVelocity = velocityBullet;
        enemyComponent.attackCapacity = Integer.MAX_VALUE;
        if (squadron != null) {
            enemyComponent.squadron = squadron;
        }
        boss.add(enemyComponent);
        PositionComponent position = entityFactory.engine.createComponent(PositionComponent.class);
        boss.add(position);
        SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        spriteComponent.sprite = entityFactory.levelAtlas.createSprite("boss");
        spriteComponent.pixelPerfectCollision = true;
        boss.add(spriteComponent);
        entityFactory.engine.addEntity(boss);
        PausableTimer.schedule(new PausableTimer.Task() {
            @Override
            public void run() {
                ComponentMapperHelper.boss.get(boss).pleaseFire1 = true;
            }
        }, 5f);
        PausableTimer.schedule(new PausableTimer.Task() {
            @Override
            public void run() {
                ComponentMapperHelper.boss.get(boss).pleaseFire2 = true;
            }
        }, 2f);

        return boss;
    }

    public Entity createBoss3(Entity squadron, float velocityBullet, float velocityBulletFireCircle, float velocityBullet2) {
        Entity boss = entityFactory.engine.createEntity();
        BossComponent bossComponent = entityFactory.engine.createComponent(BossComponent.class);
        boss.add(bossComponent);
        bossComponent.minTriggerFire1 = 3;
        bossComponent.minTriggerFire2 = 7;
        bossComponent.velocityFire1 = velocityBulletFireCircle;
        bossComponent.velocityFire2 = velocityBullet2;
        addStatusHealthBarTo(boss);
        EnemyComponent enemyComponent = entityFactory.engine.createComponent(EnemyComponent.class);
        enemyComponent.points = 50;
        enemyComponent.isBoss = true;
        enemyComponent.initLifeGauge(BOSS_LEVEL3_GAUGE);
        enemyComponent.bulletVelocity = velocityBullet;
        enemyComponent.attackCapacity = Integer.MAX_VALUE;
        if (squadron != null) {
            enemyComponent.squadron = squadron;
        }
        boss.add(enemyComponent);
        PositionComponent position = entityFactory.engine.createComponent(PositionComponent.class);
        boss.add(position);

        AnimationComponent animationComponent = entityFactory.engine.createComponent(AnimationComponent.class);
        Array<Sprite> sprites = entityFactory.levelAtlas.createSprites("boss3");
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<>(0.075f, (Sprite[]) sprites.toArray(Sprite.class)));
        animationComponent.animations.get(ANIMATION_MAIN).setPlayMode(LOOP_PINGPONG);
        boss.add(animationComponent);

        SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        spriteComponent.sprite = sprites.get(0);
        spriteComponent.pixelPerfectCollision = true;
        boss.add(spriteComponent);
        entityFactory.engine.addEntity(boss);
        PausableTimer.schedule(new PausableTimer.Task() {
            @Override
            public void run() {
                ComponentMapperHelper.boss.get(boss).pleaseFire1 = true;
            }
        }, 5f);
        boss.add(entityFactory.engine.createComponent(StateComponent.class));
        return boss;
    }

    private void addStatusHealthBarTo(Entity boss) {
        StatusHealthComponent statusBar = entityFactory.engine.createComponent(StatusHealthComponent.class);
        statusBar.setBounds(SCREEN_WIDTH / 2f - StatusHealthComponent.WIDTH / 2f, 12);
        boss.add(statusBar);
    }


    public Entity createAsteroid(Entity squadron, int asteroid) {
        Entity enemy = entityFactory.engine.createEntity();
        EnemyComponent enemyComponent = entityFactory.engine.createComponent(EnemyComponent.class);
        enemyComponent.points = 50;
        if (squadron != null) {
            enemyComponent.squadron = squadron;
        }
        enemy.add(enemyComponent);
        PositionComponent position = entityFactory.engine.createComponent(PositionComponent.class);
        enemy.add(position);
        AnimationComponent animationComponent = entityFactory.engine.createComponent(AnimationComponent.class);
        String asteroidSprite = asteroid == ASTEROID_1 ? "asteroid" : "asteroid2";
        Array<Sprite> sprites = entityFactory.levelAtlas.createSprites(asteroidSprite);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<>(FRAME_DURATION, sprites, LOOP));
        enemy.add(animationComponent);
        SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.sprite = sprites.get(0);
        component.zIndex = -99;
        enemy.add(component);
        enemy.add(entityFactory.engine.createComponent(StateComponent.class));
        entityFactory.engine.addEntity(enemy);
        return enemy;
    }


    public Array<Entity> createHouse(Entity squadron, int houseType) {
        Array<Entity> entities = new Array<>();
        Entity house = entityFactory.engine.createEntity();
        EnemyComponent enemyComponent = entityFactory.engine.createComponent(EnemyComponent.class);
        enemyComponent.points = 50;
        if (squadron != null) {
            enemyComponent.squadron = squadron;
        }
        house.add(enemyComponent);
        house.add(entityFactory.engine.createComponent(PositionComponent.class));
        house.add(entityFactory.engine.createComponent(GroundEnemyComponent.class));
        SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.sprite = entityFactory.levelAtlas.createSprite("house-" + (houseType - HOUSE_1 + 1));
        component.zIndex = -10;
        house.add(component);
        entityFactory.engine.addEntity(house);

        Entity houseDestroyed = entityFactory.engine.createEntity();
        houseDestroyed.add(entityFactory.engine.createComponent(PositionComponent.class));
        houseDestroyed.add(entityFactory.engine.createComponent(GroundEnemyComponent.class));
        component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.sprite = entityFactory.levelAtlas.createSprite("house-" + (houseType - HOUSE_1 + 1) + "_destroyed");
        component.zIndex = -11;
        houseDestroyed.add(component);
        entityFactory.engine.addEntity(houseDestroyed);

        entities.add(house);
        entities.add(houseDestroyed);
        return entities;
    }

    public Entity createEntityExploding(float x, float y) {
        Entity explosion = entityFactory.engine.createEntity();
        PositionComponent position = entityFactory.engine.createComponent(PositionComponent.class);
        explosion.add(position);
        position.setPosition(x, y);
        AnimationComponent animationComponent = entityFactory.engine.createComponent(AnimationComponent.class);
        Array<Sprite> sprites = entityFactory.commonAtlas.createSprites("explosion");
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<>(FRAME_DURATION_EXPLOSION, sprites, Animation.PlayMode.NORMAL));
        explosion.add(animationComponent);
        SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.sprite = animationComponent.animations.get(ANIMATION_MAIN).getKeyFrame(0);
        component.zIndex = 100;
        explosion.add(component);
        explosion.add(entityFactory.engine.createComponent(StateComponent.class));
        entityFactory.engine.addEntity(explosion);
        //
        if (entityFactory.rayHandler != null) {
            entityFactory.createLight(explosion);
        }
        return explosion;
    }

    public void createBossExploding(final Entity enemy) {
        final SpriteComponent sprite = ComponentMapperHelper.sprite.get(enemy);
        for (int i = 0; i < 50; ++i) {
            PausableTimer.schedule(new PausableTimer.Task() {
                @Override
                public void run() {
                    entityFactory.assets.playSound(SOUND_EXPLOSION);
                    PositionComponent position = ComponentMapperHelper.position.get(enemy);
                    if (position != null) {
                        createEntityExploding(position.x + random.nextFloat() * sprite.sprite.getWidth(),
                                position.y + random.nextFloat() * sprite.sprite.getHeight());
                    }
                }
            }, i * 0.1f);
        }
        if (entityFactory.rayHandler != null) {
            entityFactory.createLight(enemy, new Color(0.7f, 0f, 0f, 0.4f), sprite.sprite.getHeight() * 20f);
        }
    }


    public Entity createSquadron(boolean powerUp, boolean displayScoreBonus, int bonus) {
        Entity squadron = entityFactory.engine.createEntity();
        SquadronComponent squadronComponent = entityFactory.engine.createComponent(SquadronComponent.class);
        squadronComponent.powerUpAfterDestruction = powerUp;
        squadronComponent.displayBonusSquadron = displayScoreBonus;
        squadronComponent.scoreBonus = bonus;
        squadron.add(squadronComponent);
        entityFactory.engine.addEntity(squadron);
        return squadron;
    }

    public Entity createScoreSquadron(Entity squadron) {
        Entity scoreSquadron = entityFactory.engine.createEntity();
        ScoreSquadronComponent score = entityFactory.engine.createComponent(ScoreSquadronComponent.class);
        PositionComponent position = entityFactory.engine.createComponent(PositionComponent.class);
        SquadronComponent squadronComponent = ComponentMapperHelper.squadron.get(squadron);
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
        entityFactory.engine.addEntity(scoreSquadron);
        return scoreSquadron;
    }


    public void setPlayer(Entity player) {
        this.soloEnemyFactory.setPlayer(player);
    }
}
