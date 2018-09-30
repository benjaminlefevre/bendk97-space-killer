/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bendk97.components.Mappers;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.SpriteComponent;
import com.bendk97.tweens.CameraTween;
import com.bendk97.tweens.PositionComponentAccessor;

import java.util.Arrays;
import java.util.Random;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

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


    private TweenManager tweenManager;
    private EntityFactory entityFactory;
    private Engine engine;
    private Random random = new RandomXS128();
    private OrthographicCamera camera;

    public SquadronFactory(TweenManager tweenManager, EntityFactory entityFactory, OrthographicCamera camera, Engine engine) {
        this.tweenManager = tweenManager;
        this.entityFactory = entityFactory;
        this.engine = engine;
        this.camera = camera;
    }

    public void createSquadron(int shipType, int squadronType, float velocity, int number, boolean powerUp,
                               boolean displayBonus, int bonus, float bulletVelocity, int rateShoot,
                               Object... params) {
        Entity squadron = entityFactory.createSquadron(powerUp, displayBonus, bonus);

        Array<Entity> entitiesSquadron = new Array<Entity>(Entity.class);
        Array<Entity> allEntities = new Array<Entity>(Entity.class);

        for (int i = 0; i < number; ++i) {
            switch (shipType) {
                case EntityFactory.BOSS_LEVEL_1:
                    entitiesSquadron.add(entityFactory.createBoss(squadron, bulletVelocity, bulletVelocity));
                    break;
                case EntityFactory.BOSS_LEVEL_2:
                    entitiesSquadron.add(entityFactory.createBoss2(squadron, bulletVelocity, bulletVelocity, 800f));
                    break;
                case EntityFactory.BOSS_LEVEL_3:
                    entitiesSquadron.add(entityFactory.createBoss3(squadron, bulletVelocity, 250f, 800f));
                    break;
                case EntityFactory.SOUCOUPE:
                    entitiesSquadron.add(entityFactory.createEnemySoucoupe(squadron, random.nextBoolean(), bulletVelocity));
                    break;
                case EntityFactory.ASTEROID_1:
                case EntityFactory.ASTEROID_2:
                    entitiesSquadron.add(entityFactory.createAsteroid(squadron, shipType));
                    break;
                case EntityFactory.HOUSE_1:
                case EntityFactory.HOUSE_2:
                case EntityFactory.HOUSE_3:
                case EntityFactory.HOUSE_4:
                case EntityFactory.HOUSE_5:
                case EntityFactory.HOUSE_6:
                case EntityFactory.HOUSE_7:
                case EntityFactory.HOUSE_8:
                case EntityFactory.HOUSE_9:
                    Array<Entity> house = entityFactory.createHouse(squadron, shipType);
                    allEntities.add(house.get(1));
                    entitiesSquadron.add(house.get(0));

                    break;
                default:
                    entitiesSquadron.add(entityFactory.createEnemyShip(squadron, random.nextBoolean(), bulletVelocity, rateShoot, shipType));
            }
        }
        allEntities.addAll(entitiesSquadron);
        formSquadron(allEntities.toArray(), squadronType, velocity, params);
        Mappers.squadron.get(squadron).addEntities(entitiesSquadron.toArray());
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
                createSemiCircleSquadron(entities, velocity, (Float) params[0], (Float) params[1]);
                break;
            case BEZIER_SPLINE:
                createBezierSplineSquadron(entities, velocity, Arrays.copyOf(params, params.length, Vector2[].class));
                break;
            case CATMULL_ROM_SPLINE:
                createCatmullSplineSquadron(entities, velocity, Arrays.copyOf(params, params.length, Vector2[].class));
                break;
            case ARROW_DOWN:
                createArrowDownSquadron(entities, velocity);
                break;
            case ARROW_UP:
                createArrowUpSquadron(entities, velocity);
                break;
            case INFINITE_CIRCLE:
                createInfiniteCircle(entities, velocity);
                break;
        }
    }

    private void createBossMove(Entity[] entities, float velocity) {
        if (entities.length != 1) {
            throw new IllegalArgumentException("Works only with 1 boss");
        }
        final Entity entity = entities[0];
        SpriteComponent spriteComponent = Mappers.sprite.get(entity);
        PositionComponent position = Mappers.position.get(entity);
        position.setPosition(SCREEN_WIDTH / 2f - spriteComponent.sprite.getWidth() / 2f,
                SCREEN_HEIGHT + 10f);
        Tween.to(camera, CameraTween.ZOOM, 2f).ease(Linear.INOUT).target(0.5f).repeatYoyo(1, 0.5f).start(tweenManager);
        Timeline.createSequence()
                .push(Tween.to(position, PositionComponentAccessor.POSITION_Y, (spriteComponent.sprite.getHeight() + 30f) / velocity)
                        .ease(Linear.INOUT)
                        .target(SCREEN_HEIGHT - spriteComponent.sprite.getHeight() - 20f))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_X, ((SCREEN_WIDTH + spriteComponent.sprite.getWidth()) / 2f) / velocity)
                        .ease(Linear.INOUT)
                        .target(-spriteComponent.sprite.getWidth()).delay(2f))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_X, (SCREEN_WIDTH + spriteComponent.sprite.getWidth() / 2f) / velocity)
                        .ease(Linear.INOUT)
                        .target(SCREEN_WIDTH / 2f - spriteComponent.sprite.getWidth() / 2f).delay(2f))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_X, (SCREEN_WIDTH + spriteComponent.sprite.getWidth() / 2f) / velocity)
                        .ease(Linear.INOUT)
                        .target(SCREEN_WIDTH).delay(2f))

                .repeatYoyo(Tween.INFINITY, 1f)
                .start(tweenManager);
    }

    private void createBossMove2(Entity[] entities, float velocity) {
        if (entities.length != 1) {
            throw new IllegalArgumentException("Works only with 1 boss");
        }
        final Entity entity = entities[0];
        Sprite sprite = Mappers.sprite.get(entity).sprite;
        PositionComponent position = Mappers.position.get(entity);
        position.setPosition(SCREEN_WIDTH / 2f - sprite.getWidth() / 2f,
                SCREEN_HEIGHT);
        Tween.to(camera, CameraTween.ZOOM, 2f).ease(Linear.INOUT).target(0.5f).repeatYoyo(1, 0.5f).start(tweenManager);

        Timeline.createSequence()
                .push(Tween.to(position, PositionComponentAccessor.POSITION_Y, (sprite.getHeight() + 30f) / velocity)
                        .ease(Linear.INOUT)
                        .targetRelative(-sprite.getHeight() - 30f))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_X, (SCREEN_WIDTH / 2f) / (velocity * 2))
                        .ease(Linear.INOUT)
                        .targetRelative(SCREEN_WIDTH / 2f).delay(2f))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_X, (SCREEN_WIDTH) / (velocity * 2))
                        .ease(Linear.INOUT)
                        .targetRelative(-SCREEN_WIDTH).delay(2f))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_X, (SCREEN_WIDTH / 2f) / (velocity * 2))
                        .ease(Linear.INOUT)
                        .targetRelative(SCREEN_WIDTH / 2f).delay(2f))
                .repeatYoyo(Tween.INFINITY, 1f)
                .start(tweenManager);
    }

    private void createBossMove3(Entity[] entities, float velocity) {
        if (entities.length != 1) {
            throw new IllegalArgumentException("Works only with 1 boss");
        }
        final Entity entity = entities[0];
        Sprite sprite = Mappers.sprite.get(entity).sprite;
        PositionComponent position = Mappers.position.get(entity);
        position.setPosition(SCREEN_WIDTH / 2f - sprite.getWidth() / 2f,
                SCREEN_HEIGHT);
        Tween.to(camera, CameraTween.ZOOM, 2f).ease(Linear.INOUT).target(0.5f).repeatYoyo(1, 0.5f).start(tweenManager);

        float velocityMin = velocity * 0.75f;
        float velocityMax = velocity * 2f;

        Timeline.createSequence()
                .push(Tween.to(position, PositionComponentAccessor.POSITION_Y, (3 * sprite.getHeight()) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT)
                        .targetRelative(-3 * sprite.getHeight()))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_Y, (1.5f * sprite.getHeight()) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT)
                        .targetRelative(1.5f * sprite.getHeight()))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_X, (SCREEN_WIDTH / 2f) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT)
                        .targetRelative(-SCREEN_WIDTH / 2f))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_X, (SCREEN_WIDTH) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT)
                        .targetRelative(SCREEN_WIDTH))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_X, (SCREEN_WIDTH / 2f) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT)
                        .targetRelative(-SCREEN_WIDTH / 2f))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_Y, (0.5f * sprite.getHeight()) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT)
                        .targetRelative(0.5f * sprite.getHeight()))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_X, (SCREEN_WIDTH / 4f) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT).delay(1f)
                        .targetRelative(-SCREEN_WIDTH / 4f))
                .push(Tween.to(position, PositionComponentAccessor.POSITION_X, (2 * SCREEN_WIDTH / 4f) / (velocityMin + random.nextFloat() * (velocityMax - velocityMin)))
                        .ease(Linear.INOUT).delay(1f)
                        .targetRelative(2 * SCREEN_WIDTH / 4f))
                .repeatYoyo(Tween.INFINITY, 1f)
                .start(tweenManager);
    }


    private void createArrowUpSquadron(Entity[] entities, float velocity) {
        if (entities.length != 7) {
            throw new IllegalArgumentException("Works only with 5 entities");
        }
        SpriteComponent sprite = Mappers.sprite.get(entities[0]);
        float width = sprite.sprite.getWidth();
        float height = sprite.sprite.getHeight();
        Mappers.position.get(entities[0]).setPosition(SCREEN_WIDTH / 2f - width / 2f - 3 * width * 1.1f, SCREEN_HEIGHT + 3 * height * 1.1f);
        Mappers.position.get(entities[1]).setPosition(SCREEN_WIDTH / 2f - width / 2f - 2 * width * 1.1f, SCREEN_HEIGHT + 2 * height * 1.1f);
        Mappers.position.get(entities[2]).setPosition(SCREEN_WIDTH / 2f - width / 2f - width * 1.1f, SCREEN_HEIGHT + height * 1.1f);
        Mappers.position.get(entities[3]).setPosition(SCREEN_WIDTH / 2f - width / 2f, SCREEN_HEIGHT);
        Mappers.position.get(entities[4]).setPosition(SCREEN_WIDTH / 2f - width / 2f + width * 1.1f, SCREEN_HEIGHT + height * 1.1f);
        Mappers.position.get(entities[5]).setPosition(SCREEN_WIDTH / 2f - width / 2f + 2 * width * 1.1f, SCREEN_HEIGHT + 2 * height * 1.1f);
        Mappers.position.get(entities[6]).setPosition(SCREEN_WIDTH / 2f - width / 2f + 3 * width * 1.1f, SCREEN_HEIGHT + 3 * height * 1.1f);


        for (final Entity entity : entities) {
            PositionComponent position = Mappers.position.get(entity);
            Timeline.createSequence()
                    .push(Tween.to(position, PositionComponentAccessor.POSITION_Y, 3 * SCREEN_HEIGHT / velocity)
                            .ease(Linear.INOUT)
                            .targetRelative(-3f * SCREEN_HEIGHT))
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            if (i == TweenCallback.COMPLETE) {
                                removeEntitySquadron(entity);
                            }
                        }
                    })
                    .start(tweenManager);
        }
    }

    private void createArrowDownSquadron(Entity[] entities, float velocity) {
        if (entities.length != 7) {
            throw new IllegalArgumentException("Works only with 5 entities");
        }
        SpriteComponent sprite = Mappers.sprite.get(entities[0]);
        float width = sprite.sprite.getWidth();
        float height = sprite.sprite.getHeight();
        Mappers.position.get(entities[0]).setPosition(SCREEN_WIDTH / 2f - width / 2f - 3 * width * 1.1f, SCREEN_HEIGHT);
        Mappers.position.get(entities[1]).setPosition(SCREEN_WIDTH / 2f - width / 2f - 2 * width * 1.1f, SCREEN_HEIGHT + height * 1.1f);
        Mappers.position.get(entities[2]).setPosition(SCREEN_WIDTH / 2f - width / 2f - width * 1.1f, SCREEN_HEIGHT + 2 * height * 1.1f);
        Mappers.position.get(entities[3]).setPosition(SCREEN_WIDTH / 2f - width / 2f, SCREEN_HEIGHT + 3 * height * 1.1f);
        Mappers.position.get(entities[4]).setPosition(SCREEN_WIDTH / 2f - width / 2f + width * 1.1f, SCREEN_HEIGHT + 2 * height * 1.1f);
        Mappers.position.get(entities[5]).setPosition(SCREEN_WIDTH / 2f - width / 2f + 2 * width * 1.1f, SCREEN_HEIGHT + height * 1.1f);
        Mappers.position.get(entities[6]).setPosition(SCREEN_WIDTH / 2f - width / 2f + 3 * width * 1.1f, SCREEN_HEIGHT);


        for (final Entity entity : entities) {
            PositionComponent position = Mappers.position.get(entity);
            Timeline.createSequence()
                    .push(Tween.to(position, PositionComponentAccessor.POSITION_Y, 3 * SCREEN_HEIGHT / velocity)
                            .ease(Linear.INOUT)
                            .targetRelative(-3f * SCREEN_HEIGHT))
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            if (i == TweenCallback.COMPLETE) {
                                removeEntitySquadron(entity);
                            }
                        }
                    })
                    .start(tweenManager);
        }
    }

    private void createInfiniteCircle(Entity[] entities, float velocity) {
        float spriteWidth = 0;
        for (int i = 0; i < entities.length; ++i) {
            Entity entity = entities[i];
            PositionComponent position = Mappers.position.get(entity);
            Sprite sprite = Mappers.sprite.get(entity).sprite;
            spriteWidth = sprite.getWidth();
            position.y = SCREEN_HEIGHT * 3f / 4f;
            position.x = -(i + 1) * sprite.getWidth();// + 10f * i;
        }
        int k = 50;
        Vector2[] points = new Vector2[2 * k];
        Bezier<Vector2> bezier = new Bezier<Vector2>(new Vector2(0f, SCREEN_HEIGHT * 3f / 4f),
                new Vector2(SCREEN_WIDTH / 2f - spriteWidth / 2f, SCREEN_HEIGHT),
                new Vector2(SCREEN_WIDTH - spriteWidth, SCREEN_HEIGHT * 3f / 4f));
        for (int i = 0; i < k; ++i) {
            points[i] = new Vector2();
            bezier.valueAt(points[i], ((float) i) / ((float) k - 1));
        }
        bezier = new Bezier<Vector2>(new Vector2(SCREEN_WIDTH - spriteWidth, SCREEN_HEIGHT * 3f / 4f),
                new Vector2(SCREEN_WIDTH / 2f - spriteWidth / 2f, SCREEN_HEIGHT / 2f),
                new Vector2(0f, SCREEN_HEIGHT * 3f / 4f));
        for (int i = 0; i < k; ++i) {
            points[k + i] = new Vector2();
            bezier.valueAt(points[k + i], ((float) i) / ((float) k - 1));
        }
        placeEntitiesOnSplineInfinite(entities, velocity, points);
    }

    private void createBezierSplineSquadron(Entity[] entities, float velocity, Vector2... vector2s) {
        int k = 100;
        Bezier<Vector2> bezier = new Bezier<Vector2>(vector2s);
        Vector2[] points = new Vector2[k];
        for (int i = 0; i < k; ++i) {
            points[i] = new Vector2();
            bezier.valueAt(points[i], ((float) i) / ((float) k - 1));
        }
        placeEntitiesOnSpline(entities, velocity, points, vector2s[0]);
    }


    private void createCatmullSplineSquadron(Entity[] entities, float velocity, Vector2... vector2s) {
        int k = 100;
        CatmullRomSpline<Vector2> catmull = new CatmullRomSpline<Vector2>(vector2s, false);
        Vector2[] points = new Vector2[k];
        for (int i = 0; i < k; ++i) {
            points[i] = new Vector2();
            catmull.valueAt(points[i], ((float) i) / ((float) k - 1));
        }
        placeEntitiesOnSpline(entities, velocity, points, vector2s[0]);
    }

    private void placeEntitiesOnSpline(Entity[] entities, float velocity, Vector2[] points, Vector2 startPoint) {
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent position = Mappers.position.get(entity);
            position.setPosition(startPoint.x, startPoint.y + i * Mappers.sprite.get(entity).sprite.getHeight());
            Timeline timeline = Timeline.createSequence();
            timeline.push(Tween.to(position, PositionComponentAccessor.POSITION_XY, points[0].dst(position.x, position.y) / velocity)
                    .ease(Linear.INOUT)
                    .target(points[0].x, points[0].y));
            for (int j = 1; j < points.length; ++j) {
                timeline.push(Tween.to(position, PositionComponentAccessor.POSITION_XY, points[j].dst(points[j - 1].x, points[j - 1].y) / velocity)
                        .ease(Linear.INOUT)
                        .target(points[j].x, points[j].y));
            }
            timeline.setCallback(new TweenCallback() {
                @Override
                public void onEvent(int i, BaseTween<?> baseTween) {
                    if (i == TweenCallback.COMPLETE) {
                        removeEntitySquadron(entity);
                    }
                }
            })
                    .start(tweenManager);
        }
    }

    private void placeEntitiesOnSplineInfinite(Entity[] entities, final float velocity, final Vector2[] points) {
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            final PositionComponent position = Mappers.position.get(entity);
            Tween.to(position, PositionComponentAccessor.POSITION_XY, points[0].dst(position.x, position.y) / velocity)
                    .ease(Linear.INOUT)
                    .target(points[0].x, points[0].y)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            if (i == COMPLETE) {
                                Timeline timeline = Timeline.createSequence();
                                for (int j = 1; j < points.length; ++j) {
                                    timeline.push(Tween.to(position, PositionComponentAccessor.POSITION_XY, points[j].dst(points[j - 1].x, points[j - 1].y) / velocity)
                                            .ease(Linear.INOUT)
                                            .target(points[j].x, points[j].y));
                                }
                                timeline.repeat(Tween.INFINITY, 0f)
                                        .start(tweenManager);
                            }
                        }
                    }).start(tweenManager);
        }
    }

    private void removeEntitySquadron(Entity entity) {
        com.bendk97.components.EnemyComponent enemyComponent = Mappers.enemy.get(entity);
        if (enemyComponent != null && enemyComponent.squadron != null) {
            Mappers.squadron.get(enemyComponent.squadron).powerUpAfterDestruction = false;
            Mappers.squadron.get(enemyComponent.squadron).ships.removeValue(entity, true);
        }
        engine.removeEntity(entity);
    }


    private void createLinearXSquadron(final Entity[] entities, float velocity, float posX, float posY, float direction) {
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent position = Mappers.position.get(entity);
            position.setPosition(posX - direction * i * Mappers.sprite.get(entity).sprite.getWidth(), posY);
            Timeline.createSequence()
                    .push(Tween.to(position, PositionComponentAccessor.POSITION_X, 3f * SCREEN_WIDTH / velocity)
                            .ease(Linear.INOUT)
                            .targetRelative(direction * 3f * SCREEN_WIDTH))
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            if (i == TweenCallback.COMPLETE) {
                                removeEntitySquadron(entity);
                            }
                        }
                    })
                    .start(tweenManager);
        }
    }

    private void createLinearYSquadron(Entity[] entities, float velocity, float posX, float posY, boolean samePosition) {
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent position = Mappers.position.get(entity);
            if (samePosition) {
                position.setPosition(posX, posY);
            } else {
                position.setPosition(posX, posY + i * Mappers.sprite.get(entity).sprite.getHeight());
            }
            Timeline.createSequence()
                    .push(Tween.to(position, PositionComponentAccessor.POSITION_Y, 3 * SCREEN_HEIGHT / velocity)
                            .ease(Linear.INOUT)
                            .targetRelative(-3f * SCREEN_HEIGHT))
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            if (i == TweenCallback.COMPLETE) {
                                removeEntitySquadron(entity);
                            }
                        }
                    })
                    .start(tweenManager);
        }
    }


    private void createLinearXYSquadron(Entity[] entities, float velocity, float startX, float startY, float endX, float endY) {
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent position = Mappers.position.get(entity);
            position.setPosition(startX, startY + i * Mappers.sprite.get(entity).sprite.getHeight());
            Timeline.createSequence()
                    .push(Tween.to(position, PositionComponentAccessor.POSITION_XY, (new Vector2(startX, startY)).dst(new Vector2(endX, endY)) / velocity)
                            .ease(Linear.INOUT)
                            .target(endX, endY))
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            if (i == TweenCallback.COMPLETE) {
                                removeEntitySquadron(entity);
                            }
                        }
                    })
                    .start(tweenManager);
        }
    }

    private void createSemiCircleSquadron(Entity[] entities, float velocity, float posX, float posY) {
        Vector2 center = new Vector2(SCREEN_WIDTH / 2f, SCREEN_HEIGHT);
        Vector2 radius = new Vector2(-SCREEN_WIDTH / 2f, 0f);
        Vector2[] array = new Vector2[20];
        for (int i = 0; i < array.length; ++i) {
            array[i] = radius.cpy().rotate((180f / array.length - 1) * i).add(center);
        }

        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent position = Mappers.position.get(entity);
            position.setPosition(posX, posY + 2 * i * Mappers.sprite.get(entity).sprite.getHeight());
            Timeline timeline = Timeline.createSequence()
                    .push(Tween.to(position, PositionComponentAccessor.POSITION_XY, array[0].dst(position.x, position.y) / velocity)
                            .ease(Linear.INOUT)
                            .target(array[0].x, array[0].y));
            for (int j = 1; j < array.length; ++j) {
                timeline.push(Tween.to(position, PositionComponentAccessor.POSITION_XY, array[j].dst(array[j - 1].x, array[j - 1].y) / velocity)
                        .ease(Linear.INOUT)
                        .target(array[j].x, array[j].y));
            }
            timeline.setCallback(new TweenCallback() {
                @Override
                public void onEvent(int i, BaseTween<?> baseTween) {
                    if (i == TweenCallback.COMPLETE) {
                        removeEntitySquadron(entity);
                    }
                }
            })
                    .start(tweenManager);
        }
    }
}
