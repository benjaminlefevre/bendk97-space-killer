package com.benk97.listeners.impl;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Array;
import com.benk97.Settings;
import com.benk97.components.GameOverComponent;
import com.benk97.components.Mappers;
import com.benk97.components.PlayerComponent;
import com.benk97.entities.EntityFactory;
import com.benk97.listeners.PlayerListener;

import static com.benk97.SpaceKillerGameConstants.PLAYER_ORIGIN_X;
import static com.benk97.SpaceKillerGameConstants.PLAYER_ORIGIN_Y;
import static com.benk97.tweens.SpriteComponentAccessor.ALPHA;


public class PlayerListenerImpl extends EntitySystem implements PlayerListener {
    private EntityFactory entityFactory;
    private Array<Entity> lives;
    private TweenManager tweenManager;

    public PlayerListenerImpl(EntityFactory entityFactory, Array<Entity> lives, TweenManager tweenManager) {
        this.entityFactory = entityFactory;
        this.lives = lives;
        this.tweenManager = tweenManager;
    }

    @Override
    public void loseLive(Entity player) {
        PlayerComponent playerComponent = Mappers.player.get(player);
        playerComponent.loseLife();
        if (playerComponent.isGameOver()) {
            player.add(((PooledEngine)getEngine()).createComponent(GameOverComponent.class));
            Settings.addScore(playerComponent.getScoreInt());
            Settings.save();
        } else {
            if (playerComponent.lives > 0) {
                getEngine().removeEntity(lives.removeIndex(playerComponent.lives - 1));
            }
            Mappers.position.get(player).setPosition(PLAYER_ORIGIN_X, PLAYER_ORIGIN_Y);
            Mappers.sprite.get(player).sprite.setPosition(PLAYER_ORIGIN_X, PLAYER_ORIGIN_Y);
            Timeline.createSequence()
                    .push(Tween.to(Mappers.sprite.get(player), ALPHA, 0.2f).target(0f))
                    .push(Tween.to(Mappers.sprite.get(player), ALPHA, 0.2f).target(1f))
                    .repeat(10, 0f)
                    .start(tweenManager);
        }
    }

    @Override
    public void updateScore(Entity player, Entity ennemy) {
        Mappers.player.get(player).updateScore(Mappers.ennemy.get(ennemy).points);
    }


}
