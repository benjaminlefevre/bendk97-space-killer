package com.bendk97.listeners.impl;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.bendk97.components.GameOverComponent;

import static com.bendk97.SpaceKillerGameConstants.*;

public class InputListenerImpl extends EntitySystem implements com.bendk97.listeners.InputListener {
    private Family playerFamily = Family.one(com.bendk97.components.PlayerComponent.class).exclude(GameOverComponent.class).get();
    private Entity player;
    private com.bendk97.entities.EntityFactory entityFactory;
    private com.bendk97.assets.Assets assets;
    private boolean autoFire = true;
    private com.bendk97.listeners.PlayerListener playerListener;

    public InputListenerImpl(Entity player, com.bendk97.listeners.PlayerListener playerListener, com.bendk97.entities.EntityFactory entityFactory, com.bendk97.assets.Assets assets,
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
    private final float autofireDelay = 1 / 10f;

    @Override
    public void update(float deltaTime) {
        if (autoFire) {
            if (autofireTrigger > autofireDelay) {
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
            com.bendk97.components.PlayerComponent playerComponent = com.bendk97.components.Mappers.player.get(player);
            if (TimeUtils.timeSinceMillis(lastShoot) > playerComponent.fireDelay) {
                assets.playSound(com.bendk97.assets.Assets.SOUND_FIRE, 0.5f);
                entityFactory.createPlayerFire(player);
                lastShoot = TimeUtils.millis();
            }
            if (playerComponent.powerLevel.compareTo(com.bendk97.components.PlayerComponent.PowerLevel.TRIPLE_SIDE) >= 0
                    && TimeUtils.timeSinceMillis(lastShootSide) > com.bendk97.components.Mappers.player.get(player).fireDelaySide) {
                entityFactory.createPlayerFireSide(player);
                lastShootSide = TimeUtils.millis();
            }
        }
    }

    @Override
    public void dropBomb() {
        com.bendk97.components.PlayerComponent playerComponent = com.bendk97.components.Mappers.player.get(player);
        if (playerFamily.matches(player) && playerComponent.hasBombs()) {
            assets.playSound(com.bendk97.assets.Assets.SOUND_BOMB_DROP);
            playerComponent.useBomb();
            entityFactory.createPlayerBomb(player);
            playerListener.dropBomb();
        }
    }

    @Override
    public void goLeft() {
        com.bendk97.components.Mappers.velocity.get(player).x = -PLAYER_VELOCITY;
        com.bendk97.components.Mappers.state.get(player).set(GO_LEFT);
    }

    @Override
    public void goRight() {
        com.bendk97.components.Mappers.velocity.get(player).x = PLAYER_VELOCITY;
        com.bendk97.components.Mappers.state.get(player).set(GO_RIGHT);
    }

    @Override
    public void goTop() {
        com.bendk97.components.Mappers.velocity.get(player).x = 0;
        com.bendk97.components.Mappers.velocity.get(player).y = PLAYER_VELOCITY;
        com.bendk97.components.Mappers.state.get(player).set(ANIMATION_MAIN);
    }

    @Override
    public void goDown() {
        com.bendk97.components.Mappers.velocity.get(player).x = 0;
        com.bendk97.components.Mappers.velocity.get(player).y = -PLAYER_VELOCITY;
        com.bendk97.components.Mappers.state.get(player).set(ANIMATION_MAIN);
    }

    @Override
    public void goLeftTop() {
        Vector2 vector2 = new Vector2(-1f, 1f);
        vector2.nor();
        com.bendk97.components.Mappers.velocity.get(player).x = PLAYER_VELOCITY * vector2.x;
        com.bendk97.components.Mappers.velocity.get(player).y = PLAYER_VELOCITY * vector2.y;
        com.bendk97.components.Mappers.state.get(player).set(GO_LEFT);
    }

    @Override
    public void goLeftDown() {
        Vector2 vector2 = new Vector2(-1f, -1f);
        vector2.nor();
        com.bendk97.components.Mappers.velocity.get(player).x = PLAYER_VELOCITY * vector2.x;
        com.bendk97.components.Mappers.velocity.get(player).y = PLAYER_VELOCITY * vector2.y;
        com.bendk97.components.Mappers.state.get(player).set(GO_LEFT);
    }

    @Override
    public void goRightTop() {
        Vector2 vector2 = new Vector2(1f, 1f);
        vector2.nor();
        com.bendk97.components.Mappers.velocity.get(player).x = PLAYER_VELOCITY * vector2.x;
        com.bendk97.components.Mappers.velocity.get(player).y = PLAYER_VELOCITY * vector2.y;
        com.bendk97.components.Mappers.state.get(player).set(GO_RIGHT);
    }

    @Override
    public void goRightBottom() {
        Vector2 vector2 = new Vector2(1f, -1f);
        vector2.nor();
        com.bendk97.components.Mappers.velocity.get(player).x = PLAYER_VELOCITY * vector2.x;
        com.bendk97.components.Mappers.velocity.get(player).y = PLAYER_VELOCITY * vector2.y;
        com.bendk97.components.Mappers.state.get(player).set(GO_RIGHT);
    }

    @Override
    public void stop() {
        com.bendk97.components.Mappers.velocity.get(player).x = 0;
        com.bendk97.components.Mappers.velocity.get(player).y = 0;
        com.bendk97.components.Mappers.state.get(player).set(ANIMATION_MAIN);
    }

}
