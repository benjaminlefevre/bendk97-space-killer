/*
 * Developed by Benjamin Lefèvre
 * Last modified 11/10/18 21:32
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.bendk97.SpaceKillerGameConstants;
import com.bendk97.components.*;

import static com.bendk97.pools.GamePools.poolSprite;

public class StageSetEntityFactory {
    private final PooledEngine engine;

    protected StageSetEntityFactory(PooledEngine engine) {
        this.engine = engine;
    }

    public Entity createBackground(Texture texture, float velocity) {
        return createBackground(texture, 0, velocity);
    }

    public Entity createBackground(Texture texture, int zIndex, float velocity) {
        Entity background = engine.createEntity();
        BackgroundComponent component = engine.createComponent(BackgroundComponent.class);
        component.setTexture(texture);
        component.zIndex = zIndex;
        background.add(component);
        background.add(engine.createComponent(PositionComponent.class));
        VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
        background.add(velocityComponent);
        velocityComponent.y = velocity;
        engine.addEntity(background);
        return background;
    }

    public void createForeground(Texture texture, float velocity) {
        Entity foreground = engine.createEntity();
        SpriteComponent component = engine.createComponent(SpriteComponent.class);
        component.setTexture(poolSprite.getSprite(texture), 1.0f, 0, 1.0f);
        component.zIndex = 100;
        foreground.add(component);
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        foreground.add(positionComponent);
        VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
        foreground.add(velocityComponent);
        RemovableComponent removableComponent = engine.createComponent(RemovableComponent.class);
        removableComponent.setDuration(3.0f);
        foreground.add(removableComponent);
        positionComponent.setXY(0f, SpaceKillerGameConstants.SCREEN_HEIGHT + 20f);
        velocityComponent.y = -velocity;
        engine.addEntity(foreground);
    }
}