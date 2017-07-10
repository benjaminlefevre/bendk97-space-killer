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
import com.benk97.screens.Level1Screen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assets {

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
            new AssetDescriptor<BitmapFont>("fonts/arcade.ttf", BitmapFont.class,
                    getFontParameters());

    public static FreeTypeFontLoaderParameter getFontParameters(){
        FreeTypeFontLoaderParameter parameter = new FreeTypeFontLoaderParameter();
        parameter.fontFileName = "fonts/arcade.ttf";
        parameter.fontParameters.size = 28;
        return parameter;
    }

    public static Map<Class<? extends Screen>, List<AssetDescriptor>> assetsNeededByScreen = new HashMap<Class<? extends Screen>, List<AssetDescriptor>>() {{
        put(Level1Screen.class, Arrays.<AssetDescriptor>asList(
                SOUND_FIRE, SOUND_EXPLOSION, MUSIC_LEVEL_1,
                FONT_SPACE_KILLER,
                GFX_SOUCOUPE, GFX_SHIP_PLAYER, GFX_BGD_LEVEL1, GFX_BGD_STARS, GFX_BULLET, GFX_PAD_ARROW, GFX_PAD_BUTTON_FIRE, GFX_EXPLOSION));
    }};

    private AssetManager manager;


    public <T> T get(AssetDescriptor<T> descriptor) {
        return manager.get(descriptor.fileName);
    }

    public AssetManager getAssetManager(){
        AssetManager manager = new AssetManager();;
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
        manager.get(sound).play();
    }

    public void playMusic(AssetDescriptor<Music> music){
        manager.get(music).play();
    }

}
