/*
 * Developed by Benjamin Lefèvre
 * Last modified 11/10/18 22:32
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.enemies;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bendk97.components.*;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.entities.EntityFactoryIds;
import com.bendk97.screens.levels.Level;
import com.bendk97.timer.PausableTimer;

import java.util.Random;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;
import static com.bendk97.assets.GameAssets.SOUND_FIRE_ENEMY;
import static com.bendk97.components.helpers.ComponentMapperHelper.*;
import static com.bendk97.entities.EntityFactoryIds.ENEMY_FIRE_CIRCLE;
import static com.bendk97.pools.GamePools.poolSprite;
import static com.bendk97.pools.GamePools.poolVector2;
import static com.bendk97.screens.levels.Level.Level1;
import static com.bendk97.screens.levels.Level.Level3;
import static com.bendk97.tweens.SpriteComponentTweenAccessor.ALPHA;

public class EnemyActionEntityFactory {

    private final EntityFactory entityFactory;
    private final TweenManager tweenManager;
    private final Random random = new RandomXS128();

    public EnemyActionEntityFactory(EntityFactory entityFactory, TweenManager tweenManager) {
        this.entityFactory = entityFactory;
        this.tweenManager = tweenManager;
    }


    public void createEnemyFire(Entity enemy, Entity player) {
        switch (ComponentMapperHelper.enemy.get(enemy).attackType) {
            case EntityFactoryIds.ENEMY_FIRE_LASER:
                createEnemyFireLaser(enemy);
                break;
            case ENEMY_FIRE_CIRCLE:
            default:
                createEnemyFireCircle(enemy, player);
                break;
        }
    }

    private void createEnemyFireLaser(Entity enemy) {
        PositionComponent position = ComponentMapperHelper.position.get(enemy);
        Sprite sprite = ComponentMapperHelper.sprite.get(enemy).sprite;
        createEnemyFireLaser(position.x() + sprite.getWidth() / 2f, position.y(), ComponentMapperHelper.enemy.get(enemy).bulletVelocity);
    }

    private void createEnemyFireLaser(float posX, float posY, float velocity) {
        entityFactory.assets.playSound(SOUND_FIRE_ENEMY);
        Entity bullet = entityFactory.engine.createEntity();
        bullet.add(entityFactory.engine.createComponent(EnemyBulletComponent.class));
        PositionComponent positionComponent = entityFactory.engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = entityFactory.engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        spriteComponent.sprite = poolSprite.getSprite(entityFactory.levelAtlas.findRegion("laser"));
        spriteComponent.pixelPerfectCollision = true;
        bullet.add(spriteComponent);
        RemovableComponent removableComponent = entityFactory.engine.createComponent(RemovableComponent.class);
        removableComponent.setDuration(2f);
        bullet.add(removableComponent);
        entityFactory.engine.addEntity(bullet);
        positionComponent.setXY(posX - spriteComponent.sprite.getWidth() / 2f, posY - spriteComponent.sprite.getHeight() / 2f);

        Vector2 directionBullet = poolVector2.getVector2(0f, -1);
        directionBullet.scl(velocity);
        velocityComponent.x = 0;
        velocityComponent.y = directionBullet.y;
        poolVector2.free(directionBullet);
    }


    private void createEnemyFireCircle(Entity enemy, Entity player) {
        entityFactory.assets.playSound(SOUND_FIRE_ENEMY);
        Entity bullet = createEnemyBullet();
        entityFactory.engine.addEntity(bullet);
        PositionComponent playerPosition = position.get(player);
        PositionComponent enemyPosition = position.get(enemy);
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(enemy);
        PositionComponent bulletPosition = position.get(bullet);
        VelocityComponent bulletVelocity = velocity.get(bullet);
        SpriteComponent bulletSprite = sprite.get(bullet);
        if (enemyComponent.isBoss) {
            bulletPosition.setX(enemyPosition.x() + sprite.get(enemy).sprite.getWidth() / 2f - bulletSprite.sprite.getWidth() / 2f);
            bulletPosition.setY(enemyPosition.y() + sprite.get(enemy).sprite.getHeight() * 3f / 4f);
        } else if (enemyComponent.isTank) {
            Sprite tank = sprite.get(enemy).sprite;
            float rotation = tank.getRotation();
            Vector2 pos = poolVector2.getVector2(
                    enemyPosition.x() + sprite.get(enemy).sprite.getWidth() / 2f - bulletSprite.sprite.getWidth() / 2f,
                    enemyPosition.y() + 32f);
            Vector2 angle = poolVector2.getVector2(0, -1);
            Vector2 scaler = poolVector2.getVector2(32, 32);
            angle.rotate(rotation);
            angle.nor();
            angle.scl(scaler);
            angle.add(pos);
            bulletPosition.setXY(angle.x, angle.y);
            poolVector2.free(angle, scaler, pos);
        } else {
            bulletPosition.setX(enemyPosition.x() + sprite.get(enemy).sprite.getWidth() / 2f - bulletSprite.sprite.getWidth() / 2f);
            bulletPosition.setY(enemyPosition.y() + sprite.get(enemy).sprite.getHeight());
        }
        Vector2 directionBullet = poolVector2.getVector2(playerPosition.x() - bulletPosition.x(), playerPosition.y() - bulletPosition.y());
        directionBullet.nor();
        if (!enemyComponent.isTank) {
            directionBullet.rotate(-10 + random.nextFloat() * 20f);
        }
        directionBullet.scl(ComponentMapperHelper.enemy.get(enemy).bulletVelocity);
        bulletVelocity.x = directionBullet.x;
        bulletVelocity.y = directionBullet.y;
        poolVector2.free(directionBullet);
    }

    public void createBossFire(final Entity boss, final Entity player) {
        int type = random.nextInt(3);
        Level level = ComponentMapperHelper.player.get(player).level;
        if (type == 0) {
            final int bullets = level.equals(Level3) ? 20 : 10;
            float delay = level.equals(Level3) ? 0.1f : 0.2f;
            for (int i = 0; i < bullets; ++i) {
                PausableTimer.schedule(new PausableTimer.Task() {
                    @Override
                    public void run() {
                        createEnemyFire(boss, player);
                    }
                }, 0f + delay * i);
            }
        } else if (type == 1 || level.equals(Level1)
                || position.get(boss).x() < 0 || position.get(boss).x() > SCREEN_WIDTH * 3f / 4f) {
            createBossFireCircle(boss, false);
            if (level.equals(Level3)) {
                createBossFireCircle(boss, true);
            }
        } else {
            PositionComponent position = ComponentMapperHelper.position.get(boss);
            BossComponent bossComponent = ComponentMapperHelper.boss.get(boss);
            createEnemyFireLaser(position.x() + 160f, position.y() + 170f, bossComponent.velocityFire2);
            createEnemyFireLaser(position.x() + 195f, position.y() + 170f, bossComponent.velocityFire2);
            if (level.equals(Level3)) {
                createEnemyFireLaser(position.x() + 160f, position.y() + 170f, -bossComponent.velocityFire2);
                createEnemyFireLaser(position.x() + 195f, position.y() + 170f, -bossComponent.velocityFire2);

            }
        }
    }

    public void createBossFire2(Entity boss) {
        PositionComponent position = ComponentMapperHelper.position.get(boss);
        BossComponent bossComponent = ComponentMapperHelper.boss.get(boss);
        createEnemyFireLaser(position.x() + 12f, position.y() + 186f, bossComponent.velocityFire2);
        createEnemyFireLaser(position.x() + 342f, position.y() + 186f, bossComponent.velocityFire2);
    }


    private void createBossFireCircle(Entity boss, boolean yUp) {
        Array<Entity> bullets = new Array<>(10);
        entityFactory.assets.playSound(SOUND_FIRE_ENEMY);
        for (int i = 0; i < 12; ++i) {
            Entity bullet = createEnemyBullet();
            PositionComponent bulletPosition = position.get(bullet);
            PositionComponent enemyPosition = position.get(boss);
            bulletPosition.setX(enemyPosition.x() + sprite.get(boss).sprite.getWidth() / 2f
                    - sprite.get(bullet).sprite.getWidth() / 2f);
            bulletPosition.setY(enemyPosition.y() + sprite.get(boss).sprite.getHeight() / 4f);

            bullets.add(bullet);
            entityFactory.engine.addEntity(bullet);
        }
        float rotation = yUp ? 35f : -35f;
        for (int i = 0; i < bullets.size; ++i) {
            Vector2 directionBullet = poolVector2.getVector2(1f, 0f);
            directionBullet.setAngle(rotation);
            rotation -= yUp ? -10f : 10f;
            directionBullet.scl(ComponentMapperHelper.boss.get(boss).velocityFire1);
            VelocityComponent velocityComponent = ComponentMapperHelper.velocity.get(bullets.get(i));
            velocityComponent.x = directionBullet.x;
            velocityComponent.y = directionBullet.y;
            poolVector2.free(directionBullet);
        }
    }

    private Entity createEnemyBullet() {
        Entity bullet = entityFactory.engine.createEntity();
        bullet.add(entityFactory.engine.createComponent(EnemyBulletComponent.class));
        bullet.add(entityFactory.engine.createComponent(PositionComponent.class));
        bullet.add(entityFactory.engine.createComponent(VelocityComponent.class));
        SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        spriteComponent.sprite = poolSprite.getSprite(entityFactory.levelAtlas.findRegion("bulletEnemy"));
        bullet.add(spriteComponent);
        Timeline.createSequence()
                .push(Tween.to(spriteComponent, ALPHA, 0.09f).target(0.5f))
                .push(Tween.to(spriteComponent, ALPHA, 0.09f).target(1f))
                .repeat(Tween.INFINITY, 0f)
                .start(tweenManager);
        RemovableComponent removableComponent = entityFactory.engine.createComponent(RemovableComponent.class);
        removableComponent.setDuration(5.0f);
        bullet.add(removableComponent);
        return bullet;
    }
}
