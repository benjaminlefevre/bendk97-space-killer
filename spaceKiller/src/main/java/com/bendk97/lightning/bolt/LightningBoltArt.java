/*
 * Developed by Benjamin Lefèvre
 * Last modified 11/11/18 12:15
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.lightning.bolt;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bendk97.assets.GameAssets;

import static com.bendk97.assets.GameAssets.*;


public class LightningBoltArt {

    protected TextureRegion lightningSegment, halfCircle, halfCircle2, pixel;
    protected final float thickness;
    private final GameAssets assets;

    public LightningBoltArt(GameAssets gameAssets, float thickness) {
        this.assets = gameAssets;
        this.thickness = thickness;
        assignResource();
    }

    public void assignResource(){
        lightningSegment = new TextureRegion(assets.get(LIGHT_BOLT_SEGMENT));
        halfCircle =  new TextureRegion(assets.get(LIGHT_BOLT_HALF_CIRCLE));
        pixel =  new TextureRegion(assets.get(LIGHT_BOLT_PIXEL));
        halfCircle2 =  new TextureRegion(assets.get(LIGHT_BOLT_HALF_CIRCLE));
        halfCircle2.flip(true, false);
    }
}