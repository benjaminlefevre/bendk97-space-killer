package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class DynamicSpriteComponent implements Component, Pool.Poolable, SpriteComponent {

    private Animation<Sprite> ahead;
    private Animation<Sprite> left;
    private Animation<Sprite> right;
    private float stateTime = 0;
    public boolean goLeft = false;
    public boolean goRight = false;
    public boolean stayInBoundaries = false;


    public void setTexture(Texture texture, int frameNumbers, int[] framesGoAhead, int[] framesGoLeft) {
        TextureRegion[] regions = TextureRegion.split(texture, texture.getWidth() / frameNumbers, texture.getHeight())[0];
        Sprite[] aheadSprites = new Sprite[(int) Math.ceil(frameNumbers / 2.0)], leftSprites = new Sprite[2], rightSprites = new Sprite[2];
        for (int i = 0; i < Math.ceil(frameNumbers / 2.0); ++i) {
            aheadSprites[i] = new Sprite(regions[framesGoAhead[i]]);
            if (framesGoLeft != null) {
                leftSprites[i] = new Sprite(regions[framesGoLeft[i]]);
                rightSprites[i] = new Sprite(regions[framesGoLeft[i]]);
                rightSprites[i].flip(true, false);
            }
        }
        this.ahead = new Animation<Sprite>(0.05f, aheadSprites);
        this.left = new Animation<Sprite>(0.05f, leftSprites);
        this.right = new Animation<Sprite>(0.05f, rightSprites);
    }

    @Override
    public Sprite getSprite(float delta) {
        stateTime += delta;
        Animation<Sprite> animation;
        if (goLeft)
            animation = left;
        else if (goRight)
            animation = right;
        else animation = ahead;
        return animation.getKeyFrame(stateTime, true);
    }

    @Override
    public float getWidth() {
        return ahead.getKeyFrame(0f).getWidth();
    }

    @Override
    public float getHeight() {
        return ahead.getKeyFrame(0f).getHeight();
    }


    @Override
    public void reset() {

    }

    public void goAhead() {
        goLeft = false;
        goRight = false;
    }

    public void goRight() {
        goLeft = false;
        goRight = true;
    }

    public void goLeft() {
        goLeft = true;
        goRight = false;
    }
}
