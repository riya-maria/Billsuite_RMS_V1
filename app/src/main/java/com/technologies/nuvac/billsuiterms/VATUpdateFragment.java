package com.technologies.nuvac.billsuiterms;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VATUpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VATUpdateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private View view;
    EditText editFrom,editTo,editDetail,editMessage,editEmail,editReg;
    Button UpdateButton;
    String UpdateUrl="http://billsuite.nuvactech.com/arabian/ios_updatefilevat.php";
    String FromDateHolder,ToDateHolder,DetailHolder,MessageHolder,EmailHolder,RegNoHolder,VAT_ID,USERNAME,usernameHolder,PASSWORD,passwordHolder;
    Fragment fragment;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VATUpdateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VATUpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VATUpdateFragment newInstance(String param1, String param2) {
        VATUpdateFragment fragment = new VATUpdateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        Bundle bundle=getArguments();
        VAT_ID=bundle.getString("id");
//        FromDateHolder=bundle.getString("from");
//        ToDateHolder=bundle.getString("to");
//        DetailHolder=bundle.getString("details");
//        MessageHolder=bundle.getString("message");
        RegNoHolder=bundle.getString("reg");

        view=inflater.inflate(R.layout.fragment_vatupdate, container, false);
        editFrom=view.findViewById(R.id.editFromDate);
        editTo=view.findViewById(R.id.editToDate);
        editDetail=view.findViewById(R.id.editDetails);
        editMessage=view.findViewById(R.id.editMessage);
        editEmail=view.findViewById(R.id.editEmailID);
        editEmail.setText(VAT_ID);
        editReg=view.findViewById(R.id.editRegNo);
        editReg.setText(RegNoHolder);
        UpdateButton=view.findViewById(R.id.updateVat);
        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FromDateHolder=editFrom.getText().toString();
                ToDateHolder=editTo.getText().toString();
                DetailHolder=editDetail.getText().toString();
                MessageHolder=editMessage.getText().toString();
                EmailHolder=editEmail.getText().toString();
                RegNoHolder=editReg.getText().toString();
                UpdateVAT(FromDateHolder,ToDateHolder,DetailHolder,MessageHolder,EmailHolder,RegNoHolder,USERNAME,PASSWORD);

            }
        });
        editFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                                editFrom.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        editTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                                editTo.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void UpdateVAT(final String fromDateHolder, final String toDateHolder, final String detailHolder, final String messageHolder, final String emailHolder, final String regNoHolder, final String username, final String password) {
        StringRequest request=new StringRequest(Request.Method.POST, UpdateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String Status=jsonObject.getString("status");
                    if (Status.equals("Successfully Updated")){
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setTitle("VAT Updated");
                        builder.setMessage(Status);
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        fragment=new VATFragment();
                        loadFragment(fragment);
                    }
                    else if (Status.equals("Updating Failed")){
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setTitle("VAT Updated");
                        builder.setMessage(Status);
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    Log.d("#####RESPONSE@@@@@",response);

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
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> updateVat=new HashMap<>();
                updateVat.put("from",fromDateHolder);
                updateVat.put("to",toDateHolder);
                updateVat.put("details",detailHolder);
                updateVat.put("message",messageHolder);
                updateVat.put("id",emailHolder);
                updateVat.put("regno",regNoHolder);
                updateVat.put("username",username);
                updateVat.put("password",password);
                return updateVat;

            }
        };
        RequestQueue queue=Volley.newRequestQueue(getContext());
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
