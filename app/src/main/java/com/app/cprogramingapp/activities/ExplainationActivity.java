package com.app.cprogramingapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cprogramingapp.R;
import com.app.cprogramingapp.Utils.ConnectionDetector;
import com.app.cprogramingapp.Utils.RetrofitApis;
import com.app.cprogramingapp.Utils.Utils;
import com.app.cprogramingapp.adapters.AppsParkAdsAdapter;
import com.app.cprogramingapp.models.App;
import com.app.cprogramingapp.models.PlaystoreappslistingResponse;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExplainationActivity extends AppCompatActivity implements AppsParkAdsAdapter.OnAppClickListener{

    @BindView(R.id.tv_explaination)
    TextView tv_explaination;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.banner)
    AdView adviewlayout;
    private ConnectionDetector cd;
    private boolean isInternetPresent = false;
    private AdView adView;

    private RelativeLayout more_apps_lay_out;
    private RecyclerView more_app_recycler_view;
    private Dialog dialog;
    private AppsParkAdsAdapter appsParkAdsAdapter;

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

        dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.servicecall_loading);
        ImageView iv_loader=(ImageView)dialog.findViewById(R.id.iv_loader);
        Glide.with(getApplicationContext()).load(R.drawable.loading_icon).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into(iv_loader);
        dialog.setCancelable(false);

        more_apps_lay_out=(RelativeLayout)findViewById(R.id.more_apps_lay_out);
        more_apps_lay_out.setVisibility(View.GONE);
        more_app_recycler_view=(RecyclerView)findViewById(R.id.more_app_recycler_view);
        more_app_recycler_view.setNestedScrollingEnabled(false);
        more_app_recycler_view.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

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
            getPlaystoreApps();
        }
    }

    public void getPlaystoreApps(){
        Call<PlaystoreappslistingResponse> call= RetrofitApis.Factory.create(ExplainationActivity.this).getAppsList();
        dialog.show();
        call.enqueue(new Callback<PlaystoreappslistingResponse>() {
            @Override
            public void onResponse(Call<PlaystoreappslistingResponse> call, Response<PlaystoreappslistingResponse> response) {
                if(dialog!=null)
                    dialog.dismiss();
                if(response.isSuccessful()){
                    PlaystoreappslistingResponse playstoreappslistingResponse=response.body();
                    if(playstoreappslistingResponse!=null)
                    {
                        List<App> appslist=playstoreappslistingResponse.getApps();
                        appsParkAdsAdapter=new AppsParkAdsAdapter(ExplainationActivity.this,appslist);
                        appsParkAdsAdapter.setOnAppClickListener(ExplainationActivity.this);
                        more_app_recycler_view.setAdapter(appsParkAdsAdapter);
                        more_apps_lay_out.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PlaystoreappslistingResponse> call, Throwable t) {
                if(dialog!=null)
                    dialog.dismiss();
            }
        });
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

    @Override
    public void onAppClick(App app) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(app.getAppurl())));
    }
}
