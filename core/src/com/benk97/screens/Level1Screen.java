package com.benk97.screens;

import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.benk97.SpaceKillerGame;
import com.benk97.assets.Assets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.*;
import static com.benk97.entities.EntityFactory.BOSS_LEVEL_1;
import static com.benk97.entities.SquadronFactory.*;
import static com.benk97.google.Achievement.KILL_BOSS;

public class Level1Screen extends LevelScreen {

    private float time = 0f;
    private Random random = new RandomXS128();


    private LinkedList<ScriptItem> scriptItemsEasy;
    private LinkedList<ScriptItem> scriptItemsMediumLeft;
    private LinkedList<ScriptItem> scriptItemsMediumRight;
    private LinkedList<ScriptItem> scriptItemsHardLeft;
    private LinkedList<ScriptItem> scriptItemsHardRight;
    private ScriptItem boss;

    public Level1Screen(Assets assets, SpaceKillerGame game) {
        super(assets, game);
        assets.playMusic(MUSIC_LEVEL_1);
        entityFactory.createBackground(assets.get(GFX_BGD_LEVEL1), -BGD_VELOCITY);
        entityFactory.createBackground(assets.get(GFX_BGD_STARS), -BGD_PARALLAX_VELOCITY);
        initSpawns();
    }

    private void initSpawns() {
        scriptItemsEasy = new LinkedList<ScriptItem>(randomEasySpawnEnemies(13));
        scriptItemsMediumLeft = new LinkedList<ScriptItem>(randomMediumSpawnEnemiesComingFromLeft(12));
        scriptItemsMediumRight = new LinkedList<ScriptItem>(randomMediumSpawnEnemiesComingFromRight(12));
        scriptItemsHardLeft = new LinkedList<ScriptItem>(randomHardSpawnEnemiesComingFromLeft(12));
        scriptItemsHardRight = new LinkedList<ScriptItem>(randomHardSpawnEnemiesComingFromRight(12));
        boss = new ScriptItem(BOSS_LEVEL_1, BOSS_MOVE, 75f, 1, false, true, 10000,
                ENEMY_BULLET_EASY_VELOCITY);
    }


    @Override
    public void render(float delta) {
        updateScript(delta);
        super.render(delta);
    }

    private void updateScript(float delta) {
        int timeBefore = (int) Math.floor(time);
        time += delta;
        int newTime = (int) Math.floor(time);
        if (newTime > timeBefore) {
            script(newTime);
        }
    }

    private void script(int second) {
        if (second % 3 == 0 || second % 7 == 0) {
            new ScriptItem(getRandomAsteroidType(), LINEAR_Y,
                    40f + random.nextFloat() * 160f,
                    1, random.nextInt() % 4 == 0, false, 0, 0f,
                    random.nextFloat() * SCREEN_WIDTH,
                    SCREEN_HEIGHT - 10f).execute();
        }

        // 13 elements
        if (second <= 60 && (second == 2 || second % 5 == 0)) {
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
        } else if (second >= 185) {
            switch (second) {
                case 185:
                    assets.playSound(SOUND_BOSS_ALERT);
                    break;
                case 189:
                    assets.stopMusic(MUSIC_LEVEL_1);
                    assets.playMusic(MUSIC_LEVEL_1_BOSS);
                    boss.execute();
                    break;
            }
        }

    }

    public void restartLevel1() {
        game.playServices.unlockAchievement(KILL_BOSS);
        time = 0f;
        initSpawns();
        assets.playMusic(MUSIC_LEVEL_1);
        assets.stopMusic(MUSIC_LEVEL_1_BOSS);
    }

