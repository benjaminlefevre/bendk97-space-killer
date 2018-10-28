/*
 * Developed by Benjamin Lefèvre
 * Last modified 28/10/18 10:34
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.sprite;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class SpriteHelper {

    public static Circle getBoundingCircle(Sprite sprite) {
        Vector2 center = getCenter(sprite);
        return new Circle(center.x, center.y, Math.max(sprite.getWidth(), sprite.getHeight()) / 2);
    }

    public static Vector2 getCenter(Sprite sprite) {
        return new Vector2(
                sprite.getX() + sprite.getWidth() / 2,
                sprite.getY() + sprite.getHeight() / 2
        );
    }
}
