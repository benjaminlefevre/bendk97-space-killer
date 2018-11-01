/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/10/18 20:30
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.google;

public class FakePlayServices implements PlayServices {
    @Override
    public void startGooglePlay() {
        // dummy
    }

    @Override
    public void signIn() {
        // dummy
    }

    @Override
    public void signOut() {
        // dummy
    }

    @Override
    public void rateGame() {
        // dummy
    }

    @Override
    public void unlockAchievement(Achievement achievement) {
        // dummy
    }

    @Override
    public void submitScore(int highScore) {
        // dummy
    }

    @Override
    public void showAchievement() {
        // dummy
    }

    @Override
    public void showScore() {
        // dummy
    }

    @Override
    public boolean isSignedIn() {
        return false;
    }
}
