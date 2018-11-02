/*
 * Developed by Benjamin Lefèvre
 * Last modified 13/10/18 23:32
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.inputs.pad;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.bendk97.components.PositionComponent;
import com.bendk97.components.helpers.ComponentMapperHelper;
import com.bendk97.listeners.InputListener;
import com.bendk97.screens.levels.LevelScreen;

import static com.bendk97.SpaceKillerGameConstants.SCREEN_HEIGHT;
import static com.bendk97.SpaceKillerGameConstants.SCREEN_WIDTH;
import static com.bendk97.pools.GamePools.poolVector3;

public class VirtualPadController extends AbstractPadController {

    private final Entity player;


    public VirtualPadController(LevelScreen screen, InputListener inputListener, Camera camera, Entity player, Rectangle bombButton) {
        super(screen, inputListener, camera, bombButton);
        this.player = player;
        this.squareTouches = new Rectangle[9];
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldTouch = poolVector3.getVector3(screenX, screenY, 0f);
        worldTouch = camera.unproject(worldTouch);
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
        Vector3 worldTouch = poolVector3.getVector3(screenX, screenY, 0f);
        worldTouch = camera.unproject(worldTouch);
        computeVirtualButtons();
        moveShip(worldTouch);
        poolVector3.free(worldTouch);
        return true;
    }

    @Override
    protected void moveShip(Vector3 worldTouch) {
        super.moveShip(worldTouch);
        if (squareTouches[8].contains(worldTouch.x, worldTouch.y)) {
            listener.stop();
        }
    }

    private void computeVirtualButtons() {
        PositionComponent position = ComponentMapperHelper.position.get(player);
        float playerWidth = ComponentMapperHelper.sprite.get(player).sprite.getWidth();
        float playerHeight = ComponentMapperHelper.sprite.get(player).sprite.getHeight();

        squareTouches[0] = new Rectangle(
                0f,
                position.y() + 1.5f * playerHeight,
                getPositionXTakingIntoAccountHorizontalScrolling(position, playerWidth, true),
                SCREEN_HEIGHT);

        squareTouches[1] = new Rectangle(
                getPositionXTakingIntoAccountHorizontalScrolling(position, playerWidth, true),
                position.y() + 1.5f * playerHeight,
                playerWidth,
                SCREEN_HEIGHT);

        squareTouches[2] = new Rectangle(
                getPositionXTakingIntoAccountHorizontalScrolling(position, playerWidth, false),
                position.y() + 1.5f * playerHeight,
                SCREEN_WIDTH - getPositionXTakingIntoAccountHorizontalScrolling(position, playerWidth, false),
                SCREEN_HEIGHT);

        squareTouches[3] = new Rectangle(
                0f,
                position.y() - 0.5f * playerHeight,
                getPositionXTakingIntoAccountHorizontalScrolling(position, playerWidth, true),
                2 * playerHeight);

        squareTouches[8] = new Rectangle(
                position.x() >= 50 && position.x() < SCREEN_WIDTH - 50 - playerWidth ?
                        getPositionXTakingIntoAccountHorizontalScrolling(position, playerWidth, true) : 666,
                position.y() - 0.5f * playerHeight,
                playerWidth,
                2 * playerHeight);


        squareTouches[4] = new Rectangle(
                getPositionXTakingIntoAccountHorizontalScrolling(position, playerWidth, false),
                position.y() - 0.5f * playerHeight,
                SCREEN_WIDTH - getPositionXTakingIntoAccountHorizontalScrolling(position, playerWidth, false),
                playerHeight * 2);


        squareTouches[5] = new Rectangle(
                0f,
                0f,
                getPositionXTakingIntoAccountHorizontalScrolling(position, playerWidth, true),
                position.y() - 0.5f * playerHeight);

        squareTouches[6] = new Rectangle(
                getPositionXTakingIntoAccountHorizontalScrolling(position, playerWidth, true),
                0f,
                playerWidth,
                position.y() - 0.5f * playerHeight);

        squareTouches[7] = new Rectangle(
                getPositionXTakingIntoAccountHorizontalScrolling(position, playerWidth, false),
                0f,
                SCREEN_WIDTH - getPositionXTakingIntoAccountHorizontalScrolling(position, playerWidth, false),
                position.y() - 0.5f * playerHeight);

    }

    private float getPositionXTakingIntoAccountHorizontalScrolling(PositionComponent positionComponent, float playerWidth, boolean left) {
        if (positionComponent.x() < 0) {
            return 50f;
        }
        if ((positionComponent.x() + playerWidth) > SCREEN_WIDTH) {
            return SCREEN_WIDTH - 50f;
        }
        return positionComponent.x() + (left ? 0 : playerWidth);
    }

}
