/*
 * Developed by Benjamin Lefèvre
 * Last modified 30/10/18 20:22
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.pools;

import com.badlogic.gdx.utils.StringBuilder;

public class GamePools {
    private static final int SPRITE_MAX = 10000;
    private static final int CIRCLE_MAX = 10;
    private static final int RECTANGLE_MAX = 10;
    private static final int VECTOR2_MAX = 10000;

    public static PoolCircle poolCircle = new PoolCircle(CIRCLE_MAX);
    public static PoolVector2 poolVector2 = new PoolVector2(VECTOR2_MAX);
    public static PoolRectangle poolRectangle = new PoolRectangle(RECTANGLE_MAX);
    public static PoolSprite poolSprite = new PoolSprite(SPRITE_MAX);

    private static StringBuilder sb = new StringBuilder();

    public static String getPoolStats() {
        sb.setLength(0);
        sb.append(poolCircle.getPoolStats());
        sb.append(poolRectangle.getPoolStats());
        sb.append(poolVector2.getPoolStats());
        sb.append(poolSprite.getPoolStats());
        return sb.toString();
    }

    public static void clearPools() {
        poolSprite.clear();
        poolCircle.clear();
        poolRectangle.clear();
        poolVector2.clear();
    }
}
