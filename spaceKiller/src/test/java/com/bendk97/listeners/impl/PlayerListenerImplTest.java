/*
 * Developed by Benjamin Lefèvre
 * Last modified 13/10/18 13:49
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.listeners.impl;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.SnapshotArray;
import com.bendk97.assets.Assets;
import com.bendk97.components.EnemyComponent;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.SpriteComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.entities.player.PlayerActionsEntityFactory;
import com.bendk97.entities.player.PlayerEntityFactory;
import com.bendk97.runner.GdxTestRunner;
import com.bendk97.screens.levels.LevelScreen;
import com.bendk97.screens.levels.utils.ScreenShake;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.bendk97.assets.Assets.*;
import static com.bendk97.helpers.EntityTestHelper.createEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class PlayerListenerImplTest {

    @Mock
    private Assets assets;

    @Mock
    private LevelScreen screen;

    @Mock
    private ScreenShake screenShake;

    @Mock
    private EntityFactory entityFactory;

    @Mock
    private PlayerEntityFactory playerEntityFactory;

    @Mock
    private PlayerActionsEntityFactory playerActionsEntityFactory;

    private PlayerListenerImpl playerListener;
    private Entity player;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        entityFactory.playerEntityFactory = playerEntityFactory;
        entityFactory.playerActionsEntityFactory = playerActionsEntityFactory;
        Engine engine = new Engine();

        SnapshotArray<Entity> bombs = new SnapshotArray<>(Entity.class);
        SnapshotArray<Entity> lives = new SnapshotArray<>(Entity.class);
        bombs.add(engine.createEntity(), engine.createEntity());
        lives.add(engine.createEntity(), engine.createEntity(), engine.createEntity());
        playerListener = new PlayerListenerImpl(assets, entityFactory, lives, bombs, screenShake, screen);
        engine.addSystem(playerListener);

        player = createEntity(engine, PlayerComponent.class, PositionComponent.class, SpriteComponent.class);
        ComponentMapperHelper.sprite.get(player).sprite = new Sprite();
        ComponentMapperHelper.player.get(player).bombs = 2;
        ComponentMapperHelper.player.get(player).lives = 3;
    }

    @Test
    public void player_drops_a_bomb() {
        playerListener.dropBomb(player);

        assertThat(playerListener.bombs).hasSize(1);
        assertThat(ComponentMapperHelper.player.get(player).bombs).isEqualTo(1);
        verify(assets).playSound(SOUND_BOMB_DROP);
        verify(playerActionsEntityFactory).createPlayerBomb(player);
    }

    @Test
    public void player_with_no_bomb_tries_to_drop_a_bomb() {
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        playerComponent.bombs = 0;

        playerListener.dropBomb(player);

        assertThat(playerComponent.bombs).isEqualTo(0);
        verify(assets, never()).playSound(SOUND_BOMB_DROP);
        verify(playerActionsEntityFactory, never()).createPlayerBomb(player);
    }

    @Test
    public void player_obtains_a_new_bomb() {
        playerListener.newBombObtained(player);

        assertThat(ComponentMapperHelper.player.get(player).bombs).isEqualTo(3);
        verify(playerEntityFactory).createEntityPlayerBombs(player);
    }

    @Test
    public void player_retrieves_lives_and_bombs_after_continue() {
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);

        playerListener.updateLivesAndBombsAfterContinue(player);

        assertThat(playerComponent.bombs).isEqualTo(3);
        assertThat(playerComponent.lives).isEqualTo(4);
        assertThat(playerComponent.getScore()).isEqualTo("0000000");
        assertThat(playerComponent.numberOfContinue).isEqualTo(1);
    }

    @Test
    public void player_loses_live_but_has_still_some() {
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);

        playerListener.loseLive(player);

        assertThat(playerComponent.lives).isEqualTo(2);
        verify(screenShake).shake(anyFloat(), anyFloat(), anyBoolean());
        verify(assets).playSound(SOUND_LOSE_LIFE);
        assertThat(playerListener.lives).hasSize(2);
    }

    @Test
    public void player_loses_last_live() {
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        playerComponent.lives = 1;

        playerListener.loseLive(player);

        assertThat(playerComponent.lives).isEqualTo(0);
        verify(screenShake).shake(anyFloat(), anyFloat(), anyBoolean());
        verify(assets).playSound(SOUND_GAME_OVER);
        verify(screen).submitScore(anyInt());
    }

    @Test
    public void updating_player_score() {
        Entity enemy = createEntity(playerListener.getEngine(), EnemyComponent.class);
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(enemy);
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        enemyComponent.points = 100;
        enemyComponent.initLifeGauge(5);
        playerComponent.setHighScore(500);

        playerListener.updateScore(player, enemy, 1);

        assertThat(playerComponent.getScore()).isEqualTo("0000100");
        verify(assets, never()).playSound(SOUND_NEW_HIGH_SCORE);
        verify(assets, never()).playSound(SOUND_NEW_LIFE);
        assertThat(playerComponent.lives).isEqualTo(3);
    }

    @Test
    public void updating_player_score_with_highscore() {
        Entity enemy = createEntity(playerListener.getEngine(), EnemyComponent.class);
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(enemy);
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        enemyComponent.points = 100;
        enemyComponent.initLifeGauge(5);
        playerComponent.setHighScore(400);

        playerListener.updateScore(player, enemy, 5);

        assertThat(playerComponent.getScore()).isEqualTo("0000500");
        verify(assets).playSound(SOUND_NEW_HIGH_SCORE);
        verify(assets, never()).playSound(SOUND_NEW_LIFE);
        assertThat(playerComponent.lives).isEqualTo(3);
    }

    @Test
    public void updating_player_score_with_new_live() {
        Entity enemy = createEntity(playerListener.getEngine(), EnemyComponent.class);
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(enemy);
        PlayerComponent playerComponent = ComponentMapperHelper.player.get(player);
        enemyComponent.points = 30000;
        enemyComponent.initLifeGauge(5);


        playerListener.updateScore(player, enemy, 5);

        assertThat(playerComponent.getScore()).isEqualTo("0150000");
        verify(assets).playSound(SOUND_NEW_LIFE);
        assertThat(playerComponent.lives).isEqualTo(4);
    }


}
