package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;

public class AnimationComponent implements Component, Pool.Poolable {
    public IntMap<Animation<Sprite>> animations = new IntMap<Animation<Sprite>>();
    public Animation.PlayMode playMode = Animation.PlayMode.LOOP;

    @Override
    public void reset() {
        animations = new IntMap<Animation<Sprite>>();
        playMode = Animation.PlayMode.LOOP;
    }

    public void tintRed(int key, float red) {
        int length = animations.get(key).getKeyFrames().length;
        for (int i = 0; i < length; ++i) {
            Sprite sprite = animations.get(key).getKeyFrames()[i];
            sprite.setColor(red, 0f, 0f, 1f);
        }
    }
}
