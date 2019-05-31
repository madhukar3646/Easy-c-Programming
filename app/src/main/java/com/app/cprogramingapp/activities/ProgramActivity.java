package com.app.cprogramingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.cprogramingapp.R;
import com.app.cprogramingapp.Utils.ConnectionDetector;
import com.app.cprogramingapp.Utils.Utils;
import com.app.cprogramingapp.models.TitlesModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgramActivity extends AppCompatActivity {

    @BindView(R.id.tv_program)
    TextView tv_program;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_output)
    TextView tv_output;
    @BindView(R.id.tv_explainationtitle)
    TextView tv_explainationtitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_previous)
    TextView tv_previous;
    @BindView(R.id.tv_next)
    TextView tv_next;

    private TitlesModel model;
    private int totalsize,selectedpos;
    private List<TitlesModel> titlesModelArrayList;

    @BindView(R.id.banner)
    AdView adviewlayout;
    private ConnectionDetector cd;
    private boolean isInternetPresent = false;
    private String program_data,output_data,explainationdata;
    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        titlesModelArrayList=(ArrayList<TitlesModel>) args.getSerializable("ARRAYLIST");
        totalsize=titlesModelArrayList.size();
        selectedpos=intent.getIntExtra("index",0);
        if(selectedpos<=0)
        {
            selectedpos=0;
            tv_next.setVisibility(View.VISIBLE);
            tv_previous.setVisibility(View.GONE);
        }
        else if(selectedpos>=(totalsize-1)) {
            selectedpos =totalsize-1;
            tv_next.setVisibility(View.GONE);
            tv_previous.setVisibility(View.VISIBLE);
        }
        else {
            tv_next.setVisibility(View.VISIBLE);
            tv_previous.setVisibility(View.VISIBLE);
        }
        setDataToScreen(selectedpos);

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

       //model=(TitlesModel) getIntent().getSerializableExtra("Titleobj");

       tv_explainationtitle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (isInternetPresent) {
                   if (mInterstitialAd.isLoaded()) {
                       mInterstitialAd.show();
                   } else {
                       Log.d("TAG", "The interstitial wasn't loaded yet.");
                   }
               }
               Intent intent=new Intent(ProgramActivity.this,ExplainationActivity.class);
               intent.putExtra("explainationfilename",model.getExplainationfilename());
               startActivity(intent);
           }
       });

       tv_previous.setText("<--Previous");
       tv_next.setText("Next-->");
       tv_previous.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               selectedpos=selectedpos-1;
               if(selectedpos<=0)
               {
                   selectedpos=0;
                   tv_next.setVisibility(View.VISIBLE);
                   tv_previous.setVisibility(View.GONE);
               }
               else {
                   tv_next.setVisibility(View.VISIBLE);
                   tv_previous.setVisibility(View.VISIBLE);
               }
               setDataToScreen(selectedpos);
           }
       });

       tv_next.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
             selectedpos=selectedpos+1;
             if(selectedpos>=(totalsize-1)) {
                 selectedpos =totalsize-1;
                 tv_next.setVisibility(View.GONE);
                 tv_previous.setVisibility(View.VISIBLE);
             }
             else {
                 tv_next.setVisibility(View.VISIBLE);
                 tv_previous.setVisibility(View.VISIBLE);
             }
              setDataToScreen(selectedpos);
           }
       });

        if (isInternetPresent) {
            mInterstitialAd_setup();
            displayAd();
        }
    }

    private void setDataToScreen(int pos)
    {
        model=titlesModelArrayList.get(pos);
        program_data= Utils.getStringFromAssestsFile(ProgramActivity.this,model.getProgramfilename()+".txt");
        output_data= Utils.getStringFromAssestsFile(ProgramActivity.this,model.getOutputfilename()+".txt");
        explainationdata= Utils.getStringFromAssestsFile(ProgramActivity.this,model.getExplainationfilename()+".txt");
        tv_program.setText(program_data);
        tv_title.setText(""+(pos+1)+"). "+model.getTitle());
        tv_output.setText(output_data);
        if(explainationdata==null || explainationdata.trim().length()==0)
            tv_explainationtitle.setVisibility(View.GONE);
        else
            tv_explainationtitle.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sharemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_share:

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String sAux = "\n\n *"+getString(R.string.app_name)+"* \n\n";
                sAux = sAux+"*"+model.getTitle()+"* \n\n";
                sAux = sAux +program_data+"\n\n";
                sAux = sAux +"*Input and Output:*"+"\n\n";
                sAux = sAux +output_data+"\n\n";
                sAux = sAux +"*Explaination:*"+"\n\n";
                sAux = sAux +explainationdata+"\n\n";
                sAux = sAux+"Let me recommend you this application.Install the app and start to learn programming in c. It is very easy to learn programming using this *"+getString(R.string.app_name)+"* app.\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.app.cprogramingapp\n\n";
                share.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(share, "Share this program"));

                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private void mInterstitialAd_setup() {
        mInterstitialAd = new InterstitialAd(ProgramActivity.this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_explaination_ad));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
}
