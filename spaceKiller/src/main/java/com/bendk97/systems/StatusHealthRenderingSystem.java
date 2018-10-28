/*
 * Developed by Benjamin Lefèvre
 * Last modified 14/10/18 11:12
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bendk97.assets.Assets;
import com.bendk97.components.StatusHealthComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;

import static com.bendk97.assets.Assets.FONT_SPACE_KILLER;
import static com.bendk97.pools.BitmapFontHelper.drawText;

public class StatusHealthRenderingSystem extends IteratingSystem {
    private static final float ALPHA = 0.4f;
    public static final String BOSS = "BOSS";
    private final SpriteBatch batcher;
    private final BitmapFontCache bitmapFont;

    public StatusHealthRenderingSystem(SpriteBatch batcher, Assets assets, int priority) {
        super(Family.all(StatusHealthComponent.class).get(), priority);
        this.batcher = batcher;
        this.bitmapFont = assets.getFont(FONT_SPACE_KILLER);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        StatusHealthComponent statusHealth = ComponentMapperHelper.healthBar.get(entity);
        statusHealth.healthBar.draw(batcher, ALPHA);
        bitmapFont.setColor(1f, 1f, 1f, ALPHA);
        drawText(batcher, bitmapFont, BOSS, statusHealth.healthBar.getX() - 65, statusHealth.healthBar.getY() + 6);
    }
}
