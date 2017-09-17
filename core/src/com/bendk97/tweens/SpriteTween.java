package com.bendk97.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteTween implements TweenAccessor<Sprite> {
    public final static int POS_XY = 2;

    @Override
    public int getValues(Sprite target, int type, float[] returnValues) {
        switch (type) {
            case POS_XY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            default:
                return -1;
        }
    }

    @Override
    public void setValues(Sprite target, int type, float[] newValues) {
        switch (type) {
            case POS_XY:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                break;
            default:
                break;
        }
    }
}
