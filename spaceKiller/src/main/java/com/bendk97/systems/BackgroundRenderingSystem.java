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
import com.bendk97.components.PositionComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;

import static com.bendk97.SpaceKillerGameConstants.*;

public class BackgroundRenderingSystem extends SortedIteratingSystem {

    private final SpriteBatch batcher;

    public BackgroundRenderingSystem(SpriteBatch batcher, int priority) {
        super(Family.all(BackgroundComponent.class, PositionComponent.class).get(),
                (o1, o2) -> Integer.compare(ComponentMapperHelper.background.get(o1).zIndex, ComponentMapperHelper.background.get(o2).zIndex),
                priority);
        this.batcher = batcher;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BackgroundComponent backgroundComponent = ComponentMapperHelper.background.get(entity);
        PositionComponent positionComponent = ComponentMapperHelper.position.get(entity);
        batcher.draw(backgroundComponent.texture, -OFFSET_WIDTH, -20,
                (int) positionComponent.x(), ((int) positionComponent.y()) % (backgroundComponent.texture.getHeight()),
                (int) (WORLD_WIDTH), (int) (SCREEN_HEIGHT * 1.05f));
    }
}
