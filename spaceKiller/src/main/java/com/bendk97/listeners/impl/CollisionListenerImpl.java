/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.listeners.impl;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.bendk97.assets.Assets;
import com.bendk97.components.*;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.listeners.CollisionListener;
import com.bendk97.listeners.PlayerListener;
import com.bendk97.screens.levels.LevelScreen;
import com.bendk97.screens.levels.utils.ScreenShake;
import com.bendk97.timer.PausableTimer;
import com.bendk97.tweens.PositionComponentTweenAccessor;
import com.bendk97.tweens.SpriteComponentTweenAccessor;

import static com.bendk97.SpaceKillerGameConstants.*;

public class CollisionListenerImpl extends EntitySystem implements CollisionListener {

    private final Assets assets;
    private final EntityFactory entityFactory;
    private final PlayerListener playerListener;
    private final TweenManager tweenManager;
    private final LevelScreen screen;
    private final ScreenShake screenShake;

    public CollisionListenerImpl(TweenManager tweenManager, ScreenShake screenShake, Assets assets,
                                 EntityFactory entityFactory, PlayerListener playerListener,
                                 LevelScreen screen) {
        this.playerListener = playerListener;
        this.assets = assets;
        this.entityFactory = entityFactory;
        this.tweenManager = tweenManager;
        this.screen = screen;
        this.screenShake = screenShake;
    }

    @Override
    public void enemyShootByExplosion(final Entity enemy, Entity player) {
        enemyShoot(enemy, player, null);
    }

    @Override
    public void enemyShoot(final Entity enemy, final Entity player, Entity bullet) {
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(enemy);
        // special case: enemy can be already dead
        if (enemyComponent.isDead()) {
            return;
        }
        // create explosion
        assets.playSound(Assets.SOUND_EXPLOSION);

        if (enemyComponent.isBoss) {
            bossIsShoot(enemy, player, bullet);
        } else {
            enemyIsShoot(enemy, player, bullet);

        }
    }

    private void enemyIsShoot(Entity enemy, Entity player, Entity bullet) {
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(enemy);
        PositionComponent explodePosition = ComponentMapperHelper.position.get(enemy);
        entityFactory.enemyEntityFactory.createEntityExploding(explodePosition.x(), explodePosition.y());
        if (bullet != null) {
            getEngine().removeEntity(bullet);
        }
        // update score
        int nbHits = bullet != null ? 1 : HIT_EXPLOSION;
        playerListener.updateScore(player, enemy, nbHits);
        enemyComponent.hit(bullet != null ? 1 : HIT_EXPLOSION);

        if (enemyComponent.isDead()) {
            enemyIsDead(enemy, player, enemyComponent);
        } else {
            flashEnemy(enemy);
        }
    }

    private void flashEnemy(Entity enemy) {
        SpriteComponent spriteComponent = ComponentMapperHelper.sprite.get(enemy);
        spriteComponent.flashing = true;
    }

    private void enemyIsDead(Entity enemy, Entity player, EnemyComponent enemyComponent) {
        if (ComponentMapperHelper.levelFinished.get(player) == null) {
            ComponentMapperHelper.player.get(player).enemyKilled();
        }
        if (enemyComponent.isLaserShip) {
            screenShake.shake(20, 0.5f, false);
            ComponentMapperHelper.player.get(player).laserShipKilled++;
        }
        if (enemyComponent.isTank) {
            screenShake.shake(20, 0.5f, false);
        }
        screen.checkAchievements(player);
        tweenManager.killTarget(ComponentMapperHelper.position.get(enemy));
        if (enemyComponent.belongsToSquadron()) {
            ComponentMapperHelper.squadron.get(enemyComponent.squadron).removeEntity(enemy);
        }
        getEngine().removeEntity(enemy);
    }

