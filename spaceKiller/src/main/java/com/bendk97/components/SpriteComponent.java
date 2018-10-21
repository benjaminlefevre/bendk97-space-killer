/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class SpriteComponent implements Component, Pool.Poolable {

    public Sprite sprite;
    public float alpha = 1.0f;
    public int zIndex = 0;
    public boolean flashing = false;

    @Override
    public void reset() {
        sprite = null;
        alpha = 1.0f;
        zIndex = 0;
    }


    public void setTexture(Sprite sprite, float alpha, float rotation, float scale) {
        this.sprite = sprite;
        this.sprite.setOrigin(0f, 0f);
        this.sprite.setScale(scale);
        this.alpha = alpha;
        this.sprite.rotate(rotation);
    }

    public void tintRed(float red){
        this.sprite.setColor(red, 0f, 0f, 1f);
    }


    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

}
