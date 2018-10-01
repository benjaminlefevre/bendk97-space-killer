/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.assets;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.bendk97.Settings;
import com.bendk97.screens.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.EMPTY_LIST;

public class Assets {

    // SPLASH SCREEN
    public static final AssetDescriptor<TextureAtlas> SPASH_ATLAS =
            new AssetDescriptor<TextureAtlas>("gfx/splashscreen.atlas", TextureAtlas.class);
    public static final AssetDescriptor<Texture> SPLASH_TXT_LOGO =
            new AssetDescriptor<Texture>("gfx/backgrounds/bendk97.png", Texture.class);
    public static final AssetDescriptor<Music> SPLASH_MUSIC =
            new AssetDescriptor<Music>("sounds/splash.ogg", Music.class);
    public static final AssetDescriptor<Texture> ICON_GAME =
            new AssetDescriptor<Texture>("gfx/space_killer.png", Texture.class);
    public static final AssetDescriptor<Texture> ICON_GOOGLE =
            new AssetDescriptor<Texture>("gfx/google.png", Texture.class);

    // MENU SCREEN
    public static final AssetDescriptor<Texture> MENU_BGD =
            new AssetDescriptor<Texture>("gfx/backgrounds/menu.jpg", Texture.class);
    public static final AssetDescriptor<Music> MENU_MUSIC =
            new AssetDescriptor<Music>("sounds/intro.mid", Music.class);
    public static final AssetDescriptor<Sound> MENU_CLICK =
            new AssetDescriptor<Sound>("sounds/click_menu.ogg", Sound.class);
    public static final AssetDescriptor<TextureAtlas> MENU_ATLAS =
            new AssetDescriptor<TextureAtlas>("gfx/menu.atlas", TextureAtlas.class);


