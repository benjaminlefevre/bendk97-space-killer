package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.benk97.components.GameOverComponent;
import com.benk97.components.Mappers;
import com.benk97.components.PositionComponent;
import com.benk97.components.SpriteComponent;

public class DynamicEntitiesRenderingSystem extends IteratingSystem {
    private SpriteBatch batcher;

    public DynamicEntitiesRenderingSystem(SpriteBatch batcher, int priority) {
        super(Family.all(SpriteComponent.class, PositionComponent.class).exclude(GameOverComponent.class).get(), priority);
        this.batcher = batcher;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.position.get(entity);
        SpriteComponent spriteComponent = Mappers.sprite.get(entity);
        Sprite sprite = spriteComponent.sprite;
        sprite.setPosition(position.x, position.y);
        sprite.draw(batcher, spriteComponent.alpha);
    }
}
