/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.listeners.impl;

import aurelienribon.tweenengine.*;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.SnapshotArray;
import com.bendk97.Settings;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;
import com.bendk97.components.EnemyComponent;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.Mappers;
import com.bendk97.components.PlayerComponent;
import com.bendk97.entities.EntityFactory;
import com.bendk97.listeners.PlayerListener;
import com.bendk97.screens.LevelScreen;
import com.bendk97.screens.ScreenShake;
import com.bendk97.timer.PausableTimer;

import static com.bendk97.SpaceKillerGameConstants.PLAYER_ORIGIN_X;
import static com.bendk97.SpaceKillerGameConstants.PLAYER_ORIGIN_Y;
import static com.bendk97.assets.Assets.*;
import static com.bendk97.tweens.SpriteComponentAccessor.ALPHA;


public class PlayerListenerImpl extends EntitySystem implements PlayerListener {
    private final com.bendk97.entities.EntityFactory entityFactory;
    private SnapshotArray<Entity> lives;
    private SnapshotArray<Entity> bombs;
    private final TweenManager tweenManager;
    private final Assets assets;
    private final LevelScreen screen;
    private final SpaceKillerGame game;
    private final com.bendk97.screens.ScreenShake screenShake;


    public PlayerListenerImpl(SpaceKillerGame game, Assets asset, EntityFactory entityFactory, SnapshotArray<Entity> lives,
                              SnapshotArray<Entity> bombs, TweenManager tweenManager, ScreenShake screenShake,
                              LevelScreen screen) {
        this.entityFactory = entityFactory;
        this.game = game;
        this.lives = lives;
        this.bombs = bombs;
        this.tweenManager = tweenManager;
        this.assets = asset;
        this.screen = screen;
        this.screenShake = screenShake;
    }

    @Override
    public void dropBomb() {
        getEngine().removeEntity(bombs.removeIndex(bombs.size - 1));
    }

    @Override
    public void newBombObtained(Entity player) {
        Mappers.player.get(player).bombs++;
        Entity[] bombsArray = bombs.begin();
        for (int i = 0; i < bombs.size; ++i) {
            getEngine().removeEntity(bombsArray[i]);
        }
        bombs.end();
        this.bombs = entityFactory.createEntityPlayerBombs(player);

    }

    public void updateLivesAndBombsAfterContinue(Entity player) {
        Entity[] livesArray = this.lives.begin();
        for (int i = 0; i < lives.size; ++i) {
            getEngine().removeEntity(livesArray[i]);
        }
        this.lives.end();
        Entity[] bombsArray = this.bombs.begin();
        for (int i = 0; i < bombs.size; ++i) {
            getEngine().removeEntity(bombsArray[i]);
        }
        this.bombs.end();
        this.lives = entityFactory.createEntityPlayerLives(player);
        this.bombs = entityFactory.createEntityPlayerBombs(player);
    }

    @Override
    public void loseLive(final Entity player) {
        PlayerComponent playerComponent = Mappers.player.get(player);
        playerComponent.loseLife();
        screenShake.shake(20, 0.5f, true);
        if (playerComponent.isGameOver()) {
            assets.playSound(SOUND_GAME_OVER);
            Mappers.player.get(player).secondScript = ((LevelScreen) game.currentScreen).getCurrentTimeScript();
            player.add(getEngine().createComponent(GameOverComponent.class));
            Settings.addScore(playerComponent.getScoreInt());
            screen.submitScore(playerComponent.getScoreInt());
            PausableTimer.schedule(new PausableTimer.Task() {
                @Override
                public void run() {
                    Gdx.input.setInputProcessor(screen.getGameOverInputProcessor());
                }
            }, 1);
        } else {
            assets.playSound(SOUND_LOSE_LIFE);
            if (playerComponent.lives > 0) {
                getEngine().removeEntity(lives.removeIndex(playerComponent.lives - 1));
            }
            Mappers.position.get(player).setPosition(PLAYER_ORIGIN_X, PLAYER_ORIGIN_Y);
            Mappers.sprite.get(player).sprite.setPosition(PLAYER_ORIGIN_X, PLAYER_ORIGIN_Y);
            entityFactory.addInvulnerableComponent(player);
            Timeline.createSequence()
                    .push(Tween.to(Mappers.sprite.get(player), ALPHA, 0.2f).target(0f))
                    .push(Tween.to(Mappers.sprite.get(player), ALPHA, 0.2f).target(1f))
                    .repeat(10, 0f)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            if (i == TweenCallback.COMPLETE) {
                                entityFactory.removeInvulnerableComponent(player);
                            }
                        }
                    })
                    .start(tweenManager);
        }
    }

    @Override
    public void updateScore(Entity player, Entity enemy, int nbHits) {
        EnemyComponent enemyComponent = Mappers.enemy.get(enemy);
        int gauge = enemyComponent.getLifeGauge();
        int nbPoints = Math.min(nbHits, gauge) * Mappers.enemy.get(enemy).points;
        updateScore(player, nbPoints);
    }

    @Override
    public void updateScore(Entity player, int score) {
        PlayerComponent playerComponent = Mappers.player.get(player);
        boolean notHighScoreOld = !playerComponent.isHighScore();
        boolean newLife = playerComponent.updateScore(score);
        boolean highScoreNew = playerComponent.isHighScore();
        if (notHighScoreOld && highScoreNew) {
            assets.playSound(SOUND_NEW_HIGH_SCORE);
        }
        if (newLife) {
            assets.playSound(SOUND_NEW_LIFE);
            Entity[] livesArray = lives.begin();
            for (int i = 0; i < lives.size; ++i) {
                getEngine().removeEntity(livesArray[i]);
            }
            lives.end();
            this.lives = entityFactory.createEntityPlayerLives(player);
        }
    }


}
