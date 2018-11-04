/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.bendk97.Settings;
import com.bendk97.assets.helper.ArchiveFileHandleResolver;
import com.bendk97.screens.SocialScoreScreen;
import com.bendk97.screens.SplashScreen;
import com.bendk97.screens.levels.Level1Screen;
import com.bendk97.screens.levels.Level2Screen;
import com.bendk97.screens.levels.Level3Screen;
import com.bendk97.screens.levels.utils.TransitionScreen;
import com.bendk97.screens.menu.MenuScreen;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;

public class GameAssets {

    private FileHandleResolver resolver;
    private AssetManager manager;

    // SPLASH SCREEN
    public static final AssetDescriptor<TextureAtlas> SPLASH_ATLAS =
            new AssetDescriptor<>("gfx/splashscreen.atlas", TextureAtlas.class);
    public static final AssetDescriptor<Texture> SPLASH_TXT_LOGO =
            new AssetDescriptor<>("gfx/backgrounds/bendk97.png", Texture.class);
    public static final AssetDescriptor<Music> SPLASH_MUSIC =
            new AssetDescriptor<>("sounds/splash.ogg", Music.class);
    public static final AssetDescriptor<Texture> ICON_GAME =
            new AssetDescriptor<>("gfx/space_killer.png", Texture.class);
    public static final AssetDescriptor<Texture> ICON_GOOGLE =
            new AssetDescriptor<>("gfx/google.png", Texture.class);

    // MENU SCREEN
    public static final AssetDescriptor<Texture> MENU_BGD =
            new AssetDescriptor<>("gfx/backgrounds/menu.jpg", Texture.class);
    public static final AssetDescriptor<Music> MENU_MUSIC =
            new AssetDescriptor<>("sounds/intro.mid", Music.class);
    public static final AssetDescriptor<Sound> MENU_CLICK =
            new AssetDescriptor<>("sounds/click_menu.ogg", Sound.class);
    public static final AssetDescriptor<TextureAtlas> MENU_ATLAS =
            new AssetDescriptor<>("gfx/menu.atlas", TextureAtlas.class);


