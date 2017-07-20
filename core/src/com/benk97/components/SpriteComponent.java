package com.benk97.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class SpriteComponent implements Component, Pool.Poolable {

    public Sprite sprite;
    public float alpha = 1.0f;
    public boolean stayInBoundaries = false;
    private Array<Polygon> bounds;
    public int zIndex = 0;

    @Override
    public void reset() {
        sprite = null;
        alpha = 1.0f;
        zIndex = 0;
        stayInBoundaries = false;
        this.bounds = null;
    }


    public void setTexture(Sprite sprite, float alpha, float rotation, float scale) {
        this.sprite = sprite;
        this.sprite.setOrigin(0f, 0f);
        this.sprite.setScale(scale);
        this.alpha = alpha;
        this.sprite.rotate(rotation);
    }


    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public void setPolygonFromWorldOrigin(Array<float[]> vertices) {
        this.bounds = new Array<Polygon>(vertices.size);
        for (int i = 0; i < vertices.size; ++i) {
            this.bounds.add(new Polygon(vertices.get(i)));
        }
    }


    public Array<Polygon> getPolygonBounds() {
        if (bounds == null) {
            this.bounds = new Array<Polygon>(1);
            this.bounds.add(new Polygon(new float[]{0f, 0f, 0f, sprite.getHeight(), sprite.getWidth(), sprite.getHeight(), sprite.getWidth(), 0f}));
        }
        for (int i = 0; i < bounds.size; ++i) {
            bounds.get(i).setPosition(sprite.getX(), sprite.getY());
        }
        return bounds;
    }
}
