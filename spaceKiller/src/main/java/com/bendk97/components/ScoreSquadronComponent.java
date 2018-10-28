/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.StringBuilder;

public class ScoreSquadronComponent implements Component, Pool.Poolable {
   public StringBuilder score = new StringBuilder();
    @Override
    public void reset() {
        score.setLength(0);
    }
}
