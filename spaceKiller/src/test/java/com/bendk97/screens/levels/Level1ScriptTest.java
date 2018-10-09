/*
 * Developed by Benjamin Lefèvre
 * Last modified 08/10/18 22:33
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.bendk97.assets.Assets;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.VelocityComponent;
import com.bendk97.entities.EntityFactory;
import com.bendk97.entities.SquadronFactory;
import com.bendk97.runner.GdxTestRunner;
import com.bendk97.screens.levels.utils.ScriptItemExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;

import static com.bendk97.assets.Assets.SOUND_BOSS_ALERT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class Level1ScriptTest {

    @Mock
    Assets assets;

    @Mock
    TweenManager tweenManager;

    @Mock
    EntityFactory entityFactory;

    @Mock
    SquadronFactory squadronFactory;

    @Mock
    ScriptItemExecutor scriptItemExecutor;

    private Level1Script scripting;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        Entity player = new Entity();
        PooledEngine engine = new PooledEngine();
        Entity background = new Entity();
        player.add(engine.createComponent(PlayerComponent.class));
        background.add(engine.createComponent(VelocityComponent.class));
        when(entityFactory.createBackground(any(Texture.class), anyFloat())).thenReturn(background);
        when(entityFactory.createBackground(any(Texture.class), anyFloat())).thenReturn(background);
        when(assets.get(any(AssetDescriptor.class))).thenReturn(mock(Texture.class));

        scripting = new Level1Script(assets, entityFactory, tweenManager, player, squadronFactory, scriptItemExecutor);
    }

    @Test
    public void mists_are_created_every_30_seconds() {
        launchScriptTimer(240);
        verify(entityFactory, times(8)).createForeground(any(Texture.class), anyFloat());
    }

    @Test
    public void asteroids_are_created_every_3_and_7_seconds() {
        scripting = spy(scripting);
        launchScriptTimer(20);
        verify(scripting, times(9)).getRandomAsteroidType();
    }

    @Test
    public void twelve_enemies_squadrons_happen_during_first_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(60);
        verify(scripting, times(12)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void sixteen_enemies_squadrons_happen_during_second_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(61, 120);
        verify(scripting, times(16)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void sixteen_enemies_squadrons_happen_during_last_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(121, 180);
        verify(scripting, times(16)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void boss_happens_end_of_level() {
        launchScriptTimer(181, 250);
        verify(assets).playSound(SOUND_BOSS_ALERT);
    }

    private void launchScriptTimer(int time) {
        launchScriptTimer(0, time);
    }

    private void launchScriptTimer(int startTime, int endTime) {
        for (int i = startTime; i < endTime; ++i) {
            scripting.script(i);
        }
    }
}
