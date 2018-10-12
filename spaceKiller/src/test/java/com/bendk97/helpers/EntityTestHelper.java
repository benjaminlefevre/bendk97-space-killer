/*
 * Developed by Benjamin Lefèvre
 * Last modified 12/10/18 22:31
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.helpers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import java.util.Arrays;

public class EntityTestHelper {

    public static Entity createEntity(Engine engine, Class ... components) {
        Entity entity = engine.createEntity();
        Arrays.stream(components).forEach(component -> entity.add(engine.createComponent(component)));
        engine.addEntity(entity);
        return entity;
    }
}
