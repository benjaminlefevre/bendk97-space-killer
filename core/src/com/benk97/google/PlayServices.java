package com.benk97.google;

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