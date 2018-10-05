/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;

public class BackgroundComponent implements Component, Pool.Poolable {

    public Texture texture;
    public int zIndex = 0;

    public void setTexture(Texture texture) {
        texture.setWrap(Repeat, Repeat);
        this.texture = texture;
    }

    @Override
    public void reset() {
        texture = null;
        zIndex = 0;

    }
}
