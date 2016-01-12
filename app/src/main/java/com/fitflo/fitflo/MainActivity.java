package com.fitflo.fitflo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //uses Volley. check out build.gradle (module: app) to compile
    //also, notice the permission added in AndroidManifest.xml
    public void sendRootHttpRequest(View view) {

        final TextView textView = (TextView) findViewById(R.id.responseText);


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //notice the http:// prefix. necessary
        String ip = "http://192.168.1.26:8080";

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
}
