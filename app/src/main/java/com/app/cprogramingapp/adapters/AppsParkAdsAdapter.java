package com.app.cprogramingapp.adapters;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.cprogramingapp.R;
import com.app.cprogramingapp.models.App;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Sridhar on 25/29/2018.
 */

public class AppsParkAdsAdapter extends RecyclerView.Adapter<AppsParkAdsAdapter.AppsViewHolder> {
    int width, height;
    List<App> moreappsList;
    Context context;
    private Animation animation;
    private OnAppClickListener onAppClickListener;

    public AppsParkAdsAdapter(Context context, List<App> moreappsList) {
        this.context = context;
        this.moreappsList = moreappsList;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        animation = new ScaleAnimation(1.0F, 0.9F, 1.0F, 0.9F,
                Animation.RELATIVE_TO_SELF, (float) 0.5,
                Animation.RELATIVE_TO_SELF, (float) 0.5);
        animation.setDuration(300);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
    }

    public void setOnAppClickListener(OnAppClickListener onAppClickListener)
    {
        this.onAppClickListener=onAppClickListener;
    }

    @Override
    public AppsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.apps_park_ads_adapter, parent, false);
        return new AppsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppsViewHolder holder, final int position) {

        holder.imgapp.getLayoutParams().height = (width / 4);
        holder.imgapp.getLayoutParams().width = width / 4;
        holder.layout_cardview.getLayoutParams().width=width/2;
        holder.txtappName.getLayoutParams().width = width / 3;
        holder.txtappName.setSelected(true);

        holder.txtappName.setText(moreappsList.get(position).getAppname());
        Glide.with(context.getApplicationContext()).load(moreappsList.get(position).getAppicon()).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into(holder.imgapp);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onAppClickListener!=null)
                    onAppClickListener.onAppClick(moreappsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return moreappsList.size();
    }

    public class AppsViewHolder extends RecyclerView.ViewHolder {
        TextView txtappName;
        ImageView imgapp;
        CardView layout_cardview;

        public AppsViewHolder(View itemView) {
            super(itemView);
            txtappName = (TextView) itemView.findViewById(R.id.txt);
            imgapp = (ImageView) itemView.findViewById(R.id.img);
            imgapp.startAnimation(animation);
            layout_cardview=(CardView) itemView.findViewById(R.id.layout_cardview);
        }
    }

    public interface OnAppClickListener
    {
       void onAppClick(App app);
    }
}
