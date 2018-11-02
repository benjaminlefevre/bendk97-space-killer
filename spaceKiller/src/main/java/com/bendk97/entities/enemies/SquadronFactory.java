/*
 * Developed by Benjamin Lefèvre
 * Last modified 11/10/18 23:29
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities.enemies;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bendk97.components.EnemyComponent;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.SpriteComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.entities.EntityFactory;
import com.bendk97.screens.levels.utils.ScriptItem;
import com.bendk97.tweens.PositionComponentTweenAccessor;

import java.util.Random;

import static aurelienribon.tweenengine.TweenCallback.COMPLETE;
import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.entities.EntityFactoryIds.*;
import static com.bendk97.pools.GamePools.poolVector2;

public class SquadronFactory {

    public final static int LINEAR_X = 0;
    public final static int LINEAR_Y = 1;
    public final static int LINEAR_Y_SAME_POS = 9;
    public final static int SEMI_CIRCLE = 2;
    public final static int BEZIER_SPLINE = 3;
    public final static int CATMULL_ROM_SPLINE = 4;
    public final static int LINEAR_XY = 5;
    public final static int ARROW_UP = 6;
    public final static int ARROW_DOWN = 7;
    public final static int INFINITE_CIRCLE = 8;
    public final static int BOSS_MOVE = 100;
    public final static int BOSS_LEVEL2_MOVE = 101;
    public final static int BOSS_LEVEL3_MOVE = 102;


    private final EntityFactory entityFactory;
    private final OrthographicCamera camera;
    private final Random random = new RandomXS128();

    protected SquadronFactory(EntityFactory entityFactory, OrthographicCamera camera) {
        this.entityFactory = entityFactory;
        this.camera = camera;
    }

    public void createSquadron(ScriptItem scriptItem) {
        Entity squadron = entityFactory.enemyEntityFactory.createSquadron(scriptItem.powerUp, scriptItem.displayBonus, scriptItem.bonus);

        Array<Entity> entitiesSquadron = new Array<>(Entity.class);
        Array<Entity> allEntities = new Array<>(Entity.class);

        for (int i = 0; i < scriptItem.number; ++i) {
            switch (scriptItem.typeShip) {
                case BOSS_LEVEL_1:
                    entitiesSquadron.add(entityFactory.enemyEntityFactory.createBoss(squadron, scriptItem.bulletVelocity, scriptItem.bulletVelocity));
                    break;
                case BOSS_LEVEL_2:
                    entitiesSquadron.add(entityFactory.enemyEntityFactory.createBoss2(squadron, scriptItem.bulletVelocity, scriptItem.bulletVelocity, 800f));
                    break;
                case BOSS_LEVEL_3:
                    entitiesSquadron.add(entityFactory.enemyEntityFactory.createBoss3(squadron, scriptItem.bulletVelocity, 250f, 800f));
                    break;
                case SOUCOUPE:
                    entitiesSquadron.add(entityFactory.enemyEntityFactory.createEnemySoucoupe(squadron, random.nextBoolean(), scriptItem.bulletVelocity));
                    break;
                case ASTEROID_1:
                case ASTEROID_2:
                    entitiesSquadron.add(entityFactory.enemyEntityFactory.createAsteroid(squadron, scriptItem.typeShip));
                    break;
                case HOUSE_1:
                case HOUSE_2:
                case HOUSE_3:
                case HOUSE_4:
                case HOUSE_5:
                case HOUSE_6:
                case HOUSE_7:
                case HOUSE_8:
                case HOUSE_9:
                    Array<Entity> house = entityFactory.enemyEntityFactory.createHouse(squadron, scriptItem.typeShip);
                    allEntities.add(house.get(1));
                    entitiesSquadron.add(house.get(0));

                    break;
                default:
                    entitiesSquadron.add(entityFactory.enemyEntityFactory.createEnemyShip(squadron, random.nextBoolean(), scriptItem.bulletVelocity, scriptItem.rateShoot, scriptItem.typeShip));
                    break;
            }
        }
        allEntities.addAll(entitiesSquadron);
        formSquadron(allEntities.toArray(), scriptItem.typeSquadron, scriptItem.velocity, scriptItem.params);
        ComponentMapperHelper.squadron.get(squadron).addEntities(entitiesSquadron.toArray());
    }

    private void formSquadron(Entity[] entities, int squadronType, float velocity, Object... params) {
        switch (squadronType) {
            case BOSS_MOVE:
                createBossMove(entities, velocity);
                break;
            case BOSS_LEVEL2_MOVE:
                createBossMove2(entities, velocity);
                break;
            case BOSS_LEVEL3_MOVE:
                createBossMove3(entities, velocity);
                break;
            case LINEAR_Y:
                createLinearYSquadron(entities, velocity, (Float) params[0], (Float) params[1], false);
                break;
            case LINEAR_Y_SAME_POS:
                createLinearYSquadron(entities, velocity, (Float) params[0], (Float) params[1], true);
                break;
            case LINEAR_X:
                createLinearXSquadron(entities, velocity, (Float) params[0], (Float) params[1], (Float) params[2]);
                break;
            case LINEAR_XY:
                createLinearXYSquadron(entities, velocity, (Float) params[0], (Float) params[1], (Float) params[2], (Float) params[3]);
                break;
            case SEMI_CIRCLE:
                createSemiCircleSquadron(entities, velocity, (Float) params[0], (Float) params[1], (boolean) params[2]);
                break;
            case BEZIER_SPLINE:
                createBezierSplineSquadron(entities, velocity, convertObjectArrayToVector2Array(params));
                break;
            case CATMULL_ROM_SPLINE:
                createCatmullSplineSquadron(entities, velocity, convertObjectArrayToVector2Array(params));
                break;
            case ARROW_DOWN:
                createArrowDownSquadron(entities, velocity);
                break;
            case ARROW_UP:
                createArrowUpSquadron(entities, velocity);
                break;
            case INFINITE_CIRCLE:
            default:
                createInfiniteCircle(entities, velocity);
                break;
        }
    }

    private Vector2[] convertObjectArrayToVector2Array(Object[] params) {
        Vector2[] vectors = new Vector2[params.length];
        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(params, 0, vectors, 0, params.length);
        return vectors;
    }

    private void createBossMove(Entity[] entities, float velocity) {
        if (entities.length != 1) {
            throw new IllegalArgumentException("Works only with 1 boss");
        }
        final Entity entity = entities[0];
        SpriteComponent spriteComponent = ComponentMapperHelper.sprite.get(entity);
        PositionComponent position = ComponentMapperHelper.position.get(entity);
        position.setXY(SCREEN_WIDTH / 2f - spriteComponent.sprite.getWidth() / 2f,
                SCREEN_HEIGHT + 10f);
        Timeline.createSequence()
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_Y, (spriteComponent.sprite.getHeight() + 30f) / velocity)
                        .ease(Linear.INOUT)
                        .target(SCREEN_HEIGHT - spriteComponent.sprite.getHeight() - 20f))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, ((SCREEN_WIDTH + spriteComponent.sprite.getWidth()) / 2f) / velocity)
                        .ease(Linear.INOUT)
                        .target(-spriteComponent.sprite.getWidth()).delay(2f))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (SCREEN_WIDTH + spriteComponent.sprite.getWidth() / 2f) / velocity)
                        .ease(Linear.INOUT)
                        .target(SCREEN_WIDTH / 2f - spriteComponent.sprite.getWidth() / 2f).delay(2f))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (SCREEN_WIDTH + spriteComponent.sprite.getWidth() / 2f) / velocity)
                        .ease(Linear.INOUT)
                        .target(SCREEN_WIDTH).delay(2f))

                .repeatYoyo(Tween.INFINITY, 1f)
                .start(entityFactory.tweenManager);
    }

    private void createBossMove2(Entity[] entities, float velocity) {
        if (entities.length != 1) {
            throw new IllegalArgumentException("Works only with 1 boss");
        }
        final Entity entity = entities[0];
        Sprite sprite = ComponentMapperHelper.sprite.get(entity).sprite;
        PositionComponent position = ComponentMapperHelper.position.get(entity);
        position.setXY(SCREEN_WIDTH / 2f - sprite.getWidth() / 2f,
                SCREEN_HEIGHT);
        Timeline.createSequence()
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_Y, (sprite.getHeight() + 30f) / velocity)
                        .ease(Linear.INOUT)
                        .targetRelative(-sprite.getHeight() - 30f))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (SCREEN_WIDTH / 2f) / (velocity * 2))
                        .ease(Linear.INOUT)
                        .targetRelative(SCREEN_WIDTH / 2f).delay(2f))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (SCREEN_WIDTH) / (velocity * 2))
                        .ease(Linear.INOUT)
                        .targetRelative(-SCREEN_WIDTH).delay(2f))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (SCREEN_WIDTH / 2f) / (velocity * 2))
                        .ease(Linear.INOUT)
                        .targetRelative(SCREEN_WIDTH / 2f).delay(2f))
                .repeatYoyo(Tween.INFINITY, 1f)
                .start(entityFactory.tweenManager);
    }

    private void createBossMove3(Entity[] entities, float velocity) {
        if (entities.length != 1) {
            throw new IllegalArgumentException("Works only with 1 boss");
        }
        final Entity entity = entities[0];
        Sprite sprite = ComponentMapperHelper.sprite.get(entity).sprite;
        PositionComponent position = ComponentMapperHelper.position.get(entity);
        position.setXY(SCREEN_WIDTH / 2f - sprite.getWidth() / 2f,
                SCREEN_HEIGHT);
        float velocityMin = velocity * 0.75f;
        float velocityMax = velocity * 2f;

        Timeline.createSequence()
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_Y, (3 * sprite.getHeight()) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT)
                        .targetRelative(-3 * sprite.getHeight()))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_Y, (1.5f * sprite.getHeight()) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT)
                        .targetRelative(1.5f * sprite.getHeight()))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (SCREEN_WIDTH / 2f) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT)
                        .targetRelative(-SCREEN_WIDTH / 2f))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (SCREEN_WIDTH) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT)
                        .targetRelative(SCREEN_WIDTH))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (SCREEN_WIDTH / 2f) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT)
                        .targetRelative(-SCREEN_WIDTH / 2f))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_Y, (0.5f * sprite.getHeight()) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT)
                        .targetRelative(0.5f * sprite.getHeight()))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (SCREEN_WIDTH / 4f) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT).delay(1f)
                        .targetRelative(-SCREEN_WIDTH / 4f))
                .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_X, (2 * SCREEN_WIDTH / 4f) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT).delay(1f)
                        .targetRelative(2 * SCREEN_WIDTH / 4f))
                .repeatYoyo(Tween.INFINITY, 1f)
                .start(entityFactory.tweenManager);
    }


    private void createArrowUpSquadron(Entity[] entities, float velocity) {
        if (entities.length != 7) {
            throw new IllegalArgumentException("Works only with 7 entities");
        }
        SpriteComponent sprite = ComponentMapperHelper.sprite.get(entities[0]);
        float width = sprite.sprite.getWidth();
        float height = sprite.sprite.getHeight();
        ComponentMapperHelper.position.get(entities[0]).setXY(SCREEN_WIDTH / 2f - width / 2f - 3 * width * 1.1f, SCREEN_HEIGHT + 3 * height * 1.1f);
        ComponentMapperHelper.position.get(entities[1]).setXY(SCREEN_WIDTH / 2f - width / 2f - 2 * width * 1.1f, SCREEN_HEIGHT + 2 * height * 1.1f);
        ComponentMapperHelper.position.get(entities[2]).setXY(SCREEN_WIDTH / 2f - width / 2f - width * 1.1f, SCREEN_HEIGHT + height * 1.1f);
        ComponentMapperHelper.position.get(entities[3]).setXY(SCREEN_WIDTH / 2f - width / 2f, SCREEN_HEIGHT);
        ComponentMapperHelper.position.get(entities[4]).setXY(SCREEN_WIDTH / 2f - width / 2f + width * 1.1f, SCREEN_HEIGHT + height * 1.1f);
        ComponentMapperHelper.position.get(entities[5]).setXY(SCREEN_WIDTH / 2f - width / 2f + 2 * width * 1.1f, SCREEN_HEIGHT + 2 * height * 1.1f);
        ComponentMapperHelper.position.get(entities[6]).setXY(SCREEN_WIDTH / 2f - width / 2f + 3 * width * 1.1f, SCREEN_HEIGHT + 3 * height * 1.1f);

        arrowTween(entities, velocity);
    }

    private void createArrowDownSquadron(Entity[] entities, float velocity) {
        if (entities.length != 7) {
            throw new IllegalArgumentException("Works only with 7 entities");
        }
        SpriteComponent sprite = ComponentMapperHelper.sprite.get(entities[0]);
        float width = sprite.sprite.getWidth();
        float height = sprite.sprite.getHeight();
        ComponentMapperHelper.position.get(entities[0]).setXY(SCREEN_WIDTH / 2f - width / 2f - 3 * width * 1.1f, SCREEN_HEIGHT);
        ComponentMapperHelper.position.get(entities[1]).setXY(SCREEN_WIDTH / 2f - width / 2f - 2 * width * 1.1f, SCREEN_HEIGHT + height * 1.1f);
        ComponentMapperHelper.position.get(entities[2]).setXY(SCREEN_WIDTH / 2f - width / 2f - width * 1.1f, SCREEN_HEIGHT + 2 * height * 1.1f);
        ComponentMapperHelper.position.get(entities[3]).setXY(SCREEN_WIDTH / 2f - width / 2f, SCREEN_HEIGHT + 3 * height * 1.1f);
        ComponentMapperHelper.position.get(entities[4]).setXY(SCREEN_WIDTH / 2f - width / 2f + width * 1.1f, SCREEN_HEIGHT + 2 * height * 1.1f);
        ComponentMapperHelper.position.get(entities[5]).setXY(SCREEN_WIDTH / 2f - width / 2f + 2 * width * 1.1f, SCREEN_HEIGHT + height * 1.1f);
        ComponentMapperHelper.position.get(entities[6]).setXY(SCREEN_WIDTH / 2f - width / 2f + 3 * width * 1.1f, SCREEN_HEIGHT);

        arrowTween(entities, velocity);
    }

    private void arrowTween(Entity[] entities, float velocity) {
        for (final Entity entity : entities) {
            PositionComponent position = ComponentMapperHelper.position.get(entity);
            Tween.to(position, PositionComponentTweenAccessor.POSITION_Y, 3 * SCREEN_HEIGHT / velocity)
                    .ease(Linear.INOUT)
                    .targetRelative(-3f * SCREEN_HEIGHT)
                    .setCallback((i, baseTween) -> {
                        if (i == COMPLETE) {
                            removeEntitySquadron(entity);
                        }
                    })
                    .start(entityFactory.tweenManager);
        }
    }

    private void createInfiniteCircle(Entity[] entities, float velocity) {
        float spriteWidth = 0;
        for (int i = 0; i < entities.length; ++i) {
            Entity entity = entities[i];
            PositionComponent positionComponent = ComponentMapperHelper.position.get(entity);
            Sprite sprite = ComponentMapperHelper.sprite.get(entity).sprite;
            spriteWidth = sprite.getWidth();
            positionComponent.setXY(-(i + 1) * sprite.getWidth(), SCREEN_HEIGHT * 3f / 4f);
        }
        int k = 50;
        Vector2[] points = new Vector2[2 * k];
        Bezier<Vector2> bezier = new Bezier<>(
                poolVector2.getVector2(0f, SCREEN_HEIGHT * 3f / 4f),
                poolVector2.getVector2(SCREEN_WIDTH / 2f - spriteWidth / 2f, SCREEN_HEIGHT),
                poolVector2.getVector2(SCREEN_WIDTH - spriteWidth, SCREEN_HEIGHT * 3f / 4f));
        for (int i = 0; i < k; ++i) {
            points[i] = poolVector2.obtain();
            bezier.valueAt(points[i], ((float) i) / ((float) k - 1));
        }
        poolVector2.free(bezier.points);
        bezier = new Bezier<>(
                poolVector2.getVector2(SCREEN_WIDTH - spriteWidth, SCREEN_HEIGHT * 3f / 4f),
                poolVector2.getVector2(SCREEN_WIDTH / 2f - spriteWidth / 2f, SCREEN_HEIGHT / 2f),
                poolVector2.getVector2(0f, SCREEN_HEIGHT * 3f / 4f));
        for (int i = 0; i < k; ++i) {
            points[k + i] = poolVector2.obtain();
            bezier.valueAt(points[k + i], ((float) i) / ((float) k - 1));
        }
        poolVector2.free(bezier.points);
        placeEntitiesOnSplineInfinite(entities, velocity, points);
    }

    private void createBezierSplineSquadron(Entity[] entities, float velocity, Vector2... vector2s) {
        int k = 100;
        Bezier<Vector2> bezier = new Bezier<>(vector2s);
        Vector2[] points = new Vector2[k];
        for (int i = 0; i < k; ++i) {
            points[i] = poolVector2.obtain();
            bezier.valueAt(points[i], ((float) i) / ((float) k - 1));
        }
        placeEntitiesOnSpline(entities, velocity, points, vector2s[0]);
        poolVector2.free(vector2s);
        poolVector2.free(points);
    }


    private void createCatmullSplineSquadron(Entity[] entities, float velocity, Vector2... vector2s) {
        int k = 100;
        CatmullRomSpline<Vector2> catmull = new CatmullRomSpline<>(vector2s, false);
        Vector2[] points = new Vector2[k];
        for (int i = 0; i < k; ++i) {
            points[i] = poolVector2.obtain();
            catmull.valueAt(points[i], ((float) i) / ((float) k - 1));
        }
        placeEntitiesOnSpline(entities, velocity, points, vector2s[0]);
        poolVector2.free(vector2s);
        poolVector2.free(points);
    }

    private void placeEntitiesOnSpline(Entity[] entities, float velocity, Vector2[] points, Vector2 startPoint) {
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent position = ComponentMapperHelper.position.get(entity);
            position.setXY(startPoint.x, startPoint.y + i * ComponentMapperHelper.sprite.get(entity).sprite.getHeight());
            Timeline timeline = Timeline.createSequence();
            timeline.push(Tween.to(position, PositionComponentTweenAccessor.POSITION_XY, points[0].dst(position.x(), position.y()) / velocity)
                    .ease(Linear.INOUT)
                    .target(points[0].x, points[0].y));
            for (int j = 1; j < points.length; ++j) {
                timeline.push(Tween.to(position, PositionComponentTweenAccessor.POSITION_XY, points[j].dst(points[j - 1].x, points[j - 1].y) / velocity)
                        .ease(Linear.INOUT)
                        .target(points[j].x, points[j].y));
            }
            timeline.setCallback((i1, baseTween) -> {
                if (i1 == COMPLETE) {
                    removeEntitySquadron(entity);
                }
            })
                    .start(entityFactory.tweenManager);
        }
    }

    private void placeEntitiesOnSplineInfinite(Entity[] entities, final float velocity, final Vector2[] points) {
        float[] pointsSpread = new float[points.length * 2];
        for (int i = 0; i < points.length; ++i) {
            pointsSpread[i * 2] = points[i].x;
            pointsSpread[i * 2 + 1] = points[i].y;
        }
        for (final Entity entity : entities) {
            final PositionComponent position = ComponentMapperHelper.position.get(entity);
            Tween.to(position, PositionComponentTweenAccessor.POSITION_XY,
                    points[0].dst(position.x(), position.y()) / velocity)
                    .ease(Linear.INOUT)
                    .target(points[0].x, points[0].y)
                    .setCallback((i, baseTween) -> {
                        if (i == COMPLETE) {
                            Timeline timeline = Timeline.createSequence();
                            for (int j = 2; j < pointsSpread.length; j += 2) {
                                Vector2 dest = poolVector2.getVector2(pointsSpread[j], pointsSpread[j + 1]);
                                timeline.push(Tween.to(position, PositionComponentTweenAccessor.POSITION_XY,
                                        dest.dst(pointsSpread[j - 2], pointsSpread[j - 1]) / velocity)
                                        .ease(Linear.INOUT)
                                        .target(pointsSpread[j], pointsSpread[j + 1]));
                                poolVector2.free(dest);
                            }
                            timeline.repeat(Tween.INFINITY, 0f)
                                    .start(entityFactory.tweenManager);
                        }
                    }).start(entityFactory.tweenManager);
        }
        poolVector2.free(points);
    }

    private void removeEntitySquadron(Entity entity) {
        EnemyComponent enemyComponent = ComponentMapperHelper.enemy.get(entity);
        if (enemyComponent != null && enemyComponent.squadron != null) {
            ComponentMapperHelper.squadron.get(enemyComponent.squadron).powerUpAfterDestruction = false;
            ComponentMapperHelper.squadron.get(enemyComponent.squadron).ships.removeValue(entity, true);
        }
        entityFactory.engine.removeEntity(entity);
    }


    private void createLinearXSquadron(final Entity[] entities, float velocity, float posX, float posY, float direction) {
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent positionComponent = ComponentMapperHelper.position.get(entity);
            positionComponent.setXY(posX - direction * i * ComponentMapperHelper.sprite.get(entity).sprite.getWidth(), posY);
            Tween.to(positionComponent, PositionComponentTweenAccessor.POSITION_X, 3f * SCREEN_WIDTH / velocity)
                    .ease(Linear.INOUT)
                    .targetRelative(direction * 3f * SCREEN_WIDTH)
                    .setCallback((state, tween) -> {
                        if (state == COMPLETE) {
                            removeEntitySquadron(entity);
                        }
                    })
                    .start(entityFactory.tweenManager);
        }
    }

    private void createLinearYSquadron(Entity[] entities, float velocity, float posX, float posY,
                                       boolean samePosition) {
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent position = ComponentMapperHelper.position.get(entity);
            if (samePosition) {
                position.setXY(posX, posY);
            } else {
                position.setXY(posX, posY + i * ComponentMapperHelper.sprite.get(entity).sprite.getHeight());
            }
            Timeline.createSequence()
                    .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_Y, 3 * SCREEN_HEIGHT / velocity)
                            .ease(Linear.INOUT)
                            .targetRelative(-3f * SCREEN_HEIGHT))
                    .setCallback((i1, baseTween) -> {
                        if (i1 == COMPLETE) {
                            removeEntitySquadron(entity);
                        }
                    })
                    .start(entityFactory.tweenManager);
        }
    }


    private void createLinearXYSquadron(Entity[] entities, float velocity, float startX, float startY, float endX,
                                        float endY) {
        Vector2 start = poolVector2.getVector2(startX, startY);
        Vector2 end = poolVector2.getVector2(endX, endY);
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent position = ComponentMapperHelper.position.get(entity);
            position.setXY(startX, startY + i * ComponentMapperHelper.sprite.get(entity).sprite.getHeight());
            Timeline.createSequence()
                    .push(
                            Tween.to(position, PositionComponentTweenAccessor.POSITION_XY,
                                    (start).dst(end) / velocity)
                                    .ease(Linear.INOUT)
                                    .target(endX, endY))
                    .setCallback((i1, baseTween) -> {
                        if (i1 == COMPLETE) {
                            removeEntitySquadron(entity);
                        }
                    })
                    .start(entityFactory.tweenManager);
        }
        poolVector2.free(start);
        poolVector2.free(end);

    }

    private void createSemiCircleSquadron(Entity[] entities, float velocity, float posX, float posY,
                                          boolean comingFromLeft) {
        float worldWidth = SCREEN_WIDTH + 2 * OFFSET_WIDTH;
        int leftOrRight = comingFromLeft ? 0 : 1;
        int direction = comingFromLeft ? 1 : -1;
        Vector2 center = poolVector2.getVector2(SCREEN_WIDTH / 2f, SCREEN_HEIGHT);
        Vector2 radius = poolVector2.getVector2((worldWidth * -direction) / 2f, 0f);
        Vector2[] array = new Vector2[20];
        for (int i = 0; i < array.length; ++i) {
            Vector2 radiusClone = poolVector2.getVector2(radius.x, radius.y);
            array[i] = radiusClone.rotate(((180f * direction) / array.length - 1) * i).add(center);
        }
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent position = ComponentMapperHelper.position.get(entity);
            position.setXY(posX + leftOrRight * worldWidth,
                    posY + 2 * i * ComponentMapperHelper.sprite.get(entity).sprite.getHeight());
            Timeline timeline = Timeline.createSequence()
                    .push(Tween.to(position, PositionComponentTweenAccessor.POSITION_XY, array[0].dst(position.x(), position.y()) / velocity)
                            .ease(Linear.INOUT)
                            .target(array[0].x, array[0].y));
            for (int j = 1; j < array.length; ++j) {
                timeline.push(Tween.to(position, PositionComponentTweenAccessor.POSITION_XY, array[j].dst(array[j - 1].x, array[j - 1].y) / velocity)
                        .ease(Linear.INOUT)
                        .target(array[j].x, array[j].y));
            }
            timeline.setCallback((i1, baseTween) -> {
                if (i1 == COMPLETE) {
                    removeEntitySquadron(entity);
                }
            })
                    .start(entityFactory.tweenManager);
        }
        poolVector2.free(radius);
        poolVector2.free(array);
        poolVector2.free(center);

    }
}
