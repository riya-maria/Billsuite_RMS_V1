package com.technologies.nuvac.billsuiterms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by isthiishq on 19-02-2018.
 */

class CategoryAdapter extends BaseAdapter{
    LayoutInflater inflater;
    Context mContext;
    List<catItems> mCatItemsList;

    public CategoryAdapter(Context mContext, List<catItems> mCatItemsList) {
        this.mContext = mContext;
        this.mCatItemsList = mCatItemsList;
    }

    @Override
    public int getCount() {
        return mCatItemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return mCatItemsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (inflater == null)
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.category_items, null);

        TextView MenuName =  view.findViewById(R.id.txt_menu_item_name);
        TextView Price1 =  view.findViewById(R.id.price1);
        TextView Price2 = view.findViewById(R.id.price2);
        TextView Price3 =  view.findViewById(R.id.price3);


        catItems items=mCatItemsList.get(i);

        MenuName.setText(items.getMenuNames());
        Price1.setText(items.getPcat1());
        Price2.setText(items.getPcat2());
        Price3.setText(items.getPcat3());
        return view;
    }

}
