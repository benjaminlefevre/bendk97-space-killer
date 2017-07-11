package com.benk97.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class FPSDisplayRenderingSystem extends EntitySystem {

    private SpriteBatch batcher;
    private BitmapFont bitmapFont;

    public FPSDisplayRenderingSystem(SpriteBatch batcher, int priority) {
        super(priority);
        this.batcher = batcher;
        this.bitmapFont = new BitmapFont();
        this.bitmapFont.getData().setScale(0.5f);
    }

    @Override
    public void update(float deltaTime) {
        bitmapFont.draw(batcher, Gdx.graphics.getFramesPerSecond() + " fps", SCREEN_WIDTH - 30f, 10f);
    }
}
