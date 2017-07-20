package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Intersector;
import com.benk97.components.*;
import com.benk97.listeners.CollisionListener;

public class CollisionSystem extends EntitySystem {

    private Family playerBullet = Family.one(PlayerBulletComponent.class).get();
    private Family enemyBullet = Family.one(EnemyBulletComponent.class).get();
    private Family shieldUp = Family.one(ShieldUpComponent.class).get();
    private Family powerUp = Family.one(PowerUpComponent.class).get();
    private Family ennemies = Family.all(EnemyComponent.class).get();
    private Family playerVulnerable = Family.one(PlayerComponent.class).exclude(InvulnerableComponent.class, GameOverComponent.class).get();
    private Family player = Family.one(PlayerComponent.class).exclude(GameOverComponent.class).get();
    private Family shield = Family.one(ShieldComponent.class).get();


    private CollisionListener collisionListener;

    public CollisionSystem(CollisionListener collisionListener, int priority) {
        super(priority);
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
                if (Intersector.overlaps(enemyBullet.sprite.getBoundingRectangle(), Mappers.sprite.get(shield).sprite.getBoundingRectangle())) {
                    collisionListener.bulletStoppedByShield(bullet);
                }
            }
            for (Entity enemy : getEngine().getEntitiesFor(ennemies)) {
                if(Mappers.boss.get(enemy)!= null){
                    break;
                }
                SpriteComponent enemySprite = Mappers.sprite.get(enemy);
                if (Intersector.overlaps(enemySprite.sprite.getBoundingRectangle(), Mappers.sprite.get(shield).sprite.getBoundingRectangle())) {
                    collisionListener.enemyShootByShield(enemy, shield);
                }
            }
        }
        for (Entity player : getEngine().getEntitiesFor(playerVulnerable)) {
            for (Entity enemy : getEngine().getEntitiesFor(ennemies)) {
                if(thereIsAShield && Mappers.boss.get(enemy)!=null){
                    break;
                }
                SpriteComponent enemySprite = Mappers.sprite.get(enemy);
                SpriteComponent playerSprite = Mappers.sprite.get(player);
                for (int i = 0; i < enemySprite.getPolygonBounds().size; ++i) {
                    for (int j = 0; j < playerSprite.getPolygonBounds().size; ++j) {
                        if (Intersector.overlapConvexPolygons(enemySprite.getPolygonBounds().get(i), playerSprite.getPolygonBounds().get(j),
                                new Intersector.MinimumTranslationVector())) {
                            collisionListener.playerHitByEnnemyBody(player, enemy);
                        }
                    }
                }
            }
            for (Entity bullet : getEngine().getEntitiesFor(enemyBullet)) {
                SpriteComponent enemyBullet = Mappers.sprite.get(bullet);
                if (Intersector.overlaps(enemyBullet.sprite.getBoundingRectangle(), Mappers.sprite.get(player).sprite.getBoundingRectangle())) {
                    collisionListener.playerHitByEnnemyBullet(player, bullet);
                }
            }
        }
        for (Entity player : getEngine().getEntitiesFor(player)) {
            for (Entity bullet : getEngine().getEntitiesFor(playerBullet)) {
                for (Entity enemy : getEngine().getEntitiesFor(ennemies)) {
                    if (!Mappers.enemy.get(enemy).isDead()) {
                        SpriteComponent enemySprite = Mappers.sprite.get(enemy);
                        SpriteComponent bulletSprite = Mappers.sprite.get(bullet);
                        for (int i = 0; i < enemySprite.getPolygonBounds().size; ++i) {
                            for (int j = 0; j < bulletSprite.getPolygonBounds().size; ++j) {
                                if (Intersector.overlapConvexPolygons(enemySprite.getPolygonBounds().get(i),
                                        bulletSprite.getPolygonBounds().get(j),
                                        new Intersector.MinimumTranslationVector())) {
                                    collisionListener.enemyShoot(enemy, player, bullet);
                                }
                            }
                        }
                    }
                }
            }
            for (Entity powerUp : getEngine().getEntitiesFor(powerUp)) {
                if (Intersector.overlaps(Mappers.sprite.get(player).sprite.getBoundingRectangle(), Mappers.sprite.get(powerUp).sprite.getBoundingRectangle())) {
                    collisionListener.playerPowerUp(player, powerUp);
                }
            }
            for (Entity shieldUp : getEngine().getEntitiesFor(shieldUp)) {
                if (Intersector.overlaps(Mappers.sprite.get(player).sprite.getBoundingRectangle(), Mappers.sprite.get(shieldUp).sprite.getBoundingRectangle())) {
                    collisionListener.playerShieldUp(player, shieldUp);
                }
            }
        }

    }
}
