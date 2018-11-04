/*
 * Developed by Benjamin Lefèvre
 * Last modified 02/11/18 18:17
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels.scripting;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Disposable;
import com.bendk97.assets.GameAssets;
import com.bendk97.components.helpers.Families;
import com.bendk97.components.texts.BossAlertComponent;
import com.bendk97.entities.EntityFactory;
import com.bendk97.screens.levels.Level;
import com.bendk97.screens.levels.Level.MusicTrack;
import com.bendk97.screens.levels.LevelScreen;
import com.bendk97.screens.levels.utils.ScriptItem;
import com.bendk97.screens.levels.utils.ScriptItemBuilder;
import com.bendk97.screens.levels.utils.ScriptItemExecutor;
import com.bendk97.tweens.CameraTweenAccessor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.graphics.Color.RED;
import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.GameAssets.*;
import static com.bendk97.entities.enemies.SquadronFactory.*;
import static com.bendk97.pools.GamePools.poolVector2;
import static com.bendk97.screens.levels.Level.MusicTrack.BOSS;
import static com.bendk97.screens.levels.Level.MusicTrack.LEVEL;
import static com.bendk97.screens.levels.Level.SoundEffect.BOSS_ALERT;
import static com.bendk97.tweens.BitmapFontCacheTweenAccessor.ALPHA;
import static com.bendk97.tweens.TextComponentTweenAccessor.POSY;

public abstract class LevelScript implements Disposable {

    protected final GameAssets assets;
    protected final EntityFactory entityFactory;
    protected final TweenManager tweenManager;
    protected final Entity player;
    protected final Random random = new RandomXS128();
    protected ScriptItemExecutor scriptItemExecutor;
    protected ScriptItem boss;
    protected final Level level;
    protected final LevelScreen levelScreen;


    /*
     for test purposes only
      */
    protected LevelScript(final LevelScreen levelScreen, Level level, GameAssets assets, EntityFactory entityFactory, TweenManager tweenManager,
                          Entity player, ScriptItemExecutor scriptItemExecutor) {
        this(levelScreen, level, assets, entityFactory, tweenManager, player);
        this.scriptItemExecutor = scriptItemExecutor;
    }

    protected LevelScript(final LevelScreen levelScreen, Level level, GameAssets assets, EntityFactory entityFactory, TweenManager tweenManager,
                          Entity player) {
        this.level = level;
        this.levelScreen = levelScreen;
        this.assets = assets;
        this.entityFactory = entityFactory;
        this.tweenManager = tweenManager;
        this.player = player;
        this.initSpawns();
        this.playMusic(LEVEL, level.volume);
        this.scriptItemExecutor = new ScriptItemExecutor(entityFactory.enemyEntityFactory.squadronFactory, player);
    }


    protected abstract void initSpawns();

    protected void playMusic(MusicTrack track) {
        playMusic(track, 1f);
    }

    protected void playMusic(MusicTrack track, float volume) {
        levelScreen.currentMusic(assets.playMusic(level.musics.get(track), volume));
    }

    protected void stopMusic(MusicTrack track) {
        assets.stopMusic(level.musics.get(track));
    }

    protected void playSound(Level.SoundEffect sound) {
        assets.playSound(level.sounds.get(sound));
    }

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
        List<ScriptItem> list = new ArrayList<>(nbSpawns);
        for (int i = 0; i < nbSpawns; ++i) {
            int randomMoveType = getRandomMoveType();
            int number = randomMoveType == ARROW_DOWN || randomMoveType == ARROW_UP ? 7 : minEnemies + random.nextInt(maxEnemies - minEnemies + 1);
            list.add(
                    new ScriptItemBuilder()
                            .typeShip(getRandomShipType())
                            .typeSquadron(randomMoveType)
                            .velocity(velocity)
                            .number(number)
                            .powerUp(false)
                            .displayBonus(true)
                            .withBonus(number * bonus)
                            .rateShoot(rateShoot)
                            .bulletVelocity(bulletVelocity)
                            .withParams(getRandomMoveParams(randomMoveType, comingFromLeft == null ? random.nextBoolean() : comingFromLeft))
                            .createScriptItem());
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
                return new Object[]{-OFFSET_WIDTH, SCREEN_HEIGHT, comingFromLeft};
            case BEZIER_SPLINE:
                if (random.nextBoolean()) {
                    return new Object[]{
                            poolVector2.getVector2(-OFFSET_WIDTH + leftOrRight * (SCREEN_WIDTH + OFFSET_WIDTH), SCREEN_HEIGHT),
                            poolVector2.getVector2(SCREEN_WIDTH - leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                            poolVector2.getVector2(SCREEN_WIDTH - leftOrRight * SCREEN_WIDTH, 0f),
                            poolVector2.getVector2(-OFFSET_WIDTH + leftOrRight * (SCREEN_WIDTH + OFFSET_WIDTH), 0f)};
                } else {
                    return new Object[]{
                            poolVector2.getVector2((-11 - OFFSET_WIDTH) * SHIP_WIDTH + leftOrRight * (SCREEN_WIDTH + OFFSET_WIDTH + (11 + OFFSET_WIDTH) * SHIP_WIDTH),
                                    SCREEN_HEIGHT / 2f),
                            poolVector2.getVector2(-OFFSET_WIDTH + leftOrRight * (SCREEN_WIDTH + OFFSET_WIDTH), SCREEN_HEIGHT),
                            poolVector2.getVector2(SCREEN_WIDTH + OFFSET_WIDTH - leftOrRight * (SCREEN_WIDTH + OFFSET_WIDTH * 2 + 2 * SHIP_WIDTH),
                                    SCREEN_HEIGHT),
                            poolVector2.getVector2(SCREEN_WIDTH + OFFSET_WIDTH - leftOrRight * (SCREEN_WIDTH + OFFSET_WIDTH * 2 + 6 * SHIP_WIDTH),
                                    SCREEN_HEIGHT / 2f)};
                }
            case CATMULL_ROM_SPLINE:
            default:
                return new Object[]{
                        poolVector2.getVector2(0f + leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                        poolVector2.getVector2(SCREEN_WIDTH * (0.8f - leftOrRight * 0.6f), 3 * SCREEN_HEIGHT / 4f),
                        poolVector2.getVector2(SCREEN_WIDTH * (0.2f + leftOrRight * 0.6f), 2 * SCREEN_HEIGHT / 4f),
                        poolVector2.getVector2(SCREEN_WIDTH * (0.8f - leftOrRight * 0.6f), SCREEN_HEIGHT / 4f),
                        poolVector2.getVector2(SCREEN_WIDTH * (0.2f + leftOrRight * 0.6f), -SCREEN_HEIGHT / 4f),
                        poolVector2.getVector2(SCREEN_WIDTH * (0.8f - leftOrRight * 0.6f), -2 * SCREEN_HEIGHT / 4f)
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

    protected void bossIsComing() {
        playSound(BOSS_ALERT);
        BossAlertComponent bossAlertComponent = entityFactory.engine.createComponent(BossAlertComponent.class);
        bossAlertComponent.font = assets.getFont(FONT_SPACE_KILLER_LARGE);
        bossAlertComponent.font.setColor(RED);
        player.add(bossAlertComponent);
        Tween.to(bossAlertComponent.font, ALPHA, 0.5f)
                .ease(Quad.OUT).target(0.2f)
                .setCallback((i, baseTween) -> {
                    if (i == TweenCallback.COMPLETE) {
                        player.remove(BossAlertComponent.class);
                        assets.get(FONT_SPACE_KILLER_LARGE).getColor().a = 1f;
                    }
                }).repeatYoyo(8, 0f).start(entityFactory.tweenManager);
        Tween.to(bossAlertComponent, POSY, 4f)
                .ease(Bounce.OUT).target(bossAlertComponent.targetPosY)
                .start(entityFactory.tweenManager);
    }

    protected void bossIsHere() {
        stopMusic(LEVEL);
        playMusic(BOSS);
        scriptItemExecutor.execute(boss);
        Tween.to(levelScreen.getCamera(), CameraTweenAccessor.ZOOM, 2f).ease(Linear.INOUT).target(0.5f).repeatYoyo(1, 0.5f)
                .start(entityFactory.tweenManager);
        Entity theBoss = entityFactory.engine.getEntitiesFor(Families.boss).get(0);
        levelScreen.makeEntityInvulnerable(theBoss);
    }

    @Override
    public abstract void dispose();
}
