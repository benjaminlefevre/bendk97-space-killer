/*
 * Developed by Benjamin Lefèvre
 * Last modified 28/10/18 11:59
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.sprite;

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
public class SpriteHelperTest {

    @Test
    public void get_center_sprite() {
        Sprite sprite = new Sprite(mock(Texture.class), 150, 300);
        Vector2 center = SpriteHelper.getCenter(sprite);
        assertThat(center.x).isEqualTo(75);
        assertThat(center.y).isEqualTo(150);
    }

    @Test
    public void get_boundary_circle_sprite() {
        Sprite sprite = new Sprite(mock(Texture.class), 150, 300);
        Circle circle = SpriteHelper.getBoundingCircle(sprite);
        assertThat(circle.x).isEqualTo(75);
        assertThat(circle.y).isEqualTo(150);
        assertThat(circle.radius).isEqualTo(150);
    }
}
