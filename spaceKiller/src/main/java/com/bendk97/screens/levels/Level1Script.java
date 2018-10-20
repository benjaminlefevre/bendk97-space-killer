/*
 * Developed by Benjamin Lefèvre
 * Last modified 08/10/18 19:43
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;
import box2dLight.ConeLight;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.bendk97.assets.Assets;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.screens.levels.utils.ScriptItem;
import com.bendk97.screens.levels.utils.ScriptItemBuilder;
import com.bendk97.screens.levels.utils.ScriptItemExecutor;
import com.bendk97.tweens.ConeLightTween;

import java.util.LinkedList;
import java.util.List;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.Assets.GFX_BGD_LEVEL1;
import static com.bendk97.assets.Assets.GFX_BGD_STARS;
import static com.bendk97.entities.EntityFactoryIds.BOSS_LEVEL_1;
import static com.bendk97.entities.enemies.SquadronFactory.BOSS_MOVE;
import static com.bendk97.entities.enemies.SquadronFactory.LINEAR_Y;
import static com.bendk97.screens.levels.Level.Level1;
import static com.bendk97.screens.levels.Level.MusicTrack.BOSS;
import static com.bendk97.screens.levels.Level.MusicTrack.LEVEL;
import static com.bendk97.screens.levels.Level.SoundEffect.BOSS_ALERT;
import static com.bendk97.screens.levels.Level.SoundEffect.GO;
import static com.bendk97.tweens.VelocityComponentAccessor.VELOCITY_Y;

public final class Level1Script extends LevelScript {
    private LinkedList<ScriptItem> scriptItemsEasy;
    private LinkedList<ScriptItem> scriptItemsMediumLeft;
    private LinkedList<ScriptItem> scriptItemsMediumRight;
    private LinkedList<ScriptItem> scriptItemsHardLeft;
    private LinkedList<ScriptItem> scriptItemsHardRight;
    private ScriptItem boss;
    private Entity background;
    private Entity background2;

    /*
    for test purposes only
     */
    protected Level1Script(final LevelScreen levelScreen, Assets assets, EntityFactory entityFactory, TweenManager tweenManager, Entity player,
                           ScriptItemExecutor scriptItemExecutor) {
        super(levelScreen, Level1, assets, entityFactory, tweenManager, player, scriptItemExecutor);
        initLevel1(assets, entityFactory);
    }

    public Level1Script(final LevelScreen levelScreen, final Assets assets, EntityFactory entityFactory, TweenManager tweenManager, Entity player) {
        super(levelScreen, Level1, assets, entityFactory, tweenManager, player);
        initLevel1(assets, entityFactory);
    }

    private void initLevel1(Assets assets, EntityFactory entityFactory) {
        background = entityFactory.stageSetEntityFactory.createBackground(assets.get(GFX_BGD_LEVEL1), -BGD_VELOCITY);
        background2 = entityFactory.stageSetEntityFactory.createBackground(assets.get(GFX_BGD_STARS), -BGD_PARALLAX_VELOCITY);
       if(levelScreen.fxLightEnabled) {
           initAmbiantLights(entityFactory);
       }
    }

    private void initAmbiantLights(EntityFactory entityFactory) {
        ConeLight c1 = new ConeLight(entityFactory.rayHandler, 500, Color.GOLDENROD, 0,
                SCREEN_WIDTH + 40, SCREEN_HEIGHT + 40,
                225, 60);
        c1.setActive(true);
        Timeline timeline = Timeline.createSequence();
        timeline.push(
                Tween.to(c1, ConeLightTween.DISTANCE, 7f)
                        .delay(10f)
                        .ease(Linear.INOUT).target(1500f)
                        .delay(1f)
        );
        timeline.push(
                Tween.to(c1, ConeLightTween.DISTANCE, 3f)
                        .ease(Linear.INOUT).target(0f)
        );
        timeline.repeat(Tween.INFINITY, 5f).start(tweenManager);
    }

    @Override
    public void initSpawns() {
        scriptItemsEasy = new LinkedList<>(randomEasySpawnEnemies(13));
        scriptItemsMediumLeft = new LinkedList<>(randomMediumSpawnEnemiesComingFromLeft(12));
        scriptItemsMediumRight = new LinkedList<>(randomMediumSpawnEnemiesComingFromRight(12));
        scriptItemsHardLeft = new LinkedList<>(randomHardSpawnEnemiesComingFromLeft(12));
        scriptItemsHardRight = new LinkedList<>(randomHardSpawnEnemiesComingFromRight(12));
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
            entityFactory.stageSetEntityFactory.createForeground(getRandomMist(), BGD_VELOCITY_FORE);
        }
    }

    private void scriptEasyPart(int second) {
        if (second % 5 == 0) {
            executeScriptFromList(scriptItemsEasy);
        }
    }

    private void scriptMediumPart(int second) {
        if (second == 65) {
            playSound(GO);
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
            playSound(GO);
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
            playSound(BOSS_ALERT);
            Tween.to(ComponentMapperHelper.velocity.get(background), VELOCITY_Y, 4).ease(Quad.IN)
                    .target(50f).start(tweenManager);
            Tween.to(ComponentMapperHelper.velocity.get(background2), VELOCITY_Y, 4).ease(Quad.IN)
                    .target(20f).start(tweenManager);
            return;
        }
        if (second == 189) {
            stopMusic(LEVEL);
            playMusic(BOSS);
            scriptItemExecutor.execute(boss);
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
