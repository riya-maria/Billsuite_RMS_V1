package com.technologies.nuvac.billsuiterms;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminPageFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private View view;
    private TextView from;
    private TextView to;
    TextView Client;
    String adminFROM,adminTO,adminSpinner,TotalSale,TotalPurchase,TotalProfit,TotalVatCollected,TotalVatRemitted,TotalGov,USERNAME,usernameHolder,PASSWORD,passwordHolder;
    private TextView saleDetails,purchaseDetails,profitDetails,cDetails,rDetails,govDetails;
    ArrayList<adminItems> itemsArrayList=new ArrayList<>();
    private Spinner spinner;
    ArrayList<String> hotel_list;
    String ClientName;
    String url="http://billsuite.nuvactech.com/arabian/ios_hotel.php";
    String adminUrl="http://billsuite.nuvactech.com/arabian/ios_new_admin_index1.php";
    private Button submit;
    RecyclerView recyclerView;
    TopFoodAdapter topFoodAdapter;
    List<topFoodItems> mFoodItems;
    //List<String> recyclerFood=new ArrayList<>();
    ProgressDialog progressDialog;
//    ProgressBar progressBar;
    String topUrl="http://billsuite.nuvactech.com/arabian/ios_new_admin_dashbbbooaard.php";
    Boolean Checkbox;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AdminPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment AdminPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminPageFragment newInstance() {
        AdminPageFragment fragment = new AdminPageFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        USERNAME=sharedpreferences.getString("username",usernameHolder);
        PASSWORD=sharedpreferences.getString("password",passwordHolder);
        view=inflater.inflate(R.layout.fragment_admin_page, container, false);
        from=view.findViewById(R.id.editFrom);
        to=view.findViewById(R.id.editTo);
        Client=view.findViewById(R.id.client_name);
        Client.setText(ClientName);
        hotel_list=new ArrayList<>();
        spinner=view.findViewById(R.id.spinnerHotel);
        submit=view.findViewById(R.id.submitButton);
        saleDetails=view.findViewById(R.id.sales_values);
        purchaseDetails=view.findViewById(R.id.purchase_values);
        profitDetails=view.findViewById(R.id.profit_values);
        cDetails=view.findViewById(R.id.cVat_values);
        rDetails=view.findViewById(R.id.rVat_values);
        govDetails=view.findViewById(R.id.gov_values);
        recyclerView=view.findViewById(R.id.foodRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setHasFixedSize(true);
        TopFoodDetails();
//        progressBar=view.findViewById(R.id.progress);
        from.setOnClickListener(this);
        to.setOnClickListener(this);
        LoadHotelSpinner(USERNAME,PASSWORD);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAdminDetails();
            }
        });
        getDetails(USERNAME,PASSWORD);
        return view;

    }

    private void TopFoodDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Top Delivered Food");
        progressDialog.show();

        StringRequest request=new StringRequest(Request.Method.GET, topUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("Food");
                    List<topFoodItems> mFoodItems=new ArrayList<>();
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);

                        topFoodItems items=new topFoodItems();

                        String FoodIcon=object.getString("image");
                        String FoodName=object.getString("name");
                        String[] x=FoodIcon.split(",");
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bitmap1.compress(Bitmap.CompressFormat.PNG,50,baos);
                        byte[] decodedString= Base64.decode(x[1], Base64.DEFAULT);
//
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);

//                        decodedByte.compress(Bitmap.CompressFormat.PNG,50,baos);
                        items.setIcon(decodedByte);
                        items.setFoodName(FoodName);

                        mFoodItems.add(items);
                        topFoodAdapter = new TopFoodAdapter(getContext(),mFoodItems);
                        recyclerView.setAdapter(topFoodAdapter);
                    }

                    progressDialog.dismiss();

                }catch (Exception e){
                    Log.i("myTag", e.toString());
                    e.printStackTrace();
                }
//

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String>topFood=new HashMap<>();
//                topFood.put("username",USERNAME);
//                topFood.put("password",PASSWORD);
//                return topFood;
//            }
//        };
        RequestQueue queue=Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void getAdminDetails() {
        adminFROM=from.getText().toString();
        adminTO=to.getText().toString();
        adminSpinner=spinner.getSelectedItem().toString();
        if (TextUtils.isEmpty(adminFROM)||TextUtils.isEmpty(adminTO)||TextUtils.isEmpty(adminSpinner)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Fill All the Fields");
            builder.setCancelable(true);
            builder.create().show();
        }else {

            AdminTask(adminFROM,adminTO,adminSpinner,USERNAME,PASSWORD);
        }

    }

    private void AdminTask(final String adminFROM, final String adminTO, final String adminSpinner, final String username, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, adminUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject object=jsonObject.getJSONObject("message");
                    TotalSale=object.getString("sales");
                    TotalPurchase=object.getString("purchase");
                    TotalProfit=object.getString("profit");
                    TotalVatCollected=object.getString("collectedvat");
                    TotalVatRemitted=object.getString("remittedvat");
                    TotalGov=object.getString("gov");

                    saleDetails.setText("     Total Sales               :     "+TotalSale);
                    purchaseDetails.setText("    Total Purchase         :    "+TotalPurchase);
                    profitDetails.setText("    Total Profit                 :   "+TotalProfit);
                    cDetails.setText("    Total VAT Collected  :   "+TotalVatCollected);
                    rDetails.setText("    Total VAT Remitted  :    "+TotalVatRemitted);
                    govDetails.setText("   Total GOV                    :   "+TotalGov);


//                 }
             }catch (Exception e){

                    e.getMessage().equals("Invalid date");

//                    getActivity().finish();


//                    Intent i = new Intent(getActivity().finish());
//                    // Closing all previous activities
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
                 e.printStackTrace();
             }
             progressDialog.dismiss();

         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
             VolleyLog.d(TAG, "Error: " + error.getMessage());
             Toast.makeText(getActivity(),
                     error.getMessage(), Toast.LENGTH_SHORT).show();
