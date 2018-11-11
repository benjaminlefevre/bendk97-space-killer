/*
 * Developed by Benjamin Lefèvre
 * Last modified 11/11/18 12:15
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.lightning.bolt;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.bendk97.pools.GamePools.poolColor;
import static com.bendk97.pools.GamePools.poolVector2;

public class LightningBolt implements Disposable {
    private float alpha;
    private float alphaMultiplier;
    private float fadeOutRate;
    private final Random random = new RandomXS128();
    private final LightningBoltArt lightningBoltArt;
    private Color tint;
    public Array<Line> segments = new Array<>();

    public Vector2 start() {
        return segments.get(0).a;
    }

    public Vector2 end() {
        return segments.get(segments.size - 1).b;
    }

    public boolean isComplete() {
        return (getAlpha() <= 0);
    }

    public float getAlpha() {
        return alpha;
    }

    public Color getTint() {
        return tint;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setTint(Color tint) {
        this.tint = tint;
    }

    public LightningBolt(LightningBoltArt art, float sourceX, float sourceY, float destX, float destY) {
        this.lightningBoltArt = art;
        init(sourceX, sourceY, destX, destY, poolColor.getColor(Color.WHITE));
    }

    private void init(float sourceX, float sourceY, float destX, float destY, Color color) {
        Vector2 source = poolVector2.getVector2(sourceX, sourceY);
        Vector2 dest = poolVector2.getVector2(destX, destY);
        segments = createBolt(source, dest, lightningBoltArt.thickness);
        tint = color;
        alpha = 1f;
        alphaMultiplier = 1;
        fadeOutRate = 0.03f;
        poolVector2.free(source, dest);
    }

    public void draw(SpriteBatch spriteBatch) {
        if (alpha <= 0)
            return;
        Color color = poolColor.getColor(tint).mul(alpha * alphaMultiplier);
        for (int i = 0; i < segments.size; i++) {
            Line segment = segments.get(i);
            segment.draw(spriteBatch, color);
        }
        poolColor.free(color);
    }

    public void update() {
        alpha -= fadeOutRate;
    }

    protected Array<Line> createBolt(Vector2 source, Vector2 dest, float thickness) {
        Array<Line> results = new Array<>();
        Vector2 tangent = poolVector2.getVector2(dest.x, dest.y).sub(source);
        Vector2 normal = poolVector2.getVector2(tangent.y, -tangent.x).nor();
        float length = tangent.len();

        List<Float> positions = new ArrayList<>();
        positions.add(0f);
        for (int i = 0; i < length / 4; i++)
            positions.add(rand(0, 1));

        Collections.sort(positions);

        float sway = 500;
        float jaggedness = 1 / sway;

        Vector2 prevPoint = poolVector2.getVector2(source.x, source.y);
        float prevDisplacement = 0;
        for (int i = 1; i < positions.size(); i++) {
            float pos = positions.get(i);

            // used to prevent sharp angles by ensuring very close positions also have small perpendicular variation.
            float scale = (length * jaggedness) * (pos - positions.get(i - 1));

            // defines an envelope. Points near the middle of the bolt can be further from the central line.
            float envelope = pos > 0.95f ? 20 * (1 - pos) : 1;

            float displacement = rand(-sway, sway);
            displacement -= (displacement - prevDisplacement) * (1 - scale);
            displacement *= envelope;
            Vector2 point = poolVector2.getVector2(source.x, source.y).mulAdd(tangent, pos).mulAdd(normal, displacement);
            results.add(new Line(lightningBoltArt, prevPoint, point, thickness));
            prevPoint = poolVector2.getVector2(point.x, point.y);
            prevDisplacement = displacement;
        }
        poolVector2.free(tangent, normal);

        results.add(new Line(lightningBoltArt, prevPoint, poolVector2.getVector2(dest.x, dest.y), thickness));

        return results;
    }

    private float rand(float min, float max) {
        return (float) random.nextDouble() * (max - min) + min;
    }

    public void dispose() {
        poolColor.free(this.tint);
        for (int i = 0; i < segments.size; ++i) {
            segments.get(i).dispose();
        }
    }

}
