/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PositionComponent implements Component, Poolable {
    private float previousX = 0.0f, previousY = 0.0f;
    private float x = 0.0f, y = 0.0f;


    @Override
    public void reset() {
        x = 0;
        y = 0;
        previousX = 0;
        previousY = 0;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public void setXY(float x, float y){
        savePreviousCoordinates();
        this.x = x;
        this.y = y;
    }

    public void setX(float x){
        savePreviousCoordinates();
        this.x = x;
    }

    public void setY(float y){
        savePreviousCoordinates();
        this.y = y;
    }

    public float previousX() {
        return previousX;
    }

    public float previousY() {
        return previousY;
    }

    private void savePreviousCoordinates() {
        this.previousX = this.x;
        this.previousY = this.y;
    }
}