//             hidepDialog();
         }
     }){
         protected Map<String, String> getParams() {
             Map<String, String> adminDetail = new HashMap<String, String>();
             adminDetail.put("from", adminFROM); //Add the data you'd like to send to the server.
             adminDetail.put("to", adminTO);
             adminDetail.put("client", adminSpinner);
             adminDetail.put("username",username);
             adminDetail.put("password",password);
             return adminDetail;

         }
     };
     RequestQueue queue=Volley.newRequestQueue(getActivity());
     queue.add(request);
    }

//
    @Override
    public void onClick(View view) {
        if (view==from){
            // Get Current From Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            from.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (view==to){
            // Get Current To Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);


            final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {


                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

//                           datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                            to.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }


    private void getDetails(final String username, final String password) {
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        final StringRequest request=new StringRequest(Request.Method.POST, adminUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String getSale="";
                    String getPurchase="";
                    String getProfit="";
                    String getCVAT="";
                    String getRVAT="";
                    String getGOV="";
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String sale=jsonObject.getString("sales");
                        String purchase=jsonObject.getString("purchase");
                        String profit=jsonObject.getString("profit");
                        String cVAT=jsonObject.getString("collectedvat");
                        String rVAT=jsonObject.getString("remittedvat");
                        String gov=jsonObject.getString("gov");
                        getSale=sale;
                        getPurchase=purchase;
                        getProfit=profit;
                        getCVAT=cVAT;
                        getRVAT=rVAT;
                        getGOV=gov;
                    }
                    saleDetails.setText("     Total Sales               :     "+getSale);
                    purchaseDetails.setText("    Total Purchase         :    "+getPurchase);
                    profitDetails.setText("    Total Profit               :   "+getProfit);
                    cDetails.setText("    Total VAT Collected  :   "+getCVAT);
                    rDetails.setText("    Total VAT Remitted  :    "+getRVAT);
                    govDetails.setText("Total VAT To Be Remitted :  "+getGOV);

                }catch (Exception e){
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> getDetails=new HashMap<>();
                getDetails.put("username",username);
                getDetails.put("password",password);
                return getDetails;
            }
        };
        RequestQueue queue=Volley.newRequestQueue(getContext());
        queue.add(request);
//                try {
//                    String getSale="";
//                    String getPurchase="";
//                    String getProfit="";
//                    String getCVAT="";
//                    String getRVAT="";
//                    String getGOV="";
//                    for(int i=0;i<response.length();i++){
//                        JSONObject details=response.getJSONObject(i);
//
//                        String sale=details.getString("sales");
//                        String purchase=details.getString("purchase");
//                        String profit=details.getString("profit");
//                        String cVAT=details.getString("collectedvat");
//                        String rVAT=details.getString("remittedvat");
//                        String gov=details.getString("gov");
//
//                        getSale=sale;
//                        getPurchase=purchase;
//                        getProfit=profit;
//                        getCVAT=cVAT;
//                        getRVAT=rVAT;
//                        getGOV=gov;
//
//
//
//                    }
//
//                    saleDetails.setText("     Total Sales               :     "+getSale);
//                    purchaseDetails.setText("    Total Purchase         :    "+getPurchase);
//                    profitDetails.setText("    Total Profit               :   "+getProfit);
//                    cDetails.setText("    Total VAT Collected  :   "+getCVAT);
//                    rDetails.setText("    Total VAT Remitted  :    "+getRVAT);
//                    govDetails.setText("   Total GOV                  :   "+getGOV);

//

    }
    private void LoadHotelSpinner(final String username, final String password) {
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("hotel");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject Object=jsonArray.getJSONObject(i);
                        String name=Object.getString("name");
                        hotel_list.add(name);
                        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),R.layout.spinner_items,hotel_list);
                        spinner.setAdapter(arrayAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>hotel=new HashMap<>();
                hotel.put("username",username);
                hotel.put("password",password);
                return hotel;
            }
        };
        queue.add(request);

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
