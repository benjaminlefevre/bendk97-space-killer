package com.benk97;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.benk97.ads.AdsController;
import com.benk97.google.Achievement;
import com.benk97.google.PlayServices;
import com.benk97.screens.MenuScreen;
import com.benk97.space.killer.R;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import static com.benk97.SpaceKillerGameConstants.DEBUG;


public class AndroidLauncher extends AndroidApplication implements AdsController, IUnityAdsListener, PlayServices {

    private final String UNITY_ADS_GAME_ID = "1487325";
    private GameHelper gameHelper;
    private final static int requestCode = 1;
    private SpaceKillerGame game = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAds();
        setupGooglePlay();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        config.numSamples = 2;
        game = new SpaceKillerGame(this, this);
        initialize(game, config);
    }

    private void setupAds() {
        UnityAds.initialize(this, UNITY_ADS_GAME_ID, this, SpaceKillerGameConstants.AD_TEST);
    }

    private void setupGooglePlay() {
        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(false);
        gameHelper.setMaxAutoSignInAttempts(0);
        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
            @Override
            public void onSignInFailed() {
                if (game != null) {
                    game.signInFailed();
                }
            }

            @Override
            public void onSignInSucceeded() {
                if (game != null) {
                    game.signInSucceeded();
                }
            }
        };
        gameHelper.setup(gameHelperListener);
    }


    @Override
    public void showInterstitialAd() {
        if (UnityAds.isReady()) {
            UnityAds.show(this);
        }
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                game.currentScreen.dispose();
                game.goToScreen(MenuScreen.class);
            }
        });
    }


    @Override
    public void onUnityAdsReady(String s) {

    }

    @Override
    public void onUnityAdsStart(String s) {

    }

    @Override
    public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
    }

    @Override
    public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        gameHelper.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameHelper.onStop();
    }

    @Override
    public void startGooglePlay() {
        gameHelper.onStart(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void signIn() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (Exception e) {
            if (DEBUG) {
                Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
            }
        }
    }

    @Override
    public void signOut() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameHelper.signOut();
                }
            });
        } catch (Exception e) {
            if (DEBUG) {
                Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
            }
        }
    }

    @Override
    public void rateGame() {
        String str = "https://play.google.com/store/apps/details?id=com.benk97.space.killer";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

    @Override
    public void unlockAchievement(Achievement achievement) {
        if (!isSignedIn()) {
            return;
        }
        String r = "";
        switch (achievement) {
            case KILL_50_ENEMIES:
                r = getString(R.string.achievement_kill_50_enemies);
                break;
            case KILL_100_ENEMIES:
                r = getString(R.string.achievement_kill_100_enemies);
                break;
            case KILL_BOSS:
                r = getString(R.string.achievement_kill_the_boss);
                break;
            case KILL_500_ENEMIES:
                r = getString(R.string.achievement_kill_500_enemies);
                break;
            case KILL_BOSS_WITHOUT_HAVING_LOSING_LIFE:
                r = getString(R.string.achievement_kill_the_boss_without_losing_life);
                break;
        }

        Games.Achievements.unlock(gameHelper.getApiClient(), r);
    }

    @Override
    public void submitScore(int highScore) {
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_highscore), highScore);
        }
    }

    @Override
    public void showAchievement() {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), requestCode);
        } else {
            signIn();
        }
    }

    @Override
    public void showScore() {
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_highscore)), requestCode);
        } else {
            signIn();
        }
    }


    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }
}
