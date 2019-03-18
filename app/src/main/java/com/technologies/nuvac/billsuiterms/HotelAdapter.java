package com.technologies.nuvac.billsuiterms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by isthiishq on 15-02-2018.
 */

class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.MyViewHolder> {
    Context mContext;
    List<HotelList> mHotelList;
    public HotelAdapter(Context mContext, List<HotelList> mHotelList) {
        this.mContext = mContext;
        this.mHotelList = mHotelList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView HotelNames,StaffDetails;
        public MyViewHolder(View itemView) {
            super(itemView);
            HotelNames = itemView.findViewById(R.id.txt_hotel_name);
            StaffDetails = itemView.findViewById(R.id.view_staff);
//            StaffDetails.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mListener!=null){
//                        int position=getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION);
//                        mListener.onItemClick(position);
//
//                    }
//                }
//            });


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_items,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HotelList list=mHotelList.get(position);
        holder.HotelNames.setText(list.getHotelNames());

    }

    @Override
    public int getItemCount() {
        return mHotelList.size();
    }
}
