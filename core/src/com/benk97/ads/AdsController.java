package com.benk97.ads;

public interface AdsController {
    boolean isNetworkConnected();

    void showInterstitialAd(Runnable then);

}