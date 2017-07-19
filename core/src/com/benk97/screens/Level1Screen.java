package com.benk97.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.benk97.SpaceKillerGame;
import com.benk97.assets.Assets;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.*;
import static com.benk97.entities.SquadronFactory.*;

public class Level1Screen extends LevelScreen {

    private float time = 0f;
    private Random random = new Random(System.currentTimeMillis());

    public Level1Screen(Assets assets, SpaceKillerGame game) {
        super(assets, game);
        assets.playMusic(MUSIC_LEVEL_1);
        entityFactory.createBackground(assets.get(GFX_BGD_LEVEL1), -BGD_VELOCITY);
        entityFactory.createBackground(assets.get(GFX_BGD_STARS), -BGD_PARALLAX_VELOCITY);
        Collections.shuffle(scriptItemsEasy, random);
        scriptItemsMedium1.addAll(scriptItemsMedium1);
        Collections.shuffle(scriptItemsMedium1, random);
        Collections.shuffle(scriptItemsMedium2, random);
    }


    @Override
    public void render(float delta) {
        updateScript(delta);
        super.render(delta);
    }

    private void updateScript(float delta) {
        int timeBefore = (int) Math.ceil(time);
        time += delta;
        int newTime = (int) Math.ceil(time);
        if (newTime > timeBefore) {
            script(newTime);
        }
    }

