package com.benk97.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraTween implements TweenAccessor<OrthographicCamera> {
    public final static int ZOOM = 0;
    public final static int POSITION_Y = 1;

    @Override
    public int getValues(OrthographicCamera target, int type, float[] returnValues) {
        switch (type) {
            case ZOOM:
                returnValues[0] = target.zoom;
                return 1;
            case POSITION_Y:
                returnValues[0] = target.position.y;
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public void setValues(OrthographicCamera target, int type, float[] newValues) {
        switch (type) {
            case ZOOM:
                target.zoom = newValues[0];
                break;
            case POSITION_Y:
                target.position.y = newValues[0];
                break;
            default:
                break;
        }
    }
}
