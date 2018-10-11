/*
 * Developed by Benjamin Lefèvre
 * Last modified 10/10/18 21:57
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.TankComponent.TankLevel;
import com.bendk97.components.VelocityComponent;
import com.bendk97.entities.EntityFactory;
import com.bendk97.entities.SoloEnemyFactory;
import org.junit.Test;
import org.mockito.Mock;

import java.util.LinkedList;

import static com.bendk97.assets.Assets.*;
import static org.mockito.Mockito.*;

public class Level3ScriptTest extends LevelScriptTest {

    @Mock
    private SoloEnemyFactory soloEnemyFactory;

    @Override
    public void initSpecificMocking() {
        Entity background = new Entity();
        player.add(engine.createComponent(PlayerComponent.class));
        background.add(engine.createComponent(VelocityComponent.class));
        when(entityFactory.createBackground(any(Texture.class), anyInt(), anyFloat())).thenReturn(background);

    }

    @Override
    public void initLevelScript() {
        this.scripting = new Level3Script(screen, assets, entityFactory, tweenManager, player, squadronFactory, soloEnemyFactory, scriptItemExecutor, engine);
    }


    @Test
    public void music_level_is_played() {
        verify(assets).playMusic(MUSIC_LEVEL_3, 0.6f);
    }

    @Test
    public void mists_are_created_every_12_seconds() {
        launchScriptTimer(240);
        verify(entityFactory, times(21)).createForeground(any(Texture.class), anyFloat());
    }

    @Test
    public void houses_are_created_every_2_and_5_and_9_seconds() {
        scripting = spy(scripting);
        launchScriptTimer(20);
        verify(scripting, times(14)).getRandomHouseType();
    }

    @Test
    public void three_tanks_happen_during_beginning_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(15);
        verify(soloEnemyFactory, times(3)).createTank(anyFloat(), any(TankLevel.class), anyInt(), anyInt());
    }

    @Test
    public void seventeen_enemies_squadrons_happen_during_first_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(15, 80);
        verify(scripting, times(17)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void three_solo_enemies_happen_during_first_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(15, 80);
        verify(soloEnemyFactory, times(3)).createSoloEnemy(anyFloat(), anyFloat(), anyInt(), anyInt(), anyInt());
    }

    @Test
    public void seven_tanks_happen_during_first_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(15, 80);
        verify(soloEnemyFactory, times(7)).createTank(anyFloat(), any(TankLevel.class), anyInt(), anyInt());
    }

    @Test
    public void twenty_four_enemies_squadrons_happen_during_second_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(81, 145);
        verify(scripting, times(24)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void two_solo_enemy_happen_during_second_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(81, 145);
        verify(soloEnemyFactory, times(2)).createSoloEnemy(anyFloat(), anyFloat(), anyInt(), anyInt(), anyInt());
    }

    @Test
    public void six_tanks_happen_during_second_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(81, 145);
        verify(soloEnemyFactory, times(6)).createTank(anyFloat(), any(TankLevel.class), anyInt(), anyInt());
    }

    @Test
    public void twenty_four_enemies_squadrons_happen_during_last_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(146, 210);
        verify(scripting, times(24)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void three_solo_enemy_happen_during_last_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(146, 210);
        verify(soloEnemyFactory, times(3)).createSoloEnemy(anyFloat(), anyFloat(), anyInt(), anyInt(), anyInt(), anyBoolean());
    }

    @Test
    public void seven_tanks_happen_during_last_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(146, 210);
        verify(soloEnemyFactory, times(7)).createTank(anyFloat(), any(TankLevel.class), anyInt(), anyInt());
    }

    @Test
    public void boss_happens_end_of_level() {
        launchScriptTimer(215, 300);
        verify(assets).playSound(SOUND_BOSS_ALERT);
        verify(assets).stopMusic(MUSIC_LEVEL_3);
        verify(assets).playMusic(MUSIC_LEVEL_3_BOSS, 1f);
        verify(scriptItemExecutor).execute(argThat(script -> script.typeShip == EntityFactory.BOSS_LEVEL_3));
    }

    @Test
    public void boss_never_happens_when_game_over() {
        player.add(engine.createComponent(GameOverComponent.class));
        launchScriptTimer(215, 300);
        verify(assets, never()).playSound(SOUND_BOSS_ALERT);
        verify(assets, never()).stopMusic(MUSIC_LEVEL_3);
        verify(assets, never()).playMusic(MUSIC_LEVEL_3_BOSS, 1f);
        verify(scriptItemExecutor, never()).execute(argThat(script -> script.typeShip == EntityFactory.BOSS_LEVEL_3));
    }

    @Test
    public void music_level_3_is_played() {
        verify(assets).playMusic(MUSIC_LEVEL_3, 0.6f);
    }
}
