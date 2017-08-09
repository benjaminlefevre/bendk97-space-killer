package com.benk97.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.RandomXS128;
import com.benk97.tweens.CameraTween;

import java.util.Random;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class ScreenShake {

    public static final float STEP_INTERVAL = 0.01f;

    private Camera camera;

    private TweenManager tweenManager;

    private Random random = new RandomXS128();

    public ScreenShake(TweenManager tweenManager, Camera camera) {
        this.tweenManager = tweenManager;
        this.camera = camera;
    }

    public void shake(float strength, final float duration) {
        final int STEPS = Math.round(duration / STEP_INTERVAL);
        final float STRENGTH_STEP = strength / STEPS;
        tweenManager.killTarget(camera);
        for (int step = 0; step < STEPS; ++step) {
            double angle = Math.toRadians(random.nextFloat() * 360f);
            float x = (float) Math.floor(SCREEN_WIDTH / 2f + strength *
                    Math.cos(angle));
            float y = (float) Math.floor(SCREEN_HEIGHT / 2f + strength * Math.sin(angle));
            Tween.to(camera, CameraTween.X, STEP_INTERVAL).delay(step * STEP_INTERVAL).target(x).ease(TweenEquations.easeInOutCubic).start(tweenManager);
            Tween.to(camera, CameraTween.Y, STEP_INTERVAL).delay(step * STEP_INTERVAL).target(y).ease(TweenEquations.easeInOutCubic).start(tweenManager);
            strength -= STRENGTH_STEP;
        }
    }
}
