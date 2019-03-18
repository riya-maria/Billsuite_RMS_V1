package com.technologies.nuvac.billsuiterms;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HotelsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotelsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_STAFF_NAME = "staffName";
    View view;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<HotelList> hotelItemList;
    HotelAdapter adapter;
    String url= "http://nuvactech-001-site8.etempurl.com/ios_hotel.php";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HotelsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment HotelsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HotelsFragment newInstance() {
        HotelsFragment fragment = new HotelsFragment();
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
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_hotels, container, false);

        hotelItemList=new ArrayList<>();
        progressBar=view.findViewById(R.id.progress);

        recyclerView=view.findViewById(R.id.hotel_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        //new HotelTask().execute(url);
        //hotelDetails(url);
        return view;
    }

//    private void hotelDetails(String url) {
//        final JsonArrayRequest request=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                HotelList items=new HotelList();
//                if (response!=null)
//                for (int i=0; i<response.length();i++){
//                    try {
//
//                        JSONObject object=response.getJSONObject(i);
//                        items.setHotelNames(object.getString("hotel"));
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                hotelItemList.add(items);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        adapter=new HotelAdapter(getActivity(),hotelItemList);
//        recyclerView.setAdapter(adapter);
//        RequestQueue queue= Volley.newRequestQueue(getActivity());
//        queue.add(request);
//    }

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

//    @Override
//    public void onItemClick(int position) {
//        Intent detailIntent=new Intent(getContext(),StaffDetailsActivity.class);
//        HotelList hotelList=hotelItemList.get(position);
//        detailIntent.putExtra(ARG_STAFF_NAME,hotelList.getHotelNames());
//        startActivity(detailIntent);
   // }

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

//    private class HotelTask extends AsyncTask<String,Void,Integer> {
//
//        @Override
//        protected Integer doInBackground(String... strings) {
//
//            HttpHelper helper=new HttpHelper();
//
//            String json=helper.MakeServiceCall(url);
//
//            try {
//                JSONArray jsonArray=new JSONArray(json);
//                for (int i=0;i<jsonArray.length();i++){
//                    JSONObject object=jsonArray.getJSONObject(i);
//
//                    HotelList items=new HotelList();
//
//                    items.setHotelNames(object.getString("hotel"));
//
//                    hotelItemList.add(items);
//                }
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Integer integer) {
//            super.onPostExecute(integer);
//
//            adapter=new HotelAdapter(getActivity(),hotelItemList);
//            recyclerView.setAdapter(adapter);
//           // adapter.setOnItemClickListener(HotelsFragment.newInstance());
//        }
//    }
}
