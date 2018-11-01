/*
 * Developed by Benjamin Lefèvre
 * Last modified 01/11/18 14:07
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.pools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PoolVector3 extends GamePool<Vector3> {

    protected PoolVector3(int max) {
        super(Vector3.class, max);
    }

    public Vector3 getVector3(float x, float y, float z) {
        Vector3 v3 = obtain();
        v3.set(x, y, z);
        return v3;
    }

    public Vector3 getVector3(Vector2 v, float z) {
        Vector3 v3 = obtain();
        v3.set(v, z);
        return v3;
    }

    public void free(Vector3... vector3s) {
        for (Vector3 v3 : vector3s) {
            free(v3);
        }
    }

    @Override
    public void reset(Vector3 vector3) {
        vector3.set(0, 0, 0);
    }
}
