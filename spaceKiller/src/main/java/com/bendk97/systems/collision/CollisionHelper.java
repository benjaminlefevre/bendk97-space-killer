/*
 * Developed by Benjamin Lefèvre
 * Last modified 28/10/18 21:28
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems.collision;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import static com.bendk97.pools.GamePools.poolCircle;
import static com.bendk97.pools.GamePools.poolVector2;

public class CollisionHelper {

    public Circle getBoundingCircle(Sprite sprite) {
        Vector2 center = getCenter(sprite);
        Circle circle = poolCircle.obtain();
        circle.set(center, Math.max(sprite.getWidth(), sprite.getHeight()) / 2);
        poolVector2.free(center);
        return circle;
    }

    public Vector2 getCenter(Sprite sprite) {
        Vector2 vector2 = poolVector2.obtain();
        vector2.x = sprite.getX() + sprite.getWidth() / 2;
        vector2.y = sprite.getY() + sprite.getHeight() / 2;
        return vector2;
    }
}
