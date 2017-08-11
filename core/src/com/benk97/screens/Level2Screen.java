package com.benk97.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.benk97.SpaceKillerGame;
import com.benk97.assets.Assets;
import com.benk97.components.GameOverComponent;
import com.benk97.components.Mappers;
import com.benk97.entities.SoloEnemyFactory;
import com.benk97.systems.FollowPlayerSystem;

import java.util.LinkedList;
import java.util.List;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.*;
import static com.benk97.entities.EntityFactory.BOSS_LEVEL_2;
import static com.benk97.entities.SquadronFactory.BOSS_LEVEL2_MOVE;
import static com.benk97.entities.SquadronFactory.LINEAR_Y;
import static com.benk97.screens.LevelScreen.Level.Level2;
import static com.benk97.tweens.VelocityComponentAccessor.VELOCITY_Y;

public class Level2Screen extends LevelScreen {

    private SoloEnemyFactory soloEnemyFactory;

    private LinkedList<ScriptItem> scriptItemsEasy;
    private LinkedList<ScriptItem> scriptItemsMediumLeft;
    private LinkedList<ScriptItem> scriptItemsMediumRight;
    private LinkedList<ScriptItem> scriptItemsHardLeft;
    private LinkedList<ScriptItem> scriptItemsHardRight;
    private ScriptItem boss;


    private Array<Entity> backgrounds = new Array<Entity>();


    public Level2Screen(final Assets assets, SpaceKillerGame game) {
        super(assets, game, Level2);
        soloEnemyFactory = new SoloEnemyFactory(engine, tweenManager, entityFactory);
        backgrounds.add(entityFactory.createBackground(assets.get(GFX_BGD_LEVEL2), 0, -500f));
        backgrounds.add(entityFactory.createBackground(assets.get(GFX_BGD_STARS_LEVEL2), 1, -300f));
        backgrounds.add(entityFactory.createBackground(assets.get(GFX_BGD_BIG_PLANET), 4, -250f));
        backgrounds.add(entityFactory.createBackground(assets.get(GFX_BGD_FAR_PLANETS), 2, -275f));
        backgrounds.add(entityFactory.createBackground(assets.get(GFX_BGD_RISING_PLANETS), 3, -325f));
        new Thread(new Runnable() {
            @Override
            public void run() {
                spriteMaskFactory.addMask(assets.get(GFX_LEVEL2_ATLAS_MASK).getTextures().first());
            }
        }).start();
        playMusic(MUSIC_LEVEL_2);
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
        boss = new ScriptItem(BOSS_LEVEL_2, BOSS_LEVEL2_MOVE, 100f, 1, false, true, 15000,
                ENEMY_BULLET_EASY_VELOCITY);
    }

