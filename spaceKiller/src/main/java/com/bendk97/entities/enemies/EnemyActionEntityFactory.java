/*
 * Developed by Benjamin Lefèvre
 * Last modified 11/10/18 22:32
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.enemies;

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
import static com.bendk97.assets.Assets.SOUND_FIRE_ENEMY;
import static com.bendk97.components.helpers.ComponentMapperHelper.position;
import static com.bendk97.components.helpers.ComponentMapperHelper.sprite;
import static com.bendk97.entities.EntityFactoryIds.ENEMY_FIRE_CIRCLE;
import static com.bendk97.screens.levels.Level.Level1;
import static com.bendk97.screens.levels.Level.Level3;

public class EnemyActionEntityFactory {
    
    private final EntityFactory entityFactory;
    private final Random random = new RandomXS128();

    public EnemyActionEntityFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
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
        createEnemyFireLaser(position.x + sprite.getWidth() / 2f, position.y, ComponentMapperHelper.enemy.get(enemy).bulletVelocity);
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
        spriteComponent.sprite = new Sprite(entityFactory.atlasMask.findRegion("laser"));
        bullet.add(spriteComponent);
        bullet.add(entityFactory.engine.createComponent(RemovableComponent.class));
        entityFactory.engine.addEntity(bullet);
        positionComponent.x = posX - spriteComponent.sprite.getWidth() / 2f;
        positionComponent.y = posY - spriteComponent.sprite.getHeight() / 2f;

        Vector2 directionBullet = new Vector2(0f, -1);
        directionBullet.scl(velocity);
        velocityComponent.x = 0;
        velocityComponent.y = directionBullet.y;
    }


    private void createEnemyFireCircle(Entity enemy, Entity player) {
        entityFactory.assets.playSound(SOUND_FIRE_ENEMY);
        Entity bullet = entityFactory.engine.createEntity();
        bullet.add(entityFactory.engine.createComponent(EnemyBulletComponent.class));
        PositionComponent positionComponent = entityFactory.engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = entityFactory.engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        spriteComponent.sprite = new Sprite(entityFactory.atlasMask.findRegion("bulletEnemy"));
        bullet.add(spriteComponent);
        bullet.add(entityFactory.engine.createComponent(RemovableComponent.class));
        entityFactory.engine.addEntity(bullet);
        PositionComponent playerPosition = position.get(player);
        PositionComponent enemyPosition = position.get(enemy);
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(enemy);
        if (enemyComponent.isBoss) {
            positionComponent.x = enemyPosition.x + ComponentMapperHelper.sprite.get(enemy).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f;
            positionComponent.y = enemyPosition.y + sprite.get(enemy).sprite.getHeight() * 3f / 4f;
        } else if (enemyComponent.isTank) {
            Sprite tank = sprite.get(enemy).sprite;
            float rotation = tank.getRotation();
            Vector2 pos = new Vector2(enemyPosition.x + ComponentMapperHelper.sprite.get(enemy).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f,
                    enemyPosition.y + 32f);
            Vector2 angle = new Vector2(0, -1);
            angle.rotate(rotation);
            angle.nor();
            angle.scl(new Vector2(32, 32));
            angle.add(pos);
            positionComponent.x = angle.x;
            positionComponent.y = angle.y;
        } else {
            positionComponent.x = enemyPosition.x + ComponentMapperHelper.sprite.get(enemy).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f;
            positionComponent.y = enemyPosition.y + sprite.get(enemy).sprite.getHeight();
        }
        Vector2 directionBullet = new Vector2(playerPosition.x - positionComponent.x, playerPosition.y - positionComponent.y);
        directionBullet.nor();
        if (!enemyComponent.isTank) {
            directionBullet.rotate(-10 + random.nextFloat() * 20f);
        }
        directionBullet.scl(ComponentMapperHelper.enemy.get(enemy).bulletVelocity);
        velocityComponent.x = directionBullet.x;
        velocityComponent.y = directionBullet.y;
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
                || ComponentMapperHelper.position.get(boss).x < 0 || ComponentMapperHelper.position.get(boss).x > SCREEN_WIDTH * 3f / 4f) {
            createBossFireCircle(boss, false);
            if (level.equals(Level3)) {
                createBossFireCircle(boss, true);
            }
        } else {
            PositionComponent position = ComponentMapperHelper.position.get(boss);
            BossComponent bossComponent = ComponentMapperHelper.boss.get(boss);
            createEnemyFireLaser(position.x + 160f, position.y + 170f, bossComponent.velocityFire2);
            createEnemyFireLaser(position.x + 195f, position.y + 170f, bossComponent.velocityFire2);
            if (level.equals(Level3)) {
                createEnemyFireLaser(position.x + 160f, position.y + 170f, -bossComponent.velocityFire2);
                createEnemyFireLaser(position.x + 195f, position.y + 170f, -bossComponent.velocityFire2);

            }
        }
    }

    public void createBossFire2(Entity boss) {
        PositionComponent position = ComponentMapperHelper.position.get(boss);
        BossComponent bossComponent = ComponentMapperHelper.boss.get(boss);
        createEnemyFireLaser(position.x + 12f, position.y + 186f, bossComponent.velocityFire2);
        createEnemyFireLaser(position.x + 342f, position.y + 186f, bossComponent.velocityFire2);
    }

    private void createBossFireCircle(Entity boss, boolean yUp) {
        Array<Entity> bullets = new Array<>(10);
        entityFactory.assets.playSound(SOUND_FIRE_ENEMY);
        for (int i = 0; i < 12; ++i) {
            Entity bullet = entityFactory.engine.createEntity();

            bullet.add(entityFactory.engine.createComponent(EnemyBulletComponent.class));

            PositionComponent positionComponent = entityFactory.engine.createComponent(PositionComponent.class);
            bullet.add(positionComponent);

            VelocityComponent velocityComponent = entityFactory.engine.createComponent(VelocityComponent.class);
            bullet.add(velocityComponent);

            SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
            spriteComponent.sprite = new Sprite(entityFactory.atlasMask.findRegion("bulletEnemy"));
            bullet.add(spriteComponent);

            bullet.add(entityFactory.engine.createComponent(RemovableComponent.class));

            PositionComponent enemyPosition = position.get(boss);
            positionComponent.x = enemyPosition.x + ComponentMapperHelper.sprite.get(boss).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f;
            positionComponent.y = enemyPosition.y + ComponentMapperHelper.sprite.get(boss).sprite.getHeight() / 4f;

            bullets.add(bullet);
            entityFactory.engine.addEntity(bullet);
        }
        float rotation = yUp ? 35f : -35f;
        for (int i = 0; i < bullets.size; ++i) {
            Vector2 directionBullet = new Vector2(1f, 0f);
            directionBullet.setAngle(rotation);
            rotation -= yUp ? -10f : 10f;
            directionBullet.scl(ComponentMapperHelper.boss.get(boss).velocityFire1);
            VelocityComponent velocityComponent = ComponentMapperHelper.velocity.get(bullets.get(i));
            velocityComponent.x = directionBullet.x;
            velocityComponent.y = directionBullet.y;
        }
    }
}
