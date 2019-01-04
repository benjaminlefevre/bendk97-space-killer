/*
 * Developed by Benjamin Lefèvre
 * Last modified 31/10/18 08:32
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.pools;

import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BitmapFontHelper {

    public static void drawText(SpriteBatch batcher, BitmapFontCache font, float alpha, String text, float posX, float posY) {
        font.setText(text, posX, posY);
        font.setAlphas(alpha);
        font.draw(batcher);
    }

    public static void drawText(SpriteBatch batcher, BitmapFontCache font, String text, float posX, float posY) {
        drawText(batcher, font, 1f, text, posX, posY);
    }
}
