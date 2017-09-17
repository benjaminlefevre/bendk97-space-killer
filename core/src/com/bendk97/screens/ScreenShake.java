package com.bendk97.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.RandomXS128;
import com.bendk97.Settings;
import com.bendk97.tweens.CameraTween;

import java.util.Random;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class ScreenShake {

    public static final float STEP_INTERVAL = 0.02f;

    private OrthographicCamera camera;

    private TweenManager tweenManager;

    private Random random = new RandomXS128();

    public ScreenShake(TweenManager tweenManager, OrthographicCamera camera) {
        this.tweenManager = tweenManager;
        this.camera = camera;
    }

    public void shake(float strength, final float duration, boolean vibration) {
        final int STEPS = Math.round(duration / STEP_INTERVAL);
        final float STRENGTH_STEP = strength / STEPS;
        tweenManager.killTarget(camera);
        if (vibration && Settings.isVibrationEnabled()) {
            Gdx.input.vibrate(new long[]{0, 100, 0, 100, 0, 100, 0, 100, 0}, -1);
        }
        camera.zoom = 1;
        for (int step = 0; step < STEPS; ++step) {
            double angle = Math.toRadians(random.nextFloat() * 360f);
            float x = (float) Math.floor(SCREEN_WIDTH / 2f + strength *
                    Math.cos(angle));
            float y = (float) Math.floor(SCREEN_HEIGHT / 2f + strength * Math.sin(angle));
            Tween.to(camera, CameraTween.X, STEP_INTERVAL).delay(step * STEP_INTERVAL).target(x).ease(Linear.INOUT).start(tweenManager);
            Tween.to(camera, CameraTween.Y, STEP_INTERVAL).delay(step * STEP_INTERVAL).target(y).ease(Linear.INOUT).start(tweenManager);
            strength -= STRENGTH_STEP;
        }
    }
}
