package com.benk97.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class StaticSpriteComponent extends SpriteComponent  {
    public float alpha;


    public void setTexture(Texture texture, float alpha, float rotation, float scale) {
        sprite = new Sprite(texture);
        sprite.setOriginCenter();
        this.alpha = alpha;
        sprite.rotate(rotation);
        sprite.setScale(scale);
    }

    public void setTexture(Sprite sprite, float alpha, float rotation, float scale) {
        this.sprite =sprite;
        this.sprite.setOriginCenter();
        this.alpha = alpha;
        this.sprite.rotate(rotation);
        this.sprite.setScale(scale);
    }

    @Override
    public void reset() {
        super.reset();
        alpha = 0.0f;
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }
}
