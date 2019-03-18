package com.technologies.nuvac.billsuiterms;

import android.app.DatePickerDialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VATHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VATHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private View view;
    TextView From,To;
    Spinner spinner_hotel;
    Button GetList;
    ArrayList<String> hotel_list;
    ListView listHistory;
    String hotelUrl="http://billsuite.nuvactech.com/arabian/ios_hotel.php",history_url="http://billsuite.nuvactech.com/arabian/ios_history.php";
    String HotelName,FromHolder,ToHolder,USERNAME,usernameHolder,PASSWORD,passwordHolder;
    VatHistoryAdapter adapter;

    ArrayList<vatItems> vatItemsArrayList=new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VATHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VATHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VATHistoryFragment newInstance(String param1, String param2) {
        VATHistoryFragment fragment = new VATHistoryFragment();
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

        view=inflater.inflate(R.layout.fragment_vathistory, container, false);
        From=view.findViewById(R.id.vatFrom);
        To=view.findViewById(R.id.vatTo);
        GetList=view.findViewById(R.id.buttonGet);
        listHistory=view.findViewById(R.id.vat_history);
        From.setOnClickListener(new View.OnClickListener() {
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

                                From.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        To.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                                To.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        spinner_hotel=view.findViewById(R.id.getHotel);
        GetList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FromHolder=From.getText().toString();
                ToHolder=To.getText().toString();
                History_VAT(HotelName,FromHolder,ToHolder,USERNAME,PASSWORD);
            }
        });

        LoadHotelSpinner(USERNAME,PASSWORD);
        // Inflate the layout for this fragment
        return view;
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
                        spinner_hotel.setAdapter(arrayAdapter);
                        spinner_hotel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                HotelName=spinner_hotel.getSelectedItem().toString();

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
                Map<String,String>history=new HashMap<>();
                history.put("username",username);
                history.put("password",password);
                return history;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void History_VAT(final String hotelName, final String fromHolder, final String toHolder, final String username, final String password) {

        StringRequest request=new StringRequest(Request.Method.POST, history_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                vatItemsArrayList=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("history");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);
                        String ID=object.getString("id");
                        String BRANCH=object.getString("branch");
                        String FROM=object.getString("fromdate");
                        String TO=object.getString("todate");
                        String REG=object.getString("regno");
                        String STATUS=object.getString("status");

                        vatItems items=new vatItems();

                        items.setID(ID);
                        items.setBranch(BRANCH);
                        items.setFromDate(FROM);
                        items.setToDate(TO);
                        items.setRegNo(REG);
                        items.setStatus(STATUS);

                        vatItemsArrayList.add(items);
                        adapter=new VatHistoryAdapter(getActivity(),vatItemsArrayList);
                        listHistory.setAdapter(adapter);

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
                Map<String,String>history=new HashMap<>();
                history.put("hotel",hotelName);
                history.put("from",fromHolder);
                history.put("to",toHolder);
                history.put("username",username);
                history.put("password",password);
                return history;
            }
        };
        RequestQueue queue=Volley.newRequestQueue(getActivity());
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
