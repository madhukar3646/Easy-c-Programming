package com.app.cprogramingapp.activities;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.cprogramingapp.R;
import com.app.cprogramingapp.Utils.ConnectionDetector;
import com.app.cprogramingapp.Utils.Utils;
import com.app.cprogramingapp.adapters.TitlesListAdapter;
import com.app.cprogramingapp.models.TitlesModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TitlesListAdapter.TitleClickListener{

    @BindView(R.id.recyclerview_titles)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private SearchView searchView;
    private List<TitlesModel> titlesModelArrayList=new ArrayList<>();
    private TitlesListAdapter titlesListAdapter;

    @BindView(R.id.banner)
    AdView adviewlayout;
    private ConnectionDetector cd;
    private boolean isInternetPresent = false;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        String data= Utils.getStringFromFile(MainActivity.this,R.raw.titleslist);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<TitlesModel>>(){}.getType();
        titlesModelArrayList = gson.fromJson(data, listType);
        Log.e("array length","is "+titlesModelArrayList.size());
        titlesListAdapter=new TitlesListAdapter(MainActivity.this,titlesModelArrayList);
        titlesListAdapter.setTitleClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(titlesListAdapter);

        if (isInternetPresent) {
            displayAd();
        }
    }

    @Override
    public void onTitleClick(TitlesModel model) {
        Intent intent=new Intent(MainActivity.this,ProgramActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)titlesModelArrayList);
        intent.putExtra("BUNDLE",args);
        intent.putExtra("index",model.getIndex());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.item_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                titlesListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                titlesListAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_search:

                return true;

            case R.id.item_share:

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String sAux = "\nLet me recommend you this application.Install the app and start to learn programming in c. It is very easy to learn programming using this *"+getString(R.string.app_name)+"* app.\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.app.cprogramingapp\n\n";
                share.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(share, "Share App"));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayExitDialog()
    {
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exitlayout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        RelativeLayout layout_rateus,layout_cancel,layout_exit,layout_share;
        layout_share=(RelativeLayout)dialog.findViewById(R.id.layout_share);
        layout_rateus=(RelativeLayout) dialog.findViewById(R.id.layout_rateus);
        layout_cancel=(RelativeLayout) dialog.findViewById(R.id.layout_cancel);
        layout_exit=(RelativeLayout) dialog.findViewById(R.id.layout_exit);

        layout_rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(cd.isConnectingToInternet())
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.app.cprogramingapp")));
                    else
                        Toast.makeText(MainActivity.this,"You've no internet connection. Please rate us after connected to internet.",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Log.e("rate us error",""+e.getMessage());
                }
            }
        });

        layout_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String sAux = "\nLet me recommend you this application.Install the app and start to learn programming in c. It is very easy to learn programming using this *"+getString(R.string.app_name)+"* app.\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.app.cprogramingapp\n\n";
                share.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(share, "Share App"));
            }
        });

        layout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        layout_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
    }

    @Override
    public void onBackPressed() {
        displayExitDialog();
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
