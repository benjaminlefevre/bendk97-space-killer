/*
 * Developed by Benjamin Lefèvre
 * Last modified 26/10/18 18:43
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.SnapshotArray;
import com.bendk97.Settings;
import com.bendk97.SpaceKillerGame;
import com.bendk97.SpaceKillerGameConstants;
import com.bendk97.assets.GameAssets;
import com.bendk97.listeners.impl.CollisionListenerImpl;
import com.bendk97.listeners.impl.InputListenerImpl;
import com.bendk97.listeners.impl.PlayerListenerImpl;
import com.bendk97.runner.GdxTestRunner;
import com.bendk97.screens.levels.utils.ScreenShake;
import com.bendk97.systems.*;
import com.bendk97.systems.collision.CollisionSystem;
import com.bendk97.systems.screen.GameOverRenderingSystem;
import com.bendk97.systems.screen.PauseRenderingSystem;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.bendk97.assets.GameAssets.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(GdxTestRunner.class)
public class LevelScreenTest {

    @Mock
    private GameAssets assets;

    @Mock
    private SpaceKillerGame game;

    @Mock
    private SpriteBatch spriteBatch;

    @Mock
    private Texture texture;

    private LevelScreen levelScreen;

    @BeforeClass
    public static void setPreferences() {
        if (Settings.isLightFXEnabled()) {
            Settings.changeLightFXEnabled();
        }
        SpaceKillerGameConstants.DEBUG = false;
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockAssets();
        levelScreen = new Level1Screen(assets, game, spriteBatch);
    }

    private void mockAssets() {
        TextureAtlas textureAtlas = mock(TextureAtlas.class);
        when(assets.get(GFX_LEVEL1)).thenReturn(textureAtlas);
        when(assets.get(GFX_LEVEL_COMMON)).thenReturn(textureAtlas);
        when(assets.getFont(FONT_SPACE_KILLER_LARGE)).thenReturn(mock(BitmapFontCache.class));
        when(assets.getFont(FONT_SPACE_KILLER_MEDIUM)).thenReturn(mock(BitmapFontCache.class));
        when(assets.getFont(FONT_SPACE_KILLER_SMALLEST)).thenReturn(mock(BitmapFontCache.class));
        when(assets.getFont(FONT_SPACE_KILLER)).thenReturn(mock(BitmapFontCache.class));
        when(assets.get(GFX_BGD_LEVEL1)).thenReturn(mock(Texture.class));
        when(assets.get(GFX_BGD_STARS)).thenReturn(mock(Texture.class));
        AtlasRegion atlasRegion = new AtlasRegion(texture, 0, 0, 1, 1);
        when(textureAtlas.findRegions(anyString())).thenReturn(new Array<>(
                new AtlasRegion[]{atlasRegion, atlasRegion, atlasRegion, atlasRegion}
        ));
        when(textureAtlas.findRegion(anyString())).thenReturn(atlasRegion);
        ObjectSet<Texture> textures = new ObjectSet<>();
        textures.add(mock(Texture.class));
        when(textures.first().getTextureData()).thenReturn(mock(TextureData.class));
        when(textures.first().getTextureData().consumePixmap()).thenReturn(mock(Pixmap.class));
        when(textureAtlas.getTextures()).thenReturn(textures);
    }

    @Test
    public void systems_creation() {
        levelScreen.createSystems(mock(Entity.class),
                mock(SnapshotArray.class),
                mock(SnapshotArray.class), mock(SpriteBatch.class), mock(ScreenShake.class));

        assertThat(levelScreen.engine.getSystems()).hasSize(29);
        assertThat(levelScreen.engine.getSystems())
                .hasOnlyElementsOfTypes(
                        PlayerListenerImpl.class,
                        InputListenerImpl.class,
                        CollisionListenerImpl.class,
                        DirectionableSpriteSystem.class,
                        AnimationSystem.class,
                        BombExplosionSystem.class,
                        StateSystem.class,
                        MovementPlayerSystem.class,
                        MovementSystem.class,
                        ShieldSystem.class,
                        BatcherBeginSystem.class,
                        BackgroundRenderingSystem.class,
                        DynamicEntitiesRenderingSystem.class,
                        ScoreSquadronSystem.class,
                        BatcherEndSystem.class,
                        BatcherHUDBeginSystem.class,
                        StaticEntitiesRenderingSystem.class,
                        TextHUDRenderingSystem.class,
                        StatusHealthRenderingSystem.class,
                        GameOverRenderingSystem.class,
                        PauseRenderingSystem.class,
                        LevelFinishedRenderingSystem.class,
                        BatcherHUDEndSystem.class,
                        CollisionSystem.class,
                        TankAttackSystem.class,
                        EnemyAttackSystem.class,
                        BossAttackSystem.class,
                        SquadronSystem.class,
                        RemovableSystem.class
                );
    }

    @Test
    public void systems_creation_in_debug_mode() {
        SpaceKillerGameConstants.DEBUG = true;
        levelScreen.createSystems(mock(Entity.class),
                mock(SnapshotArray.class),
                mock(SnapshotArray.class), mock(SpriteBatch.class), mock(ScreenShake.class));

        assertThat(levelScreen.engine.getSystems()).hasSize(30);
        assertThat(levelScreen.engine.getSystems())
                .hasAtLeastOneElementOfType(
                        DebugStatsSystem.class
                );
    }


}
