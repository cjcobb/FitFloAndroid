package com.fitflo.fitflo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.jar.JarException;

public class MainActivity extends AppCompatActivity {
    final static String cjsServerIp = "192.168.1.26";
    final static String raulsServerIp = null;
    static RequestQueue requestQueue = null;

    private ArrayAdapter<String> mSearchResultsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        requestQueue = Volley.newRequestQueue(this);

        //adpater is how a view keeps in sync with datastructure
        mSearchResultsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>());
        ListView listView = (ListView) findViewById(R.id.searchResultsList);
        listView.setAdapter(mSearchResultsAdapter);

        //listener for list items. bring up event details activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //this is how we start new activities

                //first create an intent, first arg is context, second is class
                Intent intent = new Intent(MainActivity.this, EventDetailsActivity.class);



               /* //add a key value to intent, way to pass messages
                //EXTRA_MESSAGE is static string at top of file
                intent.putExtra(EXTRA_MESSAGE, message);
                if(message.equals("")) {
                    intent.putExtra(EXTRA_MESSAGE, msg);
                }*/
                //start the activity, using the intent
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.create_event:
                Intent intent = new Intent(this,CreateEventActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //uses Volley. check out build.gradle (module: app) to compile
    //also, notice the permission added in AndroidManifest.xml
    public void sendRootRequest(View view) {
        final TextView textView = (TextView) findViewById(R.id.rootResponseText);

        //notice the http:// prefix. necessary
        String ip = "http://" + cjsServerIp + ":8080";

        StringRequest request = new StringRequest(Request.Method.GET,ip,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                textView.setText("Received : " + response);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("sorry, error!"+error);
            }
        });
        requestQueue.add(request);
    }


    //for now, this function simply adds an item to the list
    //however, the commented out code shows how to actually get all events
    public void sendGetAllEventsRequest(View view) {
        Log.d("hey","got inside");
       /* mSearchResultsAdapter.add("clicked");
        mSearchResultsAdapter.notifyDataSetChanged();*/


        //notice the http:// prefix. necessary
        String ip = "http://" + cjsServerIp + ":8080/getAllEvents";

        JsonArrayRequest request = new JsonArrayRequest(ip,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mSearchResultsAdapter.clear();
                for(int i = 0; i < response.length();i++) {
                    try {
                        JSONObject jObj = response.getJSONObject(i);
                        String title = jObj.getString("title");
                        String instructor = jObj.getString("instructor");
                        double price = jObj.getDouble("price");
                        mSearchResultsAdapter.add(title + ", " + instructor + ", " + price + " dollars");
                    } catch(JSONException exc) {
                        mSearchResultsAdapter.add("error");
                    }



                }
                mSearchResultsAdapter.notifyDataSetChanged();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSearchResultsAdapter.clear();
                mSearchResultsAdapter.add(error.toString());
                mSearchResultsAdapter.notifyDataSetChanged();

            }
        });

        requestQueue.add(request);
    }

}
