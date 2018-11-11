/*
 * Developed by Benjamin Lefèvre
 * Last modified 11/11/18 10:41
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.lightningbolt;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bendk97.assets.GameAssets;

import static com.bendk97.assets.GameAssets.*;


public class LightningBoltArt {

    protected TextureRegion lightningSegment, halfCircle, halfCircle2, pixel;
    private final GameAssets assets;

    public LightningBoltArt(GameAssets gameAssets) {
        this.assets = gameAssets;
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