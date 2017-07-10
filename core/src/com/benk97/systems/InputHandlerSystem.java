package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.benk97.components.Mappers;
import com.benk97.inputs.InputHandler;
import com.benk97.screens.LevelScreen;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.SOUND_FIRE;
import static com.benk97.components.Mappers.velocity;

public class InputHandlerSystem extends EntitySystem implements InputHandler {
    private Entity player;
    private PooledEngine engine;
    private LevelScreen screen;


    public InputHandlerSystem(Entity entity, PooledEngine engine, LevelScreen screen) {
        super(0);
        this.player = entity;
        this.engine = engine;
        this.screen = screen;
    }

    @Override
    public void fire() {
        screen.assets.playSound(SOUND_FIRE);
        screen.playerFires(player);
    }

    @Override
    public void goLeft() {
        velocity.get(player).x = -PLAYER_VELOCITY;
        Mappers.state.get(player).set(GO_LEFT);
    }

    @Override
    public void goRight() {
        velocity.get(player).x = PLAYER_VELOCITY;
        Mappers.state.get(player).set(GO_RIGHT);
    }

    @Override
    public void goTop() {
        velocity.get(player).x = 0;
        velocity.get(player).y = PLAYER_VELOCITY;
        Mappers.state.get(player).set(GO_AHEAD);
    }

    @Override
    public void goDown() {
        velocity.get(player).x = 0;
        velocity.get(player).y = -PLAYER_VELOCITY;
        Mappers.state.get(player).set(GO_AHEAD);
    }

    @Override
    public void goLeftTop() {
        Vector2 vector2 = new Vector2(-1f, 1f);
        vector2.nor();
        velocity.get(player).x = PLAYER_VELOCITY * vector2.x;
        velocity.get(player).y = PLAYER_VELOCITY * vector2.y;
        Mappers.state.get(player).set(GO_LEFT);
    }

    @Override
    public void goLeftDown() {
        Vector2 vector2 = new Vector2(-1f, -1f);
        vector2.nor();
        velocity.get(player).x = PLAYER_VELOCITY * vector2.x;
        velocity.get(player).y = PLAYER_VELOCITY * vector2.y;
        Mappers.state.get(player).set(GO_LEFT);
    }

    @Override
    public void goRightTop() {
        Vector2 vector2 = new Vector2(1f, 1f);
        vector2.nor();
        velocity.get(player).x = PLAYER_VELOCITY * vector2.x;
        velocity.get(player).y = PLAYER_VELOCITY * vector2.y;
        Mappers.state.get(player).set(GO_RIGHT);
    }

    @Override
    public void goRightBottom() {
        Vector2 vector2 = new Vector2(1f, -1f);
        vector2.nor();
        velocity.get(player).x = PLAYER_VELOCITY * vector2.x;
        velocity.get(player).y = PLAYER_VELOCITY * vector2.y;
        Mappers.state.get(player).set(GO_RIGHT);
    }

    @Override
    public void stop() {
        velocity.get(player).x = 0;
        velocity.get(player).y = 0;
        Mappers.state.get(player).set(GO_AHEAD);
    }
}
