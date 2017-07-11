package com.benk97.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.benk97.components.SpriteComponent;

public class SpriteComponentAccessor implements TweenAccessor<SpriteComponent> {

    public final static int ALPHA = 0;

    @Override
    public int getValues(SpriteComponent sprite, int type, float[] returnValues) {
        switch (type) {
            case ALPHA:
                returnValues[0] = sprite.alpha;
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public void setValues(SpriteComponent sprite, int type, float[] newValues) {
        switch (type) {
            case ALPHA:
                sprite.alpha = newValues[0];
                break;
            default:
                break;
        }
    }
}
