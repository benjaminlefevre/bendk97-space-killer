/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Quad;
import com.badlogic.ashley.core.Entity;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.Mappers;

import java.util.LinkedList;
import java.util.List;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.Assets.*;
import static com.bendk97.entities.EntityFactory.BOSS_LEVEL_1;
import static com.bendk97.entities.SquadronFactory.BOSS_MOVE;
import static com.bendk97.entities.SquadronFactory.LINEAR_Y;
import static com.bendk97.screens.LevelScreen.Level.Level1;
import static com.bendk97.tweens.VelocityComponentAccessor.VELOCITY_Y;

public class Level1Screen extends com.bendk97.screens.LevelScreen {
    private LinkedList<ScriptItem> scriptItemsEasy;
    private LinkedList<ScriptItem> scriptItemsMediumLeft;
    private LinkedList<ScriptItem> scriptItemsMediumRight;
    private LinkedList<ScriptItem> scriptItemsHardLeft;
    private LinkedList<ScriptItem> scriptItemsHardRight;
    private ScriptItem boss;
    private Entity background;
    private Entity background2;

    public Level1Screen(final Assets assets, SpaceKillerGame game) {
        super(assets, game, Level1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                spriteMaskFactory.addMask(assets.get(GFX_LEVEL1_ATLAS_MASK).getTextures().first());
            }
        }).start();
        playMusic(MUSIC_LEVEL_1, 0.3f);
        background = entityFactory.createBackground(assets.get(GFX_BGD_LEVEL1), -BGD_VELOCITY);
        background2 = entityFactory.createBackground(assets.get(GFX_BGD_STARS), -BGD_PARALLAX_VELOCITY);
        startLevel(-3);
    }

    @Override
    protected void initSpawns() {
        scriptItemsEasy = new LinkedList<ScriptItem>(randomEasySpawnEnemies(13));
        scriptItemsMediumLeft = new LinkedList<ScriptItem>(randomMediumSpawnEnemiesComingFromLeft(12));
        scriptItemsMediumRight = new LinkedList<ScriptItem>(randomMediumSpawnEnemiesComingFromRight(12));
        scriptItemsHardLeft = new LinkedList<ScriptItem>(randomHardSpawnEnemiesComingFromLeft(12));
        scriptItemsHardRight = new LinkedList<ScriptItem>(randomHardSpawnEnemiesComingFromRight(12));
        boss = new ScriptItem(BOSS_LEVEL_1, BOSS_MOVE, 75f, 1, false, true, 10000,
                ENEMY_BULLET_EASY_VELOCITY);
    }


    @Override
    protected void script(int second) {
        super.script(second);
        if (second % 3 == 0 || second % 7 == 0) {
            new ScriptItem(getRandomAsteroidType(), LINEAR_Y,
                    40f + random.nextFloat() * 160f,
                    1, random.nextInt() % 6 == 0, false, 0, 0f,
                    random.nextFloat() * (SCREEN_WIDTH - 36f),
                    SCREEN_HEIGHT).execute();
        }
        if (second % 30 == 0) {
            entityFactory.createForeground(getRandomMist(), BGD_VELOCITY_FORE);
        }

        if (second < 0) {
            return;
        }
        // 13 elements
        if (second <= 60 && second % 5 == 0) {
            scriptItemsEasy.poll().execute();
            // 18 elements
        } else if (second <= 120 && (second % 5 == 0)) {
            if (second == 65) {
                assets.playSound(SOUND_GO);
            }
            boolean left = random.nextBoolean();
            LinkedList<ScriptItem> medium = left ? scriptItemsMediumLeft : scriptItemsMediumRight;
            LinkedList<ScriptItem> mediumOther = left ? scriptItemsMediumRight : scriptItemsMediumLeft;

            medium.poll().execute();
            if (second % 10 == 0) {
                mediumOther.poll().execute();
            }
        } else if (second <= 180 && (second % 5 == 0)) {
            if (second == 125) {
                assets.playSound(SOUND_GO);
            }
            boolean left = random.nextBoolean();
            LinkedList<ScriptItem> hard = left ? scriptItemsHardLeft : scriptItemsHardRight;
            LinkedList<ScriptItem> hardOther = left ? scriptItemsHardRight : scriptItemsHardLeft;

            hard.poll().execute();
            if (second % 10 == 0) {
                hardOther.poll().execute();
            }
        } else if (second >= 185 && player.getComponent(GameOverComponent.class) == null) {
            switch (second) {
                case 185:
                    assets.playSound(SOUND_BOSS_ALERT);
                    Tween.to(Mappers.velocity.get(background), VELOCITY_Y, 4).ease(Quad.IN)
                            .target(50f).start(tweenManager);
                    Tween.to(Mappers.velocity.get(background2), VELOCITY_Y, 4).ease(Quad.IN)
                            .target(20f).start(tweenManager);

                    break;
                case 189:
                    assets.stopMusic(MUSIC_LEVEL_1);
                    playMusic(MUSIC_LEVEL_1_BOSS, 1f);
                    boss.execute();
                    break;
            }
        }

    }

    private List<ScriptItem> randomEasySpawnEnemies(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_EASY, STANDARD_RATE_SHOOT, ENEMY_BULLET_EASY_VELOCITY, BONUS_SQUADRON_EASY, 5, 10, null);

    }

    private List<ScriptItem> randomMediumSpawnEnemiesComingFromLeft(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_MEDIUM, STANDARD_RATE_SHOOT, ENEMY_BULLET_MEDIUM_VELOCITY, BONUS_SQUADRON_MEDIUM, 5, 12, true);

    }

    private List<ScriptItem> randomMediumSpawnEnemiesComingFromRight(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_MEDIUM, STANDARD_RATE_SHOOT, ENEMY_BULLET_MEDIUM_VELOCITY, BONUS_SQUADRON_MEDIUM, 5, 12, false);

    }

    private List<ScriptItem> randomHardSpawnEnemiesComingFromLeft(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_HARD, STANDARD_RATE_SHOOT, ENEMY_BULLET_HARD_VELOCITY, BONUS_SQUADRON_HARD, 7, 15, true);

    }

    private List<ScriptItem> randomHardSpawnEnemiesComingFromRight(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_HARD, STANDARD_RATE_SHOOT, ENEMY_BULLET_HARD_VELOCITY, BONUS_SQUADRON_HARD, 7, 15, false);

    }
}
