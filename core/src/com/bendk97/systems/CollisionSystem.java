package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.bendk97.components.EnemyComponent;
import com.bendk97.listeners.CollisionListener;
import com.bendk97.mask.SpriteMaskFactory;

public class CollisionSystem extends EntitySystem {

    private Family playerBullet = Family.one(com.bendk97.components.PlayerBulletComponent.class).get();
    private Family enemyBullet = Family.one(com.bendk97.components.EnemyBulletComponent.class).get();
    private Family shieldUp = Family.one(com.bendk97.components.ShieldUpComponent.class).get();
    private Family powerUp = Family.one(com.bendk97.components.PowerUpComponent.class).get();
    private Family bombUp = Family.one(com.bendk97.components.BombUpComponent.class).get();
    private Family enemyBodies = Family.all(com.bendk97.components.EnemyComponent.class).exclude(com.bendk97.components.GroundEnemyComponent.class).get();
    private Family enemies = Family.all(EnemyComponent.class).get();
    private Family playerVulnerable = Family.one(com.bendk97.components.PlayerComponent.class).exclude(com.bendk97.components.InvulnerableComponent.class, com.bendk97.components.GameOverComponent.class).get();
    private Family player = Family.one(com.bendk97.components.PlayerComponent.class).exclude(com.bendk97.components.GameOverComponent.class).get();
    private Family shield = Family.one(com.bendk97.components.ShieldComponent.class).get();


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
                com.bendk97.components.SpriteComponent enemyBullet = com.bendk97.components.Mappers.sprite.get(bullet);
                if (isCollisionBetween(enemyBullet.sprite, com.bendk97.components.Mappers.sprite.get(shield).sprite)) {
                    collisionListener.bulletStoppedByShield(bullet);
                    return;
                }
            }
            for (Entity enemy : getEngine().getEntitiesFor(enemyBodies)) {
                if (com.bendk97.components.Mappers.boss.get(enemy) != null || com.bendk97.components.Mappers.enemy.get(enemy).isLaserShip) {
                    break;
                }
                com.bendk97.components.SpriteComponent enemySprite = com.bendk97.components.Mappers.sprite.get(enemy);
                if (isCollisionBetween(enemySprite.sprite, com.bendk97.components.Mappers.sprite.get(shield).sprite)) {
                    collisionListener.enemyShootByShield(enemy, shield);
                    return;
                }
            }
        }
        for (Entity player : getEngine().getEntitiesFor(playerVulnerable)) {
            for (Entity enemy : getEngine().getEntitiesFor(enemyBodies)) {
                if (thereIsAShield && com.bendk97.components.Mappers.boss.get(enemy) != null) {
                    break;
                }
                if (isCollisionBetween(com.bendk97.components.Mappers.sprite.get(enemy).sprite, com.bendk97.components.Mappers.sprite.get(player).sprite)) {
                    collisionListener.playerHitByEnnemyBody(player, enemy);
                    return;
                }

            }
            for (Entity bullet : getEngine().getEntitiesFor(enemyBullet)) {
                com.bendk97.components.SpriteComponent enemyBullet = com.bendk97.components.Mappers.sprite.get(bullet);
                if (isCollisionBetween(enemyBullet.sprite, com.bendk97.components.Mappers.sprite.get(player).sprite)) {
                    collisionListener.playerHitByEnnemyBullet(player, bullet);
                    return;
                }
            }
        }
        for (Entity player : getEngine().getEntitiesFor(player)) {
            for (Entity bullet : getEngine().getEntitiesFor(playerBullet)) {
                for (Entity enemy : getEngine().getEntitiesFor(enemies)) {
                    if (!com.bendk97.components.Mappers.enemy.get(enemy).isDead()) {
                        if (isCollisionBetween(com.bendk97.components.Mappers.sprite.get(enemy).sprite, com.bendk97.components.Mappers.sprite.get(bullet).sprite)) {
                            collisionListener.enemyShoot(enemy, player, bullet);
                            return;
                        }
                    }
                }
            }

            for (Entity powerUp : getEngine().getEntitiesFor(powerUp)) {
                if (isCollisionBetween(com.bendk97.components.Mappers.sprite.get(player).sprite, com.bendk97.components.Mappers.sprite.get(powerUp).sprite)) {
                    collisionListener.playerPowerUp(player, powerUp);
                    return;
                }
            }
            for (Entity shieldUp : getEngine().getEntitiesFor(shieldUp)) {
                if (isCollisionBetween(com.bendk97.components.Mappers.sprite.get(player).sprite, com.bendk97.components.Mappers.sprite.get(shieldUp).sprite)) {
                    collisionListener.playerShieldUp(player, shieldUp);
                    return;
                }
            }
            for (Entity bombUp : getEngine().getEntitiesFor(bombUp)) {
                if (isCollisionBetween(com.bendk97.components.Mappers.sprite.get(player).sprite, com.bendk97.components.Mappers.sprite.get(bombUp).sprite)) {
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

