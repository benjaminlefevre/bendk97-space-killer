package com.benk97;

public class SpaceKillerGameConstants {
    // general settings
    public final static int SCREEN_WIDTH = 400;
    public final static int SCREEN_HEIGHT = 700;
    public final static boolean DEBUG = true;
    public final static boolean SKIP_SPLASH = true;
    public final static boolean AD_TEST = true;
    // player
    public final static int LIVES = 5;
    public final static float PLAYER_ORIGIN_X = SCREEN_WIDTH / 2 - 16f;
    public final static float PLAYER_ORIGIN_Y = 0f;
    public final static long FIRE_DELAY = 150;
    public final static long FIRE_DELAY_FAST = 100;
    public final static long FIRE_DELAY_VERY_FAST = 50;
    public final static float PLAYER_VELOCITY = 300.0f;
    public final static float PLAYER_BULLET_VELOCITY = 400.0f;

    // enemies
    public final static float SOUCOUPE_WIDTH = 40f;
    public final static float SHIP_WIDTH = 50f;
    public final static float ENEMY_VELOCITY_EASY = 200f;
    public final static float ENEMY_VELOCITY_MEDIUM = 250f;
    public final static float ENEMY_VELOCITY_HARD = 300f;
    public final static int BONUS_SQUADRON_EASY = 100;
    public final static int BONUS_SQUADRON_MEDIUM = 150;
    public final static int BONUS_SQUADRON_HARD = 200;

    public final static float ENEMY_BULLET_EASY_VELOCITY = 250.0f;
    public final static float ENEMY_BULLET_MEDIUM_VELOCITY = 300.0f;
    public final static float ENEMY_BULLET_HARD_VELOCITY = 450.0f;

    // backgrounds
    public final static float BGD_PARALLAX_VELOCITY = 100.0f;
    public final static float BGD_VELOCITY = 500.0f;

    // animated sprites
    public static final float FRAME_DURATION = 0.05f;
    public static final float FRAME_DURATION_ENEMY_3 = 0.07f;
    public static final float FRAME_DURATION_EXPLOSION = 0.01f;
    public static final float FRAME_DURATION_POWERUP = 0.5f;
    public static final int GO_LEFT = 1;
    public static final int GO_RIGHT = 2;
    public static final int ANIMATION_MAIN = 0;

    // virtual pad
    public static final float PAD_X = 0f;
    public static final float PAD_Y = 40f;
    public static final float FIRE_X = 275f;
    public static final float FIRE_Y = 90f;

    // SCORE LIVES HIGH
    public static final float SCORE_X = 20f;
    public static final float SCORE_Y = SCREEN_HEIGHT - 10f;
    public static final float LIVES_X = SCREEN_WIDTH / 2f - 50f;
    public static final float LIVES_Y = SCREEN_HEIGHT - 10f;
    public static final float HIGH_X = SCREEN_WIDTH - 100f;
    public static final float HIGH_Y = SCREEN_HEIGHT - 10f;


}
