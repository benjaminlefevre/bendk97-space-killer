/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {
    private static final String LIGHT_FX = "lightFX";
    private static final String MUSIC_ON = "music-on";
    private static final String SOUND_ON = "sound-on";
    private static final String HIGHSCORES = "highscores";
    private static final String CONTROLLER = "controller";
    private static final String VIBRATION = "vibration";

    private final Preferences preferences;
    private int[] highscores;

    private static final Settings settings = new Settings();

    private Settings() {
        preferences = Gdx.app.getPreferences("space-killer");
    }

    public static boolean isVirtualPad() {
        return settings.preferences.getString(CONTROLLER).equals("virtual");
    }

    public static void setRetroPad() {
        settings.preferences.putString(CONTROLLER, "retro");
        settings.preferences.flush();
    }

    public static void setVirtualPad() {
        settings.preferences.putString(CONTROLLER, "virtual");
        settings.preferences.flush();
    }

    public static void setMusicOn() {
        settings.preferences.putBoolean(MUSIC_ON, true);
        settings.preferences.flush();
    }

    public static void setMusicOff() {
        settings.preferences.putBoolean(MUSIC_ON, false);
        settings.preferences.flush();
    }

    public static boolean isMusicOn() {
        return settings.preferences.getBoolean(MUSIC_ON, true);
    }

    public static void setSoundOn() {
        settings.preferences.putBoolean(SOUND_ON, true);
        settings.preferences.flush();
    }

    public static void setSoundOff() {
        settings.preferences.putBoolean(SOUND_ON, false);
        settings.preferences.flush();
    }

    public static boolean isSoundOn() {
        return settings.preferences.getBoolean(SOUND_ON, true);
    }

    public static String getHighScoreString() {
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

    public static boolean isVibrationEnabled() {
        return settings.preferences.getBoolean(VIBRATION, true);
    }

    private void loadHighScores() {
        String scoreString = settings.preferences.getString(HIGHSCORES, "0;0;0;0;0");
        String[] scores = scoreString.split(";");
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
                System.arraycopy(settings.highscores, i, settings.highscores, i + 1, 4 - i);
                settings.highscores[i] = score;
                break;
            }
        }
        StringBuilder highScoreStr = new StringBuilder();
        for (int i = 0; i < settings.highscores.length; ++i) {
            highScoreStr.append(settings.highscores[i]);
            highScoreStr.append(";");
        }
        settings.preferences.putString(HIGHSCORES, highScoreStr.toString());
        settings.preferences.flush();
    }

    public static int getHighscore() {
        if (settings.highscores == null) {
            settings.loadHighScores();
        }
        return settings.highscores[0];
    }

    public static void changeLightFXEnabled() {
        settings.preferences.putBoolean(LIGHT_FX, !isLightFXEnabled());
        settings.preferences.flush();
    }


    public static void changeVibrationEnabled() {
        settings.preferences.putBoolean(VIBRATION, !isVibrationEnabled());
        settings.preferences.flush();
    }
}
