/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.mask;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class SpriteMaskFactory {

    private final Map<Texture, Array<Array<Boolean>>> masks = new HashMap<>();

    public void addMask(Texture texture){
        computeMask(texture);
    }
    public Array<Array<Boolean>> getMask(Texture texture) {
        return masks.get(texture);
    }

    private void computeMask(Texture texture) {
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        Array<Array<Boolean>> mask = new Array<>();
        for (int i = 0; i < texture.getWidth(); ++i) {
            mask.add(new Array<>());
            for (int j = 0; j < texture.getHeight(); ++j) {
                mask.get(i).add(
                        (pixmap.getPixel(i, j) & 0x000000ff) != 0
                );
            }
        }
        pixmap.dispose();
        masks.put(texture, mask);
    }

    public void clear() {
        masks.clear();
    }
}
