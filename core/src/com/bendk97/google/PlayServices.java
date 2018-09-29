/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 21:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.google;

public interface PlayServices
{
    void startGooglePlay();
    void signIn();
    void signOut();
    void rateGame();
    void unlockAchievement(Achievement achievement);
    void submitScore(int highScore);
    void showAchievement();
    void showScore();
    boolean isSignedIn();
}