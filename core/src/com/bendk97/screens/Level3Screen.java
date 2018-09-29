/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 22:14
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.bendk97.components.TankComponent;
import com.bendk97.entities.EntityFactory;
import com.bendk97.entities.SquadronFactory;

import java.util.LinkedList;
import java.util.List;

import static com.bendk97.SpaceKillerGameConstants.*;

public class Level3Screen extends LevelScreen {


    private com.bendk97.entities.SoloEnemyFactory soloEnemyFactory;

    private LinkedList<ScriptItem> scriptItemsEasy;
    private LinkedList<ScriptItem> scriptItemsMediumLeft;
    private LinkedList<ScriptItem> scriptItemsMediumRight;
    private LinkedList<ScriptItem> scriptItemsHardLeft;
    private LinkedList<ScriptItem> scriptItemsHardRight;
    private ScriptItem boss;


    public Level3Screen(final com.bendk97.assets.Assets assets, com.bendk97.SpaceKillerGame game) {
        super(assets, game, Level.Level3);
        soloEnemyFactory = new com.bendk97.entities.SoloEnemyFactory(Level.Level3, engine, tweenManager, entityFactory, player);
        Array<Entity> backgrounds = new Array<Entity>();
        backgrounds.add(entityFactory.createBackground(assets.get(com.bendk97.assets.Assets.GFX_BGD_LEVEL3), 0, -BGD_VELOCIY_LEVEL3));
        new Thread(new Runnable() {
            @Override
            public void run() {
                spriteMaskFactory.addMask(assets.get(com.bendk97.assets.Assets.GFX_LEVEL3_ATLAS_MASK).getTextures().first());
            }
        }).start();
        playMusic(com.bendk97.assets.Assets.MUSIC_LEVEL_3, 0.6f);
        startLevel(-3f);
    }

    @Override
    protected void createSystems(Entity player, SnapshotArray<Entity> lives, SnapshotArray<Entity> bombs, SpriteBatch batcher, ScreenShake screenShake) {
        super.createSystems(player, lives, bombs, batcher, screenShake);
        engine.addSystem(new com.bendk97.systems.FollowPlayerSystem(2));
    }

    @Override
    protected void initSpawns() {
        scriptItemsEasy = new LinkedList<ScriptItem>(randomEasySpawnEnemies(30));
        scriptItemsMediumLeft = new LinkedList<ScriptItem>(randomMediumSpawnEnemiesComingFromLeft(30));
        scriptItemsMediumRight = new LinkedList<ScriptItem>(randomMediumSpawnEnemiesComingFromRight(30));
        scriptItemsHardLeft = new LinkedList<ScriptItem>(randomHardSpawnEnemiesComingFromLeft(30));
        scriptItemsHardRight = new LinkedList<ScriptItem>(randomHardSpawnEnemiesComingFromRight(30));
        boss = new ScriptItem(EntityFactory.BOSS_LEVEL_3, SquadronFactory.BOSS_LEVEL3_MOVE, 150f, 1, false, true, 20000,
                ENEMY_BULLET_HARD_VELOCITY);
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


    @Override
    protected void script(int second) {
        super.script(second);
        if (second % 2 == 0 || second % 5 == 0 || second % 7 == 0) {
            new ScriptItem(getRandomHouseType(), SquadronFactory.LINEAR_Y_SAME_POS,
                    BGD_VELOCIY_LEVEL3,
                    1, random.nextInt() % 5 == 0, false, 0, 0f,
                    random.nextFloat() * (SCREEN_WIDTH - 64f),
                    SCREEN_HEIGHT).execute();
        }


        if (second % 12 == 0) {
            entityFactory.createForeground(getRandomMist(), 450f);
        }

        if (second < 0) {
            return;
        }
        if (second < 15 && second % 5 == 0) {
            soloEnemyFactory.createTank(BGD_VELOCIY_LEVEL3, TankComponent.TankLevel.EASY, 5, 200);
        }

        if (second >= 15 && second <= 80) {
            if (second % 10 == 0) {
                soloEnemyFactory.createTank(BGD_VELOCIY_LEVEL3, TankComponent.TankLevel.EASY, 5, 200);
            }
            if (second % 25 == 0) {
                soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL3_VELOCITY, STATIC_ENEMY_LEVEL3_BULLET_VELOCITY, STATIC_ENEMY_LEVEL3_RATE_SHOOT, 10, 200);
            }
            if (second % 4 == 0) {
                scriptItemsEasy.poll().execute();
            }

        } else if (second > 80 && second <= 145) {
            if (second == 85) {
                assets.playSound(com.bendk97.assets.Assets.SOUND_GO);
            }
            if (second % 10 == 0) {
                soloEnemyFactory.createTank(BGD_VELOCIY_LEVEL3, TankComponent.TankLevel.MEDIUM, 5, 300);
            }
            if (second % 25 == 0) {
                soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL3_VELOCITY, STATIC_ENEMY_LEVEL3_BULLET_VELOCITY, STATIC_ENEMY_LEVEL3_RATE_SHOOT, 10, 200);
            }
            boolean left = random.nextBoolean();
            LinkedList<ScriptItem> medium = left ? scriptItemsMediumLeft : scriptItemsMediumRight;
            LinkedList<ScriptItem> mediumOther = left ? scriptItemsMediumRight : scriptItemsMediumLeft;

            if (second % 4 == 0) {
                medium.poll().execute();
            }
            if (second % 8 == 0) {
                mediumOther.poll().execute();
            }

        } else if (second > 145 && second <= 210) {
            if (second == 150) {
                assets.playSound(com.bendk97.assets.Assets.SOUND_GO);
            }
            if (second % 10 == 0) {
                soloEnemyFactory.createTank(BGD_VELOCIY_LEVEL3, TankComponent.TankLevel.HARD, 5, 400);
            }
            if (second == 180 || second == 190 || second == 200) {
                soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL3_VELOCITY, STATIC_ENEMY_LEVEL3_BULLET_VELOCITY, STATIC_ENEMY_LEVEL3_RATE_SHOOT_WHEN_TWICE, 10, 200, false);
            }

            boolean left = random.nextBoolean();
            LinkedList<ScriptItem> hard = left ? scriptItemsHardLeft : scriptItemsHardRight;
            LinkedList<ScriptItem> hardOther = left ? scriptItemsHardRight : scriptItemsHardLeft;

            if (second % 4 == 0) {
                hard.poll().execute();
            }
            if (second % 8 == 0) {
                hardOther.poll().execute();
            }

        } else if (second >= 215 && player.getComponent(com.bendk97.components.GameOverComponent.class) == null) {
            switch (second) {
                case 215:
                    assets.playSound(com.bendk97.assets.Assets.SOUND_BOSS_ALERT);
                    break;
                case 219:
                    assets.stopMusic(com.bendk97.assets.Assets.MUSIC_LEVEL_3);
                    playMusic(com.bendk97.assets.Assets.MUSIC_LEVEL_3_BOSS, 1f);
                    boss.execute();
                    break;
            }
        }
    }


    @Override
    public int getRandomShipType() {
        return EntityFactory.SHIP_LV3_1 + random.nextInt(EntityFactory.NB_SHIP_LV3);
    }

    @Override
    protected int getRandomMoveType() {
        return random.nextInt(9);
    }

}

