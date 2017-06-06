package com.berpesan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.berpesan.R;
import com.berpesan.model.TrendingSpam;
import java.util.ArrayList;

/**
 * Created by itdel on 4/3/17.
 */

public class Data extends RecyclerView.Adapter<Data.ViewHolder>{
    private ArrayList<TrendingSpam> trending_spam;
    Context context;

    public Data(ArrayList<TrendingSpam> trending_spam, Context context){
        this.trending_spam = trending_spam;
        this.context = context;
    }

    @Override
    public Data.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Data.ViewHolder viewHolder, final int i){
        viewHolder.tv_konten_spam.setText(trending_spam.get(i).getKonten_spam());
        viewHolder.tv_jlh_laporan.setText(trending_spam.get(i).getJlh_laporan() + " laporan");
    }

    @Override
    public int getItemCount(){
        return trending_spam.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_konten_spam, tv_jlh_laporan;
        public ViewHolder(View view){
            super(view);

            tv_konten_spam = (TextView)view.findViewById(R.id.tv_konten_spam) ;
            tv_jlh_laporan = (TextView)view.findViewById(R.id.tv_jlh_laporan);

        }
    }
}
