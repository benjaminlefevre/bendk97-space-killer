package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benk97.components.BackgroundComponent;
import com.benk97.components.Mappers;
import com.benk97.components.PositionComponent;

import java.util.Comparator;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;
import static com.benk97.components.Mappers.background;

public class BackgroundRenderingSystem extends SortedIteratingSystem {

    private SpriteBatch batcher;

    public BackgroundRenderingSystem(SpriteBatch batcher, int priority) {
        super(Family.all(BackgroundComponent.class, PositionComponent.class).get(),
                new Comparator<Entity>() {
                    @Override
                    public int compare(Entity o1, Entity o2) {
                        return Integer.valueOf(Mappers.background.get(o1).zIndex).compareTo(Integer.valueOf(Mappers.background.get(o2).zIndex));
                    }
                },
                priority);
        this.batcher = batcher;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BackgroundComponent backgroundComponent = background.get(entity);
        PositionComponent positionComponent = Mappers.position.get(entity);
        batcher.draw(backgroundComponent.texture, 0, 0, (int) positionComponent.x, ((int) positionComponent.y) % (backgroundComponent.texture.getHeight()), (int) SCREEN_WIDTH, (int) SCREEN_HEIGHT);
    }
}
