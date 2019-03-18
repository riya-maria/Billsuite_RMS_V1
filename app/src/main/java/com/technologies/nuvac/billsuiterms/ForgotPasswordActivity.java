package com.technologies.nuvac.billsuiterms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    String ResetUserHolder,ResetPassHolder,ResetConfirmPasswordHolder;
    EditText ResetUsername,ResetPassword,ResetConfirmPassword;
    Button resetButton;
    String ResetUrl="http://billsuite.nuvactech.com/arabian/forgotpassword_ios.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ResetUsername=findViewById(R.id.editUsername);
        ResetPassword=findViewById(R.id.editPassword);
        ResetConfirmPassword=findViewById(R.id.editConfirmPassword);
        resetButton=findViewById(R.id.btn_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetUserHolder = ResetUsername.getText().toString();
                ResetPassHolder = ResetPassword.getText().toString();
                ResetConfirmPasswordHolder = ResetConfirmPassword.getText().toString();

                if (ResetPassHolder.equals(ResetConfirmPasswordHolder)) {

                    ResetPasswordTask(ResetUrl,ResetUserHolder,ResetPassHolder);


                    //new ResetTask().execute(ResetUrl,ResetUserHolder,ResetPassHolder);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void ResetPasswordTask(String resetUrl, final String resetUserHolder, final String resetPassHolder) {
        StringRequest request=new StringRequest(Request.Method.POST, resetUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    if(status.equals("True")) {
                        Log.d("#######", jsonObject.getString("status"));
                        Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                        startActivity(intent);
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
                Map<String, String> reset = new HashMap<String, String>();
                reset.put("username", resetUserHolder); //Add the data you'd like to send to the server.
                reset.put("password", resetPassHolder);
                return reset;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);

    }
}
