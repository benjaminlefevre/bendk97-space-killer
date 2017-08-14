package com.benk97.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.benk97.SpaceKillerGame;
import com.benk97.assets.Assets;
import com.benk97.components.GameOverComponent;
import com.benk97.entities.SoloEnemyFactory;
import com.benk97.systems.FollowPlayerSystem;

import java.util.LinkedList;
import java.util.List;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.*;
import static com.benk97.components.TankComponent.TankLevel.*;
import static com.benk97.entities.EntityFactory.*;
import static com.benk97.entities.SquadronFactory.BOSS_LEVEL3_MOVE;
import static com.benk97.entities.SquadronFactory.LINEAR_Y_SAME_POS;
import static com.benk97.screens.LevelScreen.Level.Level3;

public class Level3Screen extends LevelScreen {


    private SoloEnemyFactory soloEnemyFactory;

    private LinkedList<ScriptItem> scriptItemsEasy;
    private LinkedList<ScriptItem> scriptItemsMediumLeft;
    private LinkedList<ScriptItem> scriptItemsMediumRight;
    private LinkedList<ScriptItem> scriptItemsHardLeft;
    private LinkedList<ScriptItem> scriptItemsHardRight;
    private ScriptItem boss;


    private Array<Entity> backgrounds = new Array<Entity>();


    public Level3Screen(final Assets assets, SpaceKillerGame game) {
        super(assets, game, Level3);
        soloEnemyFactory = new SoloEnemyFactory(Level3, engine, tweenManager, entityFactory);
        backgrounds.add(entityFactory.createBackground(assets.get(GFX_BGD_LEVEL3), 0, -BGD_VELOCIY_LEVEL3));
        new Thread(new Runnable() {
            @Override
            public void run() {
                spriteMaskFactory.addMask(assets.get(GFX_LEVEL3_ATLAS_MASK).getTextures().first());
            }
        }).start();
        playMusic(MUSIC_LEVEL_3);
        startLevel(-3f);
    }

    @Override
    protected void createSystems(Entity player, Array<Entity> lives, Array<Entity> bombs, SpriteBatch batcher) {
        super.createSystems(player, lives, bombs, batcher);
        engine.addSystem(new FollowPlayerSystem(2));
    }

    @Override
    protected void initSpawns() {
        scriptItemsEasy = new LinkedList<ScriptItem>(randomEasySpawnEnemies(30));
        scriptItemsMediumLeft = new LinkedList<ScriptItem>(randomMediumSpawnEnemiesComingFromLeft(30));
        scriptItemsMediumRight = new LinkedList<ScriptItem>(randomMediumSpawnEnemiesComingFromRight(30));
        scriptItemsHardLeft = new LinkedList<ScriptItem>(randomHardSpawnEnemiesComingFromLeft(30));
        scriptItemsHardRight = new LinkedList<ScriptItem>(randomHardSpawnEnemiesComingFromRight(30));
        boss = new ScriptItem(BOSS_LEVEL_3, BOSS_LEVEL3_MOVE, 150f, 1, false, true, 20000,
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
        if (second % 2 == 0 || second % 5 == 0 || second % 7 == 0) {
            new ScriptItem(getRandomHouseType(), LINEAR_Y_SAME_POS,
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
            soloEnemyFactory.createTank(BGD_VELOCIY_LEVEL3, EASY, 5, 200);
        }

        if (second >= 15 && second <= 80) {
            if (second % 10 == 0) {
                soloEnemyFactory.createTank(BGD_VELOCIY_LEVEL3, EASY, 5, 200);
            }
            if (second % 25 == 0) {
                soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT, 10, 200);
            }
            if (second % 4 == 0) {
                scriptItemsEasy.poll().execute();
            }

        } else if (second > 80 && second <= 145) {
            if (second == 85) {
                assets.playSound(SOUND_GO);
            }
            if (second % 10 == 0) {
                soloEnemyFactory.createTank(BGD_VELOCIY_LEVEL3, MEDIUM, 5, 300);
            }
            if (second % 25 == 0) {
                soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT, 10, 200);
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
                assets.playSound(SOUND_GO);
            }
            if (second % 10 == 0) {
                soloEnemyFactory.createTank(BGD_VELOCIY_LEVEL3, HARD, 5, 400);
            }
            if (second == 180 || second == 190 || second == 200) {
                soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT_WHEN_TWICE, 10, 200, false);
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

        } else if (second >= 215 && player.getComponent(GameOverComponent.class) == null) {
            switch (second) {
                case 215:
                    assets.playSound(SOUND_BOSS_ALERT);
                    break;
                case 219:
                    assets.stopMusic(MUSIC_LEVEL_3);
                    playMusic(MUSIC_LEVEL_3_BOSS);
                    boss.execute();
                    break;
            }
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

}

