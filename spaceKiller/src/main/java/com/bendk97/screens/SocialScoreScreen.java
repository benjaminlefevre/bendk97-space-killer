/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class SocialScoreScreen extends ScreenAdapter {

    public static final String TEMP_DIRECTORY = "temp/spacekiller";
    private static final String TEMP_SPACEKILLER_SCORE_PNG = TEMP_DIRECTORY + "/score-";
    private final SpaceKillerGame game;
    private final OrthographicCamera camera;
    private final SpriteBatch batcher;
    private int score = 0;
    private final NumberFormat numberFormatter;
    private final static String SPACE_KILLER = "space killer";
    private final static String SCORE = "score";
    private final static int screen_width = 900;
    private final static int screen_height = 300;

    private final Sprite iconGame;
    private final Sprite google;
    private final BitmapFont font;
    private final BitmapFont fontSmall;

    public SocialScoreScreen(Assets assets, SpaceKillerGame game, int score) {
        this.game = game;
        this.score = score;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, screen_width, screen_height);
        this.batcher = new SpriteBatch();
        iconGame = new Sprite(assets.get(Assets.ICON_GAME));
        google = new Sprite(assets.get(Assets.ICON_GOOGLE));
        fontSmall = assets.get(Assets.FONT_SPACE_KILLER_SMALL);
        fontSmall.setColor(Color.BLACK);
        font = assets.get(Assets.FONT_SPACE_KILLER_MEDIUM);
        font.setColor(Color.BLACK);
        iconGame.setPosition(10f, 25);
        google.setPosition(700f, 10f);
        numberFormatter = NumberFormat.getNumberInstance(Locale.US);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        batcher.setProjectionMatrix(camera.combined);
        iconGame.draw(batcher);
        google.draw(batcher);
        font.draw(batcher, SPACE_KILLER, 350f, 235f);
        fontSmall.draw(batcher, SCORE, 350f, 100f);
        font.setColor(Color.RED);
        font.draw(batcher, numberFormatter.format(score), 550f, 105f);
        font.setColor(Color.WHITE);
        batcher.end();
    }

    public String takeScreenshot() {
        try {
            String filePath = TEMP_SPACEKILLER_SCORE_PNG + UUID.randomUUID()+".png";
            FrameBuffer fBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, screen_width, screen_height, false);
            fBuffer.begin();
            this.render(Gdx.graphics.getDeltaTime());
            fBuffer.end();
            fBuffer.bind();
            byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, screen_width, screen_height, true);
            Pixmap pixmap = new Pixmap(screen_width, screen_height, Pixmap.Format.RGBA8888);
            BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
            PixmapIO.writePNG(Gdx.files.external(filePath), pixmap);
            pixmap.dispose();
            FrameBuffer.unbind();
            fBuffer.dispose();
            return filePath;
        } catch (Exception e) {
            game.intentShare.verifyStoragePermissions();
            takeScreenshot();
            return null;
        }
    }

    @Override
    public void dispose() {
        batcher.dispose();
    }
}
