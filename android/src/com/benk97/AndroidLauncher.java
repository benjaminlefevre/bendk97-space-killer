package com.benk97;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.benk97.ads.AdsController;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AndroidLauncher extends AndroidApplication implements AdsController {

    private final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-1330140877736963/9186218336";
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAds();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        config.numSamples = 2;
        initialize(new SpaceKillerGame(this), config);
    }

    public void setupAds() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(INTERSTITIAL_AD_UNIT_ID);
        if (isNetworkConnected()) {
            loadAd();
        }
    }

    private void loadAd() {
        AdRequest.Builder builder = new AdRequest.Builder();
        if (SpaceKillerGameConstants.AD_TEST) {
            builder.addTestDevice("B87853C38FD08D637E7381A9DE1748A2");
        }
        AdRequest ad = builder.build();
        interstitialAd.loadAd(ad);
    }

    @Override
    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void showInterstitialAd(final Runnable then) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (then != null) {
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            Gdx.app.postRunnable(then);
                            if (isNetworkConnected()) {
                                loadAd();
                            }
                        }
                    });
                }
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    Gdx.app.postRunnable(then);
                    if (!interstitialAd.isLoading() && isNetworkConnected()) {
                        loadAd();
                    }
                }
            }
        });
    }


}