    private void bossIsShoot(Entity boss, Entity player, Entity bullet) {
        PositionComponent impactPosition;
        EnemyComponent bossComponent = ComponentMapperHelper.enemy.get(boss);
        if (bullet != null) {
            impactPosition = ComponentMapperHelper.position.get(bullet);
        } else {
            impactPosition = ComponentMapperHelper.position.get(boss);
        }
        Entity explosion = entityFactory.enemyEntityFactory.createEntityExploding(impactPosition.x(), impactPosition.y());
        PositionComponent explosionPosition = ComponentMapperHelper.position.get(explosion);
        if (bullet != null) {
            explosionPosition.setX(explosionPosition.x() - ComponentMapperHelper.sprite.get(explosion).sprite.getWidth() / 2f);
        } else {
            explosionPosition.setX(explosionPosition.x() + ComponentMapperHelper.sprite.get(boss).sprite.getWidth() / 2f);
            explosionPosition.setY(explosionPosition.y() + ComponentMapperHelper.sprite.get(boss).sprite.getHeight() / 2f);
        }
        if (bullet != null) {
            getEngine().removeEntity(bullet);
        }
        // update score
        int nbHits = bullet != null ? 1 : HIT_EXPLOSION;
        playerListener.updateScore(player, boss, nbHits);
        checkBossHealth(boss, bullet, bossComponent);
        if (bossComponent.isDead()) {
            bossIdDead(boss, player, bossComponent);
        } else {
            flashEnemy(boss);
        }
    }

    private void checkBossHealth(Entity boss, Entity bullet, EnemyComponent bossComponent) {
        // check health of enemy
        float percentLifeBefore = bossComponent.getRemainingLifeInPercent();
        bossComponent.hit(bullet != null ? 1 : HIT_EXPLOSION);
        ComponentMapperHelper.healthBar.get(boss).setValue(bossComponent.getRemainingLifeInPercent());
        float percentLifeAfter = bossComponent.getRemainingLifeInPercent();
        if (percentLifeBefore >= 0.25 && percentLifeAfter < 0.25) {
            AnimationComponent animationComponent = ComponentMapperHelper.animation.get(boss);
            if (animationComponent != null) {
                animationComponent.tintRed(ANIMATION_MAIN, 0.99f);
            } else {
                ComponentMapperHelper.sprite.get(boss).tintRed(0.99f);
            }
        }
    }

    private void bossIdDead(Entity boss, Entity player, EnemyComponent enemyComponent) {
        assets.playSound(Assets.SOUND_BOSS_FINISHED);
        boss.remove(StatusHealthComponent.class);
        if (ComponentMapperHelper.levelFinished.get(player) == null) {
            ComponentMapperHelper.player.get(player).enemyKilled();
        }
        screen.checkAchievements(player);
        tweenManager.killTarget(ComponentMapperHelper.position.get(boss));
        ComponentMapperHelper.squadron.get(enemyComponent.squadron).removeEntity(boss);
        SpriteComponent spriteComponent = ComponentMapperHelper.sprite.get(boss);

        entityFactory.enemyEntityFactory.createBossExploding(boss);
        screenShake.shake(20, 2f, true);
        entityFactory.playerEntityFactory.addInvulnerableComponent(player);
        tweenBossDeath(boss, player, spriteComponent);
    }

    private void tweenBossDeath(Entity enemy, Entity player, SpriteComponent spriteComponent) {
        Timeline.createSequence()
                .push(Tween.to(ComponentMapperHelper.sprite.get(player), SpriteComponentTweenAccessor.ALPHA, 0.2f).target(0f))
                .push(Tween.to(ComponentMapperHelper.sprite.get(player), SpriteComponentTweenAccessor.ALPHA, 0.2f).target(1f))
                .repeat(Tween.INFINITY, 0f)
                .start(tweenManager);
        Timeline.createSequence()
                .beginParallel()
                .push(Tween.to(ComponentMapperHelper.position.get(enemy), PositionComponentTweenAccessor.POSITION_XY, 5f).ease(Linear.INOUT)
                        .target(SCREEN_WIDTH / 2f - spriteComponent.sprite.getWidth() / 2f,
                                SCREEN_HEIGHT - spriteComponent.sprite.getHeight() - 20f))

                .push(Tween.to(ComponentMapperHelper.sprite.get(enemy), SpriteComponentTweenAccessor.ALPHA, 0.2f).ease(Linear.INOUT)
                        .target(0.2f).repeatYoyo(25, 0f))
                .end()
                .setCallback((i, baseTween) -> {
                    if (i == TweenCallback.COMPLETE) {
                        getEngine().removeEntity(enemy);
                    }
                    PausableTimer.schedule(new PausableTimer.Task() {
                        @Override
                        public void run() {
                            player.add(getEngine().createComponent(LevelFinishedComponent.class));
                            PausableTimer.schedule(new PausableTimer.Task() {
                                @Override
                                public void run() {
                                    player.remove(LevelFinishedComponent.class);
                                    screen.nextLevel();
                                }
                            }, 5f);
                        }
                    }, 2f);
                })
                .start(tweenManager);
    }

