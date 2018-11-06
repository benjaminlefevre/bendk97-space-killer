/*
 * Developed by Benjamin Lefèvre
 * Last modified 06/11/18 06:41
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.player;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.GameAssets;
import com.bendk97.components.*;
import com.bendk97.entities.EntityFactory;
import com.bendk97.runner.GdxTestRunner;
import com.bendk97.screens.levels.utils.ScreenShake;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static com.bendk97.screens.levels.Level.Level1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class BonusEntityFactoryTest {

    private BonusEntityFactory bonusEntityFactory;

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

    Entity squadron = new Entity();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(assets.get(Level1.sprites)).thenReturn(sprites);
        TextureAtlas.AtlasRegion atlasRegion = new TextureAtlas.AtlasRegion(texture, 0, 0, 100, 200);
        atlasRegion.setTexture(texture);
        Array<TextureAtlas.AtlasRegion> regionArray = new Array<>();
        regionArray.add(atlasRegion);
        when(sprites.findRegions(anyString())).thenReturn(regionArray);
        when(sprites.findRegion(anyString())).thenReturn(atlasRegion);
        EntityFactory entityFactory = new EntityFactory(mock(SpaceKillerGame.class),
                engine, assets, tweenManager, null, mock(ScreenShake.class),
                Level1);
        squadron.add(engine.createComponent(SquadronComponent.class));
        bonusEntityFactory = new BonusEntityFactory(entityFactory);
    }

    @Test
    public void a_powerUp_bonus_is_created() {
        Entity powerUp = new Entity();
        when(engine.createEntity()).thenReturn(powerUp);
        bonusEntityFactory.createPowerUp(squadron);

        verify(sprites).findRegions("power-up");
        assertThat(powerUp.getComponents()).hasSize(6);
        assertThat(powerUp.getComponents())
                .hasOnlyElementsOfTypes(
                        PowerUpComponent.class,
                        StateComponent.class,
                        PositionComponent.class,
                        VelocityComponent.class,
                        AnimationComponent.class,
                        SpriteComponent.class
                );
        verify(tweenManager).add(any(Timeline.class));
    }

    @Test
    public void a_shield_bonus_is_created() {
        Entity shield = new Entity();
        when(engine.createEntity()).thenReturn(shield);
        bonusEntityFactory.createShieldUp(squadron);

        verify(sprites).findRegions("shieldup");
        assertThat(shield.getComponents()).hasSize(6);
        assertThat(shield.getComponents())
                .hasOnlyElementsOfTypes(
                        ShieldUpComponent.class,
                        StateComponent.class,
                        PositionComponent.class,
                        VelocityComponent.class,
                        AnimationComponent.class,
                        SpriteComponent.class
                );
        verify(tweenManager).add(any(Timeline.class));
    }

    @Test
    public void a_bomb_bonus_is_created() {
        Entity bombUp = new Entity();
        when(engine.createEntity()).thenReturn(bombUp);
        bonusEntityFactory.createBombUp(squadron);

        verify(sprites).findRegion("bombUp");
        assertThat(bombUp.getComponents()).hasSize(4);
        assertThat(bombUp.getComponents())
                .hasOnlyElementsOfTypes(
                        BombUpComponent.class,
                        PositionComponent.class,
                        VelocityComponent.class,
                        SpriteComponent.class
                );
        verify(tweenManager).add(any(Timeline.class));
    }

}
