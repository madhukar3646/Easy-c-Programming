package com.app.cprogramingapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cprogramingapp.R;
import com.app.cprogramingapp.Utils.ConnectionDetector;
import com.app.cprogramingapp.Utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExplainationActivity extends AppCompatActivity {

    @BindView(R.id.tv_explaination)
    TextView tv_explaination;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.banner)
    AdView adviewlayout;
    private ConnectionDetector cd;
    private boolean isInternetPresent = false;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explaination);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Easy C");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
       String explainationfilename=getIntent().getStringExtra("explainationfilename");
       String explaination_data= Utils.getStringFromAssestsFile(ExplainationActivity.this,explainationfilename+".txt");
       tv_explaination.setText(explaination_data);

        if (isInternetPresent) {
            displayAd();
        }
    }

    private void displayAd() {
        try {
            LinearLayout banner_ads_layout = (LinearLayout) findViewById(R.id.banner_ads_layout);
            banner_ads_layout.setVisibility(View.VISIBLE);
            adView = findViewById(R.id.banner);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } catch (Exception e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isInternetPresent) {
            if (adView != null) {
                adView.resume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isInternetPresent) {
            if (adView != null) {
                adView.pause();
            }
        }
    }


    @Override
    public void onDestroy() {
        if (isInternetPresent) {
            if (adView != null) {
                adView.destroy();
            }
        }

        super.onDestroy();
    }
}
