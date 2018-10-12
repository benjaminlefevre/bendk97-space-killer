/*
 * Developed by Benjamin Lefèvre
 * Last modified 12/10/18 22:17
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.listeners.impl;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.bendk97.assets.Assets;
import com.bendk97.components.*;
import com.bendk97.components.PlayerComponent.PowerLevel;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.entities.enemies.EnemyEntityFactory;
import com.bendk97.entities.player.PlayerEntityFactory;
import com.bendk97.listeners.PlayerListener;
import com.bendk97.screens.levels.LevelScreen;
import com.bendk97.screens.levels.utils.ScreenShake;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.bendk97.assets.Assets.*;
import static com.bendk97.components.PlayerComponent.PowerLevel.*;
import static com.bendk97.helpers.EntityTestHelper.createEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CollisionListenerImplTest {

    @Mock
    private Assets assets;

    @Mock
    private EntityFactory entityFactory;

    @Mock
    private EnemyEntityFactory enemyEntityFactory;

    @Mock
    private PlayerEntityFactory playerEntityFactory;

    @Mock
    private PlayerListener playerListener;

    @Mock
    private TweenManager tweenManager;

    @Mock
    private LevelScreen levelScreen;

    @Mock
    private ScreenShake screenShake;

    @InjectMocks
    private CollisionListenerImpl collisionListener;

    private Engine engine = new Engine();

    @Before
    public void initMocks() {
        this.entityFactory.enemyEntityFactory = enemyEntityFactory;
        this.entityFactory.playerEntityFactory = playerEntityFactory;
        engine.addSystem(collisionListener);
    }

    @Test
    public void enemy_is_shoot_by_explosion_but_already_dead() {
        Entity enemy = createEntity(engine, EnemyComponent.class);
        ComponentMapperHelper.enemy.get(enemy).initLifeGauge(0);

        collisionListener.enemyShootByExplosion(enemy, null);

        verify(assets, never()).playSound(SOUND_EXPLOSION);
    }

    @Test
    public void enemy_is_shoot_by_explosion() {
        Entity enemy = createEntity(engine, EnemyComponent.class, PositionComponent.class);
        Entity player = createEntity(engine, PlayerComponent.class);
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(enemy);
        enemyComponent.initLifeGauge(40);

        collisionListener.enemyShootByExplosion(enemy, player);

        verify(assets).playSound(SOUND_EXPLOSION);
        assertThat(enemyComponent.isDead()).isEqualTo(true);
    }

    @Test
    public void enemy_is_hurt_by_explosion() {
        Entity enemy = createEntity(engine, EnemyComponent.class, PositionComponent.class);
        Entity player = createEntity(engine, PlayerComponent.class);
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(enemy);
        enemyComponent.initLifeGauge(400);

        collisionListener.enemyShootByExplosion(enemy, player);

        verify(assets).playSound(SOUND_EXPLOSION);
        assertThat(enemyComponent.isDead()).isEqualTo(false);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(350);
    }

    @Test
    public void enemy_is_shoot_by_bullet() {
        Entity enemy = createEntity(engine, EnemyComponent.class, PositionComponent.class);
        Entity player = createEntity(engine, PlayerComponent.class);
        Entity bullet = createEntity(engine, PlayerBulletComponent.class);
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(enemy);
        enemyComponent.initLifeGauge(1);

        collisionListener.enemyShoot(enemy, player, bullet);

        verify(assets).playSound(SOUND_EXPLOSION);
        assertThat(enemyComponent.isDead()).isEqualTo(true);
    }

    @Test
    public void boss_is_shoot_by_bullet() {
        Entity enemy = createEntity(engine, BossComponent.class, EnemyComponent.class, SpriteComponent.class, PositionComponent.class);
        Entity player = createEntity(engine, PlayerComponent.class);
        Entity bullet = createEntity(engine, PlayerBulletComponent.class, PositionComponent.class);
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(enemy);
        enemyComponent.isBoss = true;
        enemyComponent.initLifeGauge(1);
        ComponentMapperHelper.sprite.get(enemy).sprite = new Sprite();
        Entity explosion = createEntity(engine, SpriteComponent.class, PositionComponent.class, BombExplosionComponent.class);
        ComponentMapperHelper.sprite.get(explosion).sprite = new Sprite();
        when(enemyEntityFactory.createEntityExploding(anyFloat(), anyFloat())).thenReturn(explosion);

        collisionListener.enemyShoot(enemy, player, bullet);

        verify(assets).playSound(SOUND_EXPLOSION);
        verify(assets).playSound(SOUND_BOSS_FINISHED);
        verify(screenShake).shake(anyFloat(), anyFloat(), anyBoolean());
        assertThat(enemyComponent.isDead()).isEqualTo(true);
    }

    @Test
    public void enemy_is_hurt_by_bullet() {
        Entity enemy = createEntity(engine, EnemyComponent.class, PositionComponent.class);
        Entity player = createEntity(engine, PlayerComponent.class);
        Entity bullet = createEntity(engine, PlayerBulletComponent.class, PositionComponent.class);
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(enemy);
        enemyComponent.initLifeGauge(400);

        collisionListener.enemyShoot(enemy, player, bullet);

        verify(assets).playSound(SOUND_EXPLOSION);
        assertThat(enemyComponent.isDead()).isEqualTo(false);
        assertThat(enemyComponent.getLifeGauge()).isEqualTo(399);
    }

    @Test
    public void player_is_hit_by_enemy_body() {
        Entity player = createEntity(engine, PlayerComponent.class, PositionComponent.class);

        collisionListener.playerHitByEnemyBody(player);

        verify(assets).playSound(SOUND_EXPLOSION);
        verify(enemyEntityFactory).createEntityExploding(anyFloat(), anyFloat());
        verify(playerListener).loseLive(player);
    }

    @Test
    public void player_is_hit_by_enemy_bullet() {
        Entity player = createEntity(engine, PlayerComponent.class, PositionComponent.class);
        Entity bullet = createEntity(engine, EnemyBulletComponent.class);

        collisionListener.playerHitByEnemyBullet(player, bullet);

        verify(assets).playSound(SOUND_EXPLOSION);
        verify(enemyEntityFactory).createEntityExploding(anyFloat(), anyFloat());
        verify(playerListener).loseLive(player);
    }

    @Test
    public void player_normal_gets_power_up() {
        Entity player = createEntity(engine, PlayerComponent.class, PositionComponent.class);
        player_gets_power_up(player, NORMAL);
        verify(assets).playSound(SOUND_POWER_UP);
        verify(assets).playSound(SOUND_POWER_UP_VOICE);
        verify(tweenManager, times(2)).killTarget(any());
        assertThat(ComponentMapperHelper.player.get(player).powerLevel).isEqualTo(DOUBLE);

    }

    @Test
    public void player_double_gets_power_up() {
        Entity player = createEntity(engine, PlayerComponent.class, PositionComponent.class);
        player_gets_power_up(player, DOUBLE);
        verify(assets).playSound(SOUND_POWER_UP);
        verify(assets).playSound(SOUND_POWER_UP_VOICE);
        verify(tweenManager, times(2)).killTarget(any());
        assertThat(ComponentMapperHelper.player.get(player).powerLevel).isEqualTo(TRIPLE);

    }

    @Test
    public void player_triple_gets_power_up() {
        Entity player = createEntity(engine, PlayerComponent.class, PositionComponent.class);
        player_gets_power_up(player, TRIPLE);
        verify(assets).playSound(SOUND_POWER_UP);
        verify(assets).playSound(SOUND_POWER_UP_VOICE);
        verify(tweenManager, times(2)).killTarget(any());
        assertThat(ComponentMapperHelper.player.get(player).powerLevel).isEqualTo(TRIPLE_SIDE);

    }

    @Test
    public void player_triple_side_gets_power_up() {
        Entity player = createEntity(engine, PlayerComponent.class, PositionComponent.class);
        player_gets_power_up(player, TRIPLE_SIDE);
        verify(assets).playSound(SOUND_POWER_UP);
        verify(assets).playSound(SOUND_POWER_UP_VOICE);
        verify(tweenManager, times(2)).killTarget(any());
        assertThat(ComponentMapperHelper.player.get(player).powerLevel).isEqualTo(TRIPLE_FAST);

    }

    @Test
    public void player_triple_fast_gets_power_up() {
        Entity player = createEntity(engine, PlayerComponent.class, PositionComponent.class);
        player_gets_power_up(player, TRIPLE_FAST);
        verify(assets).playSound(SOUND_POWER_UP);
        verify(assets).playSound(SOUND_POWER_UP_VOICE);
        verify(tweenManager, times(2)).killTarget(any());
        assertThat(ComponentMapperHelper.player.get(player).powerLevel).isEqualTo(TRIPLE_VERY_FAST);

    }

    @Test
    public void player_triple_very_fast_gets_power_up() {
        Entity player = createEntity(engine, PlayerComponent.class, PositionComponent.class);
        player_gets_power_up(player, TRIPLE_VERY_FAST);
        verify(assets).playSound(SOUND_POWER_UP);
        verify(assets, never()).playSound(SOUND_POWER_UP_VOICE);
        verify(tweenManager, times(2)).killTarget(any());
        assertThat(ComponentMapperHelper.player.get(player).powerLevel).isEqualTo(TRIPLE_VERY_FAST);
    }

    private void player_gets_power_up(Entity player, PowerLevel level) {
        Entity powerUp = createEntity(engine, PowerUpComponent.class);
        ComponentMapperHelper.player.get(player).powerLevel = level;

        collisionListener.playerPowerUp(player, powerUp);
    }

    @Test
    public void player_gets_shield_up() {
        Entity player = createEntity(engine, PlayerComponent.class, PositionComponent.class);
        Entity shieldUp = createEntity(engine, ShieldUpComponent.class);

        collisionListener.playerShieldUp(player, shieldUp);

        verify(assets).playSound(SOUND_SHIELD_UP);
        verify(playerEntityFactory).createShield(player);
        verify(tweenManager, times(2)).killTarget(any());
    }

    @Test
    public void player_gets_bomb_up() {
        Entity player = createEntity(engine, PlayerComponent.class, PositionComponent.class);
        Entity bombUp = createEntity(engine, BombUpComponent.class);

        collisionListener.playerBombUp(player, bombUp);

        verify(assets).playSound(SOUND_SHIELD_UP);
        verify(playerListener).newBombObtained(player);
        verify(tweenManager, times(2)).killTarget(any());
    }

    @Test
    public void bullet_is_stopped_by_shield() {
        Entity bullet = createEntity(engine, EnemyBulletComponent.class);

        collisionListener.bulletStoppedByShield(bullet);

        verify(assets).playSound(SOUND_SHIELD_BULLET);
    }

    @Test
    public void enemy_is_shoot_by_shield() {
        Entity enemy = createEntity(engine, EnemyComponent.class, PositionComponent.class);
        ComponentMapperHelper.enemy.get(enemy).squadron = createEntity(engine, SquadronComponent.class);

        collisionListener.enemyShootByShield(enemy);

        verify(assets).playSound(SOUND_EXPLOSION);
        verify(enemyEntityFactory).createEntityExploding(anyFloat(), anyFloat());
        verify(tweenManager).killTarget(any());
    }

}
