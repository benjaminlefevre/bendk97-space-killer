/*
 * Developed by Benjamin Lefèvre
 * Last modified 30/10/18 22:55
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.pools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PoolVector2 extends GamePool<Vector2> {

    protected PoolVector2(int max) {
        super(Vector2.class, max);
    }

    public Vector2 getVector2(float x, float y) {
        Vector2 v2 = obtain();
        v2.set(x, y);
        return v2;
    }


    public void free(Vector2... vector2s) {
        for (Vector2 v2 : vector2s) {
            free(v2);
        }
    }

    public void free(Array<Vector2> vector2s) {
        for (Vector2 v2 : vector2s) {
            free(v2);
        }
    }

    @Override
    public void reset(Vector2 vector2) {
        vector2.set(0, 0);
    }
}
