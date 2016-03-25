package com.fitflo.fitflo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView eventTitleView = (TextView) findViewById(R.id.event_title);
        eventTitleView.setText(MainActivity.selectedEvent.title);
        TextView instructorNameView = (TextView) findViewById(R.id.event_instructor);
        instructorNameView.setText(MainActivity.selectedEvent.instructorName);
    }


    public void sendRegisterForEventRequest(View view) {
        String username = MainActivity.mUsername;
        FitFloEvent selectedEvent = MainActivity.selectedEvent;
        //TODO: dont hard code this
        String ip = "https://" + MainActivity.cjsServerIp + ":3000/registration/registerForEvent/"
                + selectedEvent.eventID + "/"
                + username;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,ip,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast toast = Toast.makeText(EventDetailsActivity.this,"registered successfully",Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(EventDetailsActivity.this,"error: couldn't register",Toast.LENGTH_SHORT);
                toast.show();
                error.printStackTrace();

            }
        });

        MainActivity.requestQueue.add(request);
    }

}
