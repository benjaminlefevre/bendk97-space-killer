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
        for(Entity shield : getEngine().getEntitiesFor(shield)){
            for(Entity bullet : getEngine().getEntitiesFor(enemyBullet)){
                SpriteComponent enemyBullet = Mappers.sprite.get(bullet);
                if (Intersector.overlaps(enemyBullet.sprite.getBoundingRectangle(), Mappers.sprite.get(shield).sprite.getBoundingRectangle())) {
                    collisionListener.bulletStoppedByShield(bullet);
                }
            }
            for (Entity enemy : getEngine().getEntitiesFor(ennemies)) {
                SpriteComponent enemySprite = Mappers.sprite.get(enemy);
                if (Intersector.overlaps(enemySprite.sprite.getBoundingRectangle(), Mappers.sprite.get(shield).sprite.getBoundingRectangle())) {
                    collisionListener.enemyShootByShield(enemy, shield);
                }
            }
        }
        for (Entity player : getEngine().getEntitiesFor(playerVulnerable)) {
            for (Entity ennemy : getEngine().getEntitiesFor(ennemies)) {
                SpriteComponent ennemySprite = Mappers.sprite.get(ennemy);
                if (Intersector.overlaps(ennemySprite.sprite.getBoundingRectangle(), Mappers.sprite.get(player).sprite.getBoundingRectangle())) {
                    collisionListener.playerHitByEnnemyBody(player, ennemy);
                }
            }
            for(Entity bullet : getEngine().getEntitiesFor(enemyBullet)){
                SpriteComponent enemyBullet = Mappers.sprite.get(bullet);
                if (Intersector.overlaps(enemyBullet.sprite.getBoundingRectangle(), Mappers.sprite.get(player).sprite.getBoundingRectangle())) {
                    collisionListener.playerHitByEnnemyBullet(player, bullet);
                }
            }
        }
        for (Entity player : getEngine().getEntitiesFor(player)) {
            for (Entity bullet : getEngine().getEntitiesFor(playerBullet)) {
                for (Entity ennemy : getEngine().getEntitiesFor(ennemies)) {
                    SpriteComponent ennemySprite = Mappers.sprite.get(ennemy);
                    if (Intersector.overlaps(ennemySprite.sprite.getBoundingRectangle(), Mappers.sprite.get(bullet).sprite.getBoundingRectangle())) {
                        collisionListener.enemyShoot(ennemy, player, bullet);
                    }
                }
            }
            for(Entity powerUp: getEngine().getEntitiesFor(powerUp)){
                if(Intersector.overlaps(Mappers.sprite.get(player).sprite.getBoundingRectangle(), Mappers.sprite.get(powerUp).sprite.getBoundingRectangle())){
                    collisionListener.playerPowerUp(player, powerUp);
                }
            }
            for(Entity shieldUp: getEngine().getEntitiesFor(shieldUp)){
                if(Intersector.overlaps(Mappers.sprite.get(player).sprite.getBoundingRectangle(), Mappers.sprite.get(shieldUp).sprite.getBoundingRectangle())){
                    collisionListener.playerShieldUp(player, shieldUp);
                }
            }
        }

    }
}
