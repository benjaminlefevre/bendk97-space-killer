/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.bendk97.components.SpriteComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.components.helpers.Families;
import com.bendk97.listeners.CollisionListener;
import com.bendk97.mask.SpriteMaskFactory;

public class CollisionSystem extends EntitySystem {


    private final CollisionListener collisionListener;
    private final SpriteMaskFactory spriteMaskFactory;

    public CollisionSystem(CollisionListener collisionListener, SpriteMaskFactory spriteMaskFactory, int priority) {
        super(priority);
        this.spriteMaskFactory = spriteMaskFactory;
        this.collisionListener = collisionListener;
    }

    @Override
    public void update(float delta) {
        detectCollisionWithPlayer();
        detectCollisionWithShields();
        detectCollisionWithPlayerVulnerable();
    }

    private void detectCollisionWithPlayer() {
        for (Entity player : getEngine().getEntitiesFor(Families.player)) {
            for (Entity bullet : getEngine().getEntitiesFor(Families.playerBullet)) {
                for (Entity enemy : getEngine().getEntitiesFor(Families.enemies)) {
                    if (!ComponentMapperHelper.enemy.get(enemy).isDead()
                            && isCollisionBetween(ComponentMapperHelper.sprite.get(enemy).sprite, ComponentMapperHelper.sprite.get(bullet).sprite)) {
                        collisionListener.enemyShoot(enemy, player, bullet);
                        return;
                    }
                }
            }

            for (Entity powerUp : getEngine().getEntitiesFor(Families.powerUp)) {
                if (isCollisionBetween(ComponentMapperHelper.sprite.get(player).sprite, ComponentMapperHelper.sprite.get(powerUp).sprite)) {
                    collisionListener.playerPowerUp(player, powerUp);
                    return;
                }
            }
            for (Entity shieldUp : getEngine().getEntitiesFor(Families.shieldUp)) {
                if (isCollisionBetween(ComponentMapperHelper.sprite.get(player).sprite, ComponentMapperHelper.sprite.get(shieldUp).sprite)) {
                    collisionListener.playerShieldUp(player, shieldUp);
                    return;
                }
            }
            for (Entity bombUp : getEngine().getEntitiesFor(Families.bombUp)) {
                if (isCollisionBetween(ComponentMapperHelper.sprite.get(player).sprite, ComponentMapperHelper.sprite.get(bombUp).sprite)) {
                    collisionListener.playerBombUp(player, bombUp);
                    return;
                }
            }
        }
    }

    private void detectCollisionWithPlayerVulnerable() {
        for (Entity player : getEngine().getEntitiesFor(Families.playerVulnerable)) {
            for (Entity enemy : getEngine().getEntitiesFor(Families.enemyBodies)) {
                if (isCollisionBetween(ComponentMapperHelper.sprite.get(enemy).sprite, ComponentMapperHelper.sprite.get(player).sprite)) {
                    collisionListener.playerHitByEnemyBody(player);
                    return;
                }

            }
            for (Entity bullet : getEngine().getEntitiesFor(Families.enemyBullet)) {
                SpriteComponent enemyBullet = ComponentMapperHelper.sprite.get(bullet);
                if (isCollisionBetween(enemyBullet.sprite, ComponentMapperHelper.sprite.get(player).sprite)) {
                    collisionListener.playerHitByEnemyBullet(player, bullet);
                    return;
                }
            }
        }
    }

    private void detectCollisionWithShields() {
        for (Entity shield : getEngine().getEntitiesFor(Families.shield)) {
            for (Entity bullet : getEngine().getEntitiesFor(Families.enemyBullet)) {
                SpriteComponent enemyBullet = ComponentMapperHelper.sprite.get(bullet);
                if (isCollisionBetween(enemyBullet.sprite, ComponentMapperHelper.sprite.get(shield).sprite)) {
                    collisionListener.bulletStoppedByShield(bullet);
                    return;
                }
            }
            for (Entity enemy : getEngine().getEntitiesFor(Families.enemyBodies)) {
                if (ComponentMapperHelper.boss.get(enemy) != null || ComponentMapperHelper.enemy.get(enemy).isLaserShip) {
                    break;
                }
                SpriteComponent enemySprite = ComponentMapperHelper.sprite.get(enemy);
                if (isCollisionBetween(enemySprite.sprite, ComponentMapperHelper.sprite.get(shield).sprite)) {
                    collisionListener.enemyShootByShield(enemy);
                    return;
                }
            }
        }
    }

    private boolean isCollisionBetween(Sprite sprite1, Sprite sprite2) {
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

