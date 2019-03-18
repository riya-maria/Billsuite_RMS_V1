package com.technologies.nuvac.billsuiterms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class StaffDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
//    String url=
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_details);
        Bundle bundle=getIntent().getExtras();
        String staffName=bundle.getString("Hotel Name");
        TextView name=findViewById(R.id.staffName);
        name.setText(staffName);


    }
}
