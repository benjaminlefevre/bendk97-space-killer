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
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.timer.PausableTimer;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP_PINGPONG;
import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.Assets.SOUND_BOMB_EXPLOSION;
import static com.bendk97.components.helpers.ComponentMapperHelper.position;
import static com.bendk97.components.helpers.ComponentMapperHelper.sprite;
import static com.bendk97.tweens.PositionComponentAccessor.POSITION_XY;
import static com.bendk97.tweens.PositionComponentAccessor.POSITION_Y;

public class PlayerActionsEntityFactory {

    private final EntityFactory entityFactory;

    public PlayerActionsEntityFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    public void createPlayerFire(Entity player) {
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        SpriteComponent spriteBulletComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        VelocityComponent velocityBulletComponent = entityFactory.engine.createComponent(VelocityComponent.class);
        PositionComponent positionBulletComponent = entityFactory.engine.createComponent(PositionComponent.class);
        Entity bullet = createBullet(playerComponent.powerLevel.bulletRegionName, spriteBulletComponent, velocityBulletComponent, positionBulletComponent);
        PositionComponent playerPosition = position.get(player);
        positionBulletComponent.x = playerPosition.x + ComponentMapperHelper.sprite.get(player).sprite.getWidth() / 2f - spriteBulletComponent.sprite.getWidth() / 2f;
        positionBulletComponent.y = playerPosition.y + sprite.get(player).sprite.getHeight();
        velocityBulletComponent.y = PLAYER_BULLET_VELOCITY;
        if (entityFactory.rayHandler != null) {
            entityFactory.createLight(bullet, playerComponent.powerLevel.color, spriteBulletComponent.sprite.getWidth() * 7f);
        }
    }

    public void createPlayerFireSide(Entity player) {
        createPlayerLeftFire(player);
        createPlayerRightFire(player);
    }

    private void createPlayerLeftFire(Entity player) {
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        SpriteComponent spriteBulletComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        VelocityComponent velocityBulletComponent = entityFactory.engine.createComponent(VelocityComponent.class);
        PositionComponent positionBulletComponent = entityFactory.engine.createComponent(PositionComponent.class);
        Entity bullet = createBullet(playerComponent.powerLevel.bulletLeftSidedRegionName, spriteBulletComponent, velocityBulletComponent, positionBulletComponent);
        PositionComponent playerPosition = position.get(player);
        positionBulletComponent.x = playerPosition.x - spriteBulletComponent.sprite.getWidth();
        positionBulletComponent.y = playerPosition.y + sprite.get(player).sprite.getHeight();
        Vector2 direction = new Vector2(0, 1);
        direction.rotate(35f);
        direction.nor();
        direction.scl(PLAYER_BULLET_VELOCITY * 1.5f);
        velocityBulletComponent.y = direction.y;
        velocityBulletComponent.x = direction.x;
        if (entityFactory.rayHandler != null) {
            entityFactory.createLight(bullet, playerComponent.powerLevel.color, spriteBulletComponent.sprite.getWidth() * 7f);
        }
    }


    private void createPlayerRightFire(Entity player) {
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        SpriteComponent spriteBulletComponent = entityFactory.engine.createComponent(SpriteComponent.class);
        VelocityComponent velocityBulletComponent = entityFactory.engine.createComponent(VelocityComponent.class);
        PositionComponent positionBulletComponent = entityFactory.engine.createComponent(PositionComponent.class);
        Entity bullet = createBullet(playerComponent.powerLevel.bulletRightSidedRegionName, spriteBulletComponent, velocityBulletComponent, positionBulletComponent);
        PositionComponent playerPosition = position.get(player);
        positionBulletComponent.x = playerPosition.x + sprite.get(player).sprite.getWidth();
        positionBulletComponent.y = playerPosition.y + sprite.get(player).sprite.getHeight();
        Vector2 direction = new Vector2(0, 1);
        direction.rotate(-35f);
        direction.nor();
        direction.scl(PLAYER_BULLET_VELOCITY * 1.5f);
        velocityBulletComponent.y = direction.y;
        velocityBulletComponent.x = direction.x;
        if (entityFactory.rayHandler != null) {
            entityFactory.createLight(bullet, playerComponent.powerLevel.color, spriteBulletComponent.sprite.getWidth() * 7f);
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
        positionComponent.x = playerPosition.x + ComponentMapperHelper.sprite.get(player).sprite.getWidth() / 2f - spriteComponent.sprite.getWidth() / 2f;
        positionComponent.y = playerPosition.y + ComponentMapperHelper.sprite.get(player).sprite.getHeight() >= SCREEN_HEIGHT * 3f / 4f ?
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

    private Entity createBullet(String bulletSpriteName, SpriteComponent spritebulletComponent, VelocityComponent velocityBulletComponent, PositionComponent positionBulletComponent) {
        Entity bullet = entityFactory.engine.createEntity();
        bullet.add(entityFactory.engine.createComponent(PlayerBulletComponent.class));
        bullet.add(positionBulletComponent);
        bullet.add(velocityBulletComponent);
        spritebulletComponent.sprite = new Sprite(entityFactory.atlasMask.findRegion(bulletSpriteName));
        bullet.add(spritebulletComponent);
        bullet.add(entityFactory.engine.createComponent(RemovableComponent.class));
        entityFactory.engine.addEntity(bullet);
        return bullet;
    }

}
