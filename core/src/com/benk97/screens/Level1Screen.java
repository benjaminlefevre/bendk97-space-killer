package com.benk97.screens;

import com.benk97.assets.Assets;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.*;

public class Level1Screen extends LevelScreen {

    public Level1Screen(Assets assets) {
        super(assets);
        assets.playMusic(MUSIC_LEVEL_1);
        entityFactory.createBackground(assets.get(GFX_BGD_LEVEL1), -BGD_VELOCITY);
        entityFactory.createBackground(assets.get(GFX_BGD_STARS), -BGD_PARALLAX_VELOCITY);
        // create ennemies, test purposes
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                entityFactory.createEnnemySoucoupe(x * 40, SCREEN_HEIGHT  - 40 * (y + 3));

            }
        }
    }


    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void dispose() {
        assets.unloadResources(this.getClass());
    }
}
