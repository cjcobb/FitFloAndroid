package com.fitflo.fitflo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Set;

public class CreateEventActivity extends AppCompatActivity {
    MapCallback mMapCallback;
    public final int SELECT_SKILLS_ACTIVITY = 1;
    public Set<String> skills;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMapCallback = new MapCallback(MainActivity.mGoogleApiClient);
        SupportMapFragment mapFrag = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFrag));
                mapFrag.getMapAsync(mMapCallback);
        skills = new HashSet<>();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode) {
            case (SELECT_SKILLS_ACTIVITY) : {
                if (resultCode == Activity.RESULT_OK) {
                    String[] skillsArray = data.getStringArrayExtra("skills");
                    for(String s : skillsArray) {
                        skills.add(s);
                    }
                }
            }
        }
    }

    public void onSelectSkills(View view) {
        Intent intent = new Intent(CreateEventActivity.this, SelectSkillsActivity.class);
        startActivityForResult(intent,SELECT_SKILLS_ACTIVITY);
    }

    public void onSubmitClick(View view) {
        sendCreateEventRequest();
    }

    public void sendCreateEventRequest() {


        String title = ((EditText) findViewById(R.id.title)).getText().toString();
        String price = ((EditText) findViewById(R.id.price)).getText().toString();
        String instructorName = ((EditText) findViewById(R.id.instructorName)).getText().toString();
        LatLng pos = mMapCallback.curMarker.getPosition();
        //TODO: handrolled urlEncoding. Simply replace spaces. Need to change this
        String url = ("https://" + MainActivity.cjsServerIp
                + ":3000/events/addEvent/"
                + title + "/"
                + instructorName + "/"
                + price + "/"
                + pos.longitude + "/"
                + pos.latitude).replaceAll("\\s", "%20");


        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sendCreateEventRequest", response);
                Toast toast = Toast.makeText(CreateEventActivity.this, "event created", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("sendCreateEventRequest", "error:" + error.toString());
                Toast toast = Toast.makeText(CreateEventActivity.this, "error: event not created. try again", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MainActivity.requestQueue.add(request);
    }

}