    private void script(int second) {
        if (second % 3 == 0 || second % 7 == 0) {
            new ScriptItem(ASTEROID, LINEAR_Y,
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
            scriptItemsMedium1.poll().execute();
            if (second % 10 == 0) {
                scriptItemsMedium2.poll().execute();
            }
        } else if (second <= 180) {
            switch (second) {
                case 125:
                case 126:
                case 130:
                case 131:
                case 135:
                case 140:
                case 145:
                case 150:
                case 151:
                case 155:
                case 156:
                    Gdx.app.log("execute", "" + second);
                    scriptItemsHard.poll().execute();
                    break;
            }
        }

    }


    private LinkedList<ScriptItem> scriptItemsEasy = new LinkedList<ScriptItem>(Arrays.asList(
            new ScriptItem(SOUCOUPE, BEZIER_SPLINE, 150f, 5, false, true, 500, ENEMY_BULLET_EASY_VELOCITY,
                    new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT), new Vector2(0f, SCREEN_HEIGHT),
                    new Vector2(0f, 0f), new Vector2(SCREEN_WIDTH, 0f)),
            new ScriptItem(SHIP, BEZIER_SPLINE, 150f, 5, false, true, 500, ENEMY_BULLET_EASY_VELOCITY,
                    new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT), new Vector2(0f, SCREEN_HEIGHT),
                    new Vector2(0f, 0f), new Vector2(SCREEN_WIDTH, 0f)),
            new ScriptItem(SOUCOUPE, BEZIER_SPLINE, 150f, 5, false, true, 500, ENEMY_BULLET_EASY_VELOCITY,
                    new Vector2(0f, SCREEN_HEIGHT), new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT),
                    new Vector2(SCREEN_WIDTH, 0f), new Vector2(0f, 0f)),
            new ScriptItem(SHIP, BEZIER_SPLINE, 150f, 5, false, true, 500, ENEMY_BULLET_EASY_VELOCITY,
                    new Vector2(0f, SCREEN_HEIGHT), new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT),
                    new Vector2(SCREEN_WIDTH, 0f), new Vector2(0f, 0f)),

            new ScriptItem(SOUCOUPE, LINEAR_Y, 150f, 10, false, true, 1000, ENEMY_BULLET_EASY_VELOCITY,
                    (float) SCREEN_WIDTH - SOUCOUPE_WIDTH, (float) SCREEN_HEIGHT),
            new ScriptItem(SHIP, LINEAR_Y, 150f, 10, false, true, 1000, ENEMY_BULLET_EASY_VELOCITY,
                    (float) SCREEN_WIDTH - SHIP_WIDTH, (float) SCREEN_HEIGHT),
            new ScriptItem(SOUCOUPE, LINEAR_Y, 150f, 5, false, true, 500, ENEMY_BULLET_EASY_VELOCITY,
                    0f, (float) SCREEN_HEIGHT),
            new ScriptItem(SHIP, LINEAR_Y, 150f, 5, false, true, 500, ENEMY_BULLET_EASY_VELOCITY,
                    0f, (float) SCREEN_HEIGHT),
            new ScriptItem(SOUCOUPE, LINEAR_X, 150f, 10, false, true, 1000, ENEMY_BULLET_EASY_VELOCITY,
                    -SOUCOUPE_WIDTH, 2 * SCREEN_HEIGHT / 3f, 1f),
            new ScriptItem(SHIP, LINEAR_X, 150f, 10, false, true, 1000, ENEMY_BULLET_EASY_VELOCITY,
                    -SOUCOUPE_WIDTH, 2 * SCREEN_HEIGHT / 3f, 1f),

            new ScriptItem(SOUCOUPE, BEZIER_SPLINE, 150f, 5, false, true, 500, ENEMY_BULLET_EASY_VELOCITY,
                    new Vector2(-SOUCOUPE_WIDTH, SCREEN_HEIGHT / 2f), new Vector2(0f, SCREEN_HEIGHT),
                    new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT), new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT / 2f)),
            new ScriptItem(SHIP, BEZIER_SPLINE, 150f, 5, false, true, 500, ENEMY_BULLET_EASY_VELOCITY,
                    new Vector2(-SHIP_WIDTH, SCREEN_HEIGHT / 2f), new Vector2(0f, SCREEN_HEIGHT),
                    new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT), new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT / 2f)),
            new ScriptItem(SHIP, LINEAR_X, 50f, 10, false, true, 1000, ENEMY_BULLET_EASY_VELOCITY, 0f, SCREEN_HEIGHT - SHIP_WIDTH, 1f)

    )
    );

    private LinkedList<ScriptItem> scriptItemsMedium1 = new LinkedList<ScriptItem>(Arrays.asList(
            new ScriptItem(SHIP, CATMULL_ROM_SPLINE, 200f, 5, false, true, 750, ENEMY_BULLET_MEDIUM_VELOCITY,
                    new Vector2(0f, SCREEN_HEIGHT), new Vector2(SCREEN_WIDTH * 0.8f, 3 * SCREEN_HEIGHT / 4f),
                    new Vector2(SCREEN_WIDTH * 0.2f, 2 * SCREEN_HEIGHT / 4f), new Vector2(SCREEN_WIDTH * 0.8f, SCREEN_HEIGHT / 4f),
                    new Vector2(SCREEN_WIDTH * 0.2f, -SCREEN_HEIGHT / 4f),
                    new Vector2(SCREEN_WIDTH * 0.8f, -2 * SCREEN_HEIGHT / 4f)),
            new ScriptItem(SOUCOUPE, CATMULL_ROM_SPLINE, 200f, 5, false, true, 750, ENEMY_BULLET_MEDIUM_VELOCITY,
                    new Vector2(0f, SCREEN_HEIGHT), new Vector2(SCREEN_WIDTH * 0.8f, 3 * SCREEN_HEIGHT / 4f),
                    new Vector2(SCREEN_WIDTH * 0.2f, 2 * SCREEN_HEIGHT / 4f), new Vector2(SCREEN_WIDTH * 0.8f, SCREEN_HEIGHT / 4f),
                    new Vector2(SCREEN_WIDTH * 0.2f, -SCREEN_HEIGHT / 4f),
                    new Vector2(SCREEN_WIDTH * 0.8f, -2 * SCREEN_HEIGHT / 4f)),
            new ScriptItem(SOUCOUPE, BEZIER_SPLINE, 200f, 10, false, true, 1500, ENEMY_BULLET_MEDIUM_VELOCITY,
                    new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT), new Vector2(0f, SCREEN_HEIGHT),
                    new Vector2(0f, 0f), new Vector2(SCREEN_WIDTH, 0f)),
            new ScriptItem(SOUCOUPE, BEZIER_SPLINE, 200f, 10, false, true, 1500, ENEMY_BULLET_MEDIUM_VELOCITY,
                    new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT), new Vector2(0f, SCREEN_HEIGHT),
                    new Vector2(0f, 0f), new Vector2(SCREEN_WIDTH, 0f)),
            new ScriptItem(SHIP, BEZIER_SPLINE, 200f, 10, false, true, 1500, ENEMY_BULLET_MEDIUM_VELOCITY,
                    new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT), new Vector2(0f, SCREEN_HEIGHT),
                    new Vector2(0f, 0f), new Vector2(SCREEN_WIDTH, 0f)),
            new ScriptItem(SHIP, LINEAR_Y, 200f, 10, false, true, 1500, ENEMY_BULLET_MEDIUM_VELOCITY,
                    (float) SCREEN_WIDTH - SHIP_WIDTH, (float) SCREEN_HEIGHT)
    )
    );

    private LinkedList<ScriptItem> scriptItemsMedium2 = new LinkedList<ScriptItem>(Arrays.asList(
            new ScriptItem(SOUCOUPE, CATMULL_ROM_SPLINE, 200f, 10, false, true, 1500, ENEMY_BULLET_MEDIUM_VELOCITY,
                    new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT), new Vector2(SCREEN_WIDTH * 0.2f, 3 * SCREEN_HEIGHT / 4f),
                    new Vector2(SCREEN_WIDTH * 0.8f, 2 * SCREEN_HEIGHT / 4f), new Vector2(SCREEN_WIDTH * 0.2f, SCREEN_HEIGHT / 4f),
                    new Vector2(SCREEN_WIDTH * 0.8f, -SCREEN_HEIGHT / 4f),
                    new Vector2(SCREEN_WIDTH * 0.2f, -2 * SCREEN_HEIGHT / 4f)),
            new ScriptItem(SOUCOUPE, BEZIER_SPLINE, 200f, 5, false, true, 750, ENEMY_BULLET_MEDIUM_VELOCITY,
                    new Vector2(0f, SCREEN_HEIGHT), new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT),
                    new Vector2(SCREEN_WIDTH, 0f), new Vector2(0f, 0f)),
            new ScriptItem(SHIP, LINEAR_Y, 200f, 10, false, true, 1500, ENEMY_BULLET_MEDIUM_VELOCITY,
                    (float) SCREEN_WIDTH - SHIP_WIDTH, (float) SCREEN_HEIGHT),
            new ScriptItem(SOUCOUPE, LINEAR_Y, 200f, 5, false, true, 750, ENEMY_BULLET_MEDIUM_VELOCITY,
                    SCREEN_WIDTH / 3f, (float) SCREEN_HEIGHT),
            new ScriptItem(SHIP, LINEAR_X, 200f, 10, false, true, 1500, ENEMY_BULLET_MEDIUM_VELOCITY,
                    -SOUCOUPE_WIDTH, 2 * SCREEN_HEIGHT / 3f, 1f),
            new ScriptItem(SHIP, LINEAR_X, 200f, 10, false, true, 1500, ENEMY_BULLET_MEDIUM_VELOCITY,
                    -SHIP_WIDTH, 2 * SCREEN_HEIGHT / 3f, 1f)
    )
    );

    private LinkedList<ScriptItem> scriptItemsHard = new LinkedList<ScriptItem>(
            Arrays.asList(
                    new ScriptItem(SOUCOUPE, BEZIER_SPLINE, 250f, 5, false, true, 1000, ENEMY_BULLET__HARD_VELOCITY,
                            new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT), new Vector2(0f, SCREEN_HEIGHT),
                            new Vector2(0f, 0f), new Vector2(SCREEN_WIDTH, 0f)),
                    new ScriptItem(SOUCOUPE, BEZIER_SPLINE, 250f, 5, false, true, 1000, ENEMY_BULLET__HARD_VELOCITY,
                            new Vector2(0f, SCREEN_HEIGHT), new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT),
                            new Vector2(SCREEN_WIDTH, 0f), new Vector2(0f, 0f)),
                    new ScriptItem(SHIP, LINEAR_XY, 250f, 10, false, true, 1500, ENEMY_BULLET__HARD_VELOCITY,
                            0f, (float) SCREEN_HEIGHT, (float) SCREEN_WIDTH, 0f),
                    new ScriptItem(SHIP, LINEAR_XY, 250f, 10, false, true, 1500, ENEMY_BULLET__HARD_VELOCITY,
                            (float) SCREEN_WIDTH, (float) SCREEN_HEIGHT, 0f, 0f),
                    new ScriptItem(SOUCOUPE, SEMI_CIRCLE, 250f, 10, false, true, 1500, ENEMY_BULLET__HARD_VELOCITY,
                            0f, (float) SCREEN_HEIGHT),
                    new ScriptItem(SOUCOUPE, BEZIER_SPLINE, 250f, 20, false, true, 2000, ENEMY_BULLET__HARD_VELOCITY,
                            new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT), new Vector2(0f, SCREEN_HEIGHT),
                            new Vector2(0f, 0f), new Vector2(SCREEN_WIDTH, 0f)),
                    new ScriptItem(SHIP, BEZIER_SPLINE, 250f, 5, false, true, 1000, ENEMY_BULLET__HARD_VELOCITY,
                            new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT), new Vector2(0f, SCREEN_HEIGHT),
                            new Vector2(0f, 0f), new Vector2(SCREEN_WIDTH, 0f)),
                    new ScriptItem(SHIP, LINEAR_X, 250f, 10, false, true, 1500, ENEMY_BULLET__HARD_VELOCITY,
                            -SHIP_WIDTH, 2 * SCREEN_HEIGHT / 3f, 1f),
                    new ScriptItem(SOUCOUPE, LINEAR_X, 200f, 10, false, true, 1500, ENEMY_BULLET__HARD_VELOCITY,
                            SCREEN_WIDTH + SOUCOUPE_WIDTH, 2 * SCREEN_HEIGHT / 3f - SOUCOUPE_WIDTH / 2f, -1f),
                    new ScriptItem(SHIP, LINEAR_X, 250f, 10, false, true, 1500, ENEMY_BULLET__HARD_VELOCITY,
                            SCREEN_WIDTH + SHIP_WIDTH, 2 * SCREEN_HEIGHT / 3f - 2 * SOUCOUPE_WIDTH, -1f),
                    new ScriptItem(SOUCOUPE, LINEAR_X, 200f, 10, false, true, 1500, ENEMY_BULLET__HARD_VELOCITY,
                            -SOUCOUPE_WIDTH, 2 * SCREEN_HEIGHT / 3f - 3 * SOUCOUPE_WIDTH / 2f, 1f)

            )
    );

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
