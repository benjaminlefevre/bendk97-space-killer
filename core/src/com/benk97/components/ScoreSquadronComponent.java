package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ScoreSquadronComponent implements Component, Pool.Poolable {
   public String score = "";
    @Override
    public void reset() {
        score = "";
    }
}
