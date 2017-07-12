package com.benk97.entities;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.benk97.components.Mappers;
import com.benk97.components.PositionComponent;
import com.benk97.tweens.PositionComponentAccessor;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class SquadronFactory {
    public final static int SOUCOUPE = 0;
    public final static int LINEAR_X = 0;
    public final static int LINEAR_Y = 1;


    private TweenManager tweenManager;
    private EntityFactory entityFactory;
    private Engine engine;

    public SquadronFactory(TweenManager tweenManager, EntityFactory entityFactory, Engine engine) {
        this.tweenManager = tweenManager;
        this.entityFactory = entityFactory;
        this.engine = engine;
    }

    public void createSquadron(int shipType, int squadronType, float velocity, int number, float posX, float posY) {
        Entity[] entities = new Entity[number];
        for (int i = 0; i < number; ++i) {
            Entity ship = entityFactory.createEnnemySoucoupe();
            entities[i] = ship;
        }
        formSquadron(entities, squadronType, velocity, posX, posY);
    }

    private void formSquadron(Entity[] entities, int squadronType, float velocity, float posX, float posY) {
        switch (squadronType) {
            case LINEAR_Y:
                createLinearYSquadron(entities, velocity, posX, posY);
                break;
            case LINEAR_X:
                createLinearXSquadron(entities, velocity, posX, posY);
                break;
        }
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
                            if(i==TweenCallback.COMPLETE){
                                engine.removeEntity(entity);
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
                            if(i==TweenCallback.COMPLETE){
                                engine.removeEntity(entity);
                            }
                        }
                    })
                    .start(tweenManager);
        }
    }
}
