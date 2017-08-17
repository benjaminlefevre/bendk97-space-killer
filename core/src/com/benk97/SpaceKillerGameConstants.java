package com.benk97;

public class SpaceKillerGameConstants {
    // general settings
    public final static String GAME_VERSION = "v1.0.8";
    public final static float SCREEN_WIDTH = 400;
    public final static float SCREEN_HEIGHT = 700;
    public final static boolean DEBUG = false;
    public final static boolean NO_GOOGLE = false;
    public final static boolean SKIP_SPLASH = false;
    public final static boolean AD_TEST = false;
    // player
    public final static int LIVES = 4;
    public final static int EXTRA_LIVES_ADS = 1;
    public final static int BOMBS = 3;
    public final static int HIT_EXPLOSION = 50;
    public static final float NEW_LIFE = 100000f;
    public final static float PLAYER_ORIGIN_X = SCREEN_WIDTH / 2 - 16f;
    public final static float PLAYER_ORIGIN_Y = 0f;
    public final static long FIRE_DELAY = 150;
    public final static long FIRE_DELAY_FAST = 100;
    public final static long FIRE_DELAY_VERY_FAST = 50;
    public final static long FIRE_DELAY_SIDE = 600;
    public final static long FIRE_DELAY_SIDE_FAST = 400;
    public final static long FIRE_DELAY_SIDE_VERY_FAST = 200;
    public final static float PLAYER_VELOCITY = 300.0f;
    public final static float PLAYER_BULLET_VELOCITY = 400.0f;

    // enemies level 1
    public final static int BOSS_LEVEL1_GAUGE = 300;
    public final static float SHIP_WIDTH = 5f;
    public final static float ENEMY_VELOCITY_EASY = 200f;
    public final static float ENEMY_VELOCITY_MEDIUM = 250f;
    public final static float ENEMY_VELOCITY_HARD = 300f;
    public final static int BONUS_SQUADRON_EASY = 100;
    public final static int BONUS_SQUADRON_MEDIUM = 150;
    public final static int BONUS_SQUADRON_HARD = 200;

    public final static float ENEMY_BULLET_EASY_VELOCITY = 250.0f;
    public final static float ENEMY_BULLET_MEDIUM_VELOCITY = 300.0f;
    public final static float ENEMY_BULLET_HARD_VELOCITY = 450.0f;

    public final static int STANDARD_RATE_SHOOT = 150;
    public final static int MEDIUM_RATE_SHOOT = 130;

    // enemies level 2
    public final static int BOSS_LEVEL2_GAUGE = 400;
    public final static float ENEMY_LEVEL2_VELOCITY_EASY = 225f;
    public final static float ENEMY_LEVEL2_VELOCITY_MEDIUM = 275f;
    public final static float ENEMY_LEVEL2_VELOCITY_HARD = 300f;
    public final static int BONUS_LEVEL2_SQUADRON_EASY = 150;
    public final static int BONUS_LEVEL2_SQUADRON_MEDIUM = 200;
    public final static int BONUS_LEVEL2_SQUADRON_HARD = 250;
    public final static float STATIC_ENEMY_VELOCITY = 200f;
    public final static float STATIC_ENEMY_BULLET_VELOCITY = 300f;
    public final static int STATIC_ENEMY_RATE_SHOOT = 100;
    public final static int STATIC_ENEMY_RATE_SHOOT_WHEN_TWICE = 150;

    public final static float ENEMY_LEVEL2_BULLET_EASY_VELOCITY = 300.0f;
    public final static float ENEMY_LEVEL2_BULLET_MEDIUM_VELOCITY = 350.0f;
    public final static float ENEMY_LEVEL2_BULLET_HARD_VELOCITY = 450.0f;

    // enemies level 3
    public final static int BOSS_LEVEL3_GAUGE = 400;
    public final static float ENEMY_LEVEL3_VELOCITY_EASY = 250f;
    public final static float ENEMY_LEVEL3_VELOCITY_MEDIUM = 300f;
    public final static float ENEMY_LEVEL3_VELOCITY_HARD = 325f;
    public final static int BONUS_LEVEL3_SQUADRON_EASY = 150;
    public final static int BONUS_LEVEL3_SQUADRON_MEDIUM = 200;
    public final static int BONUS_LEVEL3_SQUADRON_HARD = 250;
    public final static float STATIC_ENEMY_LEVEL3_VELOCITY = 250f;
    public final static float STATIC_ENEMY_LEVEL3_BULLET_VELOCITY = 325f;
    public final static int STATIC_ENEMY_LEVEL3_RATE_SHOOT = 75;
    public final static int STATIC_ENEMY_LEVEL3_RATE_SHOOT_WHEN_TWICE = 125;

    public final static float ENEMY_LEVEL3_BULLET_EASY_VELOCITY = 310.0f;
    public final static float ENEMY_LEVEL3_BULLET_MEDIUM_VELOCITY = 360.0f;
    public final static float ENEMY_LEVEL3_BULLET_HARD_VELOCITY = 460.0f;


    // backgrounds
    public final static float BGD_PARALLAX_VELOCITY = 100.0f;
    public final static float BGD_VELOCITY = 500.0f;
    public final static float BGD_VELOCITY_FORE = 300.0f;
    public static final float BGD_VELOCIY_LEVEL3 = 150f;
    // animated sprites
    public static final float FRAME_DURATION = 0.05f;
    public static final float FRAME_DURATION_ENEMY_3 = 0.07f;
    public static final float FRAME_DURATION_EXPLOSION = 0.01f;
    public static final float FRAME_DURATION_BOMB_EXPLOSION = 0.1f;
    public static final float FRAME_DURATION_POWERUP = 0.5f;
    public static final int GO_LEFT = 1;
    public static final int GO_RIGHT = 2;
    public static final int ANIMATION_MAIN = 0;

    // virtual pad
    public static final float PAD_X = 0f;
    public static final float PAD_Y = 40f;
    public static final float FIRE_X = 275f;
    public static final float FIRE_Y = 90f;
    public static final float BOMB_X = 330f;
    public static final float BOMB_Y = 180f;
    public static final float BOMB_Y_VIRTUAL = 30f;

    // icon game over
    public static final float ICON_SIZE = 64f;
    public static final float EXTRA_X = 25f;
    public static final float EXTRA_Y = 40f;
    public static final float PLAY_X = 100f;
    public static final float PLAY_Y = 40f;
    public static final float HOME_X = 175f;
    public static final float HOME_Y = 40f;
    public static final float SHARE_X = 250f;
    public static final float SHARE_Y = 40f;


    // SCORE LIVES HIGH
    public static final float SCORE_X = 20f;
    public static final float SCORE_Y = SCREEN_HEIGHT - 10f;
    public static final float LIVES_X = SCREEN_WIDTH / 2f - 50f;
    public static final float LIVES_Y = SCREEN_HEIGHT - 10f;
    public static final float HIGH_X = SCREEN_WIDTH - 100f;
    public static final float HIGH_Y = SCREEN_HEIGHT - 10f;
    public static final float BOMB_STOCK_X = SCREEN_WIDTH - 22f;
    public static final float BOMB_STOCK_Y = 10f;

}
