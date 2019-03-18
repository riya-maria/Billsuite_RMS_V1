package com.technologies.nuvac.billsuiterms;

import android.app.DatePickerDialog;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VATFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VATFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private View view;
    private TextView vat_from;
    private TextView vat_to;
    String From,To;
    ProgressDialog progressDialog;
    String VatCollected,VatRemitted,VatGOV,regHolder,USERNAME,usernameHolder,PASSWORD,passwordHolder;
    TextView remittedVat,collectedVat,govVat;
    Button getVatDetails,FileVAT,VATReq,VATHistory;
    String url="http://billsuite.nuvactech.com/arabian/ios_new_vat.php";
    Fragment fragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VATFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment VATFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VATFragment newInstance() {
        VATFragment fragment = new VATFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        USERNAME=sharedpreferences.getString("username",usernameHolder);
        PASSWORD=sharedpreferences.getString("password",passwordHolder);
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_vat, container, false);
        vat_from=view.findViewById(R.id.vatFrom);
        vat_to=view.findViewById(R.id.vatTo);
        getVatDetails=view.findViewById(R.id.getVat);
        getVatDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                From=vat_from.getText().toString();
                To=vat_to.getText().toString();
                GetVATdetails(From,To,USERNAME,PASSWORD);
//                new VatTask().execute(url,From,To);

            }
        });
         vat_from.setOnClickListener(this);
         vat_to.setOnClickListener(this);
         collectedVat=view.findViewById(R.id.vatCollected);
        remittedVat=view.findViewById(R.id.VatRemitted);
        govVat=view.findViewById(R.id.gov);
        FileVAT=view.findViewById(R.id.fileVAT);
        VATReq=view.findViewById(R.id.fileRequest);
        VATHistory=view.findViewById(R.id.VATHistory);
        FileVAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new FileMyVATFragment();
                loadFragment(fragment);

            }
        });
        VATReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle=getArguments();
//                regHolder=bundle.getString("reg");

                fragment=new VATrequestFragment();
//                Bundle extra=new Bundle();
//                extra.putString("registerNO",regHolder);
//                fragment.setArguments(extra);
                loadFragment(fragment);
            }
        });
        VATHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new VATHistoryFragment();
                loadFragment(fragment);
            }
        });


        return view;
    }
    private void loadFragment(Fragment fragment) {


        try {
            if (getFragmentManager() != null) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }catch (Exception  e){

        }


//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();

//        Bundle bundle=new Bundle();
//        bundle.putString("user_id",idHolder);
//        bundle.putString("post",postHolder);
//        bundle.putString("balance",balanceHolder);
//        fragment.setArguments(bundle);

    }


    private void GetVATdetails(final String from, final String to, final String username, final String password) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        final StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);
                        VatCollected=object.getString("vat_collected");
                        VatRemitted=object.getString("vatremitted");
                        VatGOV=object.getString("gov");

                        collectedVat.setText(VatCollected);
                        remittedVat.setText(VatRemitted);
                        govVat.setText(VatGOV);
                    }
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

                Map<String,String> vatData=new HashMap<>();

                vatData.put("from",from);
                vatData.put("to",to);
                vatData.put("username",username);
                vatData.put("password",password);
                return vatData;
            }
        };
        RequestQueue queue=Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    @Override
    public void onClick(View view) {
        if (view==vat_from){
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

                            vat_from.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view==vat_to){
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

                            vat_to.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

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
