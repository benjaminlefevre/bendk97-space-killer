/*
 * Developed by Benjamin Lefèvre
 * Last modified 06/11/18 06:41
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.player;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.GameAssets;
import com.bendk97.components.*;
import com.bendk97.components.PlayerComponent.PowerLevel;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.runner.GdxTestRunner;
import com.bendk97.screens.levels.Level;
import com.bendk97.screens.levels.utils.ScreenShake;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static com.bendk97.components.PlayerComponent.PowerLevel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class PlayerActionsEntityFactoryTest {

    private PlayerActionsEntityFactory playerActionsEntityFactory;

    private EntityFactory entityFactory;

    @Mock
    private GameAssets assets;

    @Mock
    private TextureAtlas sprites;

    @Mock
    private Texture texture;

    @Mock
    private TweenManager tweenManager;

    @Mock
    private ScreenShake screenShake;

    @Spy
    PooledEngine engine = new PooledEngine();

    Entity player;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(assets.get(any(AssetDescriptor.class))).thenReturn(sprites);
        TextureAtlas.AtlasRegion atlasRegion = new TextureAtlas.AtlasRegion(texture, 0, 0, 100, 200);
        when(sprites.findRegion(anyString())).thenReturn(atlasRegion);
        when(sprites.findRegions(anyString())).thenReturn(Array.with(atlasRegion, atlasRegion,
                atlasRegion, atlasRegion, atlasRegion, atlasRegion, atlasRegion));
        entityFactory = new EntityFactory(mock(SpaceKillerGame.class),
                engine, assets, tweenManager, null, screenShake,
                Level.Level2);

        player = new Entity();
        player.add(engine.createComponent(PlayerComponent.class));
        player.add(engine.createComponent(PositionComponent.class));
        SpriteComponent spriteComponent = (SpriteComponent) player.addAndReturn(engine.createComponent(SpriteComponent.class));
        spriteComponent.sprite = new Sprite();
        playerActionsEntityFactory = new PlayerActionsEntityFactory(entityFactory);

    }

    @Test
    public void player_fires_at_normal_level() {
        player_fires(NORMAL, "bullet");
    }

    @Test
    public void player_fires_at_double_level() {
        player_fires(DOUBLE, "bullet2");
    }

    @Test
    public void player_fires_at_triple_level() {
        player_fires(TRIPLE, "bullet3");
    }

    @Test
    public void player_fires_at_triple_side_level() {
        player_fires(TRIPLE_SIDE, "bullet3");
    }

    @Test
    public void player_fires_at_triple_fast_level() {
        player_fires(TRIPLE_FAST, "bullet4");
    }

    @Test
    public void player_fires_at_triple_very_fast_level() {
        player_fires(TRIPLE_VERY_FAST, "bullet5");
    }

    @Test
    public void player_fires_side_at_normal_level() {
        ComponentMapperHelper.player.get(player).powerLevel = NORMAL;
        assertThatThrownBy(() -> playerActionsEntityFactory.createPlayerFireSide(player))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void player_fires_side_at_double_level() {
        ComponentMapperHelper.player.get(player).powerLevel = DOUBLE;
        assertThatThrownBy(() -> playerActionsEntityFactory.createPlayerFireSide(player))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void player_fires_side_at_triple_level() {
        ComponentMapperHelper.player.get(player).powerLevel = TRIPLE;
        assertThatThrownBy(() -> playerActionsEntityFactory.createPlayerFireSide(player))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void player_fires_side_at_triple_side_level() {
        player_fires_side(TRIPLE_SIDE, "bulletLeft1", "bulletRight1");
    }

    @Test
    public void player_fires_side_at_triple_fast_side_level() {
        player_fires_side(TRIPLE_FAST, "bulletLeft2", "bulletRight2");
    }

    @Test
    public void player_fires_side_at_triple_very_fast_side_level() {
        player_fires_side(TRIPLE_VERY_FAST, "bulletLeft3", "bulletRight3");
    }

    @Test
    public void player_drop_bomb() {
        Entity bomb = new Entity();
        when(engine.createEntity()).thenReturn(bomb);
        playerActionsEntityFactory.createPlayerBomb(player);

        verify(sprites).findRegions("bomb");
        assertThat(bomb.getComponents()).hasSize(4);
        assertThat(bomb.getComponents())
                .hasOnlyElementsOfTypes(
                        StateComponent.class,
                        AnimationComponent.class,
                        SpriteComponent.class,
                        PositionComponent.class
                );
        verify(tweenManager).add(any(Tween.class));
    }

    @Test
    public void bomb_explodes() {
        Entity bomb = new Entity();
        Entity bombExplosion = new Entity();
        when(engine.createEntity())
                .thenReturn(bomb)
                .thenReturn(bombExplosion);
        playerActionsEntityFactory.createPlayerBomb(player);
        Mockito.reset(tweenManager);
        playerActionsEntityFactory.createBombExplosion(bomb);

        verify(screenShake).shake(anyFloat(), anyFloat(), anyBoolean());
        verify(sprites).findRegions("bomb_explosion");
        verify(assets).playSound(GameAssets.SOUND_BOMB_EXPLOSION);
        assertThat(bombExplosion.getComponents()).hasSize(4);
        assertThat(bombExplosion.getComponents())
                .hasOnlyElementsOfTypes(
                        StateComponent.class,
                        AnimationComponent.class,
                        SpriteComponent.class,
                        PositionComponent.class
                );
        verify(tweenManager).add(any(Tween.class));
    }

    private void player_fires(PowerLevel powerLevel, String regionName) {
        ComponentMapperHelper.player.get(player).powerLevel = powerLevel;
        Entity bullet = new Entity();
        when(engine.createEntity()).thenReturn(bullet);
        playerActionsEntityFactory.createPlayerFire(player);

        check_bullet(regionName, bullet);
    }

    private void player_fires_side(PowerLevel powerLevel, String regionNameLeft, String regionNameRight) {
        ComponentMapperHelper.player.get(player).powerLevel = powerLevel;
        Entity bulletLeft = new Entity();
        Entity bulletRight = new Entity();
        when(engine.createEntity()).thenReturn(bulletLeft).thenReturn(bulletRight);
        playerActionsEntityFactory.createPlayerFireSide(player);

        check_bullet(regionNameLeft, bulletLeft);
        check_bullet(regionNameRight, bulletRight);
    }

    private void check_bullet(String regionName, Entity bullet) {
        verify(sprites).findRegion(regionName);
        assertThat(bullet.getComponents()).hasSize(5);
        assertThat(bullet.getComponents())
                .hasOnlyElementsOfTypes(
                        PositionComponent.class,
                        VelocityComponent.class,
                        SpriteComponent.class,
                        PlayerBulletComponent.class,
                        RemovableComponent.class
                );
    }

}
