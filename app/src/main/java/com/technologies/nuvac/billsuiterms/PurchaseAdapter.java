package com.technologies.nuvac.billsuiterms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nuvac on 20/02/2018.
 */

class PurchaseAdapter extends BaseAdapter{
     Context mContext;
     LayoutInflater inflater;
     List<purchaseItems> purchaseItemsList;

    public PurchaseAdapter(Context mContext, List<purchaseItems> purchaseItemsList) {

        this.mContext = mContext;
        this.purchaseItemsList = purchaseItemsList;
    }

    @Override
    public int getCount() {
        return purchaseItemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return purchaseItemsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.purchase_items, null);

        TextView Invoice = (TextView) convertView.findViewById(R.id.txt_invoice_num);
        TextView Amount = (TextView) convertView.findViewById(R.id.txt_amount);
        TextView PaymentMode = (TextView) convertView.findViewById(R.id.txt_payment_mode);
        //TextView Remark = (TextView) convertView.findViewById(R.id.txt_remark);
        //TextView DocName = (TextView) convertView.findViewById(R.id.txt_document_name);
        TextView BrandID = (TextView) convertView.findViewById(R.id.txt_brand_id);

        purchaseItems items=purchaseItemsList.get(i);

        Invoice.setText(items.getInvoiceNum());
        Amount.setText(items.getAmount());
        PaymentMode.setText(items.getPaymentMode());
//        Remark.setText(items.getRemark());
       // DocName.setText(items.getDocumentName());
        BrandID.setText(items.getBrandID());

        return convertView;
    }
}
