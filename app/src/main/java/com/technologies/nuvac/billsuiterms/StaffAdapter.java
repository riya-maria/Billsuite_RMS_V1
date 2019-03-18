package com.technologies.nuvac.billsuiterms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import java.util.List;

/**
 * Created by isthiishq on 14-02-2018.
 */

class StaffAdapter extends BaseAdapter{

    private Context mContext;
    private List<StaffDetailsItems> mitemsList;
    private LayoutInflater inflater;
    ImageLoader imageLoader = Controller.getInstance().getImageLoader();
    public StaffAdapter(Context mContext, List<StaffDetailsItems> itemsList) {
        this.mContext = mContext;
        this.mitemsList = itemsList;
    }

    @Override
    public int getCount() {
        return mitemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return mitemsList.get(i);
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
            convertView = inflater.inflate(R.layout.staff_items, null);
        if (imageLoader == null)
            imageLoader = Controller.getInstance().getImageLoader();
//        ImageView thumbNail = (ImageView) convertView
//                .findViewById(R.id.img_logo);
        //TextView ID=convertView.findViewById(R.id.)
        TextView StaffName = (TextView) convertView.findViewById(R.id.txt_name);
        TextView Address=convertView.findViewById(R.id.txt_address);
        TextView Gender=convertView.findViewById(R.id.txt_gender);
        TextView Phone=convertView.findViewById(R.id.txt_phone);
        TextView Designation=convertView.findViewById(R.id.txt_designation);
        ImageView Logo=convertView.findViewById(R.id.img_logo);
        StaffDetailsItems detailsItems = mitemsList.get(i);
        StaffName.setText(detailsItems.getName());
        Address.setText(detailsItems.getAddress());
        Gender.setText(detailsItems.getGender());
        Phone.setText(detailsItems.getPhoneNumber());
        Designation.setText(detailsItems.getDesignation());

//        byte[] imageAsBytes = Base64.decode(String.valueOf(detailsItems.getImage()), Base64.DEFAULT);
//        Logo.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        Logo.setImageBitmap(detailsItems.getImage());
        return convertView;
    }
}
