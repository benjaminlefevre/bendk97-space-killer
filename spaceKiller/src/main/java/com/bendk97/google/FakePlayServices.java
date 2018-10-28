/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/10/18 20:30
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97.google;

public class FakePlayServices implements PlayServices {
    @Override
    public void startGooglePlay() {

    }

    @Override
    public void signIn() {

    }

    @Override
    public void signOut() {

    }

    @Override
    public void rateGame() {

    }

    @Override
    public void unlockAchievement(Achievement achievement) {

    }

    @Override
    public void submitScore(int highScore) {

    }

    @Override
    public void showAchievement() {

    }

    @Override
    public void showScore() {

    }

    @Override
    public boolean isSignedIn() {
        return false;
    }
}
