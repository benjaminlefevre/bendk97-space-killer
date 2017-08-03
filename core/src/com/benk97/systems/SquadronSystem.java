package com.benk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.RandomXS128;
import com.benk97.components.Mappers;
import com.benk97.components.SquadronComponent;
import com.benk97.entities.EntityFactory;
import com.benk97.listeners.PlayerListener;
import com.benk97.timer.PausableTimer;

import java.util.Random;

public class SquadronSystem extends IteratingSystem {

    private EntityFactory entityFactory;
    private Entity player;
    private PlayerListener playerListener;

    public SquadronSystem(int priority, EntityFactory entityFactory, Entity player, PlayerListener playerListener) {
        super(Family.all(SquadronComponent.class).get(), priority);
        this.entityFactory = entityFactory;
        this.player = player;
        this.playerListener = playerListener;
    }

    private Random random = new RandomXS128();

    @Override
    public void processEntity(final Entity entity, float deltaTime) {
        SquadronComponent squadron = Mappers.squadron.get(entity);
        if (squadron.ships.size == 0) {
            getEngine().removeEntity(entity);
            if (squadron.toShoot == 0) {
                if (squadron.powerUpAfterDestruction) {
                    int type = random.nextInt(22);
                    if (type < 17) {
                        entityFactory.createPowerUp(entity);
                    } else if (type < 21) {
                        entityFactory.createShieldUp(entity);
                    } else {
                        entityFactory.createBombUp(entity);
                    }
                }
                if (squadron.displayBonusSquadron) {
                    playerListener.updateScore(player, squadron.scoreBonus);
                    final Entity score = entityFactory.createScoreSquadron(entity);
                    PausableTimer.schedule(new PausableTimer.Task() {
                        @Override
                        public void run() {
                            getEngine().removeEntity(score);
                        }
                    }, 1.5f);
                }
            }
        }
    }
}