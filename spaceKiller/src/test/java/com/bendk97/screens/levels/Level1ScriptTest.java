/*
 * Developed by Benjamin Lefèvre
 * Last modified 08/10/18 22:33
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.VelocityComponent;
import com.bendk97.entities.EntityFactory;
import com.bendk97.screens.levels.utils.ScriptItem;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.LinkedList;

import static com.bendk97.assets.Assets.MUSIC_LEVEL_1;
import static com.bendk97.assets.Assets.SOUND_BOSS_ALERT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.*;


public class Level1ScriptTest extends LevelScriptTest{

    @Override
    public void initSpecificMocking(){
        Entity background = new Entity();
        background.add(engine.createComponent(VelocityComponent.class));
        when(entityFactory.createBackground(any(Texture.class), anyFloat())).thenReturn(background);
    }

    @Override
    public void initLevelScript() {
        this.scripting = new Level1Script(assets, entityFactory, tweenManager, player, squadronFactory, scriptItemExecutor);
    }

    @Test
    public void mists_are_created_every_30_seconds() {
        launchScriptTimer(240);
        verify(entityFactory, times(9)).createForeground(any(Texture.class), anyFloat());
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
    public void eightteen_enemies_squadrons_happen_during_second_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(61, 120);
        verify(scripting, times(18)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void eightteen_enemies_squadrons_happen_during_last_part_of_level() {
        scripting = spy(scripting);
        launchScriptTimer(121, 180);
        verify(scripting, times(18)).executeScriptFromList(any(LinkedList.class));
    }

    @Test
    public void boss_happens_end_of_level() {
        launchScriptTimer(181, 250);
        verify(assets).playSound(SOUND_BOSS_ALERT);
        verify(scriptItemExecutor).execute(argThat(new ArgumentMatcher<ScriptItem>() {
            @Override
            public boolean matches(ScriptItem script) {
                return script.typeShip == EntityFactory.BOSS_LEVEL_1;
            }
        }));
    }

    @Test
    public void boss_never_happens_when_game_over() {
        player.add(engine.createComponent(GameOverComponent.class));
        launchScriptTimer(181, 250);
        verify(assets, never()).playSound(SOUND_BOSS_ALERT);
        verify(scriptItemExecutor, never()).execute(argThat(new ArgumentMatcher<ScriptItem>() {
            @Override
            public boolean matches(ScriptItem script) {
                return script.typeShip == EntityFactory.BOSS_LEVEL_1;
            }
        }));
    }

    @Test
    public void music_level_1_is_played() {
        verify(assets).playMusic(MUSIC_LEVEL_1, 0.3f);
    }
}
