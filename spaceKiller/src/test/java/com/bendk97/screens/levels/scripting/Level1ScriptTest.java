/*
 * Developed by Benjamin Lefèvre
 * Last modified 03/11/18 11:37
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels.scripting;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.VelocityComponent;
import org.junit.Test;

import java.util.LinkedList;

import static com.bendk97.assets.Assets.*;
import static com.bendk97.entities.EntityFactoryIds.BOSS_LEVEL_1;
import static org.mockito.Mockito.*;


public class Level1ScriptTest extends LevelScriptTest{

    @Override
    public void initSpecificMocking(){
        Entity background = new Entity();
        background.add(engine.createComponent(VelocityComponent.class));
        when(stageSetEntityFactory.createBackground(any(Texture.class), anyFloat())).thenReturn(background);
    }

    @Override
    public void initLevelScript() {
        this.scripting = new Level1Script(screen, assets, entityFactory, tweenManager, player, scriptItemExecutor);
    }

    @Test
    public void music_level_is_played() {
        verify(assets).playMusic(MUSIC_LEVEL_1, 0.3f);
    }

    @Test
    public void mists_are_created_every_30_seconds() {
        launchScriptTimer(240);
        verify(stageSetEntityFactory, times(9)).createForeground(any(Texture.class), anyFloat());
    }

    @Test
    public void asteroids_are_created_every_3_and_7_seconds() {
        scripting = spy(scripting);
        launchScriptTimer(20);
        verify(scripting, times(9)).getRandomAsteroidType();
    }

    @Test
    public void thirteen_enemies_squadrons_happen_during_first_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(60);
        verify(scripting, times(13)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void eighteen_enemies_squadrons_happen_during_second_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(61, 120);
        verify(scripting, times(18)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void eighteen_enemies_squadrons_happen_during_last_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(121, 180);
        verify(scripting, times(18)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void boss_happens_end_of_level() {
        launchScriptTimer(181, 250);
        verify(assets).playSound(SOUND_BOSS_ALERT);
        verify(assets).stopMusic(MUSIC_LEVEL_1);
        verify(assets).playMusic(MUSIC_LEVEL_1_BOSS, 1f);
        verify(scriptItemExecutor).execute(argThat(script -> script.typeShip == BOSS_LEVEL_1));
    }

    @Test
    public void boss_never_happens_when_game_over() {
        player.add(engine.createComponent(GameOverComponent.class));
        launchScriptTimer(181, 250);
        verify(assets, never()).playSound(SOUND_BOSS_ALERT);
        verify(assets, never()).stopMusic(MUSIC_LEVEL_1);
        verify(assets, never()).playMusic(MUSIC_LEVEL_1_BOSS, 1f);
        verify(scriptItemExecutor, never()).execute(argThat(script -> script.typeShip == BOSS_LEVEL_1));
    }

    @Test
    public void music_level_1_is_played() {
        verify(assets).playMusic(MUSIC_LEVEL_1, 0.3f);
    }
}
