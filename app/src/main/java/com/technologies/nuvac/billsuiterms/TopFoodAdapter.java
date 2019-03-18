package com.technologies.nuvac.billsuiterms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by isthiishq on 12-03-2018.
 */

public class TopFoodAdapter extends RecyclerView.Adapter<TopFoodAdapter.MyViewHolder> {

    private Context mContext;
    private List<topFoodItems> FoodItemlist;

    public TopFoodAdapter(Context mContext, List<topFoodItems> foodItemlist) {
        this.mContext = mContext;
        FoodItemlist = foodItemlist;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list,null);
        MyViewHolder cv=new MyViewHolder(v);
        return cv;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        topFoodItems feedItems=FoodItemlist.get(position);

//        if(!TextUtils.isEmpty(feedItems.getIcon().toString())){
//            Glide.with(mContext).load(feedItems.getIcon())
//                    .error(R.drawable.if_caution_1055096)
//                    .placeholder(R.drawable.ic_featured_play_list_black_24dp)
//                    .into(holder.FoodIcon);
//        }
//        Glide.with(mContext).load(feedItems.getIcon())
//                .error(R.drawable.if_caution_1055096)
//                .placeholder(R.drawable.ic_featured_play_list_black_24dp)
//                .into(holder.FoodIcon);
        holder.FoodIcon.setImageBitmap(feedItems.getIcon());
        holder.Foodtitle.setText(feedItems.getFoodName());

    }

    @Override
    public int getItemCount() {
        return FoodItemlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView FoodIcon;
        TextView Foodtitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.FoodIcon=itemView.findViewById(R.id.topFoodIcon);
            this.Foodtitle=itemView.findViewById(R.id.topFoodName);

        }
    }
}
