/*
 * Developed by Benjamin Lefèvre
 * Last modified 07/10/18 22:24
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Array;
import com.bendk97.assets.Assets;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.TankComponent;
import com.bendk97.entities.EntityFactory;
import com.bendk97.entities.enemies.SquadronFactory;
import com.bendk97.screens.levels.utils.ScriptItem;
import com.bendk97.screens.levels.utils.ScriptItemBuilder;
import com.bendk97.screens.levels.utils.ScriptItemExecutor;
import com.bendk97.systems.FollowPlayerSystem;

import java.util.LinkedList;
import java.util.List;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.entities.EntityFactoryIds.*;
import static com.bendk97.screens.levels.Level.Level3;
import static com.bendk97.screens.levels.Level.SoundEffect.GO;

public class Level3Script extends LevelScript {

    private LinkedList<ScriptItem> scriptItemsEasy;
    private LinkedList<ScriptItem> scriptItemsMediumLeft;
    private LinkedList<ScriptItem> scriptItemsMediumRight;
    private LinkedList<ScriptItem> scriptItemsHardLeft;
    private LinkedList<ScriptItem> scriptItemsHardRight;

    public Level3Script(final LevelScreen levelScreen, final Assets assets, EntityFactory entityFactory, TweenManager tweenManager, Entity player,
                        PooledEngine engine) {
        super(levelScreen, Level3, assets, entityFactory, tweenManager, player);
        initLevel3(assets, entityFactory, engine);
    }

    /*
     for test purposes only
   */
    protected Level3Script(final LevelScreen levelScreen, Assets assets, EntityFactory entityFactory, TweenManager tweenManager, Entity player,
                           ScriptItemExecutor scriptItemExecutor, PooledEngine engine) {
        super(levelScreen, Level3, assets, entityFactory, tweenManager, player, scriptItemExecutor);
        initLevel3(assets, entityFactory, engine);
    }

    private void initLevel3(Assets assets, EntityFactory entityFactory, PooledEngine engine) {
        Array<Entity> backgrounds = new Array<>();
        backgrounds.add(entityFactory.stageSetEntityFactory.createBackground(assets.get(Assets.GFX_BGD_LEVEL3), 0, -BGD_VELOCITY_LEVEL3));
        engine.addSystem(new FollowPlayerSystem(2));
    }


    @Override
    public void initSpawns() {
        scriptItemsEasy = new LinkedList<>(randomEasySpawnEnemies(30));
        scriptItemsMediumLeft = new LinkedList<>(randomMediumSpawnEnemiesComingFromLeft(30));
        scriptItemsMediumRight = new LinkedList<>(randomMediumSpawnEnemiesComingFromRight(30));
        scriptItemsHardLeft = new LinkedList<>(randomHardSpawnEnemiesComingFromLeft(30));
        scriptItemsHardRight = new LinkedList<>(randomHardSpawnEnemiesComingFromRight(30));
        boss = new ScriptItemBuilder().typeShip(BOSS_LEVEL_3).typeSquadron(SquadronFactory.BOSS_LEVEL3_MOVE).velocity(150f).number(1).powerUp(false).displayBonus(true).withBonus(20000).bulletVelocity(ENEMY_BULLET_HARD_VELOCITY).createScriptItem();
    }


    @Override
    public void script(int second) {
        if (second < 0) {
            return;
        }
        super.script(second);

        scriptHouses(second);
        scriptMists(second);

        if (second < 15) {
            scriptFirstTanks(second);
            return;
        }
        if (second <= 80) {
            scriptEasyPart(second);
            return;

        }
        if (second <= 145) {
            scriptMediumPart(second);
            return;
        }
        if (second <= 210) {
            scriptHardPart(second);
            return;
        }
        if (second >= 215) {
            scriptBoss(second);
        }
    }

    private void scriptHouses(int second) {
        if (second % 2 == 0 || second % 5 == 0 || second % 7 == 0) {
            scriptItemExecutor.execute(new ScriptItemBuilder().typeShip(getRandomHouseType()).typeSquadron(SquadronFactory.LINEAR_Y_SAME_POS).velocity(BGD_VELOCITY_LEVEL3).number(1).powerUp(random.nextInt() % 5 == 0).displayBonus(false).withBonus(0).bulletVelocity(0f).withParams(random.nextFloat() * (SCREEN_WIDTH - 64f), SCREEN_HEIGHT).createScriptItem());
        }
    }

    private void scriptMists(int second) {
        if (second % 12 == 0) {
            entityFactory.stageSetEntityFactory.createForeground(getRandomMist(), 450f);
        }
    }

    private void scriptFirstTanks(int second) {
        if (second % 5 == 0) {
            entityFactory.enemyEntityFactory.soloEnemyFactory.createTank(BGD_VELOCITY_LEVEL3, TankComponent.TankLevel.EASY, 5, 200);
        }
    }

    private void scriptEasyPart(int second) {
        if (second % 10 == 0) {
            entityFactory.enemyEntityFactory.soloEnemyFactory.createTank(BGD_VELOCITY_LEVEL3, TankComponent.TankLevel.EASY, 5, 200);
        }
        if (second % 25 == 0) {
            entityFactory.enemyEntityFactory.soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL3_VELOCITY, STATIC_ENEMY_LEVEL3_BULLET_VELOCITY, STATIC_ENEMY_LEVEL3_RATE_SHOOT, 10, 200);
        }
        if (second % 4 == 0) {
            executeScriptFromList(scriptItemsEasy);
        }
    }

    private void scriptMediumPart(int second) {
        if (second == 85) {
            playSound(GO);
        }
        if (second % 10 == 0) {
            entityFactory.enemyEntityFactory.soloEnemyFactory.createTank(BGD_VELOCITY_LEVEL3, TankComponent.TankLevel.MEDIUM, 5, 300);
        }
        if (second % 25 == 0) {
            entityFactory.enemyEntityFactory.soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL3_VELOCITY, STATIC_ENEMY_LEVEL3_BULLET_VELOCITY, STATIC_ENEMY_LEVEL3_RATE_SHOOT, 10, 200);
        }
        boolean left = random.nextBoolean();
        if (second % 4 == 0) {
            executeScriptFromList(left ? scriptItemsMediumLeft : scriptItemsMediumRight);
        }
        if (second % 8 == 0) {
            executeScriptFromList(left ? scriptItemsMediumRight : scriptItemsMediumLeft);
        }
    }

    private void scriptHardPart(int second) {
        if (second == 150) {
            playSound(GO);
        }
        if (second % 10 == 0) {
            entityFactory.enemyEntityFactory.soloEnemyFactory.createTank(BGD_VELOCITY_LEVEL3, TankComponent.TankLevel.HARD, 5, 400);
        }
        if (second == 180 || second == 190 || second == 200) {
            entityFactory.enemyEntityFactory.soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL3_VELOCITY, STATIC_ENEMY_LEVEL3_BULLET_VELOCITY, STATIC_ENEMY_LEVEL3_RATE_SHOOT_WHEN_TWICE, 10, 200, false);
        }
        boolean left = random.nextBoolean();
        if (second % 4 == 0) {
            executeScriptFromList(left ? scriptItemsHardLeft : scriptItemsHardRight);
        }
        if (second % 8 == 0) {
            executeScriptFromList(left ? scriptItemsHardRight : scriptItemsHardLeft);
        }
    }

    private void scriptBoss(int second) {
        if (player.getComponent(GameOverComponent.class) != null) {
            return;
        }
        if (second == 215) {
            bossIsComing();
            return;
        }
        if (second == 219) {
            bossIsHere();
        }
    }

    @Override
    public int getRandomShipType() {
        return SHIP_LV3_1 + random.nextInt(NB_SHIP_LV3);
    }

    @Override
    protected int getRandomMoveType() {
        return random.nextInt(9);
    }

    private List<ScriptItem> randomEasySpawnEnemies(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL3_VELOCITY_EASY, MEDIUM_RATE_SHOOT, ENEMY_LEVEL3_BULLET_EASY_VELOCITY, BONUS_LEVEL3_SQUADRON_EASY, 4, 6, null);
    }

    private List<ScriptItem> randomMediumSpawnEnemiesComingFromLeft(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL3_VELOCITY_MEDIUM, MEDIUM_RATE_SHOOT, ENEMY_LEVEL3_BULLET_MEDIUM_VELOCITY, BONUS_LEVEL3_SQUADRON_MEDIUM, 5, 7, true);
    }

    private List<ScriptItem> randomMediumSpawnEnemiesComingFromRight(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL3_VELOCITY_MEDIUM, MEDIUM_RATE_SHOOT, ENEMY_LEVEL3_BULLET_MEDIUM_VELOCITY, BONUS_LEVEL3_SQUADRON_MEDIUM, 5, 7, false);
    }

    private List<ScriptItem> randomHardSpawnEnemiesComingFromLeft(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL3_VELOCITY_HARD, MEDIUM_RATE_SHOOT, ENEMY_LEVEL3_BULLET_HARD_VELOCITY, BONUS_LEVEL3_SQUADRON_HARD, 6, 10, true);
    }

    private List<ScriptItem> randomHardSpawnEnemiesComingFromRight(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL3_VELOCITY_HARD, MEDIUM_RATE_SHOOT, ENEMY_LEVEL3_BULLET_HARD_VELOCITY, BONUS_LEVEL3_SQUADRON_HARD, 6, 10, false);
    }
}

