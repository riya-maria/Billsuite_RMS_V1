package com.technologies.nuvac.billsuiterms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    EditText user,pass;
    Button login,resetPassword;
    String UserHolder,PasswordHolder,AppCat="BillSuite-RMS";
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String url="http://billsuite.nuvactech.com/arabian/ios_new_login.php/";
    String finalResult;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap=new HashMap<>();
    HttpHandler httpHandler=new HttpHandler();
    public static final String UserEmail = "";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = findViewById(R.id.editUsername);
        pass = findViewById(R.id.editPassword);
        login = findViewById(R.id.btn_login);
        resetPassword=findViewById(R.id.btn_reset_password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserHolder=user.getText().toString();
                PasswordHolder=pass.getText().toString();
                if (TextUtils.isEmpty(UserHolder)){
                    user.setError("Please Enter Username");
                }
                if (TextUtils.isEmpty(PasswordHolder)){
                    pass.setError("Please Enter Password");
                }
                LoginDetails(url,UserHolder,PasswordHolder,AppCat);
                //new LoginTask().execute(url,UserHolder,PasswordHolder);


            }


        });
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ResetPasswordActivity.class);
                intent.putExtra("username",UserHolder);
                startActivity(intent);
            }
        });
    }

    private void LoginDetails(String url, final String userHolder, final String passwordHolder,final String appCat) {
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if(status.equals("true")) {
                            Log.d("#######", jsonObject.getString("status"));
                            Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
                            sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            editor = sharedpreferences.edit();
                            editor.putString("username",UserHolder);
                            editor.putString("password",PasswordHolder);
                            editor.apply();
                            startActivity(intent);
                        }else if (status.equals("False")){
                            android.support.v7.app.AlertDialog.Builder dlgAlert  = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                            dlgAlert.setMessage("wrong password or username");
                            dlgAlert.setTitle("Login Error");
                            dlgAlert.setIcon(R.drawable.ic_warning);
                            dlgAlert.setPositiveButton("OK", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
                            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pass.getText().clear();
                                }
                            });
//
                        }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"server couldn't respond",Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> login = new HashMap<String, String>();
                login.put("username", userHolder); //Add the data you'd like to send to the server.
                login.put("password", passwordHolder);
                login.put("appcategory",appCat);
                return login;
            }
        };
        RequestQueue queue=Volley.newRequestQueue(this);
        queue.add(request);
    }

//    private class LoginTask extends AsyncTask<String,String,String>{
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog=new ProgressDialog(MainActivity.this);
//            progressDialog.setMessage("Singing in... please wait");
//            progressDialog.setIndeterminate(false);
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            AsyncHttpClient client = new AsyncHttpClient();
//            RequestParams params1 = new RequestParams();
//            params1.put("username",UserHolder);
//            params1.put("password",PasswordHolder );
//
//            client.post(url,params1,new AsyncHttpResponseHandler(){
//                @Override
//                public void onSuccess(String response) {
//                    try {
//                        JSONObject jsonObject=new JSONObject(response);
//                        String status=jsonObject.getString("status");
//                        if(status.equals("true")){
//                      Log.d("#######", jsonObject.getString("status"));
//
//                         Intent intent=new Intent(getApplicationContext(),BottomNavigationActivity.class);
//                            startActivity(intent);
//                        }
//
//                    }catch (JSONException e){
//                        e.printStackTrace();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(final int statusCode, Throwable error, String content) {
//                   runOnUiThread(new Runnable() {
//                       @Override
//                       public void run() {
//                           progressDialog.dismiss();
//                           if(statusCode==404){
//                               Toast.makeText(getApplication(), "Requested resource not found", Toast.LENGTH_LONG).show();
//                           }else if(statusCode==500){
//                               Toast.makeText(getApplication(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
//
////                           }else if(){
////                               Toast.makeText(getApplication(), "Device might not be connected to Internet", Toast.LENGTH_LONG).show();
//                           }else {
//                               AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MainActivity.this);
//
//                               dlgAlert.setMessage("wrong password or username");
//                               dlgAlert.setTitle("Error Message...");
//                               dlgAlert.setPositiveButton("OK", null);
//                               dlgAlert.setCancelable(true);
//                               dlgAlert.create().show();
//
//                               dlgAlert.setPositiveButton("Ok",
//                                       new DialogInterface.OnClickListener() {
//                                           public void onClick(DialogInterface dialog, int which) {
//
//                                           }
//                                       });
//                           }
//
//                       }
//                   });
//                }
//            });
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//        }
//    }
}
