/*
 * Developed by Benjamin Lefèvre
 * Last modified 11/10/18 23:26
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.player;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.bendk97.Settings;
import com.bendk97.SpaceKillerGame;
import com.bendk97.components.*;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.screens.levels.Level;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.tweens.SpriteComponentAccessor.ALPHA;

public class PlayerEntityFactory {

    private final EntityFactory entityFactory;
    private final SpaceKillerGame game;

    public PlayerEntityFactory(EntityFactory entityFactory, SpaceKillerGame game) {
        this.entityFactory = entityFactory;
        this.game = game;
    }


    public Entity createEntityPlayer(Level level) {
        Entity player = entityFactory.engine.createEntity();
        if (entityFactory.engine.getEntitiesFor(Family.one(PlayerComponent.class).get()).size() > 0) {
            throw new IllegalArgumentException("A player entity already exists!");
        }
        PlayerComponent playerComponent = entityFactory.engine.createComponent(PlayerComponent.class);
        playerComponent.setHighScore(Settings.getHighscore());
        if (game.playerData != null) {
            playerComponent.bombs = game.playerData.bombs;
            playerComponent.howManyLivesLost = game.playerData.howManyLivesLost;
            playerComponent.enemiesKilled = game.playerData.enemiesKilled;
            playerComponent.laserShipKilled = game.playerData.laserShipKilled;
            playerComponent.fireDelay = game.playerData.fireDelay;
            playerComponent.fireDelaySide = game.playerData.fireDelaySide;
            playerComponent.updateScore(game.playerData.score);
            playerComponent.lives = game.playerData.lives;
            playerComponent.powerLevel = game.playerData.powerLevel;
            playerComponent.numberOfContinue = game.playerData.numberOfContinue;
            playerComponent.level = game.playerData.level;
        } else {
            playerComponent.level = level;
        }
        player.add(playerComponent);
        player.add(entityFactory.engine.createComponent(PositionComponent.class));
        player.add(entityFactory.engine.createComponent(VelocityComponent.class));
        AnimationComponent animationComponent = entityFactory.engine.createComponent(AnimationComponent.class);
        Array<Sprite> spritesMAIN = new Array<>(2);
        spritesMAIN.add(entityFactory.atlasMask.createSprite("player", 1));
        spritesMAIN.add(entityFactory.atlasMask.createSprite("player", 2));
        Array<Sprite> spritesLEFT = new Array<>(2);
        spritesLEFT.add(entityFactory.atlasMask.createSprite("player", 0));
        spritesLEFT.add(entityFactory.atlasMask.createSprite("player", 3));
        Array<Sprite> spritesRIGHT = new Array<>(2);
        spritesRIGHT.add(entityFactory.atlasMask.createSprite("player", 0));
        spritesRIGHT.add(entityFactory.atlasMask.createSprite("player", 3));
        spritesRIGHT.get(0).flip(true, false);
        spritesRIGHT.get(1).flip(true, false);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<>(FRAME_DURATION, spritesMAIN, LOOP));
        animationComponent.animations.put(GO_LEFT, new Animation<>(FRAME_DURATION, spritesLEFT, LOOP));
        animationComponent.animations.put(GO_RIGHT, new Animation<>(FRAME_DURATION, spritesRIGHT, LOOP));
        player.add(animationComponent);
        SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.sprite = spritesMAIN.get(0);
        component.zIndex = 99;
        player.add(component);
        player.add(entityFactory.engine.createComponent(StateComponent.class));
        entityFactory.engine.addEntity(player);
        ComponentMapperHelper.position.get(player).setPosition(PLAYER_ORIGIN_X, PLAYER_ORIGIN_Y);
        entityFactory.enemyEntityFactory.setPlayer(player);
        return player;
    }

    public void createShield(final Entity player) {
        final Entity shield = entityFactory.engine.createEntity();
        PositionComponent playerPosition = ComponentMapperHelper.position.get(player);
        addInvulnerableComponent(player);
        SpriteComponent playerSprite = ComponentMapperHelper.sprite.get(player);
        SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        spriteComponent.zIndex = 99;
        spriteComponent.sprite = entityFactory.atlasMask.createSprite("shield");
        PositionComponent positionComponent = entityFactory.engine.createComponent(PositionComponent.class);
        positionComponent.setPosition(playerPosition.x - (spriteComponent.sprite.getWidth() - playerSprite.sprite.getWidth()) / 2f,
                playerPosition.y - (spriteComponent.sprite.getHeight() - playerSprite.sprite.getHeight()) / 2f);
        shield.add(positionComponent);
        shield.add(spriteComponent);
        shield.add(entityFactory.engine.createComponent(ShieldComponent.class));
        entityFactory.engine.addEntity(shield);
        Timeline.createSequence().beginSequence()
                .delay(5f)
                .push(Tween.to(spriteComponent, ALPHA, 0.2f).target(0.2f))
                .push(Tween.to(spriteComponent, ALPHA, 0.2f).target(0.8f))
                .repeat(5, 0f)
                .setCallback((i, baseTween) -> {
                    if (i == TweenCallback.COMPLETE) {
                        entityFactory.engine.removeEntity(shield);
                        removeInvulnerableComponent(player);
                    }
                })
                .start(entityFactory.tweenManager);
    }

    public SnapshotArray<Entity> createEntityPlayerLives(Entity player) {
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        SnapshotArray<Entity> entities = new SnapshotArray<>(true, playerComponent.lives, Entity.class);
        for (int i = 0; i < playerComponent.lives - 1; ++i) {
            Entity life = entityFactory.engine.createEntity();
            SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
            Sprite sprite = entityFactory.atlasMask.createSprite("player", 1);
            component.setTexture(sprite, 1f, 0f, 0.5f);
            component.setPosition(LIVES_X + 20f * i, LIVES_Y - sprite.getHeight());
            life.add(component);
            entityFactory.engine.addEntity(life);
            entities.add(life);
        }
        return entities;
    }

    public SnapshotArray<Entity> createEntityPlayerBombs(Entity player) {
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        SnapshotArray<Entity> entities = new SnapshotArray<>(true, playerComponent.bombs, Entity.class);
        for (int i = 0; i < playerComponent.bombs; ++i) {
            Entity bomb = entityFactory.engine.createEntity();
            SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
            Sprite sprite = entityFactory.atlasNoMask.createSprite("bomb", 1);
            component.zIndex = 100;
            component.setTexture(sprite, 1f, 0f, 1f);
            component.setPosition(BOMB_STOCK_X - (22f * (i % 4)), BOMB_STOCK_Y + (22f * (float) Math.floor(i/4d)));
            bomb.add(component);
            entityFactory.engine.addEntity(bomb);
            entities.add(bomb);
        }
        return entities;
    }

    public void addInvulnerableComponent(Entity player) {
        InvulnerableComponent invulnerableComponent = ComponentMapperHelper.invulnerable.get(player);
        if (invulnerableComponent != null) {
            invulnerableComponent.nbItems++;
        } else {
            player.add(entityFactory.engine.createComponent(InvulnerableComponent.class));
        }
    }

    public void removeInvulnerableComponent(Entity player) {
        InvulnerableComponent invulnerableComponent = ComponentMapperHelper.invulnerable.get(player);
        invulnerableComponent.nbItems--;
        if (invulnerableComponent.nbItems == 0) {
            player.remove(InvulnerableComponent.class);
        }
    }

    public Entity createEntityFireButton(float alpha, float posX, float posY) {
        Entity entity = entityFactory.engine.createEntity();
        SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.setTexture(entityFactory.atlasNoMask.createSprite("fire_button"), alpha, 0, 1f);
        component.zIndex = 100;
        component.setPosition(posX, posY);
        entity.add(component);
        entityFactory.engine.addEntity(entity);
        return entity;
    }

    public Entity createEntityBombButton(float alpha, float posX, float posY) {
        Entity entity = entityFactory.engine.createEntity();
        SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.zIndex = 100;
        component.setTexture(entityFactory.atlasNoMask.createSprite("bomb_button"), alpha, 0, 1f);
        component.setPosition(posX, posY);
        entity.add(component);
        entityFactory.engine.addEntity(entity);
        return entity;

    }

    public Entity createEntitiesPadController(float alpha, float scale, float posX, float posY) {
        Entity pad = entityFactory.engine.createEntity();
        SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.zIndex = 100;
        component.setTexture(entityFactory.atlasNoMask.createSprite("pad"), alpha, 0f, scale);
        component.setPosition(posX, posY);
        pad.add(component);
        entityFactory.engine.addEntity(pad);
        return pad;
    }


}
