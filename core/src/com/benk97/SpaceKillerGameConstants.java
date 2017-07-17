package com.benk97;

public class SpaceKillerGameConstants {
    // general settings
    public final static int SCREEN_WIDTH = 400;
    public final static int SCREEN_HEIGHT = 700;
    public final static boolean DEBUG = true;
    public final static boolean SKIP_SPLASH = true;
    // player
    public final static int LIVES = 3;
    public final static float PLAYER_ORIGIN_X = SCREEN_WIDTH / 2 - 16f;
    public final static float PLAYER_ORIGIN_Y = 0f;
    // enemies
    public final static int SOUCOUPE_WIDTH = 40;
    public final static float ENEMY_BULLET_VELOCITY = 200.0f;
    // velocities
    public final static float PLAYER_VELOCITY = 200.0f;
    public final static float PLAYER_BULLET_VELOCITY = 300.0f;

    // backgrounds
    public final static float BGD_PARALLAX_VELOCITY = 75.0f;
    public final static float BGD_VELOCITY = 300.0f;
    // animated sprites
    public static final float FRAME_DURATION = 0.05f;
    public static final float FRAME_DURATION_EXPLOSION = 0.01f;
    public static final float FRAME_DURATION_POWERUP = 0.5f;
    public static final int GO_LEFT = 1;
    public static final int GO_RIGHT = 2;
    public static final int ANIMATION_MAIN = 0;

    // virtual pad
    public static final float PAD_X = 10f;
    public static final float PAD_Y = 60f;
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
