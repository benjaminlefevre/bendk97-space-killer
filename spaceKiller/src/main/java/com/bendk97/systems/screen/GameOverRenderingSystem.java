/*
 * Developed by Benjamin Lefèvre
 * Last modified 12/10/18 21:24
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bendk97.assets.Assets;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.Assets.*;

public class GameOverRenderingSystem extends GLDarkRenderingSystem {

    private static final String GAME_OVER = "GAME\nOVER";
    private static final String SCORE = "score\n\n";
    private static final String HIGHSCORE = "highscore\n\n";
    private final BitmapFont largeFont;
    private final BitmapFont mediumFont;
    private final Sprite extraLife;
    private final Sprite playAgain;
    private final Sprite home;
    private final Sprite share;

    public GameOverRenderingSystem(SpriteBatch batcher, Camera camera, Assets assets, int priority) {
        super(Family.all(PlayerComponent.class, GameOverComponent.class).get(), batcher, camera, priority);
        this.largeFont = assets.get(FONT_SPACE_KILLER_LARGE);
        this.largeFont.setColor(Color.RED);
        this.mediumFont = assets.get(FONT_SPACE_KILLER_MEDIUM);
        this.mediumFont.setColor(Color.WHITE);
        this.playAgain = assets.get(GFX_LEVEL_COMMON).createSprite("icon-playagain");
        this.home = assets.get(GFX_LEVEL_COMMON).createSprite("icon-home");
        this.share = assets.get(GFX_LEVEL_COMMON).createSprite("icon-share");
        this.extraLife = assets.get(GFX_LEVEL_COMMON).createSprite("icon-extralife");
        this.playAgain.setPosition(PLAY_X, PLAY_Y);
        this.home.setPosition(HOME_X, HOME_Y);
        this.share.setPosition(SHARE_X, SHARE_Y);
        this.extraLife.setPosition(EXTRA_X, EXTRA_Y);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        super.processEntity(entity, deltaTime);
        largeFont.draw(batcher, GAME_OVER, SCREEN_WIDTH / 4f - 10f, SCREEN_HEIGHT - 100f);
        mediumFont.draw(batcher, SCORE + ComponentMapperHelper.player.get(entity).getScore(), 20f, SCREEN_HEIGHT / 2f + 30f);
        mediumFont.draw(batcher, HIGHSCORE + ComponentMapperHelper.player.get(entity).getHighScoreFormatted(), 10f, SCREEN_HEIGHT / 2f - 130f);
        if (ComponentMapperHelper.player.get(entity).numberOfContinue > 0) {
            extraLife.draw(batcher);
        }
        playAgain.draw(batcher);
        home.draw(batcher);
        share.draw(batcher);
    }
}
