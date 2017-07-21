package com.benk97.inputs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.benk97.components.Mappers;
import com.benk97.components.PositionComponent;
import com.benk97.listeners.InputListener;

import static com.benk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.benk97.SpaceKillerGameConstants.SCREEN_WIDTH;

public class VirtualPadController extends TouchInputProcessor {

    private Entity player;


    public VirtualPadController(InputListener inputListener, Camera camera, Entity player) {
        super(inputListener, camera);
        this.player = player;
        this.squareTouches = new Rectangle[8];
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldTouch = camera.unproject(new Vector3(screenX, screenY, 0f));
        touchDragged(screenX, screenY, pointer);
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
        PositionComponent position = Mappers.position.get(player);
        float playerWidth = Mappers.sprite.get(player).sprite.getWidth();
        float playerHeight = Mappers.sprite.get(player).sprite.getHeight();

        squareTouches[0] = new Rectangle(
                0f,
                position.y + playerHeight,
                position.x,
                SCREEN_HEIGHT);
        squareTouches[1] = new Rectangle(
                position.x,
                position.y + playerHeight,
                playerWidth,
                SCREEN_HEIGHT);
        squareTouches[2] = new Rectangle(
                position.x + playerWidth,
                position.y + playerHeight,
                SCREEN_WIDTH - position.x - playerWidth,
                SCREEN_HEIGHT);

        squareTouches[3] = new Rectangle(
                0f,
                position.y,
                position.x,
                playerHeight);
        squareTouches[4] = new Rectangle(
                position.x + playerWidth,
                position.y,
                SCREEN_WIDTH - position.x - playerWidth,
                playerHeight);


        squareTouches[5] = new Rectangle(
                0f,
                0f,
                position.x,
                position.y);
        squareTouches[6] = new Rectangle(
                position.x,
                0f,
                playerWidth,
                position.y);
        squareTouches[7] = new Rectangle(
                position.x + playerWidth,
                0f,
                SCREEN_WIDTH - position.x - playerWidth,
                position.y);

    }

}
