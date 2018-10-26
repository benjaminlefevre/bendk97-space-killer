/*
 * Developed by Benjamin Lefèvre
 * Last modified 11/10/18 23:02
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.enemies;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.TankComponent.TankLevel;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.screens.levels.Level;
import com.bendk97.tweens.PositionComponentTweenAccessor;

import java.util.List;
import java.util.Random;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;
import static com.bendk97.entities.EntityFactoryIds.*;
import static com.bendk97.screens.levels.Level.Level2;
import static com.bendk97.screens.levels.Level.Level3;

public class SoloEnemyFactory {

    private final static int FOLLOW_PLAYER = 0;
    private final static int LINEAR_X = 1;
    private final static int TRAPEZE = 2;
    private final static int BEZIER = 3;

    private final Random random = new RandomXS128();
    private final EntityFactory entityFactory;
    private Entity player;
    private final Level level;

    protected SoloEnemyFactory(EntityFactory entityFactory, Level level) {
        this.entityFactory = entityFactory;
        this.level = level;
    }

    public void createSoloEnemy(float velocity, float bulletVelocity,
                                int rateShoot, int gaugeLife, int points) {
        createSoloEnemy(velocity, bulletVelocity, rateShoot, gaugeLife, points, random.nextBoolean());
    }

    public void createSoloEnemy(float velocity, float bulletVelocity,
                                int rateShoot, int gaugeLife, int points, boolean comingFromLeft) {
        if (player != null) {
            ComponentMapperHelper.player.get(player).enemiesCountLevel++;
        }
        int soloType = random.nextInt(4);
        switch (soloType) {
            case FOLLOW_PLAYER:
                createSoloEnemyFollowingPlayerOnAxisX(velocity, bulletVelocity, rateShoot, gaugeLife, points, comingFromLeft);
                return;
            case LINEAR_X:
                createSoloEnemyLinearX(velocity, bulletVelocity, rateShoot, gaugeLife, points, comingFromLeft);
                return;
            case TRAPEZE:
                createSoloEnemyTrapeze(velocity, bulletVelocity, rateShoot, gaugeLife, points, comingFromLeft);
                return;
            case BEZIER:
                createSoloEnemyBezier(velocity, bulletVelocity, rateShoot, gaugeLife, points, comingFromLeft);
                return;
            default:
        }
    }

    public void createTank(float velocity, TankLevel level, int gaugeLife, int points) {
        List<Entity> entities = entityFactory.enemyEntityFactory.createTank(level, gaugeLife, points);
        ComponentMapperHelper.player.get(player).enemiesCountLevel++;
        float posX = random.nextFloat() * (SCREEN_WIDTH - 64f);
        for (final Entity entity : entities) {
            PositionComponent position = ComponentMapperHelper.position.get(entity);
            position.setPosition(posX, SCREEN_HEIGHT + 20f);
            Tween.to(position, PositionComponentTweenAccessor.POSITION_Y, (SCREEN_HEIGHT + 100f) / velocity)
                    .ease(Linear.INOUT).targetRelative(-SCREEN_HEIGHT - 100f)
                    .setCallback((i, baseTween) -> {
                        if (i == TweenCallback.COMPLETE) {
                            entityFactory.engine.removeEntity(entity);
                        }
                    }).start(entityFactory.tweenManager);
        }
    }

    private void createSoloEnemyBezier(final float velocity, float bulletVelocity, int rateShoot, int gaugeLife, int points, boolean comingFromLeft) {
        final boolean direction = comingFromLeft;
        final int directionFactor = direction ? 1 : -1;
        Entity enemy = entityFactory.enemyEntityFactory.createLaserShip(getRandomStaticEnemy(), null, bulletVelocity, rateShoot, gaugeLife, points, direction);
        final PositionComponent position = ComponentMapperHelper.position.get(enemy);
        Sprite sprite = ComponentMapperHelper.sprite.get(enemy).sprite;
        final int k = 100;
        Bezier<Vector2> bezier = new Bezier<>(new Vector2(0f, SCREEN_HEIGHT - sprite.getHeight()), new Vector2(0f, SCREEN_HEIGHT / 2f),
                new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT / 2f), new Vector2(SCREEN_WIDTH - sprite.getWidth(), SCREEN_HEIGHT - sprite.getHeight()));
        final Vector2[] vPoints = new Vector2[k];
        for (int i = 0; i < k; ++i) {
            vPoints[i] = new Vector2();
            bezier.valueAt(vPoints[i], ((float) i) / ((float) k - 1));
        }
        Tween.to(position, PositionComponentTweenAccessor.POSITION_X, sprite.getWidth() / velocity)
                .ease(Linear.INOUT).targetRelative(directionFactor * sprite.getWidth())
                .setCallback((i, baseTween) -> {
                    Timeline timeline = Timeline.createSequence();
                    timeline.push(Tween.to(position, PositionComponentTweenAccessor.POSITION_XY, vPoints[direction ? 0 : k - 1].dst(position.x, position.y) / velocity)
                            .ease(Linear.INOUT)
                            .target(vPoints[direction ? 0 : k - 1].x, vPoints[direction ? 0 : k - 1].y));
                    for (int j = 1; j < vPoints.length; ++j) {
                        int jx = direction ? j : k - j;
                        timeline.push(Tween.to(position, PositionComponentTweenAccessor.POSITION_XY, vPoints[jx].dst(vPoints[jx - 1].x, vPoints[jx - 1].y) / velocity)
                                .ease(Linear.INOUT)
                                .target(vPoints[jx].x, vPoints[jx].y));
                    }
                    timeline.repeatYoyo(Tween.INFINITY, 1f)
                            .start(entityFactory.tweenManager);

                }).start(entityFactory.tweenManager);
    }

    private void createSoloEnemyTrapeze(final float velocity, float bulletVelocity, int rateShoot, int gaugeLife, int points, boolean comingFromLeft) {
        final int directionFactor = comingFromLeft ? 1 : -1;
        Entity enemy = entityFactory.enemyEntityFactory.createLaserShip(getRandomStaticEnemy(), null, bulletVelocity, rateShoot, gaugeLife, points, comingFromLeft);
        final PositionComponent position = ComponentMapperHelper.position.get(enemy);
        final Sprite sprite = ComponentMapperHelper.sprite.get(enemy).sprite;
        Tween.to(position, PositionComponentTweenAccessor.POSITION_X, sprite.getWidth() / velocity)
                .ease(Linear.INOUT).targetRelative(directionFactor * sprite.getWidth())
                .setCallback((i, baseTween) -> {
                    if (i == TweenCallback.COMPLETE) {
                        float distance = (new Vector2(0f, SCREEN_HEIGHT - sprite.getHeight())).dst(new Vector2(75f, SCREEN_HEIGHT - sprite.getHeight() - 75f));
                        Timeline.createSequence()
                                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_XY, distance / velocity)
                                        .ease(Linear.INOUT).targetRelative(75f * directionFactor, -75f))
                                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (SCREEN_WIDTH - 2 * 75f - sprite.getWidth()) / velocity)
                                        .delay(2.0f).ease(Linear.INOUT).targetRelative(directionFactor * (SCREEN_WIDTH - 2 * 75f - sprite.getWidth())))
                                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_XY, distance / velocity)
                                        .delay(2.0f).ease(Linear.INOUT).targetRelative(75f * directionFactor, 75f))
                                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (SCREEN_WIDTH - sprite.getWidth()) / velocity)
                                        .ease(Linear.INOUT).targetRelative(-directionFactor * (SCREEN_WIDTH - sprite.getWidth())))
                                .repeat(Tween.INFINITY, 0f).start(entityFactory.tweenManager);
                    }
                }).start(entityFactory.tweenManager);
    }

    private void createSoloEnemyLinearX(final float velocity, float bulletVelocity, int rateShoot, int gaugeLife, int points, boolean comingFromLeft) {
        final int directionFactor = comingFromLeft ? 1 : -1;
        Entity enemy = entityFactory.enemyEntityFactory.createLaserShip(getRandomStaticEnemy(), null, bulletVelocity, rateShoot, gaugeLife, points, comingFromLeft);
        final PositionComponent position = ComponentMapperHelper.position.get(enemy);
        final Sprite sprite = ComponentMapperHelper.sprite.get(enemy).sprite;
        Tween.to(position, PositionComponentTweenAccessor.POSITION_X, SCREEN_WIDTH / velocity)
                .ease(Linear.INOUT).targetRelative(directionFactor * SCREEN_WIDTH)
                .setCallback((i, baseTween) -> {
                    if (i == TweenCallback.COMPLETE) {
                        Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (SCREEN_WIDTH - sprite.getWidth()) / velocity)
                                .ease(Linear.INOUT).targetRelative(-directionFactor * (SCREEN_WIDTH - sprite.getWidth()))
                                .repeatYoyo(Tween.INFINITY, 0f).start(entityFactory.tweenManager);
                    }
                }).start(entityFactory.tweenManager);
    }

    private void createSoloEnemyFollowingPlayerOnAxisX(float velocity, float bulletVelocity, int rateShoot, int gaugeLife, int points, boolean comingFromLeft) {
        entityFactory.enemyEntityFactory.createLaserShip(getRandomStaticEnemy(), velocity, bulletVelocity, rateShoot, gaugeLife, points, comingFromLeft);
    }

    private int getRandomStaticEnemy() {
        if (level.equals(Level2)) {
            return random.nextInt(NB_SHIP_LV2_LASER_SHIP) + SHIP_LV2_LASER_SHIP1;
        } else if (level.equals(Level3)) {
            return SHIP_LV3_1 + random.nextInt(NB_SHIP_LV3);
        }
        throw new IllegalArgumentException("Unexpected Exception");
    }


    public void setPlayer(Entity player) {
        this.player = player;
    }
}