    private List<ScriptItem> randomEasySpawnEnemies(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL2_VELOCITY_EASY, ENEMY_LEVEL2_BULLET_EASY_VELOCITY, BONUS_LEVEL2_SQUADRON_EASY, 5, 6, null);

    }

    private List<ScriptItem> randomMediumSpawnEnemiesComingFromLeft(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL2_VELOCITY_MEDIUM, ENEMY_LEVEL2_BULLET_MEDIUM_VELOCITY, BONUS_LEVEL2_SQUADRON_MEDIUM, 5, 8, true);

    }

    private List<ScriptItem> randomMediumSpawnEnemiesComingFromRight(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL2_VELOCITY_MEDIUM, ENEMY_LEVEL2_BULLET_MEDIUM_VELOCITY, BONUS_LEVEL2_SQUADRON_MEDIUM, 5, 10, false);

    }

    private List<ScriptItem> randomHardSpawnEnemiesComingFromLeft(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL2_VELOCITY_HARD, ENEMY_LEVEL2_BULLET_HARD_VELOCITY, BONUS_LEVEL2_SQUADRON_HARD, 6, 12, true);

    }

    private List<ScriptItem> randomHardSpawnEnemiesComingFromRight(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL2_VELOCITY_HARD, ENEMY_LEVEL2_BULLET_HARD_VELOCITY, BONUS_LEVEL2_SQUADRON_HARD, 6, 12, false);

    }

    @Override
    protected void script(int second) {
        if (second % 2 == 0 || second % 9 == 0 || second % 7 == 0) {
            new Level1Screen.ScriptItem(getRandomAsteroidType(), LINEAR_Y,
                    40f + random.nextFloat() * 260f,
                    1, random.nextInt() % 8 == 0, false, 0, 0f,
                    random.nextFloat() * (SCREEN_WIDTH - 36f),
                    SCREEN_HEIGHT).execute();
        }
        if (second % 10 == 0) {
            entityFactory.createForeground(getRandomMist(), 350f);
        }

        if (second < 0) {
            return;
        }
        if (second == 1) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT, 10, 100);
        }
        if (second == 5) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT, 10, 100);
        }
        if (second == 9) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT, 10, 100);
        }
        if (second == 13) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT, 10, 100);
        }
        if (second == 17) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT, 10, 100);
        }

        // 13 elements
        if (second >= 20 && second <= 90 && (second % 5 == 0 || second % 7 == 0)) {
            scriptItemsEasy.poll().execute();
            if (second == 55) {
                soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT, 10, 100);
            }

            // 18 elements
        } else if (second > 90 && second <= 160 && (second % 5 == 0 || second % 7 == 0)) {
            if (second == 95) {
                assets.playSound(SOUND_GO);
            }
            boolean left = random.nextBoolean();
            LinkedList<ScriptItem> medium = left ? scriptItemsMediumLeft : scriptItemsMediumRight;
            LinkedList<ScriptItem> mediumOther = left ? scriptItemsMediumRight : scriptItemsMediumLeft;

            medium.poll().execute();
            if (second % 10 == 0) {
                mediumOther.poll().execute();
            }
            if (second % 30 == 0) {
                soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT, 10, 100);
            }

        } else if (second > 160 && second <= 250 && (second % 5 == 0 || second % 8 == 0)) {
            if (second == 165) {
                assets.playSound(SOUND_GO);
            }
            if (second == 190) {
                soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT_WHEN_TWICE, 10, 200, false);
            }
            if (second == 220) {
                soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT_WHEN_TWICE, 10, 200, false);
                soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_VELOCITY, STATIC_ENEMY_BULLET_VELOCITY, STATIC_ENEMY_RATE_SHOOT_WHEN_TWICE, 10, 200, true);
            }

            boolean left = random.nextBoolean();
            LinkedList<ScriptItem> hard = left ? scriptItemsHardLeft : scriptItemsHardRight;
            LinkedList<ScriptItem> hardOther = left ? scriptItemsHardRight : scriptItemsHardLeft;

            hard.poll().execute();
            if (second % 8 == 0) {
                hardOther.poll().execute();
            }
        } else if (second >= 255 && player.getComponent(GameOverComponent.class) == null) {
            switch (second) {
                case 255:
                    assets.playSound(SOUND_BOSS_ALERT);
                    for (Entity background : backgrounds) {
                        Tween.to(Mappers.velocity.get(background), VELOCITY_Y, 4).ease(Quad.IN)
                                .target(-Mappers.velocity.get(background).y / 10f).start(tweenManager);
                    }
                    break;
                case 259:
                    assets.stopMusic(MUSIC_LEVEL_2);
                    playMusic(MUSIC_LEVEL_2_BOSS);
                    boss.execute();
                    break;
            }
        }
    }

    @Override
    protected Texture getRandomMist() {
        int randomMist = random.nextInt(8);
        return getMist(randomMist);
    }

    @Override
    protected Texture getMist(int mistType) {
        Texture texture = super.getMist(mistType);
        if (texture == null) {
            texture = assets.get(GFX_BGD_CLOUDS);
        }
        return texture;
    }

    @Override
    public int getRandomShipType() {
        return 1 + random.nextInt(5);
    }

    @Override
    protected int getRandomMoveType() {
        return random.nextInt(9);
    }

}

