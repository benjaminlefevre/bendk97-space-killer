package com.benk97.components;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface SpriteComponent {
    Sprite getSprite(float delta);

    float getWidth();

    float getHeight();
}
