package com.technologies.nuvac.billsuiterms;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class ResetPasswordActivity extends AppCompatActivity {

    String ResetUserHolder,ResetPassHolder,ResetNewPasswordHolder;
    EditText ResetUsername,ResetPassword,ResetNewPassword;
    Button resetButton;
    String ResetUrl="http://billsuite.nuvactech.com/arabian/ios_settings.php";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ResetUsername=findViewById(R.id.editUsername);
        ResetPassword=findViewById(R.id.editOldPassword);
        ResetNewPassword=findViewById(R.id.editNewPassword);
        resetButton=findViewById(R.id.btn_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetUserHolder = ResetUsername.getText().toString();
                ResetPassHolder = ResetPassword.getText().toString();
                ResetNewPasswordHolder = ResetNewPassword.getText().toString();
                ResetPasswordTask(ResetUserHolder,ResetPassHolder,ResetNewPasswordHolder);
            }
        });
    }
    private void ResetPasswordTask(final String resetUserHolder, final String resetPassHolder, final String resetNewPasswordHolder) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, ResetUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if(status.equals("Successfully Updated")) {

                        Log.d("#######", jsonObject.getString("status"));

                        Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else {
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ResetPasswordActivity.this);
//
                        dlgAlert.setMessage("wrong password or username");
                        dlgAlert.setTitle("Error Message...");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ResetPassword.getText().clear();
                            }
                        });
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
                Map<String, String> reset = new HashMap<String, String>();
                reset.put("username", resetUserHolder); //Add the data you'd like to send to the server.
                reset.put("password", resetPassHolder);
                reset.put("newpassword",resetNewPasswordHolder);
                return reset;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }

}
