package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class StaticSpriteComponent implements Component, Pool.Poolable, SpriteComponent {
    private Sprite sprite;
    public float alpha;

    public void setTexture(Texture texture, float alpha, float rotation) {
        sprite = new Sprite(texture);
        sprite.setOriginCenter();
        this.alpha = alpha;
        sprite.rotate(rotation);
    }

    @Override
    public void reset() {
    }

    @Override
    public Sprite getSprite(float delta) {
        return sprite;
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }
}
