/*
 * Developed by Benjamin Lefèvre
 * Last modified 26/10/18 20:07
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.bendk97.screens.levels.Level.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class LevelTest {

    @Test
    public void level_after_level1_is_level2() {
        assertThat(Level.nextLevelAfter(Level1)).isEqualTo(Level2);
    }

    @Test
    public void level_after_level2_is_level3() {
        assertThat(Level.nextLevelAfter(Level2)).isEqualTo(Level3);
    }


    @Test
    public void level_after_level3_comes_back_to_level1() {
        assertThat(Level.nextLevelAfter(Level3)).isEqualTo(Level1);
    }
}
