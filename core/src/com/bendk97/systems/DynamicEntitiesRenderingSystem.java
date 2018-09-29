/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.Mappers;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.SpriteComponent;

import java.util.Comparator;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class DynamicEntitiesRenderingSystem extends SortedIteratingSystem {
    private SpriteBatch batcher;

    public DynamicEntitiesRenderingSystem(SpriteBatch batcher, int priority) {
        super(Family.all(SpriteComponent.class, PositionComponent.class).exclude(GameOverComponent.class).get(),
                new Comparator<Entity>() {
                    @Override
                    public int compare(Entity o1, Entity o2) {
                        return Integer.valueOf(Mappers.sprite.get(o1).zIndex).compareTo(Integer.valueOf(Mappers.sprite.get(o2).zIndex));
                    }
                },
                priority);
        this.batcher = batcher;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.position.get(entity);
        SpriteComponent spriteComponent = Mappers.sprite.get(entity);
        Sprite sprite = spriteComponent.sprite;
        sprite.setPosition(position.x, position.y);
        if((sprite.getX()+sprite.getWidth()<0)
                || sprite.getX() > SCREEN_WIDTH
                || sprite.getY() > SCREEN_HEIGHT
                || sprite.getY() + sprite.getHeight() < 0){
            return;
        }
        sprite.draw(batcher, spriteComponent.alpha);
    }
}
