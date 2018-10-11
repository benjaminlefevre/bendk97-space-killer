/*
 * Developed by Benjamin Lefèvre
 * Last modified 11/10/18 22:32
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.player;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.bendk97.components.*;
import com.bendk97.entities.EntityFactory;
import com.bendk97.timer.PausableTimer;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP_PINGPONG;
import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.Assets.SOUND_BOMB_EXPLOSION;
import static com.bendk97.components.Mappers.position;
import static com.bendk97.components.Mappers.sprite;
import static com.bendk97.tweens.PositionComponentAccessor.POSITION_XY;
import static com.bendk97.tweens.PositionComponentAccessor.POSITION_Y;

public class PlayerActionsEntityFactory {

    private final EntityFactory entityFactory;

    public PlayerActionsEntityFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    public void createPlayerFire(Entity player) {
        Entity bullet = entityFactory.engine.createEntity();
        bullet.add(entityFactory.engine.createComponent(PlayerBulletComponent.class));
        PositionComponent positionComponent = entityFactory.engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = entityFactory.engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        PlayerComponent playerComponent = Mappers.player.get(player);
        spriteComponent.sprite = new Sprite(entityFactory.atlasMask.findRegion(playerComponent.powerLevel.regionName));
        bullet.add(spriteComponent);
        bullet.add(entityFactory.engine.createComponent(RemovableComponent.class));
        entityFactory.engine.addEntity(bullet);
        PositionComponent playerPosition = position.get(player);
        positionComponent.x = playerPosition.x + Mappers.sprite.get(player).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f;
        positionComponent.y = playerPosition.y + sprite.get(player).sprite.getHeight();
        velocityComponent.y = PLAYER_BULLET_VELOCITY;
        if (entityFactory.rayHandler != null) {
            entityFactory.createLight(bullet, playerComponent.powerLevel.color, spriteComponent.sprite.getWidth() * 7f);
        }
    }

    public void createPlayerFireSide(Entity player) {
        createPlayerLeftFire(player);
        createPlayerRightFire(player);
    }

    private void createPlayerLeftFire(Entity player) {
        Entity bullet = entityFactory.engine.createEntity();
        bullet.add(entityFactory.engine.createComponent(PlayerBulletComponent.class));
        PositionComponent positionComponent = entityFactory.engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = entityFactory.engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        PlayerComponent playerComponent = Mappers.player.get(player);
        spriteComponent.sprite = new Sprite(entityFactory.atlasMask.findRegion(playerComponent.powerLevel.leftRegionName));
        bullet.add(spriteComponent);
        bullet.add(entityFactory.engine.createComponent(RemovableComponent.class));
        entityFactory.engine.addEntity(bullet);
        PositionComponent playerPosition = position.get(player);
        positionComponent.x = playerPosition.x - spriteComponent.sprite.getWidth();
        positionComponent.y = playerPosition.y + sprite.get(player).sprite.getHeight();
        Vector2 direction = new Vector2(0, 1);
        direction.rotate(35f);
        direction.nor();
        direction.scl(PLAYER_BULLET_VELOCITY * 1.5f);
        velocityComponent.y = direction.y;
        velocityComponent.x = direction.x;
        if (entityFactory.rayHandler != null) {
            entityFactory.createLight(bullet, playerComponent.powerLevel.color, spriteComponent.sprite.getWidth() * 7f);
        }
    }


    private void createPlayerRightFire(Entity player) {
        Entity bullet = entityFactory.engine.createEntity();
        bullet.add(entityFactory.engine.createComponent(PlayerBulletComponent.class));
        PositionComponent positionComponent = entityFactory.engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = entityFactory.engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        PlayerComponent playerComponent = Mappers.player.get(player);
        spriteComponent.sprite = new Sprite(entityFactory.atlasMask.findRegion(playerComponent.powerLevel.rightRegionName));
        bullet.add(spriteComponent);
        bullet.add(entityFactory.engine.createComponent(RemovableComponent.class));
        entityFactory.engine.addEntity(bullet);
        PositionComponent playerPosition = position.get(player);
        positionComponent.x = playerPosition.x + sprite.get(player).sprite.getWidth();
        positionComponent.y = playerPosition.y + sprite.get(player).sprite.getHeight();
        Vector2 direction = new Vector2(0, 1);
        direction.rotate(-35f);
        direction.nor();
        direction.scl(PLAYER_BULLET_VELOCITY * 1.5f);
        velocityComponent.y = direction.y;
        velocityComponent.x = direction.x;
        if (entityFactory.rayHandler != null) {
            entityFactory.createLight(bullet, playerComponent.powerLevel.color, spriteComponent.sprite.getWidth() * 7f);
        }
    }

    public void createPlayerBomb(Entity player) {
        final Entity bomb = entityFactory.engine.createEntity();
        PositionComponent positionComponent = entityFactory.engine.createComponent(PositionComponent.class);
        bomb.add(positionComponent);
        SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        AnimationComponent animationComponent = entityFactory.engine.createComponent(AnimationComponent.class);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<>(FRAME_DURATION, entityFactory.atlasNoMask.createSprites("bomb"), LOOP));
        spriteComponent.sprite = animationComponent.animations.get(ANIMATION_MAIN).getKeyFrame(0);
        bomb.add(spriteComponent);
        bomb.add(animationComponent);
        bomb.add(entityFactory.engine.createComponent(StateComponent.class));
        entityFactory.engine.addEntity(bomb);
        PositionComponent playerPosition = position.get(player);
        positionComponent.x = playerPosition.x + Mappers.sprite.get(player).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f;
        positionComponent.y = playerPosition.y + Mappers.sprite.get(player).sprite.getHeight() >= SCREEN_HEIGHT * 3f / 4f ?
                playerPosition.y : playerPosition.y + sprite.get(player).sprite.getHeight();
        Tween.to(positionComponent, POSITION_XY, 0.6f).ease(Linear.INOUT)
                .target(SCREEN_WIDTH / 2f - spriteComponent.sprite.getWidth() / 2f, SCREEN_HEIGHT * 3f / 4f)
                .setCallback((event, baseTween) -> {
                    if (event == TweenCallback.COMPLETE) {
                        createBombExplosion(bomb);
                        entityFactory.engine.removeEntity(bomb);
                    }
                })
                .start(entityFactory.tweenManager);
    }

    protected void createBombExplosion(Entity bomb) {
        final Entity bombExplosion = entityFactory.engine.createEntity();
        PositionComponent positionComponent = entityFactory.engine.createComponent(PositionComponent.class);
        bombExplosion.add(positionComponent);
        final SpriteComponent spriteComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        AnimationComponent animationComponent = entityFactory.engine.createComponent(AnimationComponent.class);
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<>(FRAME_DURATION_BOMB_EXPLOSION,
                entityFactory.atlasNoMask.createSprites("bomb_explosion"), LOOP_PINGPONG));
        spriteComponent.sprite = entityFactory.atlasNoMask.createSprite("bomb_explosion", 6);
        spriteComponent.zIndex = 100;
        bombExplosion.add(spriteComponent);
        bombExplosion.add(animationComponent);
        bombExplosion.add(entityFactory.engine.createComponent(StateComponent.class));
        entityFactory.engine.addEntity(bombExplosion);
        entityFactory.assets.playSound(SOUND_BOMB_EXPLOSION);
        PositionComponent bombPosition = position.get(bomb);
        positionComponent.x = bombPosition.x - spriteComponent.sprite.getWidth() / 2f;
        positionComponent.y = bombPosition.y - spriteComponent.sprite.getHeight() / 2f;
        Tween.to(positionComponent, POSITION_Y, 0.7f).ease(Linear.INOUT)
                .target(bombPosition.y - spriteComponent.sprite.getHeight() / 2f)
                .setCallback((event, baseTween) -> {
                    if (event == TweenCallback.COMPLETE) {
                        bombExplosion.add(entityFactory.engine.createComponent(BombExplosionComponent.class));
                    }
                })
                .start(entityFactory.tweenManager);
        entityFactory.screenShake.shake(20f, 1f, false);
        PausableTimer.schedule(new PausableTimer.Task() {
            @Override
            public void run() {
                if (entityFactory.rayHandler != null) {
                    entityFactory.createLight(bombExplosion, new Color(1f, 1f, 1f, 0.8f), spriteComponent.sprite.getHeight() * 20f);
                }
            }
        }, 0.6f);
    }

}
