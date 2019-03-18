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
import android.widget.ProgressBar;
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
import org.json.JSONException;
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
 * Use the {@link PurchaseReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchaseReportFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private View view;
    private TextView purchase_from;
    private TextView purchase_to;
    String From, To, Client,USERNAME,usernameHolder,PASSWORD,passwordHolder;
    ProgressBar progressBar;
    ListView purchaseListView;
    PurchaseAdapter purchaseAdapter;
    List<String> client_list;
    ArrayList<purchaseItems> purchaseItemsArrayList = new ArrayList<>();
    Spinner clientSPinner;
    String clientURL = "http://billsuite.nuvactech.com/arabian/ios_new_merchant.php";
    String purchasedURL = "http://billsuite.nuvactech.com/arabian/ios_new_purchase_report.php";
    Button getPurchaseDetail;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PurchaseReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PurchaseReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PurchaseReportFragment newInstance() {
        PurchaseReportFragment fragment = new PurchaseReportFragment();
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
        view = inflater.inflate(R.layout.fragment_purchase_report, container, false);
        client_list = new ArrayList<>();
        purchase_from = view.findViewById(R.id.purchase_edtFrom);
        purchase_to = view.findViewById(R.id.purchase_edtTo);
        clientSPinner = view.findViewById(R.id.purchaseSpinner);
        getPurchaseDetail = view.findViewById(R.id.purchaseButton);
        purchaseListView = view.findViewById(R.id.purchaseList);
        progressBar = view.findViewById(R.id.progressPurchase);
        progressBar.setVisibility(View.GONE);
        getPurchaseDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPurchaseDetails();
            }
        });
        getClient(USERNAME,PASSWORD);
        purchase_from.setOnClickListener(this);
        purchase_to.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View view) {
        if (view == purchase_from) {
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
                            purchase_from.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == purchase_to) {
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
                            purchase_to.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }

    private void getClient(final String username, final String password) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, clientURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    client_list.add("Choose a Merchant");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("merchant");
                        client_list.add(name);
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_items, client_list);
                        clientSPinner.setAdapter(arrayAdapter);
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
                Map<String,String>clients=new HashMap<>();
                clients.put("username",username);
                clients.put("password",password);
                return clients;
            }
        };
        queue.add(request);
    }


    private void getPurchaseDetails() {

        From = purchase_from.getText().toString();
        To = purchase_to.getText().toString();
        Client = clientSPinner.getSelectedItem().toString();
        if (TextUtils.isEmpty(From)||TextUtils.isEmpty(To)||TextUtils.isEmpty(Client)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Fill All the Fields");
            builder.setCancelable(true);
            builder.create().show();

        }else {
            GetPurchaseDetails(purchasedURL, To, From, Client,USERNAME,PASSWORD);
        }

        // new PurchaseTask().execute(from,to,client,purchasedURL);
    }

    private void GetPurchaseDetails(String purchasedURL, final String To, final String From, final String Client, final String username, final String passwrod) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, purchasedURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("purchase");
                    purchaseItemsArrayList = new ArrayList<>();
                    if (jsonArray != null)
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            purchaseItems items = new purchaseItems();
                            String invoiceNo = object.getString("invoiceno");
                            String amount = object.getString("amount");
                            String paymentMode = object.getString("paymentmode");
                            String remark = object.getString("remark");
                            String docName = object.getString("docname");
                            String brandID = object.getString("branchid");
                            items.setInvoiceNum(invoiceNo);
                            items.setAmount(amount);
                            items.setPaymentMode(paymentMode);
                            items.setRemark(remark);
                            items.setDocumentName(docName);
                            items.setBrandID(brandID);
                            purchaseItemsArrayList.add(items);

                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                purchaseAdapter = new PurchaseAdapter(getActivity(), purchaseItemsArrayList);
                purchaseListView.setAdapter(purchaseAdapter);
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("from", From); //Add the data you'd like to send to the server.
                MyData.put("to", To);
                MyData.put("client", Client);
                MyData.put("username",username);
                MyData.put("password",passwrod);

                return MyData;

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
