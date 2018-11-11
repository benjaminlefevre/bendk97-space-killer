package com.bendk97.lightningbolt;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Line {

    public Vector2 a;
    public Vector2 b;
    public float thickness;
    private final LightningBoltArt lightningBoltArt;

    public Line(LightningBoltArt art, Vector2 a, Vector2 b, float thickness) {
        this.lightningBoltArt = art;
        this.a = a;
        this.b = b;
        this.thickness = thickness;
    }

    public void draw(SpriteBatch spriteBatch, Color tint) {
        Vector2 tangent = new Vector2(b).sub(new Vector2(a));
        float theta = (float) Math.toDegrees(Math.atan2(tangent.y, tangent.x));

        float scale = thickness / lightningBoltArt.halfCircle.getRegionHeight();
        Color prevColor = spriteBatch.getColor();
        spriteBatch.setColor(tint);
        int blfn = spriteBatch.getBlendDstFunc();
        spriteBatch.setBlendFunction(spriteBatch.getBlendSrcFunc(), Blending.SourceOver.ordinal());

        spriteBatch.draw(lightningBoltArt.lightningSegment, a.x, a.y, 0, thickness / 2, getLength(), thickness, 1, 1, theta);
        spriteBatch.draw(lightningBoltArt.halfCircle, a.x, a.y, 0, thickness / 2, scale * lightningBoltArt.halfCircle.getRegionWidth(), thickness, 1, 1, theta);
        spriteBatch.draw(lightningBoltArt.halfCircle2, b.x, b.y, 0, thickness / 2, scale * lightningBoltArt.halfCircle2.getRegionWidth(), thickness, 1, 1, theta);

        spriteBatch.setColor(prevColor);
        spriteBatch.setBlendFunction(spriteBatch.getBlendSrcFunc(), blfn);
    }

    public float getLength() {
        return (float) Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

}
