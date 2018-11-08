/*
 * Developed by Benjamin Lefèvre
 * Last modified 06/11/18 08:15
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.player;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.GameAssets;
import com.bendk97.components.*;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.player.PlayerData;
import com.bendk97.runner.GdxTestRunner;
import com.bendk97.screens.levels.utils.ScreenShake;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.bendk97.assets.GameAssets.GFX_LEVEL_COMMON;
import static com.bendk97.screens.levels.Level.Level1;
import static com.bendk97.screens.levels.Level.Level2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class PlayerEntityFactoryTest {

    private PlayerEntityFactory playerEntityFactory;

    @Mock
    private SpaceKillerGame game;

    @Mock
    private GameAssets assets;

    @Mock
    private TextureAtlas sprites;

    @Mock
    private Texture texture;

    @Mock
    private TweenManager tweenManager;

    @Spy
    PooledEngine engine = new PooledEngine();

    Entity player = new Entity();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(assets.get(Level1.sprites)).thenReturn(sprites);
        when(assets.get(GFX_LEVEL_COMMON)).thenReturn(sprites);
        TextureAtlas.AtlasRegion atlasRegion = new TextureAtlas.AtlasRegion(texture, 0, 0, 100, 200);
        atlasRegion.setTexture(texture);
        when(sprites.findRegions(anyString())).thenReturn(Array.with(atlasRegion, atlasRegion, atlasRegion, atlasRegion));
        when(sprites.findRegion(anyString())).thenReturn(atlasRegion);
        EntityFactory entityFactory = new EntityFactory(mock(SpaceKillerGame.class),
                engine, assets, tweenManager, null, mock(ScreenShake.class),
                Level1);
        playerEntityFactory = new PlayerEntityFactory(entityFactory, game);
        player.add(engine.createComponent(PositionComponent.class));
        player.add(engine.createComponent(PlayerComponent.class));
        player.add(engine.createComponent(SpriteComponent.class));
        ComponentMapperHelper.sprite.get(player).sprite = new Sprite();
    }

    @Test
    public void player_is_created_first_time() {
        Entity player = new Entity();
        when(engine.createEntity()).thenReturn(player);
        playerEntityFactory.createEntityPlayer(Level1);

        verify(sprites).findRegions("player");
        assertThat(player.getComponents()).hasSize(6);
        assertThat(player.getComponents())
                .hasOnlyElementsOfTypes(
                        PlayerComponent.class,
                        PositionComponent.class,
                        VelocityComponent.class,
                        AnimationComponent.class,
                        StateComponent.class,
                        SpriteComponent.class
                );
        assertThat(player.getComponent(PlayerComponent.class).bombs).isEqualTo(3);
        assertThat(player.getComponent(PlayerComponent.class).lives).isEqualTo(4);
    }

    @Test
    public void player_is_created_during_change_level() {
        Entity player = new Entity();
        game.playerData = new PlayerData();
        game.playerData.bombs = 5;
        game.playerData.lives = 1;
        when(engine.createEntity()).thenReturn(player);
        playerEntityFactory.createEntityPlayer(Level2);

        assertThat(player.getComponent(PlayerComponent.class).bombs).isEqualTo(5);
        assertThat(player.getComponent(PlayerComponent.class).lives).isEqualTo(1);
    }

    @Test
    public void player_is_created_while_already_existing() {
        playerEntityFactory.createEntityPlayer(Level1);
        assertThatThrownBy(() -> playerEntityFactory.createEntityPlayer(Level1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A player entity already exists!");

    }

    @Test
    public void player_gets_shield() {
        Entity shield = engine.createEntity();
        when(engine.createEntity()).thenReturn(shield);
        playerEntityFactory.createShield(player);

        verify(sprites).findRegion("shield");
        verify(tweenManager).add(any(Timeline.class));
        assertThat(shield.getComponents()).hasSize(3);
        assertThat(shield.getComponents())
                .hasOnlyElementsOfTypes(
                        ShieldComponent.class,
                        PositionComponent.class,
                        SpriteComponent.class
                );
        assertThat(player.getComponents())
                .hasAtLeastOneElementOfType(InvulnerableComponent.class);
    }

    @Test
    public void create_player_lives() {
        SnapshotArray<Entity> lives = playerEntityFactory.createEntityPlayerLives(player);

        verify(sprites, times(1)).findRegions("player");
        assertThat(lives).hasSize(3);
        assertThat(Arrays.stream(lives.toArray())
                .map(Entity::getComponents)
                .flatMap(components -> Arrays.stream(components.toArray()))
                .collect(Collectors.toList()))
                .hasOnlyElementsOfType(SpriteComponent.class);
    }

    @Test
    public void create_player_bombs() {
        SnapshotArray<Entity> bombs = playerEntityFactory.createEntityPlayerBombs(player);

        verify(sprites, times(1)).findRegions("bomb");
        assertThat(bombs).hasSize(3);
        assertThat(Arrays.stream(bombs.toArray())
                .map(Entity::getComponents)
                .flatMap(components -> Arrays.stream(components.toArray()))
                .collect(Collectors.toList()))
                .hasOnlyElementsOfType(SpriteComponent.class);
    }

    @Test
    public void player_becomes_invulnerable() {
        playerEntityFactory.addInvulnerableComponent(player);

        assertThat(player.getComponents())
                .hasAtLeastOneElementOfType(InvulnerableComponent.class);
        assertThat(player.getComponent(InvulnerableComponent.class).nbItems).isEqualTo(1);
    }


    @Test
    public void player_becomes_invulnerable_while_already_invulnerable() {
        playerEntityFactory.addInvulnerableComponent(player);
        playerEntityFactory.addInvulnerableComponent(player);
        assertThat(player.getComponents())
                .hasAtLeastOneElementOfType(InvulnerableComponent.class);
        assertThat(player.getComponent(InvulnerableComponent.class).nbItems).isEqualTo(2);
    }

    @Test
    public void player_becomes_vulnerable() {
        playerEntityFactory.addInvulnerableComponent(player);
        playerEntityFactory.removeInvulnerableComponent(player);

        assertThat(player.getComponents())
                .doesNotHaveAnyElementsOfTypes(InvulnerableComponent.class);
    }


    @Test
    public void player_becomes_vulnerable_while_invulnerable_several_times() {
        playerEntityFactory.addInvulnerableComponent(player);
        playerEntityFactory.addInvulnerableComponent(player);
        playerEntityFactory.removeInvulnerableComponent(player);
        assertThat(player.getComponents())
                .hasAtLeastOneElementOfType(InvulnerableComponent.class);
        assertThat(player.getComponent(InvulnerableComponent.class).nbItems).isEqualTo(1);
    }

    @Test
    public void create_fire_button() {
        Entity fireButton = playerEntityFactory.createEntityFireButton(1, 0, 0);

        verify(sprites, times(1)).findRegion("fire_button");
        assertThat(fireButton.getComponents()).hasSize(1);
        assertThat(fireButton.getComponents())
                .hasOnlyElementsOfType(SpriteComponent.class);
    }

    @Test
    public void create_bomb_button() {
        Entity bombButton = playerEntityFactory.createEntityBombButton(1, 0, 0);

        verify(sprites, times(1)).findRegion("bomb_button");
        assertThat(bombButton.getComponents()).hasSize(1);
        assertThat(bombButton.getComponents())
                .hasOnlyElementsOfType(SpriteComponent.class);
    }

    @Test
    public void create_pad_controller() {
        Entity pad = playerEntityFactory.createEntitiesPadController(1, 1, 0, 0);

        verify(sprites, times(1)).findRegion("pad");
        assertThat(pad.getComponents()).hasSize(1);
        assertThat(pad.getComponents())
                .hasOnlyElementsOfType(SpriteComponent.class);
    }

}