    @Override
    public void playerHitByEnemyBody(Entity player) {
        assets.playSound(Assets.SOUND_EXPLOSION);
        PositionComponent playerPosition = ComponentMapperHelper.position.get(player);
        entityFactory.enemyEntityFactory.createEntityExploding(playerPosition.x(), playerPosition.y());
        playerListener.loseLive(player);
    }

    @Override
    public void playerHitByEnemyBullet(Entity player, Entity bullet) {
        assets.playSound(Assets.SOUND_EXPLOSION);
        PositionComponent playerPosition = ComponentMapperHelper.position.get(player);
        entityFactory.enemyEntityFactory.createEntityExploding(playerPosition.x(), playerPosition.y());
        getEngine().removeEntity(bullet);
        playerListener.loseLive(player);
    }

    @Override
    public void playerPowerUp(Entity player, Entity powerUp) {
        assets.playSound(Assets.SOUND_POWER_UP);
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        if (!playerComponent.powerLevel.equals(PlayerComponent.PowerLevel.TRIPLE_VERY_FAST)) {
            assets.playSound(Assets.SOUND_POWER_UP_VOICE);
        }
        playerComponent.powerUp();
        tweenManager.killTarget(ComponentMapperHelper.position.get(powerUp));
        tweenManager.killTarget(ComponentMapperHelper.sprite.get(powerUp));
        getEngine().removeEntity(powerUp);
    }


    @Override
    public void playerShieldUp(Entity player, Entity shieldUp) {
        assets.playSound(Assets.SOUND_SHIELD_UP);
        entityFactory.playerEntityFactory.createShield(player);
        tweenManager.killTarget(ComponentMapperHelper.position.get(shieldUp));
        tweenManager.killTarget(ComponentMapperHelper.sprite.get(shieldUp));
        getEngine().removeEntity(shieldUp);
    }

    @Override
    public void playerBombUp(Entity player, Entity bombUp) {
        assets.playSound(Assets.SOUND_SHIELD_UP);
        playerListener.newBombObtained(player);
        tweenManager.killTarget(ComponentMapperHelper.position.get(bombUp));
        tweenManager.killTarget(ComponentMapperHelper.sprite.get(bombUp));
        getEngine().removeEntity(bombUp);
    }


    @Override
    public void bulletStoppedByShield(Entity bullet) {
        assets.playSound(Assets.SOUND_SHIELD_BULLET);
        getEngine().removeEntity(bullet);
    }

    @Override
    public void enemyShootByShield(Entity enemy) {
        assets.playSound(Assets.SOUND_EXPLOSION);
        PositionComponent enemyPosition = ComponentMapperHelper.position.get(enemy);
        entityFactory.enemyEntityFactory.createEntityExploding(enemyPosition.x(), enemyPosition.y());
        tweenManager.killTarget(ComponentMapperHelper.position.get(enemy));
        if (ComponentMapperHelper.enemy.get(enemy).squadron != null) {
            ComponentMapperHelper.squadron.get(ComponentMapperHelper.enemy.get(enemy).squadron).removeEntity(enemy);
        }
        getEngine().removeEntity(enemy);
    }

}
