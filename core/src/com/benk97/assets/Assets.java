package com.benk97.assets;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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


    public static Map<Class<? extends Screen>, List<AssetDescriptor>> assetsNeededByScreen = new HashMap<Class<? extends Screen>, List<AssetDescriptor>>() {{
        put(Level1Screen.class, Arrays.<AssetDescriptor>asList(
                SOUND_FIRE, SOUND_EXPLOSION, MUSIC_LEVEL_1,
                GFX_SOUCOUPE, GFX_SHIP_PLAYER, GFX_BGD_LEVEL1, GFX_BGD_STARS, GFX_BULLET, GFX_PAD_ARROW, GFX_PAD_BUTTON_FIRE, GFX_EXPLOSION));
    }};

    private AssetManager manager;

    public <T> T get(AssetDescriptor<T> descriptor) {
        return manager.get(descriptor.fileName);
    }

    public void loadResources(Class<? extends Screen> screen) {
        manager = new AssetManager();
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
