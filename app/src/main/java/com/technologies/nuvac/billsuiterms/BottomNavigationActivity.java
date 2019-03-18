package com.technologies.nuvac.billsuiterms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class BottomNavigationActivity extends AppCompatActivity {


    private ActionBar toolbar;
    String clientName;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.nav_staff:
                    toolbar.setTitle("View Staff");
                    toolbar.setHomeButtonEnabled(true);
                    fragment = new ViewStaffFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_purchase_report:
                    toolbar.setTitle("Purchase Report");
                    fragment = new PurchaseReportFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_sales_report:
                    toolbar.setTitle("Sale Report");
                    fragment = new SalesFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_vat:
                    toolbar.setTitle("VAT Report");
                    fragment = new VATFragment();
                loadFragment(fragment);
                return true;
                case R.id.nav_items:
                    toolbar.setTitle("Menu Items");
                    fragment = new ItemsFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
//        Bundle bundle=getIntent().getExtras();
//        clientName=bundle.getString("username");

        toolbar = getSupportActionBar();
        toolbar.setTitle("Admin");
        toolbar.setHomeButtonEnabled(true);
        loadFragment(new AdminPageFragment());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.dashboard:
                toolbar.setTitle("Dash Board");
                toolbar.setHomeButtonEnabled(true);
                fragment = new AdminPageFragment();
                Bundle bundle=new Bundle();
                bundle.putString("admin",clientName);
                fragment.setArguments(bundle);
                loadFragment(fragment);
                return true;
            case R.id.setting:
                Intent intent=new Intent(getApplicationContext(),ResetPasswordActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(BottomNavigationActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.drawable.ic_logout);
                builder.setMessage("Do you want to logout?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent(getApplication(),MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }

                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
