package com.fitflo.fitflo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyEventsActivity extends AppCompatActivity {


    ArrayAdapter<FitFloEvent> mEventsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        makeEventsView();
        sendGetMyEventsRequest();

    }

    public void sendGetMyEventsRequest() {

        //TODO: dont hard code this
        String ip = "https://" + MainActivity.cjsServerIp + ":3000/users/getUserEvents/"+MainActivity.mUsername;

        JsonArrayRequest request = new JsonArrayRequest(ip,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mEventsAdapter.clear();
                for(int i = 0; i < response.length();i++) {
                    try {
                        JSONObject jObj = response.getJSONObject(i);
                        String id = jObj.getString("_id");
                        String title = jObj.getString("title");
                        String instructor = jObj.getString("instructor");
                        double price = jObj.getDouble("price");
                        mEventsAdapter.add(new FitFloEvent(id,instructor,title,price));
                    } catch(JSONException exc) {
                    }
                }
                mEventsAdapter.notifyDataSetChanged();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mEventsAdapter.clear();
                mEventsAdapter.notifyDataSetChanged();
                error.printStackTrace();

            }
        });

        MainActivity.requestQueue.add(request);
    }

    public void makeEventsView() {
        //adpater is how a view keeps in sync with datastructure
        mEventsAdapter = new ArrayAdapter<FitFloEvent>(this,android.R.layout.simple_list_item_1,new ArrayList<FitFloEvent>());
        ListView listView = (ListView) findViewById(R.id.my_events_list);
        listView.setAdapter(mEventsAdapter);

        /*//listener for list items. bring up event details activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyEventsActivity.this, EventDetailsActivity.class);
                startActivity(intent);
            }
        });
        */
    }

}
