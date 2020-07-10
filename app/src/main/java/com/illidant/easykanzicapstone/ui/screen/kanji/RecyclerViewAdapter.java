package com.illidant.easykanzicapstone.ui.screen.kanji;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.illidant.easykanzicapstone.R;
import com.illidant.easykanzicapstone.domain.model.Kanji;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Kanji> mData;

    public RecyclerViewAdapter(Context mContext, List<Kanji> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_kanji_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_kanji_title.setText(mData.get(position).getTitle());
        holder.img_kanji_thumbnail.setText(mData.get(position).getThumbnail());

        //set click listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_kanji_title;
        TextView img_kanji_thumbnail;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_kanji_title = (TextView) itemView.findViewById(R.id.am_han_title_id);
            img_kanji_thumbnail = (TextView) itemView.findViewById(R.id.chu_han_text);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }
}
