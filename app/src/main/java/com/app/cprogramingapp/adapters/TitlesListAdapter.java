package com.app.cprogramingapp.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.app.cprogramingapp.R;
import com.app.cprogramingapp.models.TitlesModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 4/20/2017.
 */

public class TitlesListAdapter extends RecyclerView.Adapter<TitlesListAdapter.MyViewHolder> implements Filterable
{
    private Activity context;
    private List<TitlesModel> titlesModelArrayList,titlesModelArrayListFiltered;
    private TitleClickListener titleClickListener;
    public TitlesListAdapter(Activity context,List<TitlesModel> titlesModelArrayList)
    {
        this.context=context;
        this.titlesModelArrayList=titlesModelArrayList;
        this.titlesModelArrayListFiltered=titlesModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.titlemodel, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        final MyViewHolder myViewHolder=holder;
        final TitlesModel model=titlesModelArrayListFiltered.get(position);
        holder.tv_name.setText(""+(position+1)+"). "+model.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(titleClickListener!=null)
                  titleClickListener.onTitleClick(model);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return titlesModelArrayListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    titlesModelArrayListFiltered = titlesModelArrayList;
                } else {
                    List<TitlesModel> filteredList = new ArrayList<>();
                    for (TitlesModel row : titlesModelArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    titlesModelArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = titlesModelArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                titlesModelArrayListFiltered = (ArrayList<TitlesModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_name;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            tv_name=(TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    public void setTitleClickListener(TitleClickListener titleClickListener)
    {
      this.titleClickListener=titleClickListener;
    }
    public interface TitleClickListener
    {
        void onTitleClick(TitlesModel model);
    }
}
