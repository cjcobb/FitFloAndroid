package com.fitflo.fitflo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void sendCreateEventRequest(View view) {


        String title = ((EditText) findViewById(R.id.title)).getText().toString();
        String price = ((EditText) findViewById(R.id.price)).getText().toString();
        String instructorName = ((EditText) findViewById(R.id.instructorName)).getText().toString();
        String ip = "http://" + MainActivity.cjsServerIp
                + ":8080/addEvent/"
                + title + "/"
                + instructorName + "/"
                + price;

        StringRequest request = new StringRequest(Request.Method.GET,ip,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("sendCreateEventRequest", response);
                finish();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("sendCreateEventRequest", "error:" + error.toString());
                finish();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MainActivity.requestQueue.add(request);
    }

}