    // BACKGROUNDS
    public static final AssetDescriptor<Texture> GFX_BGD_STARS =
            new AssetDescriptor<>("gfx/backgrounds/starfield.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_LEVEL1 =
            new AssetDescriptor<>("gfx/backgrounds/level1.gif", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST1 =
            new AssetDescriptor<>("gfx/backgrounds/mist.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST2 =
            new AssetDescriptor<>("gfx/backgrounds/mist2.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST3 =
            new AssetDescriptor<>("gfx/backgrounds/mist3.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST4 =
            new AssetDescriptor<>("gfx/backgrounds/mist4.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST5 =
            new AssetDescriptor<>("gfx/backgrounds/mist5.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST6 =
            new AssetDescriptor<>("gfx/backgrounds/mist6.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_MIST7 =
            new AssetDescriptor<>("gfx/backgrounds/mist7.png", Texture.class);

    public static final AssetDescriptor<Texture> GFX_BGD_LEVEL2 =
            new AssetDescriptor<>("gfx/backgrounds/level2.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_BIG_PLANET =
            new AssetDescriptor<>("gfx/backgrounds/big-planet.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_FAR_PLANETS =
            new AssetDescriptor<>("gfx/backgrounds/far-planets.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_RISING_PLANETS =
            new AssetDescriptor<>("gfx/backgrounds/rising-planets.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_STARS_LEVEL2 =
            new AssetDescriptor<>("gfx/backgrounds/stars.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_CLOUDS =
            new AssetDescriptor<>("gfx/backgrounds/clouds.png", Texture.class);


    public static final AssetDescriptor<Texture> GFX_BGD_LEVEL3 =
            new AssetDescriptor<>("gfx/backgrounds/level3.jpg", Texture.class);

    // GFX
    public static final AssetDescriptor<TextureAtlas> GFX_LEVEL_COMMON = new AssetDescriptor<>("gfx/level-all-no-mask.atlas", TextureAtlas.class);
    public static final AssetDescriptor<TextureAtlas> GFX_LEVEL1 = new AssetDescriptor<>("gfx/level1-mask.atlas", TextureAtlas.class);
    public static final AssetDescriptor<TextureAtlas> GFX_LEVEL2 = new AssetDescriptor<>("gfx/level2-mask.atlas", TextureAtlas.class);
    public static final AssetDescriptor<TextureAtlas> GFX_LEVEL3 = new AssetDescriptor<>("gfx/level3-mask.atlas", TextureAtlas.class);

    // SOUNDS
    public static final AssetDescriptor<Sound> SOUND_LOSE_LIFE =
            new AssetDescriptor<>("sounds/loseLife.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_NEW_LIFE =
            new AssetDescriptor<>("sounds/newLife.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_READY =
            new AssetDescriptor<>("sounds/ready.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_GAME_OVER =
            new AssetDescriptor<>("sounds/game_over.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_SHIELD_BULLET =
            new AssetDescriptor<>("sounds/shield.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_SHIELD_UP =
            new AssetDescriptor<>("sounds/shieldUp.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_BOMB_DROP =
            new AssetDescriptor<>("sounds/bombDrop.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_BOMB_EXPLOSION =
            new AssetDescriptor<>("sounds/bombExplosion.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_EXPLOSION =
            new AssetDescriptor<>("sounds/explosion.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_BOSS_FINISHED =
            new AssetDescriptor<>("sounds/bossKilled.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_FIRE =
            new AssetDescriptor<>("sounds/fire.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_BOSS_ALERT =
            new AssetDescriptor<>("sounds/boss-alert.ogg", Sound.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_1 =
            new AssetDescriptor<>("sounds/level1.ogg", Music.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_2 =
            new AssetDescriptor<>("sounds/level2.ogg", Music.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_3 =
            new AssetDescriptor<>("sounds/level3.ogg", Music.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_1_BOSS =
            new AssetDescriptor<>("sounds/level1-boss.mid", Music.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_2_BOSS =
            new AssetDescriptor<>("sounds/level2-boss.mid", Music.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_3_BOSS =
            new AssetDescriptor<>("sounds/level3-boss.mid", Music.class);
    public static final AssetDescriptor<Sound> SOUND_POWER_UP =
            new AssetDescriptor<>("sounds/powerUp.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_POWER_UP_VOICE =
            new AssetDescriptor<>("sounds/powerUpVoice.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_FIRE_ENEMY =
            new AssetDescriptor<>("sounds/enemyFire.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_NEW_HIGH_SCORE =
            new AssetDescriptor<>("sounds/new_high_score.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_GO =
            new AssetDescriptor<>("sounds/go.ogg", Sound.class);
    // FONTS
    public static final AssetDescriptor<BitmapFont> FONT_SPACE_KILLER =
            new AssetDescriptor<>("font1.ttf", BitmapFont.class,
                    getFontParameters("fonts/arcade.ttf", 28));
    public static final AssetDescriptor<BitmapFont> FONT_SPACE_KILLER_LARGE =
            new AssetDescriptor<>("font2.ttf", BitmapFont.class,
                    getFontParameters("fonts/arcade.ttf", 100));
    public static final AssetDescriptor<BitmapFont> FONT_SPACE_KILLER_MEDIUM =
            new AssetDescriptor<>("font3.ttf", BitmapFont.class,
                    getFontParameters("fonts/regular.ttf", 35));
    public static final AssetDescriptor<BitmapFont> FONT_SPACE_KILLER_SMALL =
            new AssetDescriptor<>("font4.ttf", BitmapFont.class,
                    getFontParameters("fonts/regular.ttf", 25));
    public static final AssetDescriptor<BitmapFont> FONT_SPACE_KILLER_SMALLEST =
            new AssetDescriptor<>("font4.ttf", BitmapFont.class,
                    getFontParameters("fonts/regular.ttf", 20));

    private static final Map<Class<? extends Screen>, Set<AssetDescriptor>> assetsNeededByScreen = initializeAssetsNeededByScreen();

    private static FreeTypeFontLoaderParameter getFontParameters(String filename, int size) {
        FreeTypeFontLoaderParameter parameter = new FreeTypeFontLoaderParameter();
        parameter.fontFileName = filename;
        parameter.fontParameters.size = size;
        return parameter;
    }

    private static Map<Class<? extends Screen>, Set<AssetDescriptor>> initializeAssetsNeededByScreen() {
        Map<Class<? extends Screen>, Set<AssetDescriptor>> assets = new HashMap<>();
        assets.put(SplashScreen.class, Sets.newHashSet(
                SPLASH_MUSIC, SPLASH_ATLAS, SPLASH_TXT_LOGO
        ));
        assets.put(MenuScreen.class, Sets.newHashSet(
                MENU_BGD, FONT_SPACE_KILLER_LARGE, MENU_MUSIC, FONT_SPACE_KILLER_MEDIUM, MENU_CLICK,
                MENU_ATLAS, FONT_SPACE_KILLER_SMALL
        ));
        assets.put(Level1Screen.class, Sets.newHashSet(
                // MUSIC
                MUSIC_LEVEL_1, MUSIC_LEVEL_1_BOSS,
                // SOUNDS
                SOUND_READY, SOUND_FIRE, SOUND_EXPLOSION, SOUND_POWER_UP, SOUND_FIRE_ENEMY,
                SOUND_SHIELD_BULLET, SOUND_SHIELD_UP, SOUND_GAME_OVER, SOUND_LOSE_LIFE,
                SOUND_NEW_LIFE, SOUND_NEW_HIGH_SCORE, SOUND_GO, SOUND_BOSS_ALERT,
                SOUND_BOSS_FINISHED, SOUND_BOMB_DROP, SOUND_BOMB_EXPLOSION,
                SOUND_POWER_UP_VOICE,
                // GFX
                GFX_BGD_MIST1, GFX_BGD_MIST2, GFX_BGD_MIST3, GFX_BGD_MIST4, GFX_BGD_MIST5, GFX_BGD_MIST6, GFX_BGD_MIST7,
                GFX_LEVEL1, GFX_LEVEL_COMMON,
                ICON_GAME, ICON_GOOGLE,
                GFX_BGD_LEVEL1, GFX_BGD_STARS,
                // FONTS
                FONT_SPACE_KILLER, FONT_SPACE_KILLER_LARGE, FONT_SPACE_KILLER_SMALLEST,
                FONT_SPACE_KILLER_MEDIUM));
        assets.put(Level2Screen.class, Sets.newHashSet(
                // MUSIC
                MUSIC_LEVEL_2, MUSIC_LEVEL_2_BOSS,
                // SOUNDS
                SOUND_READY, SOUND_FIRE, SOUND_EXPLOSION, SOUND_POWER_UP, SOUND_FIRE_ENEMY,
                SOUND_SHIELD_BULLET, SOUND_SHIELD_UP, SOUND_GAME_OVER, SOUND_LOSE_LIFE,
                SOUND_NEW_LIFE, SOUND_NEW_HIGH_SCORE, SOUND_GO, SOUND_BOSS_ALERT,
                SOUND_BOSS_FINISHED, SOUND_BOMB_DROP, SOUND_BOMB_EXPLOSION,
                SOUND_POWER_UP_VOICE,
                // GFX
                GFX_BGD_MIST1, GFX_BGD_MIST2, GFX_BGD_MIST3, GFX_BGD_MIST4, GFX_BGD_MIST5, GFX_BGD_MIST6, GFX_BGD_MIST7, GFX_BGD_CLOUDS,
                GFX_LEVEL_COMMON, GFX_LEVEL2,
                ICON_GAME, ICON_GOOGLE,
                GFX_BGD_LEVEL2, GFX_BGD_BIG_PLANET, GFX_BGD_FAR_PLANETS, GFX_BGD_RISING_PLANETS, GFX_BGD_STARS_LEVEL2,
                // FONTS
                FONT_SPACE_KILLER, FONT_SPACE_KILLER_LARGE, FONT_SPACE_KILLER_MEDIUM, FONT_SPACE_KILLER_SMALLEST
        ));

        assets.put(Level3Screen.class, Sets.newHashSet(
                // MUSIC
                MUSIC_LEVEL_3, MUSIC_LEVEL_3_BOSS,
                // SOUNDS
                SOUND_READY, SOUND_FIRE, SOUND_EXPLOSION, SOUND_POWER_UP, SOUND_FIRE_ENEMY,
                SOUND_SHIELD_BULLET, SOUND_SHIELD_UP, SOUND_GAME_OVER, SOUND_LOSE_LIFE,
                SOUND_NEW_LIFE, SOUND_NEW_HIGH_SCORE, SOUND_GO, SOUND_BOSS_ALERT,
                SOUND_BOSS_FINISHED, SOUND_BOMB_DROP, SOUND_BOMB_EXPLOSION,
                SOUND_POWER_UP_VOICE,
                // GFX
                GFX_BGD_MIST1, GFX_BGD_MIST2, GFX_BGD_MIST3, GFX_BGD_MIST4, GFX_BGD_MIST5, GFX_BGD_MIST6, GFX_BGD_MIST7,
                GFX_LEVEL_COMMON, GFX_LEVEL3,
                ICON_GAME, ICON_GOOGLE,
                GFX_BGD_LEVEL3,
                // FONTS
                FONT_SPACE_KILLER, FONT_SPACE_KILLER_LARGE, FONT_SPACE_KILLER_MEDIUM, FONT_SPACE_KILLER_SMALLEST
        ));
        assets.put(TransitionScreen.class, emptySet());
        assets.put(SocialScoreScreen.class, emptySet());
        return assets;
    }

    public <T> T get(AssetDescriptor<T> descriptor) {
        return manager.get(descriptor.fileName);
    }

    public BitmapFontCache getFont(AssetDescriptor<BitmapFont> descriptor) {
        return new BitmapFontCache(manager.get(descriptor.fileName));
    }

    private AssetManager getAssetManager() {
        if (manager != null) {
            return manager;
        }
        if (resolver == null) {
            resolver = new ArchiveFileHandleResolver(Gdx.files.internal("gfx.dat"));
        }
        manager = new AssetManager();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        manager.setLoader(TextureAtlas.class, new TextureAtlasLoader(resolver));
        manager.setLoader(Texture.class, new TextureLoader(resolver));
        Texture.setAssetManager(manager);
        return manager;
    }

    public void loadResources(Class<? extends Screen> previousScreen, Class<? extends Screen> screen) {
        Set<AssetDescriptor> toUnload = previousScreen != null ? assetsNeededByScreen.get(previousScreen) : emptySet();
        Set<AssetDescriptor> toLoad = assetsNeededByScreen.get(screen);
        unloadResources(Sets.difference(toUnload, toLoad));
        loadResources(Sets.difference(toLoad, toUnload));
    }


    private void loadResources(Set<AssetDescriptor> assetDescriptors) {
        manager = getAssetManager();
        for (AssetDescriptor assetDescriptor : assetDescriptors) {
            manager.load(assetDescriptor);
        }
        manager.finishLoading();
    }

    private void unloadResources(Set<AssetDescriptor> assetDescriptors) {
        for (AssetDescriptor assetDescriptor : assetDescriptors) {
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

    public void playMusic(AssetDescriptor<Music> musicDescriptor) {
        playMusic(musicDescriptor, 1.0f);
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
