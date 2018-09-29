/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.bendk97.components.*;
import com.bendk97.listeners.CollisionListener;
import com.bendk97.mask.SpriteMaskFactory;

public class CollisionSystem extends EntitySystem {

    private Family playerBullet = Family.one(PlayerBulletComponent.class).get();
    private Family enemyBullet = Family.one(EnemyBulletComponent.class).get();
    private Family shieldUp = Family.one(ShieldUpComponent.class).get();
    private Family powerUp = Family.one(PowerUpComponent.class).get();
    private Family bombUp = Family.one(BombUpComponent.class).get();
    private Family enemyBodies = Family.all(EnemyComponent.class).exclude(GroundEnemyComponent.class).get();
    private Family enemies = Family.all(EnemyComponent.class).get();
    private Family playerVulnerable = Family.one(PlayerComponent.class).exclude(InvulnerableComponent.class, GameOverComponent.class).get();
    private Family player = Family.one(PlayerComponent.class).exclude(GameOverComponent.class).get();
    private Family shield = Family.one(ShieldComponent.class).get();


    private CollisionListener collisionListener;
    private SpriteMaskFactory spriteMaskFactory;

    public CollisionSystem(CollisionListener collisionListener, SpriteMaskFactory spriteMaskFactory, int priority) {
        super(priority);
        this.spriteMaskFactory = spriteMaskFactory;
        this.collisionListener = collisionListener;
    }

    @Override
    public void update(float delta) {
        addedToEngine(getEngine());
        boolean thereIsAShield = false;
        for (Entity shield : getEngine().getEntitiesFor(shield)) {
            thereIsAShield = true;
            for (Entity bullet : getEngine().getEntitiesFor(enemyBullet)) {
                SpriteComponent enemyBullet = Mappers.sprite.get(bullet);
                if (isCollisionBetween(enemyBullet.sprite, Mappers.sprite.get(shield).sprite)) {
                    collisionListener.bulletStoppedByShield(bullet);
                    return;
                }
            }
            for (Entity enemy : getEngine().getEntitiesFor(enemyBodies)) {
                if (Mappers.boss.get(enemy) != null || Mappers.enemy.get(enemy).isLaserShip) {
                    break;
                }
                SpriteComponent enemySprite = Mappers.sprite.get(enemy);
                if (isCollisionBetween(enemySprite.sprite, Mappers.sprite.get(shield).sprite)) {
                    collisionListener.enemyShootByShield(enemy, shield);
                    return;
                }
            }
        }
        for (Entity player : getEngine().getEntitiesFor(playerVulnerable)) {
            for (Entity enemy : getEngine().getEntitiesFor(enemyBodies)) {
                if (thereIsAShield && Mappers.boss.get(enemy) != null) {
                    break;
                }
                if (isCollisionBetween(Mappers.sprite.get(enemy).sprite, Mappers.sprite.get(player).sprite)) {
                    collisionListener.playerHitByEnnemyBody(player, enemy);
                    return;
                }

            }
            for (Entity bullet : getEngine().getEntitiesFor(enemyBullet)) {
                SpriteComponent enemyBullet = Mappers.sprite.get(bullet);
                if (isCollisionBetween(enemyBullet.sprite, Mappers.sprite.get(player).sprite)) {
                    collisionListener.playerHitByEnnemyBullet(player, bullet);
                    return;
                }
            }
        }
        for (Entity player : getEngine().getEntitiesFor(player)) {
            for (Entity bullet : getEngine().getEntitiesFor(playerBullet)) {
                for (Entity enemy : getEngine().getEntitiesFor(enemies)) {
                    if (!Mappers.enemy.get(enemy).isDead()) {
                        if (isCollisionBetween(Mappers.sprite.get(enemy).sprite, Mappers.sprite.get(bullet).sprite)) {
                            collisionListener.enemyShoot(enemy, player, bullet);
                            return;
                        }
                    }
                }
            }

            for (Entity powerUp : getEngine().getEntitiesFor(powerUp)) {
                if (isCollisionBetween(Mappers.sprite.get(player).sprite, Mappers.sprite.get(powerUp).sprite)) {
                    collisionListener.playerPowerUp(player, powerUp);
                    return;
                }
            }
            for (Entity shieldUp : getEngine().getEntitiesFor(shieldUp)) {
                if (isCollisionBetween(Mappers.sprite.get(player).sprite, Mappers.sprite.get(shieldUp).sprite)) {
                    collisionListener.playerShieldUp(player, shieldUp);
                    return;
                }
            }
            for (Entity bombUp : getEngine().getEntitiesFor(bombUp)) {
                if (isCollisionBetween(Mappers.sprite.get(player).sprite, Mappers.sprite.get(bombUp).sprite)) {
                    collisionListener.playerBombUp(player, bombUp);
                    return;
                }
            }
        }
    }

    public boolean isCollisionBetween(Sprite sprite1, Sprite sprite2) {
        Rectangle collision = new Rectangle();
        if (Intersector.intersectRectangles(sprite1.getBoundingRectangle(), sprite2.getBoundingRectangle(), collision)) {
            Array<Array<Boolean>> mask1 = spriteMaskFactory.getMask(sprite1.getTexture());
            Array<Array<Boolean>> mask2 = spriteMaskFactory.getMask(sprite2.getTexture());
            if (mask1 == null || mask2 == null) {
                return true;
            }
            for (int i = (int) collision.x; i < Math.floor(collision.x + collision.width); ++i) {
                for (int j = (int) collision.y; j < Math.floor(collision.y + collision.height); ++j) {
                    try {
                        if (mask1.get(sprite1.getRegionX() + i - (int) sprite1.getX()).get(sprite1.getRegionY() + (int) sprite1.getHeight() - (j - (int) sprite1.getY()))
                                && mask2.get(sprite2.getRegionX() + i - (int) sprite2.getX()).get(sprite2.getRegionY() + (int) sprite2.getHeight() - (j - (int) sprite2.getY()))) {
                            return true;
                        }
                    } catch (Exception e) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

