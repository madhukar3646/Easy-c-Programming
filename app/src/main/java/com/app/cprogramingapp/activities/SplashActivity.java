package com.app.cprogramingapp.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.cprogramingapp.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class SplashActivity extends AppCompatActivity {

    private InterstitialAd interstitialAd_entry;
    private boolean check_intent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (check_intent) {
                    ad_failed_intent();
                }
            }
        }, 8000);
        interstitialAd_entry_method();

    }

    private void interstitialAd_entry_method() {

        interstitialAd_entry = new InterstitialAd(SplashActivity.this);
        interstitialAd_entry.setAdUnitId(getResources().getString(R.string.interstitial_Ad_id_entry));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd_entry.loadAd(adRequest);
        this.check_intent = true;
        this.interstitialAd_entry.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (interstitialAd_entry.isLoaded() && check_intent) {
                    ads_success_intent();
                    check_intent = false;
                    interstitialAd_entry.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (check_intent) {
                            ad_failed_intent();
                        }
                    }
                }, 2000);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                SplashActivity.this.finish();
            }
        });
    }

    private void ads_success_intent() {
        this.check_intent = false;
        startActivity(new Intent(this, MainActivity.class));
    }

    private void ad_failed_intent() {
        this.check_intent = false;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
