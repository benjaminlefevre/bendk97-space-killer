package com.bendk97.entities;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;

import java.util.List;
import java.util.Random;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class SoloEnemyFactory {

    public final static int FOLLOW_PLAYER = 0;
    public final static int LINEAR_X = 1;
    public final static int TRAPEZE = 2;
    public final static int BEZIER = 3;

    private TweenManager tweenManager;
    private EntityFactory entityFactory;
    private Random random = new RandomXS128();
    private Engine engine;
    private com.bendk97.screens.LevelScreen.Level level;
    private Entity player;

    public SoloEnemyFactory(com.bendk97.screens.LevelScreen.Level level, Engine engine, TweenManager tweenManager, EntityFactory entityFactory, Entity player) {
        this.tweenManager = tweenManager;
        this.engine = engine;
        this.level = level;
        this.entityFactory = entityFactory;
        this.player = player;
    }

    public Entity createSoloEnemy(float velocity, float bulletVelocity,
                                  int rateShoot, int gaugelife, int points) {
        return createSoloEnemy(velocity, bulletVelocity, rateShoot, gaugelife, points, random.nextBoolean());
    }

    public Entity createSoloEnemy(float velocity, float bulletVelocity,
                                  int rateShoot, int gaugelife, int points, boolean comingFromLeft) {
        com.bendk97.components.Mappers.player.get(player).enemiesCountLevel++;
        int soloType = random.nextInt(4);
        switch (soloType) {
            case FOLLOW_PLAYER:
                return createSoloEnemyFollowingPlayerOnAxisX(velocity, bulletVelocity, rateShoot, gaugelife, points, comingFromLeft);
            case LINEAR_X:
                return createSoloEnemyLinearX(velocity, bulletVelocity, rateShoot, gaugelife, points, comingFromLeft);
            case TRAPEZE:
                return createSoloEnemyTrapeze(velocity, bulletVelocity, rateShoot, gaugelife, points, comingFromLeft);
            case BEZIER:
                return createSoloEnemyBezier(velocity, bulletVelocity, rateShoot, gaugelife, points, comingFromLeft);
            default:
                return null;
        }
    }

    public void createTank(float velocity, com.bendk97.components.TankComponent.TankLevel level, int gaugeLife, int points) {
        List<Entity> entities = entityFactory.createTank(level, gaugeLife, points);
        com.bendk97.components.Mappers.player.get(player).enemiesCountLevel++;
        float posX = random.nextFloat() * (SCREEN_WIDTH - 64f);
        for (final Entity entity : entities) {
            com.bendk97.components.PositionComponent position = com.bendk97.components.Mappers.position.get(entity);
            position.setPosition(posX, SCREEN_HEIGHT + 20f);
            Tween.to(position, com.bendk97.tweens.PositionComponentAccessor.POSITION_Y, (SCREEN_HEIGHT + 100f) / velocity)
                    .ease(Linear.INOUT).targetRelative(-SCREEN_HEIGHT - 100f)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            if (i == COMPLETE) {
                                engine.removeEntity(entity);
                            }
                        }
                    }).start(tweenManager);
        }
    }

    private Entity createSoloEnemyBezier(final float velocity, float bulletVelocity, int rateShoot, int gaugelife, int points, boolean comingFromLeft) {
        final boolean direction = comingFromLeft;
        final int directionFactor = direction ? 1 : -1;
        Entity enemy = entityFactory.createLaserShip(getRandomStaticEnemy(), null, bulletVelocity, rateShoot, gaugelife, points, direction);
        final com.bendk97.components.PositionComponent position = com.bendk97.components.Mappers.position.get(enemy);
        Sprite sprite = com.bendk97.components.Mappers.sprite.get(enemy).sprite;
        final int k = 100;
        Bezier<Vector2> bezier = new Bezier<Vector2>(new Vector2(0f, SCREEN_HEIGHT - sprite.getHeight()), new Vector2(0f, SCREEN_HEIGHT / 2f),
                new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT / 2f), new Vector2(SCREEN_WIDTH - sprite.getWidth(), SCREEN_HEIGHT - sprite.getHeight()));
        final Vector2[] vPoints = new Vector2[k];
        for (int i = 0; i < k; ++i) {
            vPoints[i] = new Vector2();
            bezier.valueAt(vPoints[i], ((float) i) / ((float) k - 1));
        }
        Tween.to(position, com.bendk97.tweens.PositionComponentAccessor.POSITION_X, sprite.getWidth() / velocity)
                .ease(Linear.INOUT).targetRelative(directionFactor * sprite.getWidth())
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Timeline timeline = Timeline.createSequence();
                        timeline.push(Tween.to(position, com.bendk97.tweens.PositionComponentAccessor.POSITION_XY, vPoints[direction ? 0 : k - 1].dst(position.x, position.y) / velocity)
                                .ease(Linear.INOUT)
                                .target(vPoints[direction ? 0 : k - 1].x, vPoints[direction ? 0 : k - 1].y));
                        for (int j = 1; j < vPoints.length; ++j) {
                            int jx = direction ? j : k - j;
                            timeline.push(Tween.to(position, com.bendk97.tweens.PositionComponentAccessor.POSITION_XY, vPoints[jx].dst(vPoints[jx - 1].x, vPoints[jx - 1].y) / velocity)
                                    .ease(Linear.INOUT)
                                    .target(vPoints[jx].x, vPoints[jx].y));
                        }
                        timeline.repeatYoyo(Tween.INFINITY, 1f)
                                .start(tweenManager);

                    }
                }).start(tweenManager);
        return enemy;
    }

    private Entity createSoloEnemyTrapeze(final float velocity, float bulletVelocity, int rateShoot, int gaugelife, int points, boolean comingFromLeft) {
        final boolean direction = comingFromLeft;
        final int directionFactor = direction ? 1 : -1;
        Entity enemy = entityFactory.createLaserShip(getRandomStaticEnemy(), null, bulletVelocity, rateShoot, gaugelife, points, direction);
        final com.bendk97.components.PositionComponent position = com.bendk97.components.Mappers.position.get(enemy);
        final Sprite sprite = com.bendk97.components.Mappers.sprite.get(enemy).sprite;
        Tween.to(position, com.bendk97.tweens.PositionComponentAccessor.POSITION_X, sprite.getWidth() / velocity)
                .ease(Linear.INOUT).targetRelative(directionFactor * sprite.getWidth())
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        if (i == COMPLETE) {
                            float distance = (new Vector2(0f, SCREEN_HEIGHT - sprite.getHeight())).dst(new Vector2(75f, SCREEN_HEIGHT - sprite.getHeight() - 75f));
                            Timeline.createSequence()
                                    .push(Tween.to(position, com.bendk97.tweens.PositionComponentAccessor.POSITION_XY, distance / velocity)
                                            .ease(Linear.INOUT).targetRelative(75f * directionFactor, -75f))
                                    .push(Tween.to(position, com.bendk97.tweens.PositionComponentAccessor.POSITION_X, (SCREEN_WIDTH - 2 * 75f - sprite.getWidth()) / velocity)
                                            .delay(2.0f).ease(Linear.INOUT).targetRelative(directionFactor * (SCREEN_WIDTH - 2 * 75f - sprite.getWidth())))
                                    .push(Tween.to(position, com.bendk97.tweens.PositionComponentAccessor.POSITION_XY, distance / velocity)
                                            .delay(2.0f).ease(Linear.INOUT).targetRelative(75f * directionFactor, 75f))
                                    .push(Tween.to(position, com.bendk97.tweens.PositionComponentAccessor.POSITION_X, (SCREEN_WIDTH - sprite.getWidth()) / velocity)
                                            .ease(Linear.INOUT).targetRelative(-directionFactor * (SCREEN_WIDTH - sprite.getWidth())))
                                    .repeat(Tween.INFINITY, 0f).start(tweenManager);
                        }
                    }
                }).start(tweenManager);
        return enemy;
    }

    private Entity createSoloEnemyLinearX(final float velocity, float bulletVelocity, int rateShoot, int gaugelife, int points, boolean comingFromLeft) {
        final boolean direction = comingFromLeft;
        final int directionFactor = direction ? 1 : -1;
        Entity enemy = entityFactory.createLaserShip(getRandomStaticEnemy(), null, bulletVelocity, rateShoot, gaugelife, points, direction);
        final com.bendk97.components.PositionComponent position = com.bendk97.components.Mappers.position.get(enemy);
        final Sprite sprite = com.bendk97.components.Mappers.sprite.get(enemy).sprite;
        Tween.to(position, com.bendk97.tweens.PositionComponentAccessor.POSITION_X, SCREEN_WIDTH / velocity)
                .ease(Linear.INOUT).targetRelative(directionFactor * SCREEN_WIDTH)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        if (i == COMPLETE) {
                            Tween.to(position, com.bendk97.tweens.PositionComponentAccessor.POSITION_X, (SCREEN_WIDTH - sprite.getWidth()) / velocity)
                                    .ease(Linear.INOUT).targetRelative(-directionFactor * (SCREEN_WIDTH - sprite.getWidth()))
                                    .repeatYoyo(Tween.INFINITY, 0f).start(tweenManager);
                        }
                    }
                }).start(tweenManager);
        return enemy;
    }

    private Entity createSoloEnemyFollowingPlayerOnAxisX(float velocity, float bulletVelocity, int rateShoot, int gaugelife, int points, boolean comingFromLeft) {
        return entityFactory.createLaserShip(getRandomStaticEnemy(), velocity, bulletVelocity, rateShoot, gaugelife, points, comingFromLeft);
    }

    public int getRandomStaticEnemy() {
        if (level.equals(com.bendk97.screens.LevelScreen.Level.Level2)) {
            return random.nextInt(EntityFactory.NB_SHIP_LV2_LASER_SHIP) + EntityFactory.SHIP_LV2_LASER_SHIP1;
        } else if (level.equals(com.bendk97.screens.LevelScreen.Level.Level3)) {
            return EntityFactory.SHIP_LV3_1 + random.nextInt(EntityFactory.NB_SHIP_LV3);
        }
        throw new IllegalArgumentException("Unexpecte Exception");
    }


}
