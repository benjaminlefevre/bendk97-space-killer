/*
 * Developed by Benjamin Lefèvre
 * Last modified 02/11/18 09:24
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.tweens;

import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

public class TweenCallbacks {

    public static TweenCallback removeEntity(Engine engine, Entity entity) {
        return (i, baseTween) -> {
            if (i == TweenCallback.COMPLETE) {
                engine.removeEntity(entity);
            }
        };
    }
}
