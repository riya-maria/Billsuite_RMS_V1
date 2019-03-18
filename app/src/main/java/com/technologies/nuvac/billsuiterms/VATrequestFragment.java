package com.technologies.nuvac.billsuiterms;

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
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VATrequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VATrequestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private View view;
    EditText RegNo;
    Button SubmitReq;
    String reqUrl="http://billsuite.nuvactech.com/arabian/ios_requestvat.php";
    String ID,DETAILS,MESSAGE,FROM_DATE,TO_DATE,EMAIL,REGNO,regHolder,USERNAME,usernameHolder,PASSWORD,passwordHolder;
    Fragment fragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VATrequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VATrequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VATrequestFragment newInstance(String param1, String param2) {
        VATrequestFragment fragment = new VATrequestFragment();
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

//        Bundle extra=getArguments();
//        regHolder=extra.getString("registerNO");
        view=inflater.inflate(R.layout.fragment_vatrequest, container, false);
        RegNo=view.findViewById(R.id.editRegisterNo);
        RegNo.setText(regHolder);
        SubmitReq=view.findViewById(R.id.submitReq);
        SubmitReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String RegHolder=RegNo.getText().toString();
                DetailVATDetails(RegHolder,USERNAME,PASSWORD);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void DetailVATDetails(final String regHolder, final String username, final String password) {
        StringRequest request=new StringRequest(Request.Method.POST, reqUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);
                        ID=object.getString("id");
                        DETAILS=object.getString("details");
                        MESSAGE=object.getString("message");
                        FROM_DATE=object.getString("fromdate");
                        TO_DATE=object.getString("todate");
                        EMAIL=object.getString("email");
                        REGNO=object.getString("regno");
                    }

                    fragment=new VATUpdateFragment();
                    loadFragment(fragment);
                    Bundle bundle=new Bundle();
                    bundle.putString("id",ID);
                    bundle.putString("reg",REGNO);
//                    bundle.putString("details",DETAILS);
//                    bundle.putString("message",MESSAGE);
//                    bundle.putString("from",FROM_DATE);
//                    bundle.putString("to",TO_DATE);

                    fragment.setArguments(bundle);
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
                Map<String,String> ReqVat=new HashMap<>();
                ReqVat.put("regno",regHolder);
                ReqVat.put("username",username);
                ReqVat.put("password",password);
                return ReqVat;
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
