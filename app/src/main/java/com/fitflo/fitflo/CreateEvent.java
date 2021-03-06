package com.fitflo.fitflo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/*
screen to create an event, which sends an invite to user
 */
public class CreateEvent extends AppCompatActivity {
    MapCallback mMapCallback;
    public final int SELECT_SKILLS_ACTIVITY = 1;
    public final int SELECT_DATE_ACTIVITY = 2;
    public Set<String> skills;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMapCallback = new MapCallback(HomeScreen.mGoogleApiClient);
        SupportMapFragment mapFrag = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFrag));
                mapFrag.getMapAsync(mMapCallback);
        skills = new HashSet<>();

        String pattern = "EEE, MMM dd, yyyy";
        String formattedDate  = new SimpleDateFormat(pattern).format(new Date());
        TextView dateTextField = (TextView) findViewById(R.id.date);
        dateTextField.setText(formattedDate);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle("Create a new event");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_event_menu,menu);
        return true;
    }

    public void onSelectDate(View view) {
        startActivityForResult(new Intent(CreateEvent.this,DateSelectActivity.class),SELECT_DATE_ACTIVITY);
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
        Intent intent = new Intent(CreateEvent.this, SelectSkillsActivity.class);
        startActivityForResult(intent,SELECT_SKILLS_ACTIVITY);
    }

    public void onSubmitClick(View view) {
        sendCreateEventRequest();
    }

    public void sendCreateEventRequest() {


        //these need to be stored somewhere and retreived here.
        //they could just be on the screen
        String title = "";
        String price = "";
        String instructorName = "";
        LatLng pos = mMapCallback.curMarker.getPosition();
        //TODO: handrolled urlEncoding. Simply replace spaces. Need to change this
        String url = ("https://" + HomeScreen.cjsServerIp
                + ":3000/events/addEvent/"
                + title + "/"
                + instructorName + "/"
                + price + "/"
                + pos.longitude + "/"
                + pos.latitude).replaceAll("\\s", "%20");


        //builds the request, and the callback
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sendCreateEventRequest", response);
                Toast toast = Toast.makeText(CreateEvent.this, "event created", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("sendCreateEventRequest", "error:" + error.toString());
                Toast toast = Toast.makeText(CreateEvent.this, "error: event not created. try again", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        //not sure if this is neccessary.
        //for some reason i was encountering timeouts here
        //and this fixed it. that was a while ago tho
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //sends the request
        HomeScreen.requestQueue.add(request);
    }

}
