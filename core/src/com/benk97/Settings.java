package com.benk97;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Settings {
    public static boolean soundEnabled = true;
    public static boolean musicEnabled = true;
    public final static int[] highscores = new int[]{5000, 4000, 3000, 2000, 1000};
    public final static String file = ".space-killer";

    public static void load() {
        try {
            FileHandle filehandle = Gdx.files.external(file);

            String[] strings = filehandle.readString().split("\n");
            soundEnabled = Boolean.parseBoolean(strings[0]);
            musicEnabled = Boolean.parseBoolean(strings[1]);
            for (int i = 0; i < 5; i++) {
                highscores[i] = Integer.parseInt(strings[i + 2]);
            }
        } catch (Throwable e) {
            // :( It's ok we have defaults
        }
    }

    public static void save() {
        try {
            FileHandle filehandle = Gdx.files.external(file);

            filehandle.writeString(Boolean.toString(soundEnabled) + "\n", false);
            filehandle.writeString(Boolean.toString(musicEnabled) + "\n", true);
            for (int i = 0; i < 5; i++) {
                filehandle.writeString(Integer.toString(highscores[i]) + "\n", true);
            }
        } catch (Throwable e) {
        }
    }

    public static void addScore(int score) {
        for (int i = 0; i < 5; i++) {
            if (highscores[i] < score) {
                for (int j = 4; j > i; j--)
                    highscores[j] = highscores[j - 1];
                highscores[i] = score;
                break;
            }
        }
    }
}
