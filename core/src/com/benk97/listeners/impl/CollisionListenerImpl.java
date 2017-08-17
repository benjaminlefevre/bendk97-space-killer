package com.benk97.listeners.impl;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.benk97.assets.Assets;
import com.benk97.components.*;
import com.benk97.entities.EntityFactory;
import com.benk97.listeners.CollisionListener;
import com.benk97.listeners.PlayerListener;
import com.benk97.screens.LevelScreen;
import com.benk97.screens.ScreenShake;
import com.benk97.timer.PausableTimer;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.*;
import static com.benk97.tweens.PositionComponentAccessor.POSITION_XY;
import static com.benk97.tweens.SpriteComponentAccessor.ALPHA;

public class CollisionListenerImpl extends EntitySystem implements CollisionListener {

    private Assets assets;
    private EntityFactory entityFactory;
    private PlayerListener playerListener;
    private TweenManager tweenManager;
    private LevelScreen screen;
    private ScreenShake screenShake;

    public CollisionListenerImpl(TweenManager tweenManager, ScreenShake screenShake, Assets assets, EntityFactory entityFactory, PlayerListener playerListener,
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
        EnemyComponent enemyComponent = Mappers.enemy.get(enemy);
        // special case: enemy can be already dead
        if (enemyComponent.isDead()) {
            return;
        }
        // create explosion
        assets.playSound(SOUND_EXPLOSION);
        PositionComponent explosePosition;
        if (enemyComponent.isBoss && bullet != null) {
            explosePosition = Mappers.position.get(bullet);

        } else {
            explosePosition = Mappers.position.get(enemy);
        }
        Entity explosion = entityFactory.createEntityExploding(explosePosition.x, explosePosition.y);
        if (enemyComponent.isBoss) {
            if (bullet != null) {
                Mappers.position.get(explosion).x -= Mappers.sprite.get(explosion).sprite.getWidth() / 2f;
            } else {
                Mappers.position.get(explosion).x += Mappers.sprite.get(enemy).sprite.getWidth() / 2f;
                Mappers.position.get(explosion).y += Mappers.sprite.get(enemy).sprite.getHeight() / 2f;
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
            AnimationComponent animationComponent = Mappers.animation.get(enemy);
            if (animationComponent != null) {
                animationComponent.tintRed(ANIMATION_MAIN, 0.99f);
            } else {
                Mappers.sprite.get(enemy).tintRed(0.99f);
            }
        }

        if (enemyComponent.isDead()) {
            if (Mappers.levelFinished.get(player) == null) {
                Mappers.player.get(player).enemyKilled();
            }
            if (enemyComponent.isLaserShip) {
                screenShake.shake(20, 0.5f, false);
                Mappers.player.get(player).laserShipKilled++;
            }
            if (enemyComponent.isTank) {
                screenShake.shake(20, 0.5f, false);
            }
            screen.checkAchievements(player);
            tweenManager.killTarget(Mappers.position.get(enemy));
            if (enemyComponent.belongsToSquadron()) {
                Mappers.squadron.get(enemyComponent.squadron).removeEntity(enemy);
            }
            SpriteComponent spriteComponent = Mappers.sprite.get(enemy);
            if (Mappers.boss.get(enemy) != null) {
                assets.playSound(SOUND_BOSS_FINISHED);
                entityFactory.createBossExploding(enemy);
                screenShake.shake(20, 2f, true);
                entityFactory.addInvulnerableComponent(player);
                Timeline.createSequence()
                        .push(Tween.to(Mappers.sprite.get(player), ALPHA, 0.2f).target(0f))
                        .push(Tween.to(Mappers.sprite.get(player), ALPHA, 0.2f).target(1f))
                        .repeat(Tween.INFINITY, 0f)
                        .start(tweenManager);
                Timeline.createSequence()
                        .beginParallel()
                        .push(Tween.to(Mappers.position.get(enemy), POSITION_XY, 5f).ease(Linear.INOUT)
                                .target(SCREEN_WIDTH / 2f - spriteComponent.sprite.getWidth() / 2f,
                                        SCREEN_HEIGHT - spriteComponent.sprite.getHeight() - 20f))

                        .push(Tween.to(Mappers.sprite.get(enemy), ALPHA, 0.2f).ease(Linear.INOUT)
                                .target(0.2f).repeatYoyo(25, 0f))
                        .end()
                        .setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int i, BaseTween<?> baseTween) {
                                if (i == TweenCallback.COMPLETE) {
                                    getEngine().removeEntity(enemy);
                                }
                                PausableTimer.schedule(new PausableTimer.Task() {
                                    @Override
                                    public void run() {
                                        player.add(((PooledEngine) getEngine()).createComponent(LeveLFinishedComponent.class));
                                        PausableTimer.schedule(new PausableTimer.Task() {
                                            @Override
                                            public void run() {
                                                player.remove(LeveLFinishedComponent.class);
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
        assets.playSound(SOUND_EXPLOSION);
        PositionComponent playerPosition = Mappers.position.get(player);
        entityFactory.createEntityExploding(playerPosition.x, playerPosition.y);
        playerListener.loseLive(player);
    }

    @Override
    public void playerHitByEnnemyBullet(Entity player, Entity bullet) {
        assets.playSound(SOUND_EXPLOSION);
        PositionComponent playerPosition = Mappers.position.get(player);
        entityFactory.createEntityExploding(playerPosition.x, playerPosition.y);
        getEngine().removeEntity(bullet);
        playerListener.loseLive(player);
    }

    @Override
    public void playerPowerUp(Entity player, Entity powerUp) {
        assets.playSound(SOUND_POWER_UP);
        PlayerComponent playerComponent = Mappers.player.get(player);
        if (!playerComponent.powerLevel.equals(PlayerComponent.PowerLevel.TRIPLE_VERY_FAST)) {
            assets.playSound(SOUND_POWER_UP_VOICE);
        }
        playerComponent.powerUp();
        tweenManager.killTarget(Mappers.position.get(powerUp));
        tweenManager.killTarget(Mappers.sprite.get(powerUp));
        getEngine().removeEntity(powerUp);
    }


    @Override
    public void playerShieldUp(Entity player, Entity shieldUp) {
        assets.playSound(SOUND_SHIELD_UP);
        entityFactory.createShield(player);
        tweenManager.killTarget(Mappers.position.get(shieldUp));
        tweenManager.killTarget(Mappers.sprite.get(shieldUp));
        getEngine().removeEntity(shieldUp);
    }

    @Override
    public void playerBombUp(Entity player, Entity bombUp) {
        assets.playSound(SOUND_SHIELD_UP);
        playerListener.newBombObtained(player);
        tweenManager.killTarget(Mappers.position.get(bombUp));
        tweenManager.killTarget(Mappers.sprite.get(bombUp));
        getEngine().removeEntity(bombUp);
    }


    @Override
    public void bulletStoppedByShield(Entity bullet) {
        assets.playSound(SOUND_SHIELD_BULLET);
        getEngine().removeEntity(bullet);
    }

    @Override
    public void enemyShootByShield(Entity enemy, Entity shield) {
        assets.playSound(SOUND_EXPLOSION);
        PositionComponent ennemyPosition = Mappers.position.get(enemy);
        entityFactory.createEntityExploding(ennemyPosition.x, ennemyPosition.y);
        tweenManager.killTarget(Mappers.position.get(enemy));
        if (Mappers.enemy.get(enemy).squadron != null) {
            Mappers.squadron.get(Mappers.enemy.get(enemy).squadron).removeEntity(enemy);
        }
        getEngine().removeEntity(enemy);
    }

}
