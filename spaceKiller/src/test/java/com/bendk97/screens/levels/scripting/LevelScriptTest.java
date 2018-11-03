/*
 * Developed by Benjamin Lefèvre
 * Last modified 03/11/18 11:37
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels.scripting;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.utils.Array;
import com.bendk97.assets.Assets;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.SpriteComponent;
import com.bendk97.components.helpers.Families;
import com.bendk97.components.texts.BossAlertComponent;
import com.bendk97.entities.EntityFactory;
import com.bendk97.entities.StageSetEntityFactory;
import com.bendk97.entities.enemies.EnemyEntityFactory;
import com.bendk97.entities.enemies.SoloEnemyFactory;
import com.bendk97.entities.enemies.SquadronFactory;
import com.bendk97.runner.GdxTestRunner;
import com.bendk97.screens.levels.LevelScreen;
import com.bendk97.screens.levels.utils.ScriptItemExecutor;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.bendk97.assets.Assets.FONT_SPACE_KILLER_LARGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public abstract class LevelScriptTest {

    @Mock
    protected Assets assets;

    @Mock
    protected TweenManager tweenManager;

    @Mock
    protected EntityFactory entityFactory;

    @Mock
    protected StageSetEntityFactory stageSetEntityFactory;

    @Mock
    protected EnemyEntityFactory enemyEntityFactory;

    @Mock
    protected SquadronFactory squadronFactory;

    @Mock
    protected SoloEnemyFactory soloEnemyFactory;

    @Mock
    protected ScriptItemExecutor scriptItemExecutor;

    @Mock
    protected LevelScreen screen;

    protected final Entity player = new Entity();

    protected final PooledEngine engine = new PooledEngine();

    protected LevelScript scripting;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        entityFactory.stageSetEntityFactory = stageSetEntityFactory;
        entityFactory.enemyEntityFactory = enemyEntityFactory;
        entityFactory.engine = spy(engine);
        entityFactory.tweenManager = tweenManager;
        entityFactory.enemyEntityFactory.squadronFactory = squadronFactory;
        entityFactory.enemyEntityFactory.soloEnemyFactory = soloEnemyFactory;
        player.add(engine.createComponent(PlayerComponent.class));
        when(assets.get(any(AssetDescriptor.class))).thenReturn(mock(Texture.class));
        when(assets.getFont(FONT_SPACE_KILLER_LARGE)).thenReturn(mock(BitmapFontCache.class));
        doReturn(mock(BossAlertComponent.class)).when(entityFactory.engine).createComponent(BossAlertComponent.class);
        doReturn(new ImmutableArray<>(new Array<>(new Entity[]{mockBoss()}))).when(entityFactory.engine).getEntitiesFor(Families.boss);
        initSpecificMocking();
        initLevelScript();
    }

    private Entity mockBoss() {
        Entity entity = new Entity();
        entity.add(engine.createComponent(SpriteComponent.class));
        return entity;
    }

    protected  abstract void initSpecificMocking();

    protected abstract void initLevelScript();

    protected void launchScriptTimer(int time) {
        launchScriptTimer(-1, time);
    }

    protected void launchScriptTimer(int startTime, int endTime) {
        for (int i = startTime; i <= endTime; ++i) {
            scripting.script(i);
        }
    }

}
