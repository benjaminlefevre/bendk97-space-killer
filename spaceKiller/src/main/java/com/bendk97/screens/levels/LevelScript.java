/*
 * Developed by Benjamin Lefèvre
 * Last modified 10/10/18 08:16
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.bendk97.assets.Assets;
import com.bendk97.entities.EntityFactory;
import com.bendk97.entities.SquadronFactory;
import com.bendk97.screens.levels.utils.ScriptItem;
import com.bendk97.screens.levels.utils.ScriptItemBuilder;
import com.bendk97.screens.levels.utils.ScriptItemExecutor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.Assets.*;
import static com.bendk97.entities.SquadronFactory.*;

public abstract class LevelScript {

    protected final Assets assets;
    protected final EntityFactory entityFactory;
    protected final TweenManager tweenManager;
    protected final Entity player;
    protected final Random random = new RandomXS128();
    protected final SquadronFactory squadronFactory;
    protected final ScriptItemExecutor scriptItemExecutor;

    /*
     for test purposes only
      */
    protected LevelScript(Assets assets, EntityFactory entityFactory, TweenManager tweenManager, Entity player,
                          SquadronFactory squadronFactory, ScriptItemExecutor scriptItemExecutor) {
        this.assets = assets;
        this.entityFactory = entityFactory;
        this.tweenManager = tweenManager;
        this.player = player;
        this.squadronFactory = squadronFactory;
        this.scriptItemExecutor = scriptItemExecutor;
        this.initSpawns();
    }

    protected LevelScript(Assets assets, EntityFactory entityFactory, TweenManager tweenManager, Entity player,
                          PooledEngine engine, OrthographicCamera camera) {
        this.assets = assets;
        this.entityFactory = entityFactory;
        this.tweenManager = tweenManager;
        this.player = player;
        this.squadronFactory = new SquadronFactory(tweenManager, entityFactory, camera, engine);
        this.scriptItemExecutor = new ScriptItemExecutor(squadronFactory, player);
        this.initSpawns();
    }

    public abstract void initSpawns();

    public void script(int second) {
        if (second == 0) {
            assets.playSound(SOUND_READY);
        }
    }

    protected void executeScriptFromList(LinkedList<ScriptItem> scriptItems) {
        ScriptItem item = scriptItems.poll();
        if (item != null) {
            scriptItemExecutor.execute(item);
        }
    }

    protected Texture getRandomMist() {
        int randomMist = random.nextInt(7);
        return getMist(randomMist);
    }

    protected Texture getMist(int mistType) {
        switch (mistType) {
            case 0:
                return assets.get(GFX_BGD_MIST7);
            case 1:
                return assets.get(GFX_BGD_MIST1);
            case 2:
                return assets.get(GFX_BGD_MIST2);
            case 3:
                return assets.get(GFX_BGD_MIST3);
            case 4:
                return assets.get(GFX_BGD_MIST4);
            case 5:
                return assets.get(GFX_BGD_MIST5);
            case 6:
            default:
                return assets.get(GFX_BGD_MIST6);
        }
    }

    protected List<ScriptItem> randomSpawnEnemies(int nbSpawns, float velocity, int rateShoot, float bulletVelocity, int bonus, int minEnemies, int maxEnemies, Boolean comingFromLeft) {
        List<ScriptItem> list = new ArrayList<ScriptItem>(nbSpawns);
        for (int i = 0; i < nbSpawns; ++i) {
            int randomMoveType = getRandomMoveType();
            int number = randomMoveType == ARROW_DOWN || randomMoveType == ARROW_UP ? 7 : minEnemies + random.nextInt(maxEnemies - minEnemies + 1);
            list.add(
                    new ScriptItemBuilder().typeShip(getRandomShipType()).typeSquadron(randomMoveType).velocity(velocity).number(number).powerUp(false).displayBonus(true).withBonus(number * bonus).rateShoot(rateShoot).bulletVelocity(bulletVelocity).withParams(getRandomMoveParams(randomMoveType, comingFromLeft == null ? random.nextBoolean() : comingFromLeft)).createScriptItem());
        }
        return list;
    }

    private Object[] getRandomMoveParams(int randomMoveType, boolean comingFromLeft) {
        float direction = comingFromLeft ? 1f : -1f;
        int leftOrRight = comingFromLeft ? 0 : 1;
        switch (randomMoveType) {
            case INFINITE_CIRCLE:
                return null;
            case ARROW_DOWN:
            case ARROW_UP:
                return null;
            case LINEAR_X:
                return new Object[]{-direction * SHIP_WIDTH + leftOrRight * SCREEN_WIDTH, 2f / 3f * SCREEN_HEIGHT + random.nextFloat() * SCREEN_HEIGHT / 12f, direction};
            case LINEAR_Y:
                return new Object[]{1f / 5f * SCREEN_WIDTH + random.nextFloat() * 3 * SCREEN_WIDTH / 5f, SCREEN_HEIGHT};
            case LINEAR_XY:
                return new Object[]{0f + leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_WIDTH - leftOrRight * SCREEN_WIDTH, 0f};
            case SEMI_CIRCLE:
                return new Object[]{0f, SCREEN_HEIGHT};
            case BEZIER_SPLINE:
                if (random.nextBoolean()) {
                    return new Object[]{
                            new Vector2(0f + leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                            new Vector2(SCREEN_WIDTH - leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                            new Vector2(SCREEN_WIDTH - leftOrRight * SCREEN_WIDTH, 0f),
                            new Vector2(0f + leftOrRight * SCREEN_WIDTH, 0f)};
                } else {
                    return new Object[]{
                            new Vector2(-11 * SHIP_WIDTH + leftOrRight * (SCREEN_WIDTH + 11 * SHIP_WIDTH), SCREEN_HEIGHT / 2f),
                            new Vector2(0f + leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                            new Vector2(SCREEN_WIDTH - leftOrRight * (SCREEN_WIDTH + 2 * SHIP_WIDTH), SCREEN_HEIGHT),
                            new Vector2(SCREEN_WIDTH - leftOrRight * (SCREEN_WIDTH + 6 * SHIP_WIDTH), SCREEN_HEIGHT / 2f)};
                }
            case CATMULL_ROM_SPLINE:
            default:
                return new Object[]{
                        new Vector2(0f + leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                        new Vector2(SCREEN_WIDTH * (0.8f - leftOrRight * 0.6f), 3 * SCREEN_HEIGHT / 4f),
                        new Vector2(SCREEN_WIDTH * (0.2f + leftOrRight * 0.6f), 2 * SCREEN_HEIGHT / 4f),
                        new Vector2(SCREEN_WIDTH * (0.8f - leftOrRight * 0.6f), SCREEN_HEIGHT / 4f),
                        new Vector2(SCREEN_WIDTH * (0.2f + leftOrRight * 0.6f), -SCREEN_HEIGHT / 4f),
                        new Vector2(SCREEN_WIDTH * (0.8f - leftOrRight * 0.6f), -2 * SCREEN_HEIGHT / 4f)
                };
        }
    }

    protected int getRandomShipType() {
        return random.nextInt(6);
    }

    protected int getRandomMoveType() {
        return random.nextInt(8);
    }


    protected int getRandomAsteroidType() {
        return 999 + random.nextInt(2);
    }

    protected int getRandomHouseType() {
        return 1500 + random.nextInt(9);
    }


}
