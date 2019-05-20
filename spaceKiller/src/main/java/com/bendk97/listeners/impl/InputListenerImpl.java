/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.listeners.impl;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.bendk97.assets.GameAssets;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.components.helpers.Families;
import com.bendk97.entities.EntityFactory;
import com.bendk97.listeners.PlayerListener;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.GameAssets.SOUND_FIRE;
import static com.bendk97.pools.GamePools.poolVector2;

public class InputListenerImpl extends EntitySystem implements com.bendk97.listeners.InputListener {
    private static final float AUTOFIRE_DELAY = 1 / 10f;
    private final Entity player;
    private final EntityFactory entityFactory;
    private final GameAssets assets;
    private final boolean autoFire;
    private final float playerVelocity;
    private final PlayerListener playerListener;
    private long lastShoot = 0;
    private long lastShootSide = 0;
    private float autofireTrigger = 0;

    public InputListenerImpl(Entity player, PlayerListener playerListener, EntityFactory entityFactory,
                             GameAssets assets,
                             boolean isVirtualPad) {
        this.player = player;
        this.playerListener = playerListener;
        this.assets = assets;
        this.entityFactory = entityFactory;
        this.autoFire = isVirtualPad;
        this.playerVelocity = isVirtualPad ? PLAYER_VELOCITY_VIRTUAL_PAD : PLAYER_VELOCITY;
    }


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
        if (Families.player.matches(player)) {
            PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
            if (TimeUtils.timeSinceMillis(lastShoot) > playerComponent.fireDelay) {
                assets.playSound(SOUND_FIRE, 0.5f);
                entityFactory.playerActionsEntityFactory.createPlayerFire(player);
                lastShoot = TimeUtils.millis();
            }
            if (playerComponent.powerLevel.compareTo(PlayerComponent.PowerLevel.TRIPLE_SIDE) >= 0
                    && TimeUtils.timeSinceMillis(lastShootSide) > ComponentMapperHelper.player.get(player).fireDelaySide) {
                entityFactory.playerActionsEntityFactory.createPlayerFireSide(player);
                lastShootSide = TimeUtils.millis();
            }
        }
    }

    @Override
    public void dropBomb() {
        playerListener.dropBomb(player);
    }

    @Override
    public void goLeft() {
        ComponentMapperHelper.velocity.get(player).x = -playerVelocity;
        ComponentMapperHelper.state.get(player).set(GO_LEFT);
    }

    @Override
    public void goRight() {
        ComponentMapperHelper.velocity.get(player).x = playerVelocity;
        ComponentMapperHelper.state.get(player).set(GO_RIGHT);
    }

    @Override
    public void goTop() {
        ComponentMapperHelper.velocity.get(player).x = 0;
        ComponentMapperHelper.velocity.get(player).y = playerVelocity;
        ComponentMapperHelper.state.get(player).set(ANIMATION_MAIN);
    }

    @Override
    public void goDown() {
        ComponentMapperHelper.velocity.get(player).x = 0;
        ComponentMapperHelper.velocity.get(player).y = -playerVelocity;
        ComponentMapperHelper.state.get(player).set(ANIMATION_MAIN);
    }

    @Override
    public void goLeftTop() {
        Vector2 vector2 = poolVector2.getVector2(-1f, 1f);
        vector2.nor();
        ComponentMapperHelper.velocity.get(player).x = playerVelocity * vector2.x;
        ComponentMapperHelper.velocity.get(player).y = playerVelocity * vector2.y;
        ComponentMapperHelper.state.get(player).set(GO_LEFT);
        poolVector2.free(vector2);
    }

    @Override
    public void goLeftDown() {
        Vector2 vector2 = poolVector2.getVector2(-1f, -1f);
        vector2.nor();
        ComponentMapperHelper.velocity.get(player).x = playerVelocity * vector2.x;
        ComponentMapperHelper.velocity.get(player).y = playerVelocity * vector2.y;
        ComponentMapperHelper.state.get(player).set(GO_LEFT);
        poolVector2.free(vector2);
    }

    @Override
    public void goRightTop() {
        Vector2 vector2 = poolVector2.getVector2(1f, 1f);
        vector2.nor();
        ComponentMapperHelper.velocity.get(player).x = playerVelocity * vector2.x;
        ComponentMapperHelper.velocity.get(player).y = playerVelocity * vector2.y;
        ComponentMapperHelper.state.get(player).set(GO_RIGHT);
        poolVector2.free(vector2);
    }

    @Override
    public void goRightBottom() {
        Vector2 vector2 = poolVector2.getVector2(1f, -1f);
        vector2.nor();
        ComponentMapperHelper.velocity.get(player).x = playerVelocity * vector2.x;
        ComponentMapperHelper.velocity.get(player).y = playerVelocity * vector2.y;
        ComponentMapperHelper.state.get(player).set(GO_RIGHT);
        poolVector2.free(vector2);
    }

    @Override
    public void stop() {
        ComponentMapperHelper.velocity.get(player).x = 0;
        ComponentMapperHelper.velocity.get(player).y = 0;
        ComponentMapperHelper.state.get(player).set(ANIMATION_MAIN);
    }

}
