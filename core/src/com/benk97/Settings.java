package com.benk97;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
    private static final String LIGHT_FX = "lightFX";
    private static final String MUSIC_ON = "music-on";
    private static final String SOUND_ON = "sound-on";
    private static final String HIGHSCORES = "highscores";
    private static final String CONTROLLER = "controller";

    private Preferences preferences;
    private int[] highscores;

    public static Settings settings = new Settings();

    private Settings() {
        preferences = Gdx.app.getPreferences("space-killer");
    }

    public static boolean isVirtualPad(){
        return settings.preferences.getString(CONTROLLER).equals("virtual");
    }

    public static void setRetroPad(){
        settings.preferences.putString(CONTROLLER, "retro");
        save();
    }

    public static void setVirtualPad(){
        settings.preferences.putString(CONTROLLER, "virtual");
        save();
    }

    public static void setMusicOn() {
        settings.preferences.putBoolean(MUSIC_ON, true);
        save();
    }

    public static void setMusicOff() {
        settings.preferences.putBoolean(MUSIC_ON, false);
        save();
    }

    public static boolean isMusicOn() {
        return settings.preferences.getBoolean(MUSIC_ON, true);
    }

    public static void setSoundOn() {
        settings.preferences.putBoolean(SOUND_ON, true);
        save();
    }

    public static void setSoundOff() {
        settings.preferences.putBoolean(SOUND_ON, false);
        save();
    }

    public static boolean isSoundOn() {
        return settings.preferences.getBoolean(SOUND_ON, true);
    }

    public static String getHighscoreString() {
        if (settings.highscores == null) {
            settings.loadHighScores();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; ++i) {
            sb.append(String.format("%7s", String.valueOf(settings.highscores[i])).replace(' ', '0')).append("\n\n");
        }
        return sb.toString();
    }

    public static boolean isLightFXEnabled() {
        return settings.preferences.getBoolean(LIGHT_FX, true);
    }

    public void loadHighScores() {
        String scorestr = settings.preferences.getString(HIGHSCORES, "0;0;0;0;0");
        String[] scores = scorestr.split(";");
        settings.highscores = new int[scores.length];
        for (int i = 0; i < scores.length; ++i) {
            settings.highscores[i] = Integer.valueOf(scores[i]);
        }
    }

    public static void addScore(int score) {
        if (settings.highscores == null) {
            settings.loadHighScores();
        }
        for (int i = 0; i < settings.highscores.length; ++i) {
            if (settings.highscores[i] < score) {
                for (int j = 4; j > i; j--)
                    settings.highscores[j] = settings.highscores[j - 1];
                settings.highscores[i] = score;
                break;
            }
        }
        String highscoreStr = "";
        for (int i = 0; i < settings.highscores.length; ++i) {
            highscoreStr += settings.highscores[i];
            highscoreStr += ";";
        }
        settings.preferences.putString(HIGHSCORES, highscoreStr);
        save();
    }

    public static int getHighscore() {
        if (settings.highscores == null) {
            settings.loadHighScores();
        }
        return settings.highscores[0];
    }

    public static void save() {
        settings.preferences.flush();
    }

    public static void changeLightFXEnabled() {
        settings.preferences.putBoolean(LIGHT_FX, !isLightFXEnabled());
        save();
    }
}
