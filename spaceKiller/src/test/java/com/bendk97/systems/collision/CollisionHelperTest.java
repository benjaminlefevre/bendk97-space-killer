/*
 * Developed by Benjamin Lefèvre
 * Last modified 28/10/18 21:37
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.systems.collision;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class CollisionHelperTest {

    private CollisionHelper collisionHelper = new CollisionHelper();

    @Test
    public void get_center_sprite() {
        Sprite sprite = new Sprite(mock(Texture.class), 150, 300);
        Vector2 center = collisionHelper.getCenter(sprite);
        assertThat(center.x).isEqualTo(75);
        assertThat(center.y).isEqualTo(150);
    }

    @Test
    public void get_boundary_circle_sprite() {
        Sprite sprite = new Sprite(mock(Texture.class), 150, 300);
        Circle circle = collisionHelper.getBoundingCircle(sprite);
        assertThat(circle.x).isEqualTo(75);
        assertThat(circle.y).isEqualTo(150);
        assertThat(circle.radius).isEqualTo(150);
    }
}
