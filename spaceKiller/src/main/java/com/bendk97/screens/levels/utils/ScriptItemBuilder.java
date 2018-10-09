/*
 * Developed by Benjamin Lefèvre
 * Last modified 10/10/18 07:58
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels.utils;

import static com.bendk97.SpaceKillerGameConstants.STANDARD_RATE_SHOOT;

public class ScriptItemBuilder {
    private int typeShip;
    private int typeSquadron;
    private float velocity;
    private int number;
    private boolean powerUp;
    private boolean displayBonus;
    private int bonus;
    private int rateShoot = STANDARD_RATE_SHOOT;
    private float bulletVelocity;
    private Object[] params;

    public ScriptItemBuilder typeShip(int typeShip) {
        this.typeShip = typeShip;
        return this;
    }

    public ScriptItemBuilder typeSquadron(int typeSquadron) {
        this.typeSquadron = typeSquadron;
        return this;
    }

    public ScriptItemBuilder velocity(float velocity) {
        this.velocity = velocity;
        return this;
    }

    public ScriptItemBuilder number(int number) {
        this.number = number;
        return this;
    }

    public ScriptItemBuilder powerUp(boolean powerUp) {
        this.powerUp = powerUp;
        return this;
    }

    public ScriptItemBuilder displayBonus(boolean displayBonus) {
        this.displayBonus = displayBonus;
        return this;
    }

    public ScriptItemBuilder withBonus(int bonus) {
        this.bonus = bonus;
        return this;
    }

    public ScriptItemBuilder rateShoot(int rateShoot) {
        this.rateShoot = rateShoot;
        return this;
    }

    public ScriptItemBuilder bulletVelocity(float bulletVelocity) {
        this.bulletVelocity = bulletVelocity;
        return this;
    }

    public ScriptItemBuilder withParams(Object... params) {
        this.params = params;
        return this;
    }

    public ScriptItem createScriptItem() {
        ScriptItem scriptItem = new ScriptItem();
        scriptItem.typeShip = typeShip;
        scriptItem.typeSquadron = typeSquadron;
        scriptItem.velocity = velocity;
        scriptItem.number = number;
        scriptItem.powerUp = powerUp;
        scriptItem.displayBonus = displayBonus;
        scriptItem.bonus = bonus;
        scriptItem.rateShoot = rateShoot;
        scriptItem.bulletVelocity = bulletVelocity;
        scriptItem.params = params;
        return scriptItem;
    }
}