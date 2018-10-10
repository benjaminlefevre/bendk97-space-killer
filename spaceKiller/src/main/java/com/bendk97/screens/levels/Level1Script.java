/*
 * Developed by Benjamin Lefèvre
 * Last modified 08/10/18 19:43
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.bendk97.assets.Assets;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.Mappers;
import com.bendk97.entities.EntityFactory;
import com.bendk97.entities.SquadronFactory;
import com.bendk97.screens.levels.utils.ScriptItem;
import com.bendk97.screens.levels.utils.ScriptItemBuilder;
import com.bendk97.screens.levels.utils.ScriptItemExecutor;

import java.util.LinkedList;
import java.util.List;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.Assets.*;
import static com.bendk97.entities.EntityFactory.BOSS_LEVEL_1;
import static com.bendk97.entities.SquadronFactory.BOSS_MOVE;
import static com.bendk97.entities.SquadronFactory.LINEAR_Y;
import static com.bendk97.tweens.VelocityComponentAccessor.VELOCITY_Y;

public final class Level1Script extends LevelScript {
    private LinkedList<ScriptItem> scriptItemsEasy;
    private LinkedList<ScriptItem> scriptItemsMediumLeft;
    private LinkedList<ScriptItem> scriptItemsMediumRight;
    private LinkedList<ScriptItem> scriptItemsHardLeft;
    private LinkedList<ScriptItem> scriptItemsHardRight;
    private ScriptItem boss;
    private final Entity background;
    private final Entity background2;

    /*
    for test purposes only
     */
    protected Level1Script(Assets assets, EntityFactory entityFactory, TweenManager tweenManager, Entity player,
                           SquadronFactory squadronFactory, ScriptItemExecutor scriptItemExecutor) {
        super(assets, entityFactory, tweenManager, player, squadronFactory, scriptItemExecutor);
        background = entityFactory.createBackground(assets.get(GFX_BGD_LEVEL1), -BGD_VELOCITY);
        background2 = entityFactory.createBackground(assets.get(GFX_BGD_STARS), -BGD_PARALLAX_VELOCITY);
        assets.playMusic(MUSIC_LEVEL_1, 0.3f);
    }

    public Level1Script(final Assets assets, EntityFactory entityFactory, TweenManager tweenManager, Entity player,
                        PooledEngine engine, OrthographicCamera camera) {
        super(assets, entityFactory, tweenManager, player, engine, camera);
        assets.playMusic(MUSIC_LEVEL_1, 0.3f);
        background = entityFactory.createBackground(assets.get(GFX_BGD_LEVEL1), -BGD_VELOCITY);
        background2 = entityFactory.createBackground(assets.get(GFX_BGD_STARS), -BGD_PARALLAX_VELOCITY);
    }

    @Override
    public void initSpawns() {
        scriptItemsEasy = new LinkedList<ScriptItem>(randomEasySpawnEnemies(13));
        scriptItemsMediumLeft = new LinkedList<ScriptItem>(randomMediumSpawnEnemiesComingFromLeft(12));
        scriptItemsMediumRight = new LinkedList<ScriptItem>(randomMediumSpawnEnemiesComingFromRight(12));
        scriptItemsHardLeft = new LinkedList<ScriptItem>(randomHardSpawnEnemiesComingFromLeft(12));
        scriptItemsHardRight = new LinkedList<ScriptItem>(randomHardSpawnEnemiesComingFromRight(12));
        boss = new ScriptItemBuilder().typeShip(BOSS_LEVEL_1).typeSquadron(BOSS_MOVE).velocity(75f).number(1).powerUp(false).displayBonus(true).withBonus(10000).bulletVelocity(ENEMY_BULLET_EASY_VELOCITY).createScriptItem();
    }


    @Override
    public void script(int second) {
        if (second < 0) {
            return;
        }
        super.script(second);
        scriptAsteroids(second);
        scriptMists(second);

        if (second <= 60) {
            scriptEasyPart(second);
            return;
        }
        if (second <= 120) {
            scriptMediumPart(second);
            return;
        }
        if (second <= 180) {
            scriptDifficultPart(second);
            return;
        }
        if (second >= 185) {
            scriptBoss(second);
            return;
        }
    }

    private void scriptAsteroids(int second) {
        if (second % 3 == 0 || second % 7 == 0) {
            scriptItemExecutor.execute(
                    new ScriptItemBuilder().typeShip(getRandomAsteroidType()).typeSquadron(LINEAR_Y).velocity(40f + random.nextFloat() * 160f).number(1).powerUp(random.nextInt() % 6 == 0).displayBonus(false).withBonus(0).bulletVelocity(0f).withParams(random.nextFloat() * (SCREEN_WIDTH - 36f), SCREEN_HEIGHT).createScriptItem());
        }
    }

    private void scriptMists(int second) {
        if (second % 30 == 0) {
            entityFactory.createForeground(getRandomMist(), BGD_VELOCITY_FORE);
        }
    }

    private void scriptEasyPart(int second) {
        if (second % 5 == 0) {
            executeScriptFromList(scriptItemsEasy);
        }
    }

    private void scriptMediumPart(int second) {
        if (second == 65) {
            assets.playSound(SOUND_GO);
        }
        boolean left = random.nextBoolean();
        if (second % 5 == 0) {
            executeScriptFromList(left ? scriptItemsMediumLeft : scriptItemsMediumRight);
        }
        if (second % 10 == 0) {
            executeScriptFromList(left ? scriptItemsMediumRight : scriptItemsMediumLeft);
        }
    }

    private void scriptDifficultPart(int second) {
        if (second == 125) {
            assets.playSound(SOUND_GO);
        }
        boolean left = random.nextBoolean();
        if (second % 5 == 0) {
            executeScriptFromList(left ? scriptItemsHardLeft : scriptItemsHardRight);
        }
        if (second % 10 == 0) {
            executeScriptFromList(left ? scriptItemsHardRight : scriptItemsHardLeft);
        }
    }

    private void scriptBoss(int second) {
        if (player.getComponent(GameOverComponent.class) != null) {
            return;
        }
        if (second == 185) {
            assets.playSound(SOUND_BOSS_ALERT);
            Tween.to(Mappers.velocity.get(background), VELOCITY_Y, 4).ease(Quad.IN)
                    .target(50f).start(tweenManager);
            Tween.to(Mappers.velocity.get(background2), VELOCITY_Y, 4).ease(Quad.IN)
                    .target(20f).start(tweenManager);
            return;
        }
        if (second == 189) {
            assets.stopMusic(MUSIC_LEVEL_1);
            assets.playMusic(MUSIC_LEVEL_1_BOSS, 1f);
            scriptItemExecutor.execute(boss);
            return;
        }
    }

    private List<ScriptItem> randomEasySpawnEnemies(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_EASY, STANDARD_RATE_SHOOT, ENEMY_BULLET_EASY_VELOCITY, BONUS_SQUADRON_EASY, 5, 10, null);

    }

    private List<ScriptItem> randomMediumSpawnEnemiesComingFromLeft(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_MEDIUM, STANDARD_RATE_SHOOT, ENEMY_BULLET_MEDIUM_VELOCITY, BONUS_SQUADRON_MEDIUM, 5, 12, true);

    }

    private List<ScriptItem> randomMediumSpawnEnemiesComingFromRight(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_MEDIUM, STANDARD_RATE_SHOOT, ENEMY_BULLET_MEDIUM_VELOCITY, BONUS_SQUADRON_MEDIUM, 5, 12, false);

    }

    private List<ScriptItem> randomHardSpawnEnemiesComingFromLeft(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_HARD, STANDARD_RATE_SHOOT, ENEMY_BULLET_HARD_VELOCITY, BONUS_SQUADRON_HARD, 7, 15, true);

    }

    private List<ScriptItem> randomHardSpawnEnemiesComingFromRight(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_HARD, STANDARD_RATE_SHOOT, ENEMY_BULLET_HARD_VELOCITY, BONUS_SQUADRON_HARD, 7, 15, false);

    }
}
