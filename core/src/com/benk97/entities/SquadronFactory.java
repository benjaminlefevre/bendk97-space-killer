package com.benk97.entities;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.benk97.components.EnemyComponent;
import com.benk97.components.Mappers;
import com.benk97.components.PositionComponent;
import com.benk97.tweens.PositionComponentAccessor;

import java.util.Random;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class SquadronFactory {
    public final static int SOUCOUPE = 0;
    public final static int SHIP = 1;
    public final static int LINEAR_X = 0;
    public final static int LINEAR_Y = 1;
    public final static int SEMI_CIRCLE = 2;
    public final static int BEZIER_SPLINE = 3;


    private TweenManager tweenManager;
    private EntityFactory entityFactory;
    private Engine engine;
    private Random random = new Random(System.currentTimeMillis());

    public SquadronFactory(TweenManager tweenManager, EntityFactory entityFactory, Engine engine) {
        this.tweenManager = tweenManager;
        this.entityFactory = entityFactory;
        this.engine = engine;
    }

    public void createSquadron(int shipType, int squadronType, float velocity, int number, float posX, float posY, boolean powerUp) {
        Entity squadron = entityFactory.createSquadron();

        Entity[] entities = new Entity[number];
        for (int i = 0; i < number; ++i) {
            Entity ship = null;
            switch (shipType) {
                case SOUCOUPE:
                    ship = entityFactory.createEnemySoucoupe(squadron, random.nextBoolean());
                    break;
                case SHIP:
                    ship = entityFactory.createEnemyShip(squadron, random.nextBoolean());
                    break;
            }
            entities[i] = ship;
        }
        formSquadron(entities, squadronType, velocity, posX, posY);
        Mappers.squadron.get(squadron).addEntities(entities);
    }

    private void formSquadron(Entity[] entities, int squadronType, float velocity, float posX, float posY) {
        switch (squadronType) {
            case LINEAR_Y:
                createLinearYSquadron(entities, velocity, posX, posY);
                break;
            case LINEAR_X:
                createLinearXSquadron(entities, velocity, posX, posY);
                break;
            case SEMI_CIRCLE:
                createSemiCircleSquadron(entities, velocity, posX, posY);
                break;
            case BEZIER_SPLINE:
                createBezierSplineSquadron(entities, velocity);
        }
    }

    private void createBezierSplineSquadron(Entity[] entities, float velocity) {
        int k = 100;
        Bezier<Vector2> catmull = new Bezier<Vector2>(
                new Vector2(0f, SCREEN_HEIGHT),
                new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT),
                new Vector2(SCREEN_WIDTH, 0f),
                new Vector2(0f, 0f)
        );
        Vector2[] points = new Vector2[k];
        for (int i = 0; i < k; ++i) {
            points[i] = new Vector2();
            catmull.valueAt(points[i], ((float) i) / ((float) k - 1));
        }
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent position = Mappers.position.get(entity);
            position.setPosition(0f, SCREEN_HEIGHT + i * Mappers.sprite.get(entity).sprite.getHeight());
            Timeline timeline = Timeline.createSequence();
            timeline.push(Tween.to(position, PositionComponentAccessor.POSITION_XY, points[0].dst(position.x, position.y) / velocity)
                    .ease(Linear.INOUT)
                    .target(points[0].x, points[0].y));
            for (int j = 1; j < k; ++j) {
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

    private void removeEntitySquadron(Entity entity) {
        EnemyComponent enemyComponent = Mappers.enemy.get(entity);
        if (enemyComponent != null && enemyComponent.squadron != null) {
            Mappers.squadron.get(enemyComponent.squadron).powerUpAfterDestruction = false;
            Mappers.squadron.get(enemyComponent.squadron).ships.remove(entity);
        }
        engine.removeEntity(entity);
    }


    private void createLinearXSquadron(final Entity[] entities, float velocity, float posX, float posY) {
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent position = Mappers.position.get(entity);
            position.setPosition(posX - i * Mappers.sprite.get(entity).sprite.getWidth(), posY);
            Timeline.createSequence()
                    .push(Tween.to(position, PositionComponentAccessor.POSITION_X, 2 * SCREEN_WIDTH / velocity)
                            .ease(Linear.INOUT)
                            .targetRelative(2f * SCREEN_WIDTH))
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

    private void createLinearYSquadron(Entity[] entities, float velocity, float posX, float posY) {
        for (int i = 0; i < entities.length; ++i) {
            final Entity entity = entities[i];
            PositionComponent position = Mappers.position.get(entity);
            position.setPosition(posX, posY + i * Mappers.sprite.get(entity).sprite.getHeight());
            Timeline.createSequence()
                    .push(Tween.to(position, PositionComponentAccessor.POSITION_Y, 2 * SCREEN_HEIGHT / velocity)
                            .ease(Linear.INOUT)
                            .targetRelative(-2f * SCREEN_HEIGHT))
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
