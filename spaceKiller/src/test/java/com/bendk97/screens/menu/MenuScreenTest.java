/*
 * Developed by Benjamin Lefèvre
 * Last modified 04/11/18 09:17
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.screens.menu;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.StringBuilder;
import com.bendk97.Settings;
import com.bendk97.SpaceKillerGame;
import com.bendk97.assets.Assets;
import com.bendk97.google.FakePlayServices;
import com.bendk97.google.PlayServices;
import com.bendk97.runner.GdxTestRunner;
import com.bendk97.screens.levels.Level1Screen;
import com.bendk97.share.IntentShare;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;

import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.graphics.Color.YELLOW;
import static com.bendk97.assets.Assets.*;
import static com.bendk97.screens.menu.MenuScreen.WHITE_ALPHA_60;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class MenuScreenTest {

    @Mock
    private Assets assets;


    private PlayServices playServices = new FakePlayServices();

    @Mock
    private IntentShare intentShare;

    @Mock
    private SpriteBatch spriteBatch;

    @Mock
    private TextureAtlas atlas;

    private SpaceKillerGame game;
    private MenuScreen screen;

    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        when(assets.get(MENU_BGD)).thenReturn(mock(Texture.class));
        BitmapFont bitmapFont = new BitmapFont();
        new BitmapFontCache(bitmapFont);
        when(assets.get(FONT_SPACE_KILLER_SMALL)).thenReturn(bitmapFont);
        when(atlas.findRegion(anyString())).thenReturn(mock(TextureAtlas.AtlasRegion.class));
        when(assets.get(MENU_ATLAS)).thenReturn(atlas);
        when(assets.get(MENU_MUSIC)).thenReturn(mock(Music.class));

        game = spy(new SpaceKillerGame(playServices, intentShare));
        doNothing().when(game).goToScreen(any(Class.class));

        screen = new MenuScreen(assets, game, spriteBatch);

        Settings.setVirtualPad();
    }

    @Test
    public void game_starts_on_main_menu() {
        Set<Actor> actors = Sets.newHashSet(screen.stage.getActors());
        assertThat(actors).hasSize(10);
        // background image
        assertThat(actors.stream()
                .filter(actor -> Image.class.isAssignableFrom(actor.getClass())))
                .hasSize(1);
        // google play image buttons
        assertThat(actors.stream()
                .filter(actor -> ImageButton.class.isAssignableFrom(actor.getClass())))
                .hasSize(3);
        // sounds button
        assertThat(actors.stream()
                .filter(this::isRealTable))
                .hasSize(1);
        assertThat(actors.stream()
                .filter(this::isRealTable)
                .map(table -> ((Table) table).getCells())
                .flatMap(cells -> Arrays.stream(cells.toArray()))
                .map((Function<Cell, Actor>) Cell::getActor))
                .hasSize(2);
        // menu buttons
        assertThat(actors.stream()
                .filter(actor -> TextButton.class.isAssignableFrom(actor.getClass())))
                .hasSize(5);
        assertThat(actors.stream()
                .filter(actor -> TextButton.class.isAssignableFrom(actor.getClass()))
                .map(actor -> ((TextButton) actor).getLabel())
                .map(Label::getText)
                .map(StringBuilder::toString))
                .containsExactlyInAnyOrder("play", "settings", "help", "scores", "credits");
    }

    @Test
    public void player_clicks_play_button() {
        TextButton playButton = getButton("play");
        touch(playButton);
        verify(assets).playSound(MENU_CLICK);
        verify(assets.get(MENU_MUSIC)).stop();
        verify(game).goToScreen(Level1Screen.class);
    }

    @Test
    public void player_clicks_scores_button() {
        TextButton scoresButton = getButton("scores");
        touch(scoresButton);
        Set<Actor> actors = Sets.newHashSet(screen.stage.getActors());


        verify(assets).playSound(MENU_CLICK);
        assertThat(actors).hasSize(6);
        assertThat(actors.stream()
                .filter(actor -> TextButton.class.isAssignableFrom(actor.getClass()))
                .map(actor -> ((TextButton) actor).getLabel())
                .map(Label::getText)
                .map(StringBuilder::toString))
                .containsExactlyInAnyOrder(Settings.getHighScoreString());
    }

    @Test
    public void player_clicks_anywhere_on_scores_comes_back_to_main_menu() {
        TextButton scoresButton = getButton("scores");
        touch(scoresButton);
        reset(assets);
        TextButton scoresScreen = getButton(Settings.getHighScoreString());
        touch(scoresScreen);
        Set<Actor> actors = Sets.newHashSet(screen.stage.getActors());


        verify(assets, times(1)).playSound(MENU_CLICK);
        assertThat(actors).hasSize(10);
        assertThat(actors.stream()
                .filter(actor -> TextButton.class.isAssignableFrom(actor.getClass()))
                .map(actor -> ((TextButton) actor).getLabel())
                .map(Label::getText)
                .map(StringBuilder::toString))
                .containsExactlyInAnyOrder("play", "settings", "help", "scores", "credits");
    }

    @Test
    public void player_clicks_help_button() {
        TextButton help = getButton("help");
        touch(help);
        Set<Actor> actors = Sets.newHashSet(screen.stage.getActors());


        verify(assets).playSound(MENU_CLICK);
        verify(atlas).findRegion("help");
        assertThat(actors).hasSize(6);
        assertThat(actors.stream()
                .filter(actor -> ImageButton.class.isAssignableFrom(actor.getClass())))
                .hasSize(4);
    }

    @Test
    public void player_clicks_anywhere_on_help_comes_back_to_main_menu() {
        TextButton help = getButton("help");
        touch(help);
        reset(assets);
        ImageButton helpScreen = getImageInfoButton();
        touch(helpScreen);
        Set<Actor> actors = Sets.newHashSet(screen.stage.getActors());


        verify(assets, times(1)).playSound(MENU_CLICK);
        assertThat(actors).hasSize(10);
        assertThat(actors.stream()
                .filter(actor -> TextButton.class.isAssignableFrom(actor.getClass()))
                .map(actor -> ((TextButton) actor).getLabel())
                .map(Label::getText)
                .map(StringBuilder::toString))
                .containsExactlyInAnyOrder("play", "settings", "help", "scores", "credits");
    }

    @Test
    public void player_clicks_credits_button() {
        TextButton credits = getButton("credits");
        touch(credits);
        Set<Actor> actors = Sets.newHashSet(screen.stage.getActors());


        verify(assets).playSound(MENU_CLICK);
        verify(atlas).findRegion("credits");
        assertThat(actors).hasSize(6);
        assertThat(actors.stream()
                .filter(actor -> ImageButton.class.isAssignableFrom(actor.getClass())))
                .hasSize(4);
    }

    @Test
    public void player_clicks_anywhere_on_credits_comes_back_to_main_menu() {
        TextButton credits = getButton("credits");
        touch(credits);
        reset(assets);
        ImageButton creditsScreen = getImageInfoButton();
        touch(creditsScreen);
        Set<Actor> actors = Sets.newHashSet(screen.stage.getActors());


        verify(assets, times(1)).playSound(MENU_CLICK);
        assertThat(actors).hasSize(10);
        assertThat(actors.stream()
                .filter(actor -> TextButton.class.isAssignableFrom(actor.getClass()))
                .map(actor -> ((TextButton) actor).getLabel())
                .map(Label::getText)
                .map(StringBuilder::toString))
                .containsExactlyInAnyOrder("play", "settings", "help", "scores", "credits");
    }

    @Test
    public void player_clicks_settings_button() {
        TextButton settings = getButton("settings");
        touch(settings);
        Set<Actor> actors = Sets.newHashSet(screen.stage.getActors());
        verify(assets).playSound(MENU_CLICK);
        assertThat(actors).hasSize(10);
        assertThat(actors.stream()
                .filter(actor -> TextButton.class.isAssignableFrom(actor.getClass()))
                .map(actor -> ((TextButton) actor).getLabel())
                .map(Label::getText)
                .map(StringBuilder::toString))
                .containsExactlyInAnyOrder("retro pad", "virtual pad\n\n   / autofire", "light fx", "vibration", "back");

    }

    @Test
    public void player_clicks_pads_buttons_on_settings() {
        TextButton settings = getButton("settings");
        touch(settings);
        TextButton retroPad = getButton("retro pad");
        TextButton virtualPad = getButton("virtual pad\n\n   / autofire");
        Set<Actor> actors = Sets.newHashSet(screen.stage.getActors());

        reset(assets);
        touch(retroPad);
        verify(assets, times(1)).playSound(MENU_CLICK);
        assertThat(virtualPad.getStyle().fontColor).isEqualTo(WHITE);
        assertThat(retroPad.getStyle().fontColor).isEqualTo(YELLOW);
        assertThat(Settings.isVirtualPad()).isFalse();

        reset(assets);
        touch(retroPad);
        verify(assets, never()).playSound(MENU_CLICK);
        assertThat(virtualPad.getStyle().fontColor).isEqualTo(WHITE);
        assertThat(retroPad.getStyle().fontColor).isEqualTo(YELLOW);
        assertThat(Settings.isVirtualPad()).isFalse();

        reset(assets);
        touch(virtualPad);
        verify(assets, times(1)).playSound(MENU_CLICK);
        assertThat(virtualPad.getStyle().fontColor).isEqualTo(YELLOW);
        assertThat(retroPad.getStyle().fontColor).isEqualTo(WHITE);
        assertThat(Settings.isVirtualPad()).isTrue();

        reset(assets);
        touch(virtualPad);
        verify(assets, never()).playSound(MENU_CLICK);
        assertThat(virtualPad.getStyle().fontColor).isEqualTo(YELLOW);
        assertThat(retroPad.getStyle().fontColor).isEqualTo(WHITE);
        assertThat(Settings.isVirtualPad()).isTrue();

        assertThat(actors).hasSize(10);
        assertThat(actors.stream()
                .filter(actor -> TextButton.class.isAssignableFrom(actor.getClass()))
                .map(actor -> ((TextButton) actor).getLabel())
                .map(Label::getText)
                .map(StringBuilder::toString))
                .containsExactlyInAnyOrder("retro pad", "virtual pad\n\n   / autofire", "light fx", "vibration", "back");
    }


    @Test
    public void player_clicks_lightfx_on_settings() {
        TextButton settings = getButton("settings");
        touch(settings);
        reset(assets);
        TextButton lightFx = getButton("light fx");
        touch(lightFx);
        Set<Actor> actors = Sets.newHashSet(screen.stage.getActors());


        verify(assets, times(1)).playSound(MENU_CLICK);
        assertThat(lightFx.getStyle().fontColor).isEqualTo(WHITE);
        assertThat(Settings.isLightFXEnabled()).isFalse();
        touch(lightFx);
        assertThat(lightFx.getStyle().fontColor).isEqualTo(YELLOW);
        assertThat(Settings.isLightFXEnabled()).isTrue();
        assertThat(actors).hasSize(10);
        assertThat(actors.stream()
                .filter(actor -> TextButton.class.isAssignableFrom(actor.getClass()))
                .map(actor -> ((TextButton) actor).getLabel())
                .map(Label::getText)
                .map(StringBuilder::toString))
                .containsExactlyInAnyOrder("retro pad", "virtual pad\n\n   / autofire", "light fx", "vibration", "back");
    }


    @Test
    public void player_clicks_vibration_on_settings() {
        TextButton settings = getButton("settings");
        touch(settings);
        reset(assets);
        TextButton vibration = getButton("vibration");
        touch(vibration);
        Set<Actor> actors = Sets.newHashSet(screen.stage.getActors());


        verify(assets, times(1)).playSound(MENU_CLICK);
        assertThat(vibration.getStyle().fontColor).isEqualTo(WHITE);
        assertThat(Settings.isVibrationEnabled()).isFalse();
        touch(vibration);
        assertThat(vibration.getStyle().fontColor).isEqualTo(YELLOW);
        assertThat(Settings.isVibrationEnabled()).isTrue();
        assertThat(actors).hasSize(10);
        assertThat(actors.stream()
                .filter(actor -> TextButton.class.isAssignableFrom(actor.getClass()))
                .map(actor -> ((TextButton) actor).getLabel())
                .map(Label::getText)
                .map(StringBuilder::toString))
                .containsExactlyInAnyOrder("retro pad", "virtual pad\n\n   / autofire", "light fx", "vibration", "back");
    }

    @Test
    public void player_clicks_back_on_settings_comes_back_to_main_menu() {
        TextButton settings = getButton("settings");
        touch(settings);
        reset(assets);
        TextButton back = getButton("back");
        touch(back);
        Set<Actor> actors = Sets.newHashSet(screen.stage.getActors());


        verify(assets, times(1)).playSound(MENU_CLICK);
        assertThat(actors).hasSize(10);
        assertThat(actors.stream()
                .filter(actor -> TextButton.class.isAssignableFrom(actor.getClass()))
                .map(actor -> ((TextButton) actor).getLabel())
                .map(Label::getText)
                .map(StringBuilder::toString))
                .containsExactlyInAnyOrder("play", "settings", "help", "scores", "credits");
    }


    /*** CONVENIENCE METHODS ***/

    private void touch(Button playButton) {
        InputEvent event = new InputEvent();
        event.setType(InputEvent.Type.touchDown);
        playButton.fire(event);
    }

    private ImageButton getImageInfoButton() {
        return (ImageButton) Arrays.stream(screen.stage.getActors().toArray())
                .filter(actor -> ImageButton.class.isAssignableFrom(actor.getClass()))
                .filter(actor -> actor.getColor().equals(WHITE_ALPHA_60))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ImageButton not found on stage"));
    }

    private TextButton getButton(String text) {
        return (TextButton) Arrays.stream(screen.stage.getActors().toArray())
                .filter(actor -> TextButton.class.isAssignableFrom(actor.getClass()))
                .filter(actor -> ((TextButton) actor).getLabel().getText().toString().equals(text))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Button " + text + " not found on stage"));
    }

    private boolean isRealTable(Actor actor) {
        return Table.class.isAssignableFrom(actor.getClass())
                && !TextButton.class.isAssignableFrom(actor.getClass())
                && !ImageButton.class.isAssignableFrom(actor.getClass());
    }

}