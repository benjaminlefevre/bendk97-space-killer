package com.benk97.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.benk97.components.DynamicSpriteComponent;
import com.benk97.components.PositionComponent;
import com.benk97.components.RemovableComponent;
import com.benk97.components.VelocityComponent;
import com.benk97.inputs.InputHandler;

import static com.benk97.SpaceKillerGameConstants.PLAYER_BULLET_VELOCITY;
import static com.benk97.SpaceKillerGameConstants.PLAYER_VELOCITY;

public class InputHandlerSystem extends EntitySystem implements InputHandler {
    private Entity player;
    private PooledEngine engine;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<DynamicSpriteComponent> dm = ComponentMapper.getFor(DynamicSpriteComponent.class);


    public InputHandlerSystem(Entity entity, PooledEngine engine) {
        this.player = entity;
        this.engine = engine;
    }

    @Override
    public void fire() {
        Entity bullet = engine.createEntity();
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        bullet.add(positionComponent);
        VelocityComponent velocityComponent = engine.createComponent(VelocityComponent.class);
        bullet.add(velocityComponent);
        DynamicSpriteComponent dynamicSpriteComponent = engine.createComponent(DynamicSpriteComponent.class);
        bullet.add(dynamicSpriteComponent);
        bullet.add(engine.createComponent(RemovableComponent.class));
        engine.addEntity(bullet);
        PositionComponent playerPosition = pm.get(player);
        positionComponent.x = playerPosition.x;
        positionComponent.y = playerPosition.y + dm.get(player).getHeight();
        velocityComponent.y = PLAYER_BULLET_VELOCITY;
        dynamicSpriteComponent.setTexture(new Texture(Gdx.files.internal("gfx/bullet.png")),
                1,new int[]{0}, null);

    }

    @Override
    public void goLeft() {
        vm.get(player).x = -PLAYER_VELOCITY;
        dm.get(player).goLeft();
    }

    @Override
    public void goRight() {
        vm.get(player).x = PLAYER_VELOCITY;
        dm.get(player).goRight();
    }

    @Override
    public void goTop() {
        vm.get(player).x = 0;
        vm.get(player).y = PLAYER_VELOCITY;
        dm.get(player).goAhead();
    }

    @Override
    public void goDown() {
        vm.get(player).x = 0;
        vm.get(player).y = -PLAYER_VELOCITY;
        dm.get(player).goAhead();
    }

    @Override
    public void goLeftTop() {
        Vector2 vector2 = new Vector2(-1f, 1f);
        vector2.nor();
        vm.get(player).x = PLAYER_VELOCITY * vector2.x;
        vm.get(player).y = PLAYER_VELOCITY * vector2.y;
        dm.get(player).goLeft();
    }

    @Override
    public void goLeftDown() {
        Vector2 vector2 = new Vector2(-1f, -1f);
        vector2.nor();
        vm.get(player).x = PLAYER_VELOCITY * vector2.x;
        vm.get(player).y = PLAYER_VELOCITY * vector2.y;
        dm.get(player).goLeft();
    }

    @Override
    public void goRightTop() {
        Vector2 vector2 = new Vector2(1f, 1f);
        vector2.nor();
        vm.get(player).x = PLAYER_VELOCITY * vector2.x;
        vm.get(player).y = PLAYER_VELOCITY * vector2.y;
        dm.get(player).goRight();
    }

    @Override
    public void goRightBottom() {
        Vector2 vector2 = new Vector2(1f, -1f);
        vector2.nor();
        vm.get(player).x = PLAYER_VELOCITY * vector2.x;
        vm.get(player).y = PLAYER_VELOCITY * vector2.y;
        dm.get(player).goRight();
    }

    @Override
    public void stop() {
        vm.get(player).x = 0;
        vm.get(player).y = 0;
        dm.get(player).goAhead();
    }
}
