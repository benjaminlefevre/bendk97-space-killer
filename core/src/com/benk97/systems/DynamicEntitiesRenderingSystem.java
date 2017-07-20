package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.benk97.components.GameOverComponent;
import com.benk97.components.Mappers;
import com.benk97.components.PositionComponent;
import com.benk97.components.SpriteComponent;

import java.util.Comparator;

import static com.benk97.SpaceKillerGameConstants.DEBUG_SHAPE;

public class DynamicEntitiesRenderingSystem extends SortedIteratingSystem {
    private SpriteBatch batcher;
    private ShapeRenderer shapeRenderer;

    public DynamicEntitiesRenderingSystem(SpriteBatch batcher, ShapeRenderer shapeRenderer, int priority) {
        super(Family.all(SpriteComponent.class, PositionComponent.class).exclude(GameOverComponent.class).get(),
                new Comparator<Entity>() {
                    @Override
                    public int compare(Entity o1, Entity o2) {
                        return Integer.valueOf(Mappers.sprite.get(o1).zIndex).compareTo(Integer.valueOf(Mappers.sprite.get(o2).zIndex));
                    }
                },
                priority);
        this.batcher = batcher;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.position.get(entity);
        SpriteComponent spriteComponent = Mappers.sprite.get(entity);
        Sprite sprite = spriteComponent.sprite;
        sprite.setPosition(position.x, position.y);
        sprite.draw(batcher, spriteComponent.alpha);
        if (DEBUG_SHAPE) {
            batcher.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (int i = 0; i < spriteComponent.getPolygonBounds().size; ++i) {
                shapeRenderer.polygon(spriteComponent.getPolygonBounds().get(i).getTransformedVertices());
            }
            shapeRenderer.end();
            batcher.begin();
        }
    }
}
