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

    float currentTime = 0f;
    int currentFps = 0;

    @Override
    public void update(float deltaTime) {
        float oldTime = currentTime;
        currentTime += deltaTime;
        if (Math.ceil(currentTime) > Math.ceil(oldTime)) {
            currentFps = (int) (1 / Gdx.graphics.getRawDeltaTime());
        }
        bitmapFont.draw(batcher, currentFps + " fps", SCREEN_WIDTH - 30f, 10f);
        bitmapFont.draw(batcher, Math.ceil(currentTime) + " s", SCREEN_WIDTH - 100f, 10f);
    }
}
