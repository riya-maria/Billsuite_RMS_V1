package com.technologies.nuvac.billsuiterms;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FileMyVATFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileMyVATFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private View view;
    EditText details,message,email;
    TextView fromDate,toDate;
    Button submitVat;
    Spinner HotelName;
    ArrayList<String> hotel_list;
    String vat_url="http://billsuite.nuvactech.com/arabian/ios_filevat.php",hotelUrl="http://billsuite.nuvactech.com/arabian/ios_hotel.php";
    String FromDateHolder,ToDateHolder,DetailHolder,MessageHolder,EmailHolder,HotelNameHolder,USERNAME,usernameHolder,PASSWORD,passwordHolder;
    Fragment fragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public FileMyVATFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileMyVATFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileMyVATFragment newInstance(String param1, String param2) {
        FileMyVATFragment fragment = new FileMyVATFragment();
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


        view=inflater.inflate(R.layout.fragment_file_my_vat, container, false);

        HotelName=view.findViewById(R.id.hotelSpinner);
        fromDate=view.findViewById(R.id.vatFrom);
        toDate=view.findViewById(R.id.vatTo);
        email=view.findViewById(R.id.editEmail);
        details=view.findViewById(R.id.editDetails);
        message=view.findViewById(R.id.editMessage);
        submitVat=view.findViewById(R.id.submitVat);

        fromDate.setOnClickListener(new View.OnClickListener() {
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
                                fromDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
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

                                toDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        submitVat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FromDateHolder=fromDate.getText().toString();
                ToDateHolder=toDate.getText().toString();
                DetailHolder=details.getText().toString();
                EmailHolder=email.getText().toString();
                MessageHolder=message.getText().toString();
                FileVAT(FromDateHolder,ToDateHolder,DetailHolder,EmailHolder,MessageHolder,HotelNameHolder,USERNAME,PASSWORD);
            }
        });

        // Inflate the layout for this fragment
        LoadHotelSpinner(USERNAME,PASSWORD);
        return view;
    }
    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void FileVAT(final String fromDateHolder, final String toDateHolder, final String detailHolder, final String emailHolder, final String messageHolder, final String hotelNameHolder, final String username, final String password) {
        StringRequest request=new StringRequest(Request.Method.POST, vat_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String REGNO=jsonObject.getString("regno");
                    fragment=new VATFragment();
                    loadFragment(fragment);
                    Bundle bundle=new Bundle();
                    bundle.putString("reg",REGNO);
                    fragment.setArguments(bundle);
                    Log.d("#### REG $$$$",REGNO);
                }catch (Exception e){
                    e.printStackTrace();
                }

//                try {
//                    JSONObject jsonObject=new JSONObject(response);
//                    String REG=jsonObject.getString("regno");
//                    Log.d("#####REGNO###",REG);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> vatFile=new HashMap<>();
                vatFile.put("from",fromDateHolder);
                vatFile.put("to",toDateHolder);
                vatFile.put("details",detailHolder);
                vatFile.put("message",messageHolder);
                vatFile.put("email",emailHolder);
                vatFile.put("hotel",hotelNameHolder);
                vatFile.put("username",username);
                vatFile.put("password",password);
                return vatFile;

            }
        };

        RequestQueue queue=Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void LoadHotelSpinner(final String username, final String password) {
        StringRequest request=new StringRequest(Request.Method.POST, hotelUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hotel_list=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("hotel");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject Object=jsonArray.getJSONObject(i);
                        String name=Object.getString("name");
                        hotel_list.add(name);
                        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),R.layout.spinner_items,hotel_list);
                        HotelName.setAdapter(arrayAdapter);
                        HotelName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                HotelNameHolder=HotelName.getSelectedItem().toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

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
        RequestQueue queue= Volley.newRequestQueue(getContext());
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
