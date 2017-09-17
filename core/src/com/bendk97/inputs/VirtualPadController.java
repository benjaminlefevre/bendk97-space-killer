package com.bendk97.inputs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class VirtualPadController extends com.bendk97.inputs.TouchInputProcessor {

    private Entity player;


    public VirtualPadController(com.bendk97.screens.LevelScreen screen, com.bendk97.listeners.InputListener inputListener, Camera camera, Entity player, Rectangle bombButton) {
        super(screen, inputListener, camera, bombButton);
        this.player = player;
        this.squareTouches = new Rectangle[8];
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldTouch = camera.unproject(new Vector3(screenX, screenY, 0f));
        touchDragged(screenX, screenY, pointer);
        if (bombButton.contains(worldTouch.x, worldTouch.y)) {
            listener.dropBomb();
            return true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        listener.stop();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 worldTouch = camera.unproject(new Vector3(screenX, screenY, 0f));
        computeVirtualButtons();
        moveShip(worldTouch);
        return true;
    }

    private void computeVirtualButtons() {
        com.bendk97.components.PositionComponent position = com.bendk97.components.Mappers.position.get(player);
        float playerWidth = com.bendk97.components.Mappers.sprite.get(player).sprite.getWidth();
        float playerHeight = com.bendk97.components.Mappers.sprite.get(player).sprite.getHeight();

        squareTouches[0] = new Rectangle(
                0f,
                position.y + 1.5f * playerHeight,
                position.x,
                SCREEN_HEIGHT);
        squareTouches[1] = new Rectangle(
                position.x,
                position.y + 1.5f * playerHeight,
                playerWidth,
                SCREEN_HEIGHT);
        squareTouches[2] = new Rectangle(
                position.x + playerWidth,
                position.y + 1.5f * playerHeight,
                SCREEN_WIDTH - position.x - playerWidth,
                SCREEN_HEIGHT);

        squareTouches[3] = new Rectangle(
                0f,
                position.y - 0.5f * playerHeight,
                position.x,
                2 * playerHeight);
        squareTouches[4] = new Rectangle(
                position.x + playerWidth,
                position.y - 0.5f * playerHeight,
                SCREEN_WIDTH - position.x - playerWidth,
                playerHeight * 2);


        squareTouches[5] = new Rectangle(
                0f,
                0f,
                position.x,
                position.y - 0.5f * playerHeight);
        squareTouches[6] = new Rectangle(
                position.x,
                0f,
                playerWidth,
                position.y - 0.5f * playerHeight);
        squareTouches[7] = new Rectangle(
                position.x + playerWidth,
                0f,
                SCREEN_WIDTH - position.x - playerWidth,
                position.y - 0.5f * playerHeight);

    }

}
