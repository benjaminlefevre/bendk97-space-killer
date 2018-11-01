/*
 * Developed by Benjamin Lefèvre
 * Last modified 28/10/18 18:55
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components.texts;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.utils.Pool;

public abstract class TextComponent implements Component, Pool.Poolable {

    public String text = "";
    public float posX = 0f;
    public float posY = 0f;
    public BitmapFontCache font = null;

    @Override
    public void reset() {
        this.font = null;
    }
}
