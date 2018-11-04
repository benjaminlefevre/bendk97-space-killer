/*
 * Developed by Benjamin Lefèvre
 * Last modified 03/11/18 11:37
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels.scripting;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.VelocityComponent;
import org.junit.Test;

import java.util.LinkedList;

import static com.bendk97.assets.GameAssets.*;
import static com.bendk97.entities.EntityFactoryIds.BOSS_LEVEL_2;
import static org.mockito.Mockito.*;

public class Level2ScriptTest extends LevelScriptTest {

    @Override
    public void initSpecificMocking() {
        Entity background = new Entity();
        player.add(engine.createComponent(PlayerComponent.class));
        background.add(engine.createComponent(VelocityComponent.class));
        when(stageSetEntityFactory.createBackground(any(Texture.class), anyInt(), anyFloat())).thenReturn(background);

    }

    @Override
    public void initLevelScript() {
        this.scripting = new Level2Script(screen, assets, entityFactory, tweenManager, player, scriptItemExecutor, engine);
    }


    @Test
    public void music_level_is_played() {
        verify(assets).playMusic(MUSIC_LEVEL_2, 0.3f);
    }

    @Test
    public void mists_are_created_every_10_seconds() {
        launchScriptTimer(240);
        verify(stageSetEntityFactory, times(25)).createForeground(any(Texture.class), anyFloat());
    }

    @Test
    public void asteroids_are_created_every_2_and_7_and_9_seconds() {
        scripting = spy(scripting);
        launchScriptTimer(20);
        verify(scripting, times(13)).getRandomAsteroidType();
    }

    @Test
    public void five_solo_enemies_happen_during_beginning_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(20);
        verify(soloEnemyFactory, times(5)).createSoloEnemy(anyFloat(), anyFloat(), anyInt(), anyInt(), anyInt());
    }

    @Test
    public void twenty_three_enemies_squadrons_happen_during_first_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(20, 90);
        verify(scripting, times(23)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void one_solo_enemy_happens_during_first_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(20, 90);
        verify(soloEnemyFactory).createSoloEnemy(anyFloat(), anyFloat(), anyInt(), anyInt(), anyInt());
    }

    @Test
    public void twenty_nine_enemies_squadrons_happen_during_second_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(91, 160);
        verify(scripting, times(29)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void two_solo_enemy_happen_during_second_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(91, 160);
        verify(soloEnemyFactory, times(2)).createSoloEnemy(anyFloat(), anyFloat(), anyInt(), anyInt(), anyInt());
    }

    @Test
    public void thirty_eight_enemies_squadrons_happen_during_last_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(161, 250);
        verify(scripting, times(38)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void three_solo_enemy_happen_during_last_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(161, 250);
        verify(soloEnemyFactory, times(3)).createSoloEnemy(anyFloat(), anyFloat(), anyInt(), anyInt(), anyInt(), anyBoolean());
    }

    @Test
    public void boss_happens_end_of_level() {
        launchScriptTimer(255, 300);
        verify(assets).playSound(SOUND_BOSS_ALERT);
        verify(assets).stopMusic(MUSIC_LEVEL_2);
        verify(assets).playMusic(MUSIC_LEVEL_2_BOSS, 1f);
        verify(scriptItemExecutor).execute(argThat(script -> script.typeShip == BOSS_LEVEL_2));
    }

    @Test
    public void boss_never_happens_when_game_over() {
        player.add(engine.createComponent(GameOverComponent.class));
        launchScriptTimer(255, 300);
        verify(assets, never()).stopMusic(MUSIC_LEVEL_2);
        verify(assets, never()).playMusic(MUSIC_LEVEL_2_BOSS, 1f);
        verify(assets, never()).playSound(SOUND_BOSS_ALERT);
        verify(scriptItemExecutor, never()).execute(argThat(script -> script.typeShip == BOSS_LEVEL_2));
    }

    @Test
    public void music_level_2_is_played() {
        verify(assets).playMusic(MUSIC_LEVEL_2, 0.3f);
    }
}
