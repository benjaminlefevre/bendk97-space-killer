package com.benk97.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class StaticSpriteComponent extends SpriteComponent  {
    public float alpha;

    public void setTexture(Texture texture, float alpha, float rotation) {
        sprite = new Sprite(texture);
        sprite.setOriginCenter();
        this.alpha = alpha;
        sprite.rotate(rotation);
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
