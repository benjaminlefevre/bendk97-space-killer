/*
 * Developed by Benjamin Lefèvre
 * Last modified 30/10/18 22:43
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.pools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class PoolSprite extends GamePool<Sprite> {

    protected PoolSprite(int max) {
        super(Sprite.class, max);
    }

    @Override
    public void reset(Sprite sprite) {
        sprite.setRotation(0);
        sprite.setScale(1);
        sprite.setColor(1, 1, 1, 1);
    }

    public Sprite getSprite(Texture texture) {
        Sprite sprite = obtain();
        sprite.setTexture(texture);
        int width = texture.getWidth();
        int height = texture.getHeight();
        sprite.setRegion(0, 0, width, height);
        setSpriteChars(sprite, Math.abs(width), Math.abs(height), width / 2f, height / 2f);
        return sprite;
    }

    public Sprite getSprite(TextureAtlas.AtlasRegion atlasRegion) {
        Sprite sprite = obtain();
        sprite.setRegion(atlasRegion);
        int width = atlasRegion.getRegionWidth();
        int height = atlasRegion.getRegionHeight();
        setSpriteChars(sprite, width, height, width / 2f, height / 2f);
        return sprite;
    }

    public Array<Sprite> getSprites(Array<TextureAtlas.AtlasRegion> atlasRegions) {
        Array<Sprite> sprites = new Array<>(true, atlasRegions.size, Sprite.class);
        for(TextureAtlas.AtlasRegion region : atlasRegions){
            sprites.add(getSprite(region));
        }
        return sprites;
    }

    private static void setSpriteChars(Sprite sprite, int width, int height, float originX, float originY) {
        sprite.setSize(width, height);
        sprite.setOrigin(originX, originY);

    }
}
