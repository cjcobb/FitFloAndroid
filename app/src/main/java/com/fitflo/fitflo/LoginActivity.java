package com.fitflo.fitflo;

import android.content.Context;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void startRegistrationActivity(View view) {
        Intent intent = new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }

    public void sendLogin(View view) {
        Log.d("sendLogin","making request");

        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String url = "http://" + MainActivity.cjsServerIp + ":8080/login/"+username+"/"+password;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jObj) {
                Log.d("sendLogin","got response");
                if(jObj.optBoolean("valid")) {
                    SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.logged_in_key), true);
                    editor.apply();

                    finish();
                } else {
                  TextView errorResponse = (TextView) findViewById(R.id.errorResponse);
                  errorResponse.setText("error, wrong username and password combination");
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView errorResponse = (TextView) findViewById(R.id.errorResponse);
                errorResponse.setText(error.toString());
            }
        });

        MainActivity.requestQueue.add(request);
    }

}
