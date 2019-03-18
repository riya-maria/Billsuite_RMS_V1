package com.technologies.nuvac.billsuiterms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nuvac on 28/02/2018.
 */

class SalesAdapter extends BaseAdapter{
    Context mContext;
    List<salesItems> mSalesItems;
    LayoutInflater inflater;


    public SalesAdapter(Context mContext, List<salesItems> mSalesItems) {
        this.mContext = mContext;
        this.mSalesItems = mSalesItems;
    }



    @Override
    public int getCount() {
        return mSalesItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mSalesItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (inflater==null)
            inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView==null)
            convertView=inflater.inflate(R.layout.sales_items,null);

        TextView InvoiceNo=convertView.findViewById(R.id.txt_invoice_num);
        TextView Amount=convertView.findViewById(R.id.txt_amount);
        TextView Status=convertView.findViewById(R.id.txt_status);
        TextView StaffID=convertView.findViewById(R.id.txt_staff_id);

        salesItems items=mSalesItems.get(i);

        InvoiceNo.setText(items.getInvoiceNumber());
        Amount.setText(items.getAmount());
        Status.setText(items.getStatus());
        StaffID.setText(items.getStaffID());

        return convertView;
    }
}
