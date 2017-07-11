package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class SpriteComponent implements Component, Pool.Poolable {

    public Sprite sprite;
    public float alpha = 1.0f;
    public boolean stayInBoundaries = false;

    @Override
    public void reset() {
        sprite = null;
        alpha = 1.0f;
    }

    public void setTexture(Texture texture, float alpha, float rotation, float scale) {
        sprite = new Sprite(texture);
        sprite.setOriginCenter();
        this.alpha = alpha;
        sprite.rotate(rotation);
        sprite.setScale(scale);
    }

    public void setTexture(Sprite sprite, float alpha, float rotation, float scale) {
        this.sprite = sprite;
        this.sprite.setOriginCenter();
        this.alpha = alpha;
        this.sprite.rotate(rotation);
        this.sprite.setScale(scale);
    }


    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

}
