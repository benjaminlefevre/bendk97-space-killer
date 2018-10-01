/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.listeners.impl;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.bendk97.assets.Assets;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.Mappers;
import com.bendk97.components.PlayerComponent;
import com.bendk97.entities.EntityFactory;
import com.bendk97.listeners.PlayerListener;

import static com.bendk97.SpaceKillerGameConstants.*;

public class InputListenerImpl extends EntitySystem implements com.bendk97.listeners.InputListener {
    private static final float AUTOFIRE_DELAY = 1 / 10f;
    private final Family playerFamily = Family.one(PlayerComponent.class).exclude(GameOverComponent.class).get();
    private final Entity player;
    private final com.bendk97.entities.EntityFactory entityFactory;
    private final Assets assets;
    private boolean autoFire = true;
    private final com.bendk97.listeners.PlayerListener playerListener;

    public InputListenerImpl(Entity player, PlayerListener playerListener, EntityFactory entityFactory,
                             Assets assets,
                             boolean autoFire) {
        this.player = player;
        this.playerListener = playerListener;
        this.assets = assets;
        this.entityFactory = entityFactory;
        this.autoFire = autoFire;
    }

    private long lastShoot = 0;
    private long lastShootSide = 0;

    private float autofireTrigger = 0;

    @Override
    public void update(float deltaTime) {
        if (autoFire) {
            if (autofireTrigger > AUTOFIRE_DELAY) {
                fire();
                autofireTrigger = 0f;
            } else {
                autofireTrigger += deltaTime;
            }
        }
    }

    @Override
    public void fire() {
        if (playerFamily.matches(player)) {
            PlayerComponent playerComponent = Mappers.player.get(player);
            if (TimeUtils.timeSinceMillis(lastShoot) > playerComponent.fireDelay) {
                assets.playSound(Assets.SOUND_FIRE, 0.5f);
                entityFactory.createPlayerFire(player);
                lastShoot = TimeUtils.millis();
            }
            if (playerComponent.powerLevel.compareTo(PlayerComponent.PowerLevel.TRIPLE_SIDE) >= 0
                    && TimeUtils.timeSinceMillis(lastShootSide) > Mappers.player.get(player).fireDelaySide) {
                entityFactory.createPlayerFireSide(player);
                lastShootSide = TimeUtils.millis();
            }
        }
    }

    @Override
    public void dropBomb() {
        PlayerComponent playerComponent = Mappers.player.get(player);
        if (playerFamily.matches(player) && playerComponent.hasBombs()) {
            assets.playSound(Assets.SOUND_BOMB_DROP);
            playerComponent.useBomb();
            entityFactory.createPlayerBomb(player);
            playerListener.dropBomb();
        }
    }

    @Override
    public void goLeft() {
        Mappers.velocity.get(player).x = -PLAYER_VELOCITY;
        Mappers.state.get(player).set(GO_LEFT);
    }

    @Override
    public void goRight() {
        Mappers.velocity.get(player).x = PLAYER_VELOCITY;
        Mappers.state.get(player).set(GO_RIGHT);
    }

    @Override
    public void goTop() {
        Mappers.velocity.get(player).x = 0;
        Mappers.velocity.get(player).y = PLAYER_VELOCITY;
        Mappers.state.get(player).set(ANIMATION_MAIN);
    }

    @Override
    public void goDown() {
        Mappers.velocity.get(player).x = 0;
        Mappers.velocity.get(player).y = -PLAYER_VELOCITY;
        Mappers.state.get(player).set(ANIMATION_MAIN);
    }

    @Override
    public void goLeftTop() {
        Vector2 vector2 = new Vector2(-1f, 1f);
        vector2.nor();
        Mappers.velocity.get(player).x = PLAYER_VELOCITY * vector2.x;
        Mappers.velocity.get(player).y = PLAYER_VELOCITY * vector2.y;
        Mappers.state.get(player).set(GO_LEFT);
    }

    @Override
    public void goLeftDown() {
        Vector2 vector2 = new Vector2(-1f, -1f);
        vector2.nor();
        Mappers.velocity.get(player).x = PLAYER_VELOCITY * vector2.x;
        Mappers.velocity.get(player).y = PLAYER_VELOCITY * vector2.y;
        Mappers.state.get(player).set(GO_LEFT);
    }

    @Override
    public void goRightTop() {
        Vector2 vector2 = new Vector2(1f, 1f);
        vector2.nor();
        Mappers.velocity.get(player).x = PLAYER_VELOCITY * vector2.x;
        Mappers.velocity.get(player).y = PLAYER_VELOCITY * vector2.y;
        Mappers.state.get(player).set(GO_RIGHT);
    }

    @Override
    public void goRightBottom() {
        Vector2 vector2 = new Vector2(1f, -1f);
        vector2.nor();
        Mappers.velocity.get(player).x = PLAYER_VELOCITY * vector2.x;
        Mappers.velocity.get(player).y = PLAYER_VELOCITY * vector2.y;
        Mappers.state.get(player).set(GO_RIGHT);
    }

    @Override
    public void stop() {
        Mappers.velocity.get(player).x = 0;
        Mappers.velocity.get(player).y = 0;
        Mappers.state.get(player).set(ANIMATION_MAIN);
    }

}
