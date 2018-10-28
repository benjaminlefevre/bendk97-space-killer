/*
 * Developed by Benjamin Lefèvre
 * Last modified 12/10/18 21:24
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bendk97.assets.Assets;
import com.bendk97.components.GameOverComponent;
import com.bendk97.components.PlayerComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;

import static com.bendk97.SpaceKillerGameConstants.*;
import static com.bendk97.assets.Assets.*;
import static com.bendk97.pools.BitmapFontHelper.drawText;
import static com.bendk97.pools.GamePools.poolSprite;

public class GameOverRenderingSystem extends GLDarkRenderingSystem {

    private static final String GAME_OVER = "GAME\nOVER";
    private static final String SCORE = "score\n\n";
    private static final String HIGHSCORE = "highscore\n\n";
    private final BitmapFontCache largeFont;
    private final BitmapFontCache mediumFont;
    private final Sprite extraLife;
    private final Sprite playAgain;
    private final Sprite home;
    private final Sprite share;

    public GameOverRenderingSystem(SpriteBatch batcher, Camera camera, Assets assets, int priority) {
        super(Family.all(PlayerComponent.class, GameOverComponent.class).get(), batcher, camera, priority);
        this.largeFont = assets.getFont(FONT_SPACE_KILLER_LARGE);
        this.largeFont.setColor(Color.RED);
        this.mediumFont = assets.getFont(FONT_SPACE_KILLER_MEDIUM);
        this.mediumFont.setColor(Color.WHITE);
        this.playAgain = poolSprite.getSprite(assets.get(GFX_LEVEL_COMMON).findRegion("icon-playagain"));
        this.home = poolSprite.getSprite(assets.get(GFX_LEVEL_COMMON).findRegion("icon-home"));
        this.share = poolSprite.getSprite(assets.get(GFX_LEVEL_COMMON).findRegion("icon-share"));
        this.extraLife = poolSprite.getSprite(assets.get(GFX_LEVEL_COMMON).findRegion("icon-extralife"));
        this.playAgain.setPosition(PLAY_X, PLAY_Y);
        this.home.setPosition(HOME_X, HOME_Y);
        this.share.setPosition(SHARE_X, SHARE_Y);
        this.extraLife.setPosition(EXTRA_X, EXTRA_Y);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        super.processEntity(entity, deltaTime);
        drawText(batcher, largeFont, GAME_OVER, SCREEN_WIDTH / 4f - 10f, SCREEN_HEIGHT - 100f);
        drawText(batcher, mediumFont, SCORE + ComponentMapperHelper.player.get(entity).getScore(), 20f, SCREEN_HEIGHT / 2f + 30f);
        drawText(batcher, mediumFont, HIGHSCORE + ComponentMapperHelper.player.get(entity).getHighScoreFormatted(), 10f, SCREEN_HEIGHT / 2f - 130f);
        if (ComponentMapperHelper.player.get(entity).numberOfContinue > 0) {
            extraLife.draw(batcher);
        }
        playAgain.draw(batcher);
        home.draw(batcher);
        share.draw(batcher);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        poolSprite.free(this.extraLife);
        poolSprite.free(this.home);
        poolSprite.free(this.playAgain);
        poolSprite.free(this.share);
    }
}
