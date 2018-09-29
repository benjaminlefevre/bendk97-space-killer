/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.listeners.impl;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.bendk97.components.Mappers;
import com.bendk97.tweens.PositionComponentAccessor;
import com.bendk97.tweens.SpriteComponentAccessor;

import static com.bendk97.SpaceKillerGameConstants.*;

public class CollisionListenerImpl extends EntitySystem implements com.bendk97.listeners.CollisionListener {

    private com.bendk97.assets.Assets assets;
    private com.bendk97.entities.EntityFactory entityFactory;
    private com.bendk97.listeners.PlayerListener playerListener;
    private TweenManager tweenManager;
    private com.bendk97.screens.LevelScreen screen;
    private com.bendk97.screens.ScreenShake screenShake;

    public CollisionListenerImpl(TweenManager tweenManager, com.bendk97.screens.ScreenShake screenShake, com.bendk97.assets.Assets assets, com.bendk97.entities.EntityFactory entityFactory, com.bendk97.listeners.PlayerListener playerListener,
                                 com.bendk97.screens.LevelScreen screen) {
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
        com.bendk97.components.EnemyComponent enemyComponent = com.bendk97.components.Mappers.enemy.get(enemy);
        // special case: enemy can be already dead
        if (enemyComponent.isDead()) {
            return;
        }
        // create explosion
        assets.playSound(com.bendk97.assets.Assets.SOUND_EXPLOSION);
        com.bendk97.components.PositionComponent explosePosition;
        if (enemyComponent.isBoss && bullet != null) {
            explosePosition = com.bendk97.components.Mappers.position.get(bullet);

        } else {
            explosePosition = com.bendk97.components.Mappers.position.get(enemy);
        }
        Entity explosion = entityFactory.createEntityExploding(explosePosition.x, explosePosition.y);
        if (enemyComponent.isBoss) {
            if (bullet != null) {
                com.bendk97.components.Mappers.position.get(explosion).x -= com.bendk97.components.Mappers.sprite.get(explosion).sprite.getWidth() / 2f;
            } else {
                com.bendk97.components.Mappers.position.get(explosion).x += com.bendk97.components.Mappers.sprite.get(enemy).sprite.getWidth() / 2f;
                com.bendk97.components.Mappers.position.get(explosion).y += com.bendk97.components.Mappers.sprite.get(enemy).sprite.getHeight() / 2f;
            }
        }
        if (bullet != null) {
            getEngine().removeEntity(bullet);
        }
        // update score
        int nbHits = bullet != null ? 1 : HIT_EXPLOSION;
        playerListener.updateScore(player, enemy, nbHits);
        // check health of ennemy
        float percentLifeBefore = enemyComponent.getRemainingLifeInPercent();
        enemyComponent.hit(bullet != null ? 1 : HIT_EXPLOSION);
        float percentLifeAfter = enemyComponent.getRemainingLifeInPercent();
        if (enemyComponent.isBoss && percentLifeBefore >= 0.25 && percentLifeAfter < 0.25) {
            com.bendk97.components.AnimationComponent animationComponent = com.bendk97.components.Mappers.animation.get(enemy);
            if (animationComponent != null) {
                animationComponent.tintRed(ANIMATION_MAIN, 0.99f);
            } else {
                com.bendk97.components.Mappers.sprite.get(enemy).tintRed(0.99f);
            }
        }

        if (enemyComponent.isDead()) {
            if (com.bendk97.components.Mappers.levelFinished.get(player) == null) {
                com.bendk97.components.Mappers.player.get(player).enemyKilled();
            }
            if (enemyComponent.isLaserShip) {
                screenShake.shake(20, 0.5f, false);
                com.bendk97.components.Mappers.player.get(player).laserShipKilled++;
            }
            if (enemyComponent.isTank) {
                screenShake.shake(20, 0.5f, false);
            }
            screen.checkAchievements(player);
            tweenManager.killTarget(com.bendk97.components.Mappers.position.get(enemy));
            if (enemyComponent.belongsToSquadron()) {
                com.bendk97.components.Mappers.squadron.get(enemyComponent.squadron).removeEntity(enemy);
            }
            com.bendk97.components.SpriteComponent spriteComponent = com.bendk97.components.Mappers.sprite.get(enemy);
            if (com.bendk97.components.Mappers.boss.get(enemy) != null) {
                assets.playSound(com.bendk97.assets.Assets.SOUND_BOSS_FINISHED);
                entityFactory.createBossExploding(enemy);
                screenShake.shake(20, 2f, true);
                entityFactory.addInvulnerableComponent(player);
                Timeline.createSequence()
                        .push(Tween.to(com.bendk97.components.Mappers.sprite.get(player), SpriteComponentAccessor.ALPHA, 0.2f).target(0f))
                        .push(Tween.to(com.bendk97.components.Mappers.sprite.get(player), SpriteComponentAccessor.ALPHA, 0.2f).target(1f))
                        .repeat(Tween.INFINITY, 0f)
                        .start(tweenManager);
                Timeline.createSequence()
                        .beginParallel()
                        .push(Tween.to(com.bendk97.components.Mappers.position.get(enemy), PositionComponentAccessor.POSITION_XY, 5f).ease(Linear.INOUT)
                                .target(SCREEN_WIDTH / 2f - spriteComponent.sprite.getWidth() / 2f,
                                        SCREEN_HEIGHT - spriteComponent.sprite.getHeight() - 20f))

                        .push(Tween.to(com.bendk97.components.Mappers.sprite.get(enemy), SpriteComponentAccessor.ALPHA, 0.2f).ease(Linear.INOUT)
                                .target(0.2f).repeatYoyo(25, 0f))
                        .end()
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                if (i == TweenCallback.COMPLETE) {
                                    getEngine().removeEntity(enemy);
                                }
                                com.bendk97.timer.PausableTimer.schedule(new com.bendk97.timer.PausableTimer.Task() {
                                    @Override
                                    public void run() {
                                        player.add(((PooledEngine) getEngine()).createComponent(com.bendk97.components.LeveLFinishedComponent.class));
                                        com.bendk97.timer.PausableTimer.schedule(new com.bendk97.timer.PausableTimer.Task() {
                                            @Override
                                            public void run() {
                                                player.remove(com.bendk97.components.LeveLFinishedComponent.class);
                                                screen.nextLevel();
                                            }
                                        }, 5f);
                                    }
                                }, 2f);
                            }
                        })
                        .start(tweenManager);
            } else {
                getEngine().removeEntity(enemy);
            }
        }
    }

    @Override
    public void playerHitByEnnemyBody(Entity player, Entity ennemy) {
        assets.playSound(com.bendk97.assets.Assets.SOUND_EXPLOSION);
        com.bendk97.components.PositionComponent playerPosition = com.bendk97.components.Mappers.position.get(player);
        entityFactory.createEntityExploding(playerPosition.x, playerPosition.y);
        playerListener.loseLive(player);
    }

    @Override
    public void playerHitByEnnemyBullet(Entity player, Entity bullet) {
        assets.playSound(com.bendk97.assets.Assets.SOUND_EXPLOSION);
        com.bendk97.components.PositionComponent playerPosition = com.bendk97.components.Mappers.position.get(player);
        entityFactory.createEntityExploding(playerPosition.x, playerPosition.y);
        getEngine().removeEntity(bullet);
        playerListener.loseLive(player);
    }

    @Override
    public void playerPowerUp(Entity player, Entity powerUp) {
        assets.playSound(com.bendk97.assets.Assets.SOUND_POWER_UP);
        com.bendk97.components.PlayerComponent playerComponent = com.bendk97.components.Mappers.player.get(player);
        if (!playerComponent.powerLevel.equals(com.bendk97.components.PlayerComponent.PowerLevel.TRIPLE_VERY_FAST)) {
            assets.playSound(com.bendk97.assets.Assets.SOUND_POWER_UP_VOICE);
        }
        playerComponent.powerUp();
        tweenManager.killTarget(com.bendk97.components.Mappers.position.get(powerUp));
        tweenManager.killTarget(com.bendk97.components.Mappers.sprite.get(powerUp));
        getEngine().removeEntity(powerUp);
    }


    @Override
    public void playerShieldUp(Entity player, Entity shieldUp) {
        assets.playSound(com.bendk97.assets.Assets.SOUND_SHIELD_UP);
        entityFactory.createShield(player);
        tweenManager.killTarget(com.bendk97.components.Mappers.position.get(shieldUp));
        tweenManager.killTarget(com.bendk97.components.Mappers.sprite.get(shieldUp));
        getEngine().removeEntity(shieldUp);
    }

    @Override
    public void playerBombUp(Entity player, Entity bombUp) {
        assets.playSound(com.bendk97.assets.Assets.SOUND_SHIELD_UP);
        playerListener.newBombObtained(player);
        tweenManager.killTarget(com.bendk97.components.Mappers.position.get(bombUp));
        tweenManager.killTarget(com.bendk97.components.Mappers.sprite.get(bombUp));
        getEngine().removeEntity(bombUp);
    }


    @Override
    public void bulletStoppedByShield(Entity bullet) {
        assets.playSound(com.bendk97.assets.Assets.SOUND_SHIELD_BULLET);
        getEngine().removeEntity(bullet);
    }

    @Override
    public void enemyShootByShield(Entity enemy, Entity shield) {
        assets.playSound(com.bendk97.assets.Assets.SOUND_EXPLOSION);
        com.bendk97.components.PositionComponent ennemyPosition = com.bendk97.components.Mappers.position.get(enemy);
        entityFactory.createEntityExploding(ennemyPosition.x, ennemyPosition.y);
        tweenManager.killTarget(com.bendk97.components.Mappers.position.get(enemy));
        if (com.bendk97.components.Mappers.enemy.get(enemy).squadron != null) {
            com.bendk97.components.Mappers.squadron.get(Mappers.enemy.get(enemy).squadron).removeEntity(enemy);
        }
        getEngine().removeEntity(enemy);
    }

}
