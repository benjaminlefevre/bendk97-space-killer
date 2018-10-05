/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.listeners;

public interface InputListener {
    void goLeft();

    void goRight();

    void goTop();

    void goDown();

    void goLeftTop();

    void goLeftDown();

    void goRightTop();

    void goRightBottom();

    void stop();

    void fire();

    void dropBomb();
}
