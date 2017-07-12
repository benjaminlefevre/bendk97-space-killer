package com.benk97.listeners.impl;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.benk97.assets.Assets;
import com.benk97.components.Mappers;
import com.benk97.components.PositionComponent;
import com.benk97.entities.EntityFactory;
import com.benk97.listeners.CollisionListener;
import com.benk97.listeners.PlayerListener;

import static com.benk97.assets.Assets.SOUND_EXPLOSION;

public class CollisionListenerImpl extends EntitySystem implements CollisionListener {

    private Assets assets;
    private EntityFactory entityFactory;
    private PlayerListener playerListener;
    private TweenManager tweenManager;

    public CollisionListenerImpl(TweenManager tweenManager, Assets assets, EntityFactory entityFactory, PlayerListener playerListener) {
        this.playerListener = playerListener;
        this.assets = assets;
        this.entityFactory = entityFactory;
        this.tweenManager = tweenManager;
    }

    @Override
    public void enemyShoot(Entity enemy, Entity player, Entity bullet) {
        assets.playSound(SOUND_EXPLOSION);
        PositionComponent ennemyPosition = Mappers.position.get(enemy);
        entityFactory.createEntityExploding(ennemyPosition.x, ennemyPosition.y);
        playerListener.updateScore(player, enemy);
        tweenManager.killTarget(Mappers.position.get(enemy));
        getEngine().removeEntity(enemy);
        getEngine().removeEntity(bullet);
    }

    @Override
    public void playerHitByEnnemyBody(Entity player, Entity ennemy) {
        assets.playSound(SOUND_EXPLOSION);
        PositionComponent playerPosition = Mappers.position.get(player);
        entityFactory.createEntityExploding(playerPosition.x, playerPosition.y);
        playerListener.loseLive(player);
    }
}