    // BACKGROUNDS
    public static final AssetDescriptor<Texture> GFX_BGD_STARS =
            new AssetDescriptor<Texture>("gfx/backgrounds/starfield.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_LEVEL1 =
            new AssetDescriptor<Texture>("gfx/backgrounds/level1.gif", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST1 =
            new AssetDescriptor<Texture>("gfx/backgrounds/mist.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST2 =
            new AssetDescriptor<Texture>("gfx/backgrounds/mist2.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST3 =
            new AssetDescriptor<Texture>("gfx/backgrounds/mist3.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST4 =
            new AssetDescriptor<Texture>("gfx/backgrounds/mist4.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST5 =
            new AssetDescriptor<Texture>("gfx/backgrounds/mist5.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST6 =
            new AssetDescriptor<Texture>("gfx/backgrounds/mist6.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST7 =
            new AssetDescriptor<Texture>("gfx/backgrounds/mist7.png", Texture.class);

    public static final AssetDescriptor<Texture> GFX_BGD_LEVEL2 =
            new AssetDescriptor<Texture>("gfx/backgrounds/level2.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_BIG_PLANET =
            new AssetDescriptor<Texture>("gfx/backgrounds/big-planet.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_FAR_PLANETS =
            new AssetDescriptor<Texture>("gfx/backgrounds/far-planets.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_RISING_PLANETS =
            new AssetDescriptor<Texture>("gfx/backgrounds/rising-planets.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_STARS_LEVEL2 =
            new AssetDescriptor<Texture>("gfx/backgrounds/stars.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_CLOUDS =
            new AssetDescriptor<Texture>("gfx/backgrounds/clouds.png", Texture.class);


    public static final AssetDescriptor<Texture> GFX_BGD_LEVEL3 =
            new AssetDescriptor<Texture>("gfx/backgrounds/level3.jpg", Texture.class);

    // GFX
    public static final AssetDescriptor<TextureAtlas> GFX_LEVEL_ALL_ATLAS_NOMASK = new AssetDescriptor<TextureAtlas>("gfx/level-all-nomask.atlas", TextureAtlas.class);
    public static final AssetDescriptor<TextureAtlas> GFX_LEVEL1_ATLAS_MASK = new AssetDescriptor<TextureAtlas>("gfx/level1-mask.atlas", TextureAtlas.class);
    public static final AssetDescriptor<TextureAtlas> GFX_LEVEL2_ATLAS_MASK = new AssetDescriptor<TextureAtlas>("gfx/level2-mask.atlas", TextureAtlas.class);
    public static final AssetDescriptor<TextureAtlas> GFX_LEVEL3_ATLAS_MASK = new AssetDescriptor<TextureAtlas>("gfx/level3-mask.atlas", TextureAtlas.class);

    // SOUNDS
    public static final AssetDescriptor<Sound> SOUND_LOSE_LIFE =
            new AssetDescriptor<Sound>("sounds/loseLife.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_NEW_LIFE =
            new AssetDescriptor<Sound>("sounds/newLife.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_READY =
            new AssetDescriptor<Sound>("sounds/ready.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_GAME_OVER =
            new AssetDescriptor<Sound>("sounds/game_over.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_SHIELD_BULLET =
            new AssetDescriptor<Sound>("sounds/shield.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_SHIELD_UP =
            new AssetDescriptor<Sound>("sounds/shieldUp.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_BOMB_DROP =
            new AssetDescriptor<Sound>("sounds/bombDrop.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_BOMB_EXPLOSION =
            new AssetDescriptor<Sound>("sounds/bombExplosion.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_EXPLOSION =
            new AssetDescriptor<Sound>("sounds/explosion.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_BOSS_FINISHED =
            new AssetDescriptor<Sound>("sounds/bossKilled.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_FIRE =
            new AssetDescriptor<Sound>("sounds/fire.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_BOSS_ALERT =
            new AssetDescriptor<Sound>("sounds/boss-alert.ogg", Sound.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_1 =
            new AssetDescriptor<Music>("sounds/level1.ogg", Music.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_2 =
            new AssetDescriptor<Music>("sounds/level2.ogg", Music.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_3 =
            new AssetDescriptor<Music>("sounds/level3.ogg", Music.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_1_BOSS =
            new AssetDescriptor<Music>("sounds/level1-boss.mid", Music.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_2_BOSS =
            new AssetDescriptor<Music>("sounds/level2-boss.mid", Music.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_3_BOSS =
            new AssetDescriptor<Music>("sounds/level3-boss.mid", Music.class);
    public static final AssetDescriptor<Sound> SOUND_POWER_UP =
            new AssetDescriptor<Sound>("sounds/powerUp.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_POWER_UP_VOICE =
            new AssetDescriptor<Sound>("sounds/powerUpVoice.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_FIRE_ENEMY =
            new AssetDescriptor<Sound>("sounds/enemyFire.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_NEW_HIGHSCORE =
            new AssetDescriptor<Sound>("sounds/new_highscore.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_GO =
            new AssetDescriptor<Sound>("sounds/go.ogg", Sound.class);
    // FONTS
    public static final AssetDescriptor<BitmapFont> FONT_SPACE_KILLER =
            new AssetDescriptor<BitmapFont>("font1.ttf", BitmapFont.class,
                    getFontParameters("fonts/arcade.ttf", 28));
    public static final AssetDescriptor<BitmapFont> FONT_SPACE_KILLER_LARGE =
            new AssetDescriptor<BitmapFont>("font2.ttf", BitmapFont.class,
                    getFontParameters("fonts/arcade.ttf", 100));
    public static final AssetDescriptor<BitmapFont> FONT_SPACE_KILLER_MEDIUM =
            new AssetDescriptor<BitmapFont>("font3.ttf", BitmapFont.class,
                    getFontParameters("fonts/regular.ttf", 35));
    public static final AssetDescriptor<BitmapFont> FONT_SPACE_KILLER_SMALL =
            new AssetDescriptor<BitmapFont>("font4.ttf", BitmapFont.class,
                    getFontParameters("fonts/regular.ttf", 25));
    public static final AssetDescriptor<BitmapFont> FONT_SPACE_KILLER_SMALLEST =
            new AssetDescriptor<BitmapFont>("font4.ttf", BitmapFont.class,
                    getFontParameters("fonts/regular.ttf", 20));

    private static FreeTypeFontLoaderParameter getFontParameters(String filename, int size) {
        FreeTypeFontLoaderParameter parameter = new FreeTypeFontLoaderParameter();
        parameter.fontFileName = filename;
        parameter.fontParameters.size = size;
        return parameter;
    }

    private static final Map<Class<? extends Screen>, List<AssetDescriptor>> assetsNeededByScreen = new HashMap<Class<? extends Screen>, List<AssetDescriptor>>() {{
        put(SplashScreen.class, Arrays.<AssetDescriptor>asList(
                SPLASH_MUSIC, SPASH_ATLAS, SPLASH_TXT_LOGO
        ));
        put(MenuScreen.class, Arrays.<AssetDescriptor>asList(
                MENU_BGD, FONT_SPACE_KILLER_LARGE, MENU_MUSIC, FONT_SPACE_KILLER_MEDIUM, MENU_CLICK,
                MENU_ATLAS, FONT_SPACE_KILLER_SMALL
        ));
        put(Level1Screen.class, Arrays.<AssetDescriptor>asList(
                // MUSIC
                MUSIC_LEVEL_1, MUSIC_LEVEL_1_BOSS,
                // SOUNDS
                SOUND_READY, SOUND_FIRE, SOUND_EXPLOSION, SOUND_POWER_UP, SOUND_FIRE_ENEMY,
                SOUND_SHIELD_BULLET, SOUND_SHIELD_UP, SOUND_GAME_OVER, SOUND_LOSE_LIFE,
                SOUND_NEW_LIFE, SOUND_NEW_HIGHSCORE, SOUND_GO, SOUND_BOSS_ALERT,
                SOUND_BOSS_FINISHED, SOUND_BOMB_DROP, SOUND_BOMB_EXPLOSION,
                SOUND_POWER_UP_VOICE,
                // GFX
                GFX_BGD_MIST1, GFX_BGD_MIST2, GFX_BGD_MIST3, GFX_BGD_MIST4, GFX_BGD_MIST5, GFX_BGD_MIST6, GFX_BGD_MIST7,
                GFX_LEVEL1_ATLAS_MASK, GFX_LEVEL_ALL_ATLAS_NOMASK,
                ICON_GAME, ICON_GOOGLE,
                GFX_BGD_LEVEL1, GFX_BGD_STARS,
                // FONTS
                FONT_SPACE_KILLER, FONT_SPACE_KILLER_LARGE, FONT_SPACE_KILLER_SMALLEST,
                FONT_SPACE_KILLER_MEDIUM));
        put(Level2Screen.class, Arrays.<AssetDescriptor>asList(
                // MUSIC
                MUSIC_LEVEL_2, MUSIC_LEVEL_2_BOSS,
                // SOUNDS
                SOUND_READY, SOUND_FIRE, SOUND_EXPLOSION, SOUND_POWER_UP, SOUND_FIRE_ENEMY,
                SOUND_SHIELD_BULLET, SOUND_SHIELD_UP, SOUND_GAME_OVER, SOUND_LOSE_LIFE,
                SOUND_NEW_LIFE, SOUND_NEW_HIGHSCORE, SOUND_GO, SOUND_BOSS_ALERT,
                SOUND_BOSS_FINISHED, SOUND_BOMB_DROP, SOUND_BOMB_EXPLOSION,
                SOUND_POWER_UP_VOICE,
                // GFX
                GFX_BGD_MIST1, GFX_BGD_MIST2, GFX_BGD_MIST3, GFX_BGD_MIST4, GFX_BGD_MIST5, GFX_BGD_MIST6, GFX_BGD_MIST7, GFX_BGD_CLOUDS,
                GFX_LEVEL_ALL_ATLAS_NOMASK, GFX_LEVEL2_ATLAS_MASK,
                ICON_GAME, ICON_GOOGLE,
                GFX_BGD_LEVEL2, GFX_BGD_BIG_PLANET, GFX_BGD_FAR_PLANETS, GFX_BGD_RISING_PLANETS, GFX_BGD_STARS_LEVEL2,
                // FONTS
                FONT_SPACE_KILLER, FONT_SPACE_KILLER_LARGE, FONT_SPACE_KILLER_MEDIUM, FONT_SPACE_KILLER_SMALLEST
        ));

        put(Level3Screen.class, Arrays.<AssetDescriptor>asList(
                // MUSIC
                MUSIC_LEVEL_3, MUSIC_LEVEL_3_BOSS,
                // SOUNDS
                SOUND_READY, SOUND_FIRE, SOUND_EXPLOSION, SOUND_POWER_UP, SOUND_FIRE_ENEMY,
                SOUND_SHIELD_BULLET, SOUND_SHIELD_UP, SOUND_GAME_OVER, SOUND_LOSE_LIFE,
                SOUND_NEW_LIFE, SOUND_NEW_HIGHSCORE, SOUND_GO, SOUND_BOSS_ALERT,
                SOUND_BOSS_FINISHED, SOUND_BOMB_DROP, SOUND_BOMB_EXPLOSION,
                SOUND_POWER_UP_VOICE,
                // GFX
                GFX_BGD_MIST1, GFX_BGD_MIST2, GFX_BGD_MIST3, GFX_BGD_MIST4, GFX_BGD_MIST5, GFX_BGD_MIST6, GFX_BGD_MIST7,
                GFX_LEVEL_ALL_ATLAS_NOMASK, GFX_LEVEL3_ATLAS_MASK,
                ICON_GAME, ICON_GOOGLE,
                GFX_BGD_LEVEL3,
                // FONTS
                FONT_SPACE_KILLER, FONT_SPACE_KILLER_LARGE, FONT_SPACE_KILLER_MEDIUM, FONT_SPACE_KILLER_SMALLEST
        ));

        put(TransitionScreen.class, EMPTY_LIST);
        put(SocialScoreScreen.class, EMPTY_LIST);

    }};

    private AssetManager manager;


    public <T> T get(AssetDescriptor<T> descriptor) {
        return manager.get(descriptor.fileName);
    }

    private AssetManager getAssetManager() {
        if (manager != null) {
            manager.dispose();
        }
        AssetManager manager = new AssetManager();
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        return manager;
    }

    public void loadResources(Class<? extends Screen> screen) {
        manager = getAssetManager();
        Texture.setAssetManager(manager);

        for (AssetDescriptor assetDescriptor : assetsNeededByScreen.get(screen)) {
            manager.load(assetDescriptor);
        }
        manager.finishLoading();
    }

    public void unloadResources(Class<? extends Screen> screen) {
        for (AssetDescriptor assetDescriptor : assetsNeededByScreen.get(screen)) {
            manager.unload(assetDescriptor.fileName);
        }
    }

    public void playSound(AssetDescriptor<Sound> sound) {
        playSound(sound, 1.0f);
    }

    public void playSound(AssetDescriptor<Sound> sound, float volume) {
        if (Settings.isSoundOn()) {
            if (manager.isLoaded(SOUND_FIRE.fileName)) {
                manager.get(SOUND_FIRE).stop();
            }
            if (sound.equals(SOUND_EXPLOSION) && manager.isLoaded(SOUND_EXPLOSION.fileName)) {
                manager.get(SOUND_EXPLOSION).stop();
            }

            if (sound.equals(SOUND_FIRE_ENEMY) && manager.isLoaded(SOUND_FIRE_ENEMY.fileName)) {
                manager.get(SOUND_FIRE_ENEMY).stop();
            }
            manager.get(sound).play(volume);
        }
    }

    public Music playMusic(AssetDescriptor<Music> musicDescriptor) {
        return playMusic(musicDescriptor, 1.0f);
    }

    public Music playMusic(AssetDescriptor<Music> musicDescriptor, float volume) {
        if (Settings.isMusicOn()) {
            Music music = manager.get(musicDescriptor);
            music.setLooping(true);
            music.setVolume(volume);
            music.play();
            return music;
        }
        return null;
    }

    public void stopMusic(AssetDescriptor<Music> music) {
        manager.get(music).stop();
    }

}
