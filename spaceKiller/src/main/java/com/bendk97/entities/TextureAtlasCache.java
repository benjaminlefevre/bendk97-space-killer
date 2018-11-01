/*
 * Developed by Benjamin Lefèvre
 * Last modified 01/11/18 16:18
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class TextureAtlasCache implements Disposable {

    private final TextureAtlas textureAtlas;
    private final Map<String, AtlasRegion> regionByRegionName = new HashMap<>();
    private final Map<String, Array<AtlasRegion>> regionsByRegionName = new HashMap<>();


    public TextureAtlasCache(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public AtlasRegion findRegion(String name) {
        if (!regionByRegionName.containsKey(name)) {
            regionByRegionName.put(name, textureAtlas.findRegion(name));
        }
        return regionByRegionName.get(name);
    }


    public Array<AtlasRegion> findRegions(String name) {
        if (!regionsByRegionName.containsKey(name)) {
            regionsByRegionName.put(name, textureAtlas.findRegions(name));
        }
        return regionsByRegionName.get(name);
    }

    @Override
    public void dispose() {
        regionByRegionName.clear();
        regionsByRegionName.clear();
    }
}
