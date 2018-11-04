/*
 * Developed by Benjamin Lefèvre
 * Last modified 03/11/18 11:39
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.GameAssets;

import static com.bendk97.screens.levels.Level.Level1;

public final class Level1Screen extends LevelScreen {

    public Level1Screen(GameAssets assets, SpaceKillerGame game) {
        this(assets, game, null);
    }

    protected Level1Screen(GameAssets assets, SpaceKillerGame game, SpriteBatch defaultBatcher) {
        super(assets, game, defaultBatcher);
    }

    @Override
    protected Level level() {
        return Level1;
    }
}
