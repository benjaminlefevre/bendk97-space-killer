package com.benk97.screens;

import com.benk97.SpaceKillerGame;
import com.benk97.assets.Assets;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import static com.benk97.SpaceKillerGameConstants.*;
import static com.benk97.assets.Assets.*;
import static com.benk97.entities.SquadronFactory.*;

public class Level1Screen extends LevelScreen {

    private float time = 0f;
    private Random random = new Random(System.currentTimeMillis());

    public Level1Screen(Assets assets, SpaceKillerGame game) {
        super(assets, game);
        assets.playMusic(MUSIC_LEVEL_1);
        entityFactory.createBackground(assets.get(GFX_BGD_LEVEL1), -BGD_VELOCITY);
        entityFactory.createBackground(assets.get(GFX_BGD_STARS), -BGD_PARALLAX_VELOCITY);
        Collections.shuffle(scriptItems, random);
    }


    @Override
    public void render(float delta) {
        updateScript(delta);
        super.render(delta);
    }

    private void updateScript(float delta) {
        int timeBefore = (int) Math.ceil(time);
        time += delta;
        int newTime = (int) Math.ceil(time);
        if (newTime > timeBefore) {
            script(newTime);
        }
    }

    private void script(int second) {
        switch (second) {
            case 1:
            case 5:
            case 10:
            case 15:
                scriptItems.poll().execute();
                break;

        }
    }


    private LinkedList<ScriptItem> scriptItems = new LinkedList<ScriptItem>(Arrays.asList(
            new ScriptItem(SOUCOUPE, LINEAR_Y, 200f, 5, 0f, SCREEN_HEIGHT),
            new ScriptItem(SOUCOUPE, LINEAR_Y, 200f, 5, SCREEN_WIDTH - SOUCOUPE_WIDTH, SCREEN_HEIGHT),
            new ScriptItem(SOUCOUPE, LINEAR_Y, 200f, 5, SCREEN_WIDTH / 2f - SOUCOUPE_WIDTH / 2f, SCREEN_HEIGHT),
            new ScriptItem(SOUCOUPE, LINEAR_X, 150f, 10, -SOUCOUPE_WIDTH, SCREEN_HEIGHT / 2f))
    );

    class ScriptItem {
        int typeShip;
        int typeSquadron;
        float velocity;
        int number;
        float startX, startY;

        public ScriptItem(int typeShip, int typeSquadron, float velocity, int number, float startX, float startY) {
            this.typeShip = typeShip;
            this.typeSquadron = typeSquadron;
            this.velocity = velocity;
            this.number = number;
            this.startX = startX;
            this.startY = startY;
        }

        public void execute() {
            squadronFactory.createSquadron(typeShip, typeSquadron, velocity, number, startX, startY);
        }
    }

    @Override
    public void dispose() {
        assets.unloadResources(this.getClass());
    }
}
