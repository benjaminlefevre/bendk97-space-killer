/*
 * Developed by Benjamin Lefèvre
 * Last modified 08/10/18 20:08
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.levels;


import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.bendk97.assets.Assets;
import com.bendk97.entities.EntityFactory;

import java.util.Map;

import static com.bendk97.assets.Assets.*;
import static com.bendk97.screens.levels.Level.MusicTrack.BOSS;
import static com.bendk97.screens.levels.Level.MusicTrack.LEVEL;
import static com.bendk97.screens.levels.Level.SoundEffect.BOSS_ALERT;
import static com.bendk97.screens.levels.Level.SoundEffect.GO;
import static com.google.common.collect.ImmutableMap.of;

public enum Level {
    Level1(GFX_LEVEL1_ATLAS_MASK, of(GO, SOUND_GO, BOSS_ALERT, SOUND_BOSS_ALERT), of(LEVEL, MUSIC_LEVEL_1, BOSS, MUSIC_LEVEL_1_BOSS), 0.3f),
    Level2(GFX_LEVEL2_ATLAS_MASK, of(GO, SOUND_GO, BOSS_ALERT, SOUND_BOSS_ALERT), of(LEVEL, MUSIC_LEVEL_2, BOSS, MUSIC_LEVEL_2_BOSS), 0.3f),
    Level3(GFX_LEVEL3_ATLAS_MASK, of(GO, SOUND_GO, BOSS_ALERT, SOUND_BOSS_ALERT), of(LEVEL, MUSIC_LEVEL_3, BOSS, MUSIC_LEVEL_3_BOSS), 0.6f);
    public final AssetDescriptor<TextureAtlas> sprites;
    public final Map<SoundEffect, AssetDescriptor<Sound>> sounds;
    public final Map<MusicTrack, AssetDescriptor<Music>> musics;
    public final float volume;

    Level(AssetDescriptor<TextureAtlas> sprites,
          Map<SoundEffect, AssetDescriptor<Sound>> sounds,
          Map<MusicTrack, AssetDescriptor<Music>> musics,
          float volume) {
        this.sprites = sprites;
        this.sounds = sounds;
        this.musics = musics;
        this.volume = volume;
    }

    public enum SoundEffect {
        GO,
        BOSS_ALERT
    }

    public enum MusicTrack {
        LEVEL,
        BOSS
    }

    public static LevelScript getLevelScript(Level level, LevelScreen screen, Assets assets, EntityFactory entityFactory,
                                             TweenManager tweenManager, Entity player,
                                             PooledEngine engine) {
        switch (level) {
            case Level3:
                return new Level3Script(screen, assets, entityFactory, tweenManager, player, engine);
            case Level2:
                return new Level2Script(screen, assets, entityFactory, tweenManager, player, engine);
            case Level1:
            default:
                return new Level1Script(screen, assets, entityFactory, tweenManager, player);
        }
    }

    public static Level nextLevelAfter(Level level) {
        switch (level) {
            case Level1:
                return Level2;
            case Level2:
                return Level3;
            case Level3:
            default:
                return Level1;
        }
    }
}
