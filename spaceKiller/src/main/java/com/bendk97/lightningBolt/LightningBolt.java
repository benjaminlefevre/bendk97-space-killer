package com.bendk97.lightningbolt;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LightningBolt {
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

    public LightningBolt(LightningBoltArt art, Vector2 source, Vector2 dest) {
        this.lightningBoltArt = art;
        init(source, dest, new Color(Color.WHITE));
    }

    private void init(Vector2 source, Vector2 dest, Color color) {
        segments = createBolt(source, dest, 10);
        tint = color;
        alpha = 0.75f;
        alphaMultiplier = 1;
        fadeOutRate = 0.03f;

    }

    public void draw(SpriteBatch spriteBatch) {
        if (alpha <= 0)
            return;
        for (int i = 0; i < segments.size; i++) {
            Line segment = segments.get(i);
            segment.draw(spriteBatch, new Color(tint).mul(alpha * alphaMultiplier));
        }
    }

    public void update() {
        alpha -= fadeOutRate;
    }

    protected Array<Line> createBolt(Vector2 source, Vector2 dest, float thickness) {
        Array<Line> results = new Array<>();
        Vector2 tangent = new Vector2(dest).sub(new Vector2(source));
        Vector2 normal = new Vector2(tangent.y, -tangent.x).nor();
        float length = tangent.len();

        List<Float> positions = new ArrayList<>();
        positions.add(0f);
        for (int i = 0; i < length / 4; i++)
            positions.add(rand(0, 1));

        Collections.sort(positions);

        float sway = 500;
        float jaggedness = 1 / sway;

        Vector2 prevPoint = source;
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

            Vector2 point = new Vector2(source).add(new Vector2(0, 0).mulAdd(tangent, pos)).add(new Vector2().mulAdd(normal, displacement));
            results.add(new Line(lightningBoltArt, prevPoint, point, thickness));
            prevPoint = point;
            prevDisplacement = displacement;
        }

        results.add(new Line(lightningBoltArt, new Vector2(prevPoint), new Vector2(dest), thickness));

        return results;
    }

    private float rand(float min, float max) {
        return (float) random.nextDouble() * (max - min) + min;
    }

}
