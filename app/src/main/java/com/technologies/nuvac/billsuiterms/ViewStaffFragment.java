package com.technologies.nuvac.billsuiterms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewStaffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewStaffFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    View view;
    ListView listView;
    ArrayList<StaffDetailsItems> staffList;
    StaffAdapter adapter;
    String url= "http://billsuite.nuvactech.com/arabian/ios_new_viewstaff.php";
    String USERNAME,PASSWORD,usernameHolder,passwordHolder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ViewStaffFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment ViewStaffFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewStaffFragment newInstance() {
        ViewStaffFragment fragment = new ViewStaffFragment();
        Bundle args = new Bundle();

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
        view=inflater.inflate(R.layout.fragment_view_staff, container, false);
        staffList=new ArrayList<>();
        listView=view.findViewById(R.id.staff_list);
        StaffDetails(USERNAME,PASSWORD);
        //new StaffTask().execute(url);
        return view;

    }

    private void StaffDetails(final String username, final String password) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("staff");
                    if (jsonArray!=null)
                        for (int i=0;i<jsonArray.length();i++){
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            JSONObject object=jsonArray.getJSONObject(i);
                            StaffDetailsItems items=new StaffDetailsItems();
                            items.setName(object.getString("Name"));
                            items.setGender(object.getString("Gender"));
                            items.setAddress(object.getString("Address"));
                            items.setPhoneNumber(object.getString("Phone"));
                            items.setDesignation(object.getString("Designation"));
                            String image=object.getString("Image");
                            //String[] x=image.split(",");
                           // Log.d("@@@",image);
                            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
                            items.setImage(decodedByte);

                            staffList.add(items);


                        }
                }catch (Exception e){
                    e.printStackTrace();
                }
                adapter=new StaffAdapter(getContext(),staffList);
                listView.setAdapter(adapter);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>staff=new HashMap<>();
                staff.put("username",username);
                staff.put("password",password);
                return staff;
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


//
}
