/*
 * Developed by Benjamin Lefèvre
 * Last modified 03/11/18 11:43
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;

import static com.bendk97.screens.levels.Level.Level3;

public final class Level3Screen extends LevelScreen {

    public Level3Screen(Assets assets, SpaceKillerGame game) {
        this(assets, game, null);
    }

    protected Level3Screen(Assets assets, SpaceKillerGame game, SpriteBatch defaultBatcher) {
        super(assets, game, defaultBatcher);
    }

    @Override
    protected Level level() {
        return Level3;
    }
}
