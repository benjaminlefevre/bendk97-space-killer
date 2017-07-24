package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.benk97.components.*;
import com.benk97.listeners.CollisionListener;
import com.benk97.mask.SpriteMaskFactory;

public class CollisionSystem extends EntitySystem {

    private Family playerBullet = Family.one(PlayerBulletComponent.class).get();
    private Family enemyBullet = Family.one(EnemyBulletComponent.class).get();
    private Family shieldUp = Family.one(ShieldUpComponent.class).get();
    private Family powerUp = Family.one(PowerUpComponent.class).get();
    private Family bombUp = Family.one(BombUpComponent.class).get();
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
                }
            }
            for (Entity enemy : getEngine().getEntitiesFor(enemies)) {
                if (Mappers.boss.get(enemy) != null) {
                    break;
                }
                SpriteComponent enemySprite = Mappers.sprite.get(enemy);
                if (isCollisionBetween(enemySprite.sprite, Mappers.sprite.get(shield).sprite)) {
                    collisionListener.enemyShootByShield(enemy, shield);
                }
            }
        }
        for (Entity player : getEngine().getEntitiesFor(playerVulnerable)) {
            for (Entity enemy : getEngine().getEntitiesFor(enemies)) {
                if (thereIsAShield && Mappers.boss.get(enemy) != null) {
                    break;
                }
                if (isCollisionBetween(Mappers.sprite.get(enemy).sprite, Mappers.sprite.get(player).sprite)) {
                    collisionListener.playerHitByEnnemyBody(player, enemy);
                }

            }
            for (Entity bullet : getEngine().getEntitiesFor(enemyBullet)) {
                SpriteComponent enemyBullet = Mappers.sprite.get(bullet);
                if (isCollisionBetween(enemyBullet.sprite, Mappers.sprite.get(player).sprite)) {
                    collisionListener.playerHitByEnnemyBullet(player, bullet);
                }
            }
        }
        for (Entity player : getEngine().getEntitiesFor(player)) {
            for (Entity bullet : getEngine().getEntitiesFor(playerBullet)) {
                for (Entity enemy : getEngine().getEntitiesFor(enemies)) {
                    if (!Mappers.enemy.get(enemy).isDead()) {
                        if (isCollisionBetween(Mappers.sprite.get(enemy).sprite, Mappers.sprite.get(bullet).sprite)) {
                            collisionListener.enemyShoot(enemy, player, bullet);
                        }
                    }
                }
            }

            for (Entity powerUp : getEngine().getEntitiesFor(powerUp)) {
                if (isCollisionBetween(Mappers.sprite.get(player).sprite, Mappers.sprite.get(powerUp).sprite)) {
                    collisionListener.playerPowerUp(player, powerUp);
                }
            }
            for (Entity shieldUp : getEngine().getEntitiesFor(shieldUp)) {
                if (isCollisionBetween(Mappers.sprite.get(player).sprite, Mappers.sprite.get(shieldUp).sprite)) {
                    collisionListener.playerShieldUp(player, shieldUp);
                }
            }
            for (Entity bombUp : getEngine().getEntitiesFor(bombUp)) {
                if (isCollisionBetween(Mappers.sprite.get(player).sprite, Mappers.sprite.get(bombUp).sprite)) {
                    collisionListener.playerBombUp(player, bombUp);
                }
            }
        }
    }

    public boolean isCollisionBetween(Sprite sprite1, Sprite sprite2) {
        Rectangle collision = new Rectangle();
        if (Intersector.intersectRectangles(sprite1.getBoundingRectangle(), sprite2.getBoundingRectangle(), collision)) {
            Array<Array<Boolean>> mask1 = spriteMaskFactory.getMask(sprite1.getTexture());
            Array<Array<Boolean>> mask2 = spriteMaskFactory.getMask(sprite1.getTexture());
            for (int i = (int) collision.x; i < Math.floor(collision.x + collision.width); ++i) {
                for (int j = (int) collision.y; j < Math.floor(collision.y + collision.height); ++j) {
//                    Gdx.app.log("debug",
//                            "px [x = " + i + ", y = " + j + "]\n"
//                                    + "sprite 1 [x = " + sprite1.getX() + ", y = " + sprite1.getY()
//                                    + ", height = " + sprite1.getHeight()
//                                    + ", regionX = " + sprite1.getRegionX() + ", regionY = " + sprite1.getRegionY() + "]\n"
//                                    + "sprite 1 [x = " + sprite2.getX() + ", y = " + sprite2.getY()
//                                    + ", height = " + sprite2.getHeight()
//                                    + ", regionX = " + sprite2.getRegionX() + ", regionY = " + sprite2.getRegionY() + "]\n"
//
//                    );
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

