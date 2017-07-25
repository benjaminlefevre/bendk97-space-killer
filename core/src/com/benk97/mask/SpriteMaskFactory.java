package com.benk97.mask;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class SpriteMaskFactory {

    public Map<Texture, Array<Array<Boolean>>> masks = new HashMap<Texture, Array<Array<Boolean>>>();

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
        Array<Array<Boolean>> mask = new Array<Array<Boolean>>();
        for (int i = 0; i < texture.getWidth(); ++i) {
            mask.add(new Array<Boolean>());
            for (int j = 0; j < texture.getHeight(); ++j) {
                mask.get(i).add(
                        (pixmap.getPixel(i, j) & 0x000000ff) != 0
                );
            }
        }
        masks.put(texture, mask);
    }

    public void clear() {
        masks.clear();
    }
}
