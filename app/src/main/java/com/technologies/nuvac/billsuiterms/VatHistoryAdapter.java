package com.technologies.nuvac.billsuiterms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by company ismi on 4/27/2018.
 */

public class VatHistoryAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context mContext;
    List<vatItems> mVatItemsList;
    public VatHistoryAdapter(Context mContext, List<vatItems> mVatItemsList) {
        this.mContext = mContext;
        this.mVatItemsList = mVatItemsList;
    }
    @Override
    public int getCount() {
        return mVatItemsList.size();
    }
    @Override
    public Object getItem(int position) {
        return mVatItemsList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.vat_history_items, null);
        TextView ID=convertView.findViewById(R.id.txtID);
        TextView BRANCH=convertView.findViewById(R.id.txtDetails);
        TextView FROM=convertView.findViewById(R.id.txtMessage);
        TextView TO=convertView.findViewById(R.id.txtSubmittedDate);
        TextView REG=convertView.findViewById(R.id.txtResponse);
        TextView STATUS=convertView.findViewById(R.id.txtStatus);
        vatItems items=mVatItemsList.get(position);
        ID.setText(items.getID());
        BRANCH.setText(items.getBranch());
        FROM.setText(items.getFromDate());
        TO.setText(items.getToDate());
        REG.setText(items.getRegNo());
        STATUS.setText(items.getStatus());
        return convertView;
    }
}