    private List<ScriptItem> randomEasySpawnEnemies(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_EASY, ENEMY_BULLET_EASY_VELOCITY, BONUS_SQUADRON_EASY, 5, 10, random.nextBoolean());

    }

    private List<ScriptItem> randomMediumSpawnEnemiesComingFromLeft(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_MEDIUM, ENEMY_BULLET_MEDIUM_VELOCITY, BONUS_SQUADRON_MEDIUM, 5, 12, true);

    }

    private List<ScriptItem> randomMediumSpawnEnemiesComingFromRight(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_MEDIUM, ENEMY_BULLET_MEDIUM_VELOCITY, BONUS_SQUADRON_MEDIUM, 5, 12, false);

    }

    private List<ScriptItem> randomHardSpawnEnemiesComingFromLeft(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_HARD, ENEMY_BULLET_HARD_VELOCITY, BONUS_SQUADRON_HARD, 7, 15, true);

    }

    private List<ScriptItem> randomHardSpawnEnemiesComingFromRight(int nbSpawns) {
        return randomSpawnEnemies(nbSpawns, ENEMY_VELOCITY_HARD, ENEMY_BULLET_HARD_VELOCITY, BONUS_SQUADRON_HARD, 7, 15, false);

    }

    private List<ScriptItem> randomSpawnEnemies(int nbSpawns, float velocity, float bulletVelocity, int bonus, int minEnemies, int maxEnemies, boolean comingFromLeft) {
        List<ScriptItem> list = new ArrayList<ScriptItem>(nbSpawns);
        for (int i = 0; i < nbSpawns; ++i) {
            int randomMoveType = getRandomMoveType();
            int number = randomMoveType == ARROW_DOWN || randomMoveType == ARROW_UP ? 7 : minEnemies + random.nextInt(maxEnemies - minEnemies + 1);
            list.add(
                    new ScriptItem(
                            getRandomShipType(),
                            randomMoveType,
                            velocity,
                            number,
                            false, true,
                            number * bonus,
                            bulletVelocity,
                            getRandomMoveParams(randomMoveType, comingFromLeft)
                    ));
        }
        return list;
    }

    private Object[] getRandomMoveParams(int randomMoveType, boolean comingFromLeft) {
        float direction = comingFromLeft ? 1f : -1f;
        int leftOrRight = comingFromLeft ? 0 : 1;
        switch (randomMoveType) {
            case ARROW_DOWN:
            case ARROW_UP:
                return null;
            case LINEAR_X:
                return new Object[]{-direction * SHIP_WIDTH + leftOrRight * SCREEN_WIDTH, 2f / 3f * SCREEN_HEIGHT + random.nextFloat() * SCREEN_HEIGHT / 12f, direction};
            case LINEAR_Y:
                return new Object[]{1f / 5f * SCREEN_WIDTH + random.nextFloat() * 3 * SCREEN_WIDTH / 5f, (float) SCREEN_HEIGHT};
            case LINEAR_XY:
                return new Object[]{0f + leftOrRight * SCREEN_WIDTH, (float) SCREEN_HEIGHT, (float) SCREEN_WIDTH - leftOrRight * SCREEN_WIDTH, 0f};
            case SEMI_CIRCLE:
                return new Object[]{0f, (float) SCREEN_HEIGHT};
            case BEZIER_SPLINE:
                if (random.nextBoolean()) {
                    return new Object[]{
                            new Vector2(0f + leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                            new Vector2((float) SCREEN_WIDTH - leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                            new Vector2((float) SCREEN_WIDTH - leftOrRight * SCREEN_WIDTH, 0f),
                            new Vector2(0f + leftOrRight * SCREEN_WIDTH, 0f)};
                } else {
                    return new Object[]{
                            new Vector2(-SHIP_WIDTH + leftOrRight * (SCREEN_WIDTH + SHIP_WIDTH), SCREEN_HEIGHT / 2f),
                            new Vector2(0f + leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                            new Vector2((float) SCREEN_WIDTH - leftOrRight * (SCREEN_WIDTH + 2 * SHIP_WIDTH), SCREEN_HEIGHT),
                            new Vector2((float) SCREEN_WIDTH - leftOrRight * (SCREEN_WIDTH + 6 * SHIP_WIDTH), SCREEN_HEIGHT / 2f)};
                }
            case CATMULL_ROM_SPLINE:
                return new Object[]{
                        new Vector2(0f + leftOrRight * SCREEN_WIDTH, SCREEN_HEIGHT),
                        new Vector2(SCREEN_WIDTH * (0.8f - leftOrRight * 0.6f), 3 * SCREEN_HEIGHT / 4f),
                        new Vector2(SCREEN_WIDTH * (0.2f + leftOrRight * 0.6f), 2 * SCREEN_HEIGHT / 4f),
                        new Vector2(SCREEN_WIDTH * (0.8f - leftOrRight * 0.6f), SCREEN_HEIGHT / 4f),
                        new Vector2(SCREEN_WIDTH * (0.2f + leftOrRight * 0.6f), -SCREEN_HEIGHT / 4f),
                        new Vector2(SCREEN_WIDTH * (0.8f - leftOrRight * 0.6f), -2 * SCREEN_HEIGHT / 4f)
                };


        }
        return null;
    }

    public int getRandomShipType() {
        return random.nextInt(4);
    }

    public int getRandomAsteroidType() {
        return 999 + random.nextInt(2);
    }

    public int getRandomMoveType() {
        return random.nextInt(8);
    }

    class ScriptItem {
        int typeShip;
        int typeSquadron;
        float velocity;
        int number;
        Object[] params;
        boolean powerUp;
        boolean displayBonus;
        int bonus;
        float bulletVelocity;

        public ScriptItem(int typeShip, int typeSquadron, float velocity, int number, boolean powerUp, boolean displayBonus, int bonus, float velocityBullet, Object... params) {
            this.typeShip = typeShip;
            this.typeSquadron = typeSquadron;
            this.velocity = velocity;
            this.number = number;
            this.params = params;
            this.powerUp = powerUp;
            this.bonus = bonus;
            this.displayBonus = displayBonus;
            this.bulletVelocity = velocityBullet;
        }

        public void execute() {
            squadronFactory.createSquadron(typeShip, typeSquadron, velocity, number, powerUp, displayBonus, bonus, bulletVelocity, params);
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        assets.unloadResources(this.getClass());
    }
}
