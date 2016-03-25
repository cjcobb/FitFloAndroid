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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import org.json.JSONObject;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GoogleSignIn", "finally");

                signIn(v);
            }
        });

    }

    public void signIn(View view) {
        Toast toast = Toast.makeText(this,"sign in clicked",Toast.LENGTH_SHORT);
        toast.show();
        Log.d("GoogleSignIn", "in click listener");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(MainActivity.mGoogleApiClient);
        startActivityForResult(signInIntent, MainActivity.G_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MainActivity.G_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("GoogleSignIn","display name is " + acct.getDisplayName());
            Log.d("GoogleSignIn","email is " + acct.getEmail());
            Log.d("GoogleSignIn","id is " + acct.getId());
            Log.d("GoogleSignIn","token is " + acct.getIdToken());
            Toast toast = Toast.makeText(this,"sign in successful",Toast.LENGTH_SHORT);
            toast.show();
            FileUtils.writeBoolean(this,getString(R.string.logged_in_key),true);
            FileUtils.writeString(this,getString(R.string.username),acct.getDisplayName());
            finish();
        } else {
            Toast toast = Toast.makeText(this, "sign in failed", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void startRegistrationActivity(View view) {
        Intent intent = new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }

    public void sendLogin(View view) {
        Log.d("sendLogin","making request");

        final String username = ((EditText) findViewById(R.id.username)).getText().toString();

        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String url = "https://" + MainActivity.cjsServerIp + ":3000/auth/login/"+username+"/"+password;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jObj) {
                Log.d("sendLogin","got response");
                if(jObj.optBoolean("valid")) {



                    FileUtils.writeBoolean(LoginActivity.this, getString(R.string.logged_in_key), true);
                    FileUtils.writeString(LoginActivity.this,getString(R.string.username),username);

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
