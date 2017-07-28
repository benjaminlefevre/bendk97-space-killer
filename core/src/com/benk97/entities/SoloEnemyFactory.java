package com.benk97.entities;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.benk97.components.Mappers;
import com.benk97.components.PositionComponent;
import com.benk97.tweens.PositionComponentAccessor;

import java.util.Random;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;
import static com.benk97.tweens.PositionComponentAccessor.POSITION_X;
import static com.benk97.tweens.PositionComponentAccessor.POSITION_XY;

public class SoloEnemyFactory {

    public final static int FOLLOW_PLAYER = 0;
    public final static int LINEAR_X = 1;
    public final static int TRAPEZE = 2;
    public final static int BEZIER = 3;

    private TweenManager tweenManager;
    private EntityFactory entityFactory;
    private Random random = new RandomXS128();

    public SoloEnemyFactory(TweenManager tweenManager, EntityFactory entityFactory) {
        this.tweenManager = tweenManager;
        this.entityFactory = entityFactory;
    }

    public Entity createSoloEnemy(float velocity, float bulletVelocity,
                                  int rateShoot, int gaugelife, int points) {
        return createSoloEnemy(velocity, bulletVelocity, rateShoot, gaugelife, points, random.nextBoolean());
    }
    public Entity createSoloEnemy(float velocity, float bulletVelocity,
                                  int rateShoot, int gaugelife, int points, boolean comingFromLeft) {
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

    private Entity createSoloEnemyBezier(final float velocity, float bulletVelocity, int rateShoot, int gaugelife, int points, boolean comingFromLeft) {
        final boolean direction = comingFromLeft;
        final int directionFactor = direction ? 1 : -1;
        Entity enemy = entityFactory.createStaticEnemy(getRandomStaticEnemy(), null, bulletVelocity, rateShoot, gaugelife, points, direction);
        final PositionComponent position = Mappers.position.get(enemy);
        Sprite sprite = Mappers.sprite.get(enemy).sprite;
        final int k = 100;
        Bezier<Vector2> bezier = new Bezier<Vector2>(new Vector2(0f, SCREEN_HEIGHT - sprite.getHeight()), new Vector2(0f, SCREEN_HEIGHT / 2f),
                new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT / 2f), new Vector2(SCREEN_WIDTH - sprite.getWidth(), SCREEN_HEIGHT - sprite.getHeight()));
        final Vector2[] vPoints = new Vector2[k];
        for (int i = 0; i < k; ++i) {
            vPoints[i] = new Vector2();
            bezier.valueAt(vPoints[i], ((float) i) / ((float) k - 1));
        }
        Tween.to(position, POSITION_X, sprite.getWidth() / velocity)
                .ease(Linear.INOUT).targetRelative(directionFactor * sprite.getWidth())
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        Timeline timeline = Timeline.createSequence();
                        timeline.push(Tween.to(position, PositionComponentAccessor.POSITION_XY, vPoints[direction ? 0 : k - 1].dst(position.x, position.y) / velocity)
                                .ease(Linear.INOUT)
                                .target(vPoints[direction ? 0 : k - 1].x, vPoints[direction ? 0 : k - 1].y));
                        for (int j = 1; j < vPoints.length; ++j) {
                            int jx = direction ? j : k - j;
                            timeline.push(Tween.to(position, PositionComponentAccessor.POSITION_XY, vPoints[jx].dst(vPoints[jx - 1].x, vPoints[jx - 1].y) / velocity)
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
        Entity enemy = entityFactory.createStaticEnemy(getRandomStaticEnemy(), null, bulletVelocity, rateShoot, gaugelife, points, direction);
        final PositionComponent position = Mappers.position.get(enemy);
        final Sprite sprite = Mappers.sprite.get(enemy).sprite;
        Tween.to(position, POSITION_X, sprite.getWidth() / velocity)
                .ease(Linear.INOUT).targetRelative(directionFactor * sprite.getWidth())
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        if (i == COMPLETE) {
                            float distance = (new Vector2(0f, SCREEN_HEIGHT - sprite.getHeight())).dst(new Vector2(75f, SCREEN_HEIGHT - sprite.getHeight() - 75f));
                            Timeline.createSequence()
                                    .push(Tween.to(position, POSITION_XY, distance / velocity)
                                            .ease(Linear.INOUT).targetRelative(75f * directionFactor, -75f))
                                    .push(Tween.to(position, POSITION_X, (SCREEN_WIDTH - 2 * 75f - sprite.getWidth()) / velocity)
                                            .delay(2.0f).ease(Linear.INOUT).targetRelative(directionFactor * (SCREEN_WIDTH - 2 * 75f - sprite.getWidth())))
                                    .push(Tween.to(position, POSITION_XY, distance / velocity)
                                            .delay(2.0f).ease(Linear.INOUT).targetRelative(75f * directionFactor, 75f))
                                    .push(Tween.to(position, POSITION_X, (SCREEN_WIDTH - sprite.getWidth()) / velocity)
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
        Entity enemy = entityFactory.createStaticEnemy(getRandomStaticEnemy(), null, bulletVelocity, rateShoot, gaugelife, points, direction);
        final PositionComponent position = Mappers.position.get(enemy);
        final Sprite sprite = Mappers.sprite.get(enemy).sprite;
        Tween.to(position, POSITION_X, SCREEN_WIDTH / velocity)
                .ease(Linear.INOUT).targetRelative(directionFactor * SCREEN_WIDTH)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        if (i == COMPLETE) {
                            Tween.to(position, POSITION_X, (SCREEN_WIDTH - sprite.getWidth()) / velocity)
                                    .ease(Linear.INOUT).targetRelative(-directionFactor * (SCREEN_WIDTH - sprite.getWidth()))
                                    .repeatYoyo(Tween.INFINITY, 0f).start(tweenManager);
                        }
                    }
                }).start(tweenManager);
        return enemy;
    }

    private Entity createSoloEnemyFollowingPlayerOnAxisX(float velocity, float bulletVelocity, int rateShoot, int gaugelife, int points, boolean comingFromLeft) {
        return entityFactory.createStaticEnemy(getRandomStaticEnemy(), velocity, bulletVelocity, rateShoot, gaugelife, points, comingFromLeft);
    }

    public int getRandomStaticEnemy() {
        return random.nextInt(4) + 1;
    }


}
