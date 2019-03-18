package com.technologies.nuvac.billsuiterms;

import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalesFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    View view;
    TextView sales_from;
    TextView sales_to;
    Spinner spinner;
    Button salesReport;
    ListView salesList;
    String SaleFrom,SaleTO,clientSpinner,USERNAME,usernameHolder,PASSWORD,passwordHolder;
    List<String> hotel_list;
    ArrayList<salesItems> itemsArrayList=new ArrayList<>();
    SalesAdapter salesAdapter;

    String saleURL="http://billsuite.nuvactech.com/arabian/ios_new_sales_report.php";
    String hotel_url="http://billsuite.nuvactech.com/arabian/ios_hotel.php";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SalesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment SalesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SalesFragment newInstance() {
        SalesFragment fragment = new SalesFragment();
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

        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_sales, container, false);
        sales_from=view.findViewById(R.id.saleEdtFrom);
        sales_to=view.findViewById(R.id.saleEdtTo);
        spinner=view.findViewById(R.id.saleSpinner);
        salesReport=view.findViewById(R.id.saleSubmit);
        salesList=view.findViewById(R.id.salesList);
        sales_from.setOnClickListener(this);
        sales_to.setOnClickListener(this);
        salesReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSalesDetails();
            }
        });
        hotel_list=new ArrayList<>();

        GetHotelClients(USERNAME,PASSWORD);

        return view;
    }

    private void GetHotelClients(final String username, final String password) {
        StringRequest request=new StringRequest(Request.Method.POST, hotel_url, new Response.Listener<String>() {
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

                }catch (Exception e){
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
                Map<String,String>sales=new HashMap<>();
                sales.put("username",username);
                sales.put("password",password);
                return sales;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void getSalesDetails() {
        SaleFrom=sales_from.getText().toString();
        SaleTO=sales_to.getText().toString();
        clientSpinner=spinner.getSelectedItem().toString();
        if (TextUtils.isEmpty(SaleFrom)||TextUtils.isEmpty(SaleTO)||TextUtils.isEmpty(clientSpinner)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Fill All the Fields");
            builder.setCancelable(true);
            builder.create().show();

        }else {
            GetSaleReport(SaleFrom,SaleTO,clientSpinner,USERNAME,PASSWORD);
        }
    }

    private void GetSaleReport(final String saleFrom, final String saleTO, final String clientSpinner, final String username, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, saleURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("sales");
                    itemsArrayList=new ArrayList<>();
                    if (jsonArray != null)
                        for (int i=0;i<jsonArray.length();i++){
                            salesItems items=new salesItems();
                            JSONObject object=jsonArray.getJSONObject(i);
                            String InvoiceNO=object.getString("Invoice_Number");
                            String Amount=object.getString("amount");
                            String Status=object.getString("status");
                            String StaffID=object.getString("staff_id");
                            items.setInvoiceNumber(InvoiceNO);
                            items.setAmount(Amount);
                            items.setStatus(Status);
                            items.setStaffID(StaffID);
                            itemsArrayList.add(items);

                        }
                }catch (Exception e){
                    e.printStackTrace();
                }
                salesAdapter=new SalesAdapter(getActivity(),itemsArrayList);

                salesList.setAdapter(salesAdapter);
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("from",saleFrom);
                params.put("to",saleTO);
                params.put("client",clientSpinner);
                params.put("username",username);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getActivity());
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

    @Override
    public void onClick(View view) {
        if (view==sales_from){
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

                            sales_from.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view==sales_to){
            // Get Current To Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            sales_to.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
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
