package com.fitflo.fitflo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void registerUser(View view) {

        Log.d("registerUser", "making request");

        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String confirmPassword = ((EditText) findViewById(R.id.confirmPassword)).getText().toString();

        final String cardNumber = ((EditText) findViewById(R.id.card_number)).getText().toString();
        final int expYear = Integer.parseInt(((EditText) findViewById(R.id.exp_year)).getText().toString());
        final int expMonth = Integer.parseInt(((EditText) findViewById(R.id.exp_month)).getText().toString());
        final String cvc = ((EditText) findViewById(R.id.cvc)).getText().toString();

        if(!confirmPassword.equals(password)) {
            Toast toast = Toast.makeText(this,"error: passwords dont match",Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        //validate the card information. if good, write to file
        if(!StripeUtils.validate(cardNumber, expMonth, expYear, cvc)) {
            Toast toast = Toast.makeText(this,"error: card invalid",Toast.LENGTH_LONG);
            toast.show();
            return;
        }




        String displayName = ((EditText) findViewById(R.id.displayName)).getText().toString();

        String url = "https://" + MainActivity.cjsServerIp + ":8080/addUser/"+username+"/"+password+"/"+displayName;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jObj) {
                int code = jObj.optInt("code");
                Log.d("registerUser","got response");
                //this is the duplicate key error code
                if(code == 11000) {
                    Toast toast = Toast.makeText(RegistrationActivity.this, "error: username already exists", Toast.LENGTH_SHORT);
                    toast.show();
                } else if(code == 0){

                    Toast toast = Toast.makeText(RegistrationActivity.this, "account created", Toast.LENGTH_SHORT);
                    toast.show();
                    FileUtils.writeString(RegistrationActivity.this,"cardNumber",cardNumber);
                    FileUtils.writeString(RegistrationActivity.this,"expMonth",Integer.toString(expMonth));
                    FileUtils.writeString(RegistrationActivity.this,"expYear",Integer.toString(expYear));
                    FileUtils.writeString(RegistrationActivity.this, "cvc", cvc);
                    finish();
                } else {
                    Toast toast = Toast.makeText(RegistrationActivity.this, "unknown error:" + jObj.optString("errmsg"), Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(RegistrationActivity.this, "error:" + error.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        MainActivity.requestQueue.add(request);

    }

}
