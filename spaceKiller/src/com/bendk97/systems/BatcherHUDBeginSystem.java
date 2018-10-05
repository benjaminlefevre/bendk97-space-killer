/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BatcherHUDBeginSystem extends BatcherBeginSystem {

    public BatcherHUDBeginSystem(Viewport viewport, SpriteBatch batcher, int priority) {
        super(viewport, batcher, priority);
    }

}
