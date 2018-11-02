/*
 * Developed by Benjamin Lefèvre
 * Last modified 11/10/18 22:35
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.player;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.bendk97.components.*;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.bendk97.SpaceKillerGameConstants.ANIMATION_MAIN;
import static com.bendk97.SpaceKillerGameConstants.FRAME_DURATION_POWER_UP;
import static com.bendk97.pools.GamePools.poolSprite;
import static com.bendk97.tweens.PositionComponentTweenAccessor.POSITION_Y;
import static com.bendk97.tweens.SpriteComponentTweenAccessor.ALPHA;

public class BonusEntityFactory {

    private final EntityFactory entityFactory;

    public BonusEntityFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    public void createPowerUp(Entity squadron) {
        final Entity powerUp = entityFactory.engine.createEntity();
        powerUp.add(entityFactory.engine.createComponent(PowerUpComponent.class));
        PositionComponent position = entityFactory.engine.createComponent(PositionComponent.class);
        powerUp.add(position);
        powerUp.add(entityFactory.engine.createComponent(VelocityComponent.class));
        AnimationComponent animationComponent = entityFactory.engine.createComponent(AnimationComponent.class);
        Array<Sprite> sprites = poolSprite.getSprites(entityFactory.levelAtlas.findRegions("power-up"));
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<>(FRAME_DURATION_POWER_UP, sprites, LOOP));
        powerUp.add(animationComponent);
        SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.sprite = sprites.get(0);
        powerUp.add(component);
        position.setXY(ComponentMapperHelper.squadron.get(squadron).lastKilledPosition.x,
                ComponentMapperHelper.squadron.get(squadron).lastKilledPosition.y);
        powerUp.add(entityFactory.engine.createComponent(StateComponent.class));
        tweenBonus(powerUp, position, component);
        entityFactory.engine.addEntity(powerUp);
    }


    public void createShieldUp(Entity squadron) {
        final Entity shieldUp = entityFactory.engine.createEntity();
        shieldUp.add(entityFactory.engine.createComponent(ShieldUpComponent.class));
        PositionComponent position = entityFactory.engine.createComponent(PositionComponent.class);
        shieldUp.add(position);
        shieldUp.add(entityFactory.engine.createComponent(VelocityComponent.class));
        AnimationComponent animationComponent = entityFactory.engine.createComponent(AnimationComponent.class);
        Array<Sprite> sprites = poolSprite.getSprites(entityFactory.levelAtlas.findRegions("shieldup"));
        animationComponent.animations.put(ANIMATION_MAIN, new Animation<>(FRAME_DURATION_POWER_UP, sprites, LOOP));
        shieldUp.add(animationComponent);
        SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.sprite = sprites.get(0);
        shieldUp.add(component);
        position.setXY(ComponentMapperHelper.squadron.get(squadron).lastKilledPosition.x,
                ComponentMapperHelper.squadron.get(squadron).lastKilledPosition.y);
        shieldUp.add(entityFactory.engine.createComponent(StateComponent.class));
        tweenBonus(shieldUp, position, component);
        entityFactory.engine.addEntity(shieldUp);
    }

    public void createBombUp(Entity squadron) {
        final Entity bombUp = entityFactory.engine.createEntity();
        bombUp.add(entityFactory.engine.createComponent(BombUpComponent.class));
        PositionComponent position = entityFactory.engine.createComponent(PositionComponent.class);
        bombUp.add(position);
        bombUp.add(entityFactory.engine.createComponent(VelocityComponent.class));
        SpriteComponent component = entityFactory.engine.createComponent(SpriteComponent.class);
        component.sprite = poolSprite.getSprite(entityFactory.levelAtlas.findRegion("bombUp"));
        bombUp.add(component);
        position.setXY(ComponentMapperHelper.squadron.get(squadron).lastKilledPosition.x,
                ComponentMapperHelper.squadron.get(squadron).lastKilledPosition.y);
        tweenBonus(bombUp, position, component);
        entityFactory.engine.addEntity(bombUp);
    }


    private void tweenBonus(Entity powerUp, PositionComponent position, SpriteComponent spriteComponent) {
        Timeline.createSequence()
                .beginParallel()
                .push(Tween.to(position, POSITION_Y, 8f).ease(Linear.INOUT).target(50f))
                .push(Tween.to(spriteComponent, ALPHA, 0.5f).delay(4f).ease(Linear.INOUT).target(0f).repeat(8, 0f))
                .end()
                .setCallback((i, baseTween) -> {
                    if (i == TweenCallback.COMPLETE) {
                        entityFactory.engine.removeEntity(powerUp);
                    }
                })
                .start(entityFactory.tweenManager);
    }

}
