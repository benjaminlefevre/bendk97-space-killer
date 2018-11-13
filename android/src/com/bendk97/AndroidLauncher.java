/*
 * Developed by Benjamin Lefèvre
 * Last modified 29/09/18 22:09
 * Copyright (c) 2018. All rights reserved.
 */

package com.bendk97;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bendk97.google.Achievement;
import com.bendk97.google.FakePlayServices;
import com.bendk97.google.PlayServices;
import com.bendk97.google.play.GameHelper;
import com.bendk97.share.IntentShare;
import com.bendk97.space.killer.BuildConfig;
import com.bendk97.space.killer.R;
import com.google.android.gms.games.Games;

import static android.os.Build.*;
import static com.bendk97.SpaceKillerGameConstants.NO_GOOGLE;


public class AndroidLauncher extends AndroidApplication implements PlayServices, IntentShare {

    private GameHelper gameHelper;
    private final static int requestCode = 1;
    private SpaceKillerGame game = null;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private static final int INTENT_ACTIVITY = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureSpaceKillerConstants();
        if (!NO_GOOGLE) {
            setupGooglePlay();
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        AndroidApplicationConfiguration config = configureAppAndroid();
        game = new SpaceKillerGame(NO_GOOGLE ? new FakePlayServices() : this, this);
        initialize(game, config);
    }

    private AndroidApplicationConfiguration configureAppAndroid() {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        config.numSamples = 2;
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useWakelock = true;
        return config;
    }

    private void configureSpaceKillerConstants() {
        SpaceKillerGameConstants.DEBUG = BuildConfig.DEBUG_GAME;
        NO_GOOGLE = BuildConfig.GOOGLE_PLAY_SKIP;
        SpaceKillerGameConstants.VERSION = BuildConfig.VERSION_NAME;
        SpaceKillerGameConstants.SKIP_SPLASH = BuildConfig.SPLASH_SCREEN_SKIP;
        SpaceKillerGameConstants.ABI_DEVICE = getAbi();
    }

    private String getAbi() {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            // on newer Android versions, we'll return only the most important Abi version
            return SUPPORTED_ABIS[0];
        }
        else {
            // on pre-Lollip versions, we got only one Abi
            //noinspection deprecation
            return CPU_ABI;
        }
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
    protected void onStart() {
        super.onStart();
        if (!NO_GOOGLE) {
            gameHelper.onStart(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!NO_GOOGLE) {
            gameHelper.onStop();
        }
    }

    @Override
    public void startGooglePlay() {
        if (!NO_GOOGLE) {
            gameHelper.onStart(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!NO_GOOGLE) {
            gameHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void signIn() {
        try {
            runOnUiThread(() -> gameHelper.beginUserInitiatedSignIn());
        } catch (Exception e) {
            if (SpaceKillerGameConstants.DEBUG) {
                Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
            }
        }
    }

    @Override
    public void signOut() {
        try {
            runOnUiThread(() -> gameHelper.signOut());
        } catch (Exception e) {
            if (SpaceKillerGameConstants.DEBUG) {
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
            case KILL_BOSS_2:
                r = getString(R.string.achievement_kill_the_boss_of_the_level_2);
                break;
            case KILL_LASER_SHIP:
                r = getString(R.string.achievement_kill_a_laser_ship);
                break;
            case KILL_5_LASER_SHIPS:
                r = getString(R.string.achievement_kill_5_laser_ships);
                break;
            case KILL_BOSS_3:
                r = getString(R.string.achievement_kill_the_boss_of_the_level_3);
                break;
            default:
                return;
        }

        Games.Achievements.unlock(gameHelper.getApiClient(), r);
    }

    @Override
    public void submitScore(int highScore) {
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_highscores), highScore);
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
                    getString(R.string.leaderboard_highscores)), requestCode);
        } else {
            signIn();
        }
    }


    @Override
    public boolean isSignedIn() {
        return gameHelper.isSignedIn();
    }

    @Override
    public void shareScore(final String filePath) {
        Uri pictureUri = Uri.fromFile(Gdx.files.external(filePath).file());
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey look at my Space Killer score!\nAvailable on Google Play Store: https://play.google.com/store/apps/details?id=com.benk97.space.killer");
        sendIntent.setType("image/*");
        sendIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(sendIntent, "Space Killer - Share"), INTENT_ACTIVITY);
    }


    @Override
    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
