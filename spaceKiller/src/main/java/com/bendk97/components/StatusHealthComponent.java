/*
 * Developed by Benjamin Lefèvre
 * Last modified 14/10/18 10:43
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Pool;

public final class StatusHealthComponent implements Component, Pool.Poolable {
    private final static float VALUE = 1f;
    public static final int HEIGHT = 10;
    public static final int WIDTH = 200;
    public ProgressBar healthBar;
    private static final Color GREEN = new Color(0x44ff00ff);

    public StatusHealthComponent() {
        ProgressBarStyle progressBarStyle = new ProgressBarStyle();
        progressBarStyle.background = createPixmap(WIDTH, Color.RED);
        progressBarStyle.knob = createPixmap(0, GREEN);
        progressBarStyle.knobBefore = createPixmap(WIDTH, GREEN);
        healthBar = new ProgressBar(0.0f, 1.0f, 0.01f, false, progressBarStyle);
        healthBar.setValue(VALUE);
        healthBar.setBounds(0f, 0f, WIDTH, HEIGHT);

    }

    @Override
    public void reset() {
        setValue(VALUE);
        setBounds(0f, 0f);
    }

    public void setBounds(float x, float y) {
        healthBar.setBounds(x, y, WIDTH, HEIGHT);
    }

    public void setValue(float value) {
        healthBar.setValue(value);
    }

    private TextureRegionDrawable createPixmap(int width, Color color) {
        Pixmap pixmap = new Pixmap(width, HEIGHT, Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        return drawable;
    }
}
