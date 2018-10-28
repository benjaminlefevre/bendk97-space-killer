/*
 * Developed by Benjamin Lefèvre
 * Last modified 28/10/18 18:55
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.components.texts;

import static com.bendk97.SpaceKillerGameConstants.*;

public class BossAlertComponent extends TextComponent {

    private static final String BOSS_ALERT = "BOSS\nALERT";
    public float targetPosY;

    public BossAlertComponent() {
        this.text = BOSS_ALERT;
        this.posX = BOSS_ALERT_X;
        this.posY = SCREEN_HEIGHT + 200f;
        this.targetPosY = BOSS_ALERT_Y;
    }


    @Override
    public void reset() {
        super.reset();
        this.text = BOSS_ALERT;
        this.posX = BOSS_ALERT_X;
        this.posY = SCREEN_HEIGHT + 200f;
        this.targetPosY = BOSS_ALERT_Y;
    }
}
