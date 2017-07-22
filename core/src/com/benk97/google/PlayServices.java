package com.benk97.google;

public interface PlayServices
{
    public void startGooglePlay();
    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement(Achievement achievement);
    public void submitScore(int highScore);
    public void showAchievement();
    public void showScore();
    public boolean isSignedIn();
}