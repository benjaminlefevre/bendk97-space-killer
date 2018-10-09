/*
 * Developed by Benjamin Lefèvre
 * Last modified 08/10/18 20:08
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;


import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.bendk97.assets.Assets;
import com.bendk97.entities.EntityFactory;

import static com.bendk97.assets.Assets.*;

public enum Levels {
    Level1(GFX_LEVEL1_ATLAS_MASK), Level2(GFX_LEVEL2_ATLAS_MASK), Level3(GFX_LEVEL3_ATLAS_MASK);
    private final AssetDescriptor<TextureAtlas> sprites;

    Levels(AssetDescriptor<TextureAtlas> sprites) {
        this.sprites = sprites;
    }

    public AssetDescriptor<TextureAtlas> getSprites() {
        return sprites;
    }

    public static LevelScript getLevelScript(Levels level, Assets assets, EntityFactory entityFactory,
                                             TweenManager tweenManager, Entity player,
                                             PooledEngine engine, OrthographicCamera camera) {
        switch(level) {
            case Level3:
                return new Level3Script(assets, entityFactory, tweenManager, player, engine, camera);
            case Level2:
                return new Level2Script(assets, entityFactory, tweenManager, player, engine, camera);
            case Level1:
            default:
                return new Level1Script(assets, entityFactory, tweenManager, player, engine, camera);
        }
    }

    public static Levels nextLevelAfter(Levels level) {
        switch (level) {
            case Level1:
                return Level2;
            case Level2:
                return Level3;
            case Level3:
            default:
                return Level1;
        }
    }
}
