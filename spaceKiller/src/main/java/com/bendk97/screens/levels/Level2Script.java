/*
 * Developed by Benjamin Lefèvre
 * Last modified 07/10/18 22:24
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.bendk97.assets.Assets;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.Mappers;
import com.bendk97.entities.EntityFactory;
import com.bendk97.entities.SoloEnemyFactory;
import com.bendk97.entities.SquadronFactory;
import com.bendk97.screens.levels.utils.ScriptItem;
import com.bendk97.screens.levels.utils.ScriptItemBuilder;
import com.bendk97.screens.levels.utils.ScriptItemExecutor;
import com.bendk97.systems.FollowPlayerSystem;
import com.bendk97.tweens.VelocityComponentAccessor;

import java.util.LinkedList;
import java.util.List;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.screens.levels.Level.Level2;
import static com.bendk97.screens.levels.Level.MusicTrack.BOSS;
import static com.bendk97.screens.levels.Level.MusicTrack.LEVEL;
import static com.bendk97.screens.levels.Level.SoundEffect.BOSS_ALERT;
import static com.bendk97.screens.levels.Level.SoundEffect.GO;

public class Level2Script extends LevelScript {

    private final SoloEnemyFactory soloEnemyFactory;

    private LinkedList<ScriptItem> scriptItemsEasy;
    private LinkedList<ScriptItem> scriptItemsMediumLeft;
    private LinkedList<ScriptItem> scriptItemsMediumRight;
    private LinkedList<ScriptItem> scriptItemsHardLeft;
    private LinkedList<ScriptItem> scriptItemsHardRight;
    private ScriptItem boss;


    private final Array<Entity> backgrounds = new Array<>();

    public Level2Script(final LevelScreen levelScreen, final Assets assets, EntityFactory entityFactory, TweenManager tweenManager, Entity player,
                        PooledEngine engine, OrthographicCamera camera) {
        super(levelScreen, Level2, assets, entityFactory, tweenManager, player, engine, camera);
        soloEnemyFactory = new SoloEnemyFactory(Level2, engine, tweenManager, entityFactory, player);
        initLevel2(assets, entityFactory, engine);
    }

    /*
      for test purposes only
    */
    protected Level2Script(final LevelScreen levelScreen, Assets assets, EntityFactory entityFactory, TweenManager tweenManager, Entity player,
                           SquadronFactory squadronFactory, SoloEnemyFactory soloEnemyFactory, ScriptItemExecutor scriptItemExecutor, PooledEngine engine) {
        super(levelScreen, Level2, assets, entityFactory, tweenManager, player, squadronFactory, scriptItemExecutor);
        this.soloEnemyFactory = soloEnemyFactory;
        initLevel2(assets, entityFactory, engine);
    }

    private void initLevel2(Assets assets, EntityFactory entityFactory, PooledEngine engine) {
        backgrounds.add(entityFactory.createBackground(assets.get(Assets.GFX_BGD_LEVEL2), 0, -500f));
        backgrounds.add(entityFactory.createBackground(assets.get(Assets.GFX_BGD_STARS_LEVEL2), 1, -300f));
        backgrounds.add(entityFactory.createBackground(assets.get(Assets.GFX_BGD_BIG_PLANET), 4, -250f));
        backgrounds.add(entityFactory.createBackground(assets.get(Assets.GFX_BGD_FAR_PLANETS), 2, -275f));
        backgrounds.add(entityFactory.createBackground(assets.get(Assets.GFX_BGD_RISING_PLANETS), 3, -325f));
        engine.addSystem(new FollowPlayerSystem(2));
    }


    @Override
    public void initSpawns() {
        scriptItemsEasy = new LinkedList<>(randomEasySpawnEnemies(30));
        scriptItemsMediumLeft = new LinkedList<>(randomMediumSpawnEnemiesComingFromLeft(30));
        scriptItemsMediumRight = new LinkedList<>(randomMediumSpawnEnemiesComingFromRight(30));
        scriptItemsHardLeft = new LinkedList<>(randomHardSpawnEnemiesComingFromLeft(30));
        scriptItemsHardRight = new LinkedList<>(randomHardSpawnEnemiesComingFromRight(30));
        boss = new ScriptItemBuilder().typeShip(EntityFactory.BOSS_LEVEL_2).typeSquadron(SquadronFactory.BOSS_LEVEL2_MOVE)
                .velocity(100f).number(1).powerUp(false).displayBonus(true).withBonus(15000)
                .bulletVelocity(ENEMY_BULLET_EASY_VELOCITY).createScriptItem();
    }

    @Override
    public void script(int second) {
        if (second < 0) {
            return;
        }
        super.script(second);
        scriptAsteroids(second);
        scriptMists(second);
        scriptFirstSoloEnemies(second);

        if(second < 20) {
            return;
        }
        if (second <= 90) {
            scriptEasyPart(second);
            return;
        }
        if (second <= 160) {
            scriptMediumPart(second);
            return;
        }
        if (second <= 250) {
            scriptDifficultPart(second);
            return;
        }
        if (second >= 255) {
            scriptBoss(second);
        }
    }

    private void scriptAsteroids(int second) {
        if (second % 2 == 0 || second % 9 == 0 || second % 7 == 0) {
            scriptItemExecutor.execute(
                    new ScriptItemBuilder().typeShip(getRandomAsteroidType()).typeSquadron(SquadronFactory.LINEAR_Y).velocity(40f + random.nextFloat() * 260f).number(1).powerUp(random.nextInt() % 4 == 0).displayBonus(false).withBonus(0).bulletVelocity(0f).withParams(random.nextFloat() * (SCREEN_WIDTH - 36f), SCREEN_HEIGHT).createScriptItem());
        }
    }

    private void scriptMists(int second) {
        if (second % 10 == 0) {
            entityFactory.createForeground(getRandomMist(), 350f);
        }
    }

    private void scriptFirstSoloEnemies(int second) {
        if (second == 1) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL2_VELOCITY, STATIC_ENEMY_LEVEL2_BULLET_VELOCITY, STATIC_ENEMY_LEVEL2_RATE_SHOOT, 10, 100);
        }
        if (second == 5) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL2_VELOCITY, STATIC_ENEMY_LEVEL2_BULLET_VELOCITY, STATIC_ENEMY_LEVEL2_RATE_SHOOT, 10, 100);
        }
        if (second == 9) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL2_VELOCITY, STATIC_ENEMY_LEVEL2_BULLET_VELOCITY, STATIC_ENEMY_LEVEL2_RATE_SHOOT, 10, 100);
        }
        if (second == 13) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL2_VELOCITY, STATIC_ENEMY_LEVEL2_BULLET_VELOCITY, STATIC_ENEMY_LEVEL2_RATE_SHOOT, 10, 100);
        }
        if (second == 17) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL2_VELOCITY, STATIC_ENEMY_LEVEL2_BULLET_VELOCITY, STATIC_ENEMY_LEVEL2_RATE_SHOOT, 10, 100);
        }
    }

    private void scriptEasyPart(int second) {
        if (second % 5 == 0 || second % 7 == 0) {
            executeScriptFromList(scriptItemsEasy);
        }
        if (second == 55) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL2_VELOCITY, STATIC_ENEMY_LEVEL2_BULLET_VELOCITY, STATIC_ENEMY_LEVEL2_RATE_SHOOT, 10, 100);
        }
    }

    private void scriptMediumPart(int second) {
        if (second == 95) {
            playSound(GO);
        }
        boolean left = random.nextBoolean();
        if (second % 5 == 0 || second % 7 == 0) {
            executeScriptFromList(left ? scriptItemsMediumLeft : scriptItemsMediumRight);
        }
        if (second % 10 == 0) {
            executeScriptFromList(left ? scriptItemsMediumRight : scriptItemsMediumLeft);
        }
        if (second % 30 == 0) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL2_VELOCITY, STATIC_ENEMY_LEVEL2_BULLET_VELOCITY, STATIC_ENEMY_LEVEL2_RATE_SHOOT, 10, 100);
        }
    }

    private void scriptDifficultPart(int second) {
        if (second == 165) {
            playSound(GO);
        }
        if (second == 190) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL2_VELOCITY, STATIC_ENEMY_LEVEL2_BULLET_VELOCITY, STATIC_ENEMY_LEVEL2_RATE_SHOOT_WHEN_TWICE, 10, 200, false);
        }
        if (second == 220) {
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL2_VELOCITY, STATIC_ENEMY_LEVEL2_BULLET_VELOCITY, STATIC_ENEMY_LEVEL2_RATE_SHOOT_WHEN_TWICE, 10, 200, false);
            soloEnemyFactory.createSoloEnemy(STATIC_ENEMY_LEVEL2_VELOCITY, STATIC_ENEMY_LEVEL2_BULLET_VELOCITY, STATIC_ENEMY_LEVEL2_RATE_SHOOT_WHEN_TWICE, 10, 200, true);
        }
        boolean left = random.nextBoolean();
        if (second % 5 == 0 || second % 8 == 0) {
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
        if (second == 255) {
            playSound(BOSS_ALERT);
            for (Entity background : new Array.ArrayIterator<>(backgrounds)) {
                Tween.to(Mappers.velocity.get(background), VelocityComponentAccessor.VELOCITY_Y, 4).ease(Quad.IN)
                        .target(-Mappers.velocity.get(background).y / 10f).start(tweenManager);
            }
            return;
        }
        if (second == 259) {
            stopMusic(LEVEL);
            playMusic(BOSS);
            scriptItemExecutor.execute(boss);
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
            texture = assets.get(Assets.GFX_BGD_CLOUDS);
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

    private List<ScriptItem> randomEasySpawnEnemies(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL2_VELOCITY_EASY, STANDARD_RATE_SHOOT, ENEMY_LEVEL2_BULLET_EASY_VELOCITY, BONUS_LEVEL2_SQUADRON_EASY, 5, 6, null);

    }

    private List<ScriptItem> randomMediumSpawnEnemiesComingFromLeft(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL2_VELOCITY_MEDIUM, STANDARD_RATE_SHOOT, ENEMY_LEVEL2_BULLET_MEDIUM_VELOCITY, BONUS_LEVEL2_SQUADRON_MEDIUM, 5, 8, true);

    }

    private List<ScriptItem> randomMediumSpawnEnemiesComingFromRight(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL2_VELOCITY_MEDIUM, STANDARD_RATE_SHOOT, ENEMY_LEVEL2_BULLET_MEDIUM_VELOCITY, BONUS_LEVEL2_SQUADRON_MEDIUM, 5, 10, false);

    }

    private List<ScriptItem> randomHardSpawnEnemiesComingFromLeft(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL2_VELOCITY_HARD, STANDARD_RATE_SHOOT, ENEMY_LEVEL2_BULLET_HARD_VELOCITY, BONUS_LEVEL2_SQUADRON_HARD, 6, 12, true);

    }

    private List<ScriptItem> randomHardSpawnEnemiesComingFromRight(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_LEVEL2_VELOCITY_HARD, STANDARD_RATE_SHOOT, ENEMY_LEVEL2_BULLET_HARD_VELOCITY, BONUS_LEVEL2_SQUADRON_HARD, 6, 12, false);

    }

}

