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
    public static final AssetDescriptor<Texture> SPLASH_TXT_HUMAN =
            new AssetDescriptor<Texture>("gfx/human_running.png", Texture.class);
    public static final AssetDescriptor<Texture> SPLASH_TXT_LOGO =
            new AssetDescriptor<Texture>("gfx/bendk97.png", Texture.class);
    public static final AssetDescriptor<Music> SPLASH_MUSIC =
            new AssetDescriptor<Music>("sounds/splash.ogg", Music.class);

    // MENU SCREEN
    public static final AssetDescriptor<Texture> MENU_BGD =
            new AssetDescriptor<Texture>("gfx/backgrounds/menu.jpg", Texture.class);
    public static final AssetDescriptor<Music> MENU_MUSIC =
            new AssetDescriptor<Music>("sounds/intro.mid", Music.class);
    public static final AssetDescriptor<Sound> MENU_CLICK =
            new AssetDescriptor<Sound>("sounds/click_menu.ogg", Sound.class);
    public static final AssetDescriptor<Texture> MENU_MUSIC_OFF =
            new AssetDescriptor<Texture>("gfx/music-off.png", Texture.class);
    public static final AssetDescriptor<Texture> MENU_MUSIC_ON =
            new AssetDescriptor<Texture>("gfx/music-on.png", Texture.class);
    public static final AssetDescriptor<Texture> MENU_SOUND_OFF =
            new AssetDescriptor<Texture>("gfx/sound-off.png", Texture.class);
    public static final AssetDescriptor<Texture> MENU_SOUND_ON =
            new AssetDescriptor<Texture>("gfx/sound-on.png", Texture.class);


    // BACKGROUNDS
    public static final AssetDescriptor<Texture> GFX_BGD_STARS =
            new AssetDescriptor<Texture>("gfx/backgrounds/starfield.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BGD_LEVEL1 =
            new AssetDescriptor<Texture>("gfx/backgrounds/level1.gif", Texture.class);
    // GFX
    public static final AssetDescriptor<Texture> GFX_PAD_ARROW =
            new AssetDescriptor<Texture>("gfx/pad.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_PAD_BUTTON_FIRE =
            new AssetDescriptor<Texture>("gfx/fire_button.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BULLET =
            new AssetDescriptor<Texture>("gfx/bullet.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BULLET2 =
            new AssetDescriptor<Texture>("gfx/bullet2.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_BULLET3 =
            new AssetDescriptor<Texture>("gfx/bullet3.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_SHIP_PLAYER =
            new AssetDescriptor<Texture>("gfx/player.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_SOUCOUPE =
            new AssetDescriptor<Texture>("gfx/soucoupe.png", Texture.class);
    public static final AssetDescriptor<Texture> GFX_EXPLOSION =
            new AssetDescriptor<Texture>("gfx/explosion2.png", Texture.class);
    // SOUNDS
    public static final AssetDescriptor<Sound> SOUND_EXPLOSION =
            new AssetDescriptor<Sound>("sounds/explosion.ogg", Sound.class);
    public static final AssetDescriptor<Sound> SOUND_FIRE =
            new AssetDescriptor<Sound>("sounds/fire.ogg", Sound.class);
    public static final AssetDescriptor<Music> MUSIC_LEVEL_1 =
            new AssetDescriptor<Music>("sounds/level1.mid", Music.class);
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
                SOUND_FIRE, SOUND_EXPLOSION, MUSIC_LEVEL_1,
                FONT_SPACE_KILLER, FONT_SPACE_KILLER_LARGE, FONT_SPACE_KILLER_MEDIUM,
                GFX_SOUCOUPE, GFX_SHIP_PLAYER, GFX_BGD_LEVEL1, GFX_BGD_STARS, GFX_BULLET, GFX_PAD_ARROW,
                GFX_BULLET2, GFX_BULLET3, GFX_PAD_BUTTON_FIRE, GFX_EXPLOSION));
        put(SplashScreen.class, Arrays.<AssetDescriptor>asList(
                SPLASH_MUSIC, SPLASH_TXT_HUMAN, SPLASH_TXT_LOGO
        ));
        put(MenuScreen.class, Arrays.<AssetDescriptor>asList(
                MENU_BGD, FONT_SPACE_KILLER_LARGE, MENU_MUSIC, FONT_SPACE_KILLER_MEDIUM, MENU_CLICK,
                MENU_MUSIC_OFF, MENU_MUSIC_ON, MENU_SOUND_OFF, MENU_SOUND_ON
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
        if(Settings.isSoundOn()) {
            manager.get(sound).play(volume);
        }
    }

    public void playMusic(AssetDescriptor<Music> music) {
        if(Settings.isMusicOn()) {
            manager.get(music).setLooping(true);
            manager.get(music).play();
        }
    }

}
