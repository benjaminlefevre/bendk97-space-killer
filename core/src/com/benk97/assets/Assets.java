package com.benk97.assets;

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
import com.benk97.Settings;
import com.benk97.screens.Level1Screen;
import com.benk97.screens.MenuScreen;
import com.benk97.screens.SplashScreen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assets {

    // SPLASH SCREEN
    public static final AssetDescriptor<TextureAtlas> SPASH_ATLAS =
            new AssetDescriptor<TextureAtlas>("gfx/splashscreen.atlas", TextureAtlas.class);
    public static final AssetDescriptor<Texture> SPLASH_TXT_LOGO =
            new AssetDescriptor<Texture>("gfx/backgrounds/bendk97.png", Texture.class);
    public static final AssetDescriptor<Music> SPLASH_MUSIC =
            new AssetDescriptor<Music>("sounds/splash.ogg", Music.class);

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
    // GFX
    public static final AssetDescriptor<TextureAtlas> GFX_LEVEL1_ATLAS = new AssetDescriptor<TextureAtlas>("gfx/level1.atlas", TextureAtlas.class);
    // SOUNDS
    public static final AssetDescriptor<Sound> SOUND_EXPLOSION =
            new AssetDescriptor<Sound>("sounds/explosion.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_FIRE =
            new AssetDescriptor<Sound>("sounds/fire.ogg", Sound.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_1 =
            new AssetDescriptor<Music>("sounds/level1.mid", Music.class);
    public static final AssetDescriptor<Sound> SOUND_POWER_UP =
            new AssetDescriptor<Sound>("sounds/powerUp.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_POWER_UP_VOICE =
            new AssetDescriptor<Sound>("sounds/powerUpVoice.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_FIRE_ENEMY =
            new AssetDescriptor<Sound>("sounds/enemyFire.ogg", Sound.class);
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

    public static FreeTypeFontLoaderParameter getFontParameters(String filename, int size) {
        FreeTypeFontLoaderParameter parameter = new FreeTypeFontLoaderParameter();
        parameter.fontFileName = filename;
        parameter.fontParameters.size = size;
        return parameter;
    }

    public static Map<Class<? extends Screen>, List<AssetDescriptor>> assetsNeededByScreen = new HashMap<Class<? extends Screen>, List<AssetDescriptor>>() {{
        put(Level1Screen.class, Arrays.<AssetDescriptor>asList(
                SOUND_FIRE, SOUND_EXPLOSION, MUSIC_LEVEL_1, SOUND_POWER_UP, SOUND_FIRE_ENEMY,
                FONT_SPACE_KILLER, FONT_SPACE_KILLER_LARGE, FONT_SPACE_KILLER_MEDIUM, SOUND_POWER_UP_VOICE,
                GFX_BGD_LEVEL1, GFX_BGD_STARS, GFX_LEVEL1_ATLAS));
        put(SplashScreen.class, Arrays.<AssetDescriptor>asList(
                SPLASH_MUSIC, SPASH_ATLAS, SPLASH_TXT_LOGO
        ));
        put(MenuScreen.class, Arrays.<AssetDescriptor>asList(
                MENU_BGD, FONT_SPACE_KILLER_LARGE, MENU_MUSIC, FONT_SPACE_KILLER_MEDIUM, MENU_CLICK,
                MENU_ATLAS
        ));
    }};

    private AssetManager manager;


    public <T> T get(AssetDescriptor<T> descriptor) {
        return manager.get(descriptor.fileName);
    }

    public AssetManager getAssetManager() {
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
            manager.get(sound).play(volume);
        }
    }

    public void playMusic(AssetDescriptor<Music> music) {
        if (Settings.isMusicOn()) {
            manager.get(music).setLooping(true);
            manager.get(music).play();
        }
    }

}
