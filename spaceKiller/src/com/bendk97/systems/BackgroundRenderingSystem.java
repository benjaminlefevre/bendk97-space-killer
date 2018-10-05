/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bendk97.components.BackgroundComponent;
import com.bendk97.components.Mappers;
import com.bendk97.components.PositionComponent;

import java.util.Comparator;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class BackgroundRenderingSystem extends SortedIteratingSystem {

    private final SpriteBatch batcher;

    public BackgroundRenderingSystem(SpriteBatch batcher, int priority) {
        super(Family.all(BackgroundComponent.class, PositionComponent.class).get(),
                new Comparator<Entity>() {
                    @Override
                    public int compare(Entity o1, Entity o2) {
                        return Integer.valueOf(Mappers.background.get(o1).zIndex).compareTo(Mappers.background.get(o2).zIndex);
                    }
                },
                priority);
        this.batcher = batcher;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BackgroundComponent backgroundComponent = Mappers.background.get(entity);
        PositionComponent positionComponent = Mappers.position.get(entity);
        batcher.draw(backgroundComponent.texture, 0, -20f,
                (int) positionComponent.x, ((int) positionComponent.y) % (backgroundComponent.texture.getHeight()),
                (int) (SCREEN_WIDTH), (int) (SCREEN_HEIGHT * 1.05f));
    }
}
