package com.fitflo.fitflo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {
    final static String cjsServerIp = "192.168.1.26";
    final static String raulsServerIp = null;
    static RequestQueue requestQueue = null;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private SearchFragment displayedFragment;

    private ArrayAdapter<String> mSearchResultsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpNavDrawer();


        requestQueue = Volley.newRequestQueue(this);




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void setUpNavDrawer() {
        //adpater is how a view keeps in sync with datastructure
        String[] osArray = { "Search Events","Create New Event", "My Events", "My Account", "Logout"};
        ArrayAdapter<String> navDrawerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,osArray);
        ListView navView = (ListView) findViewById(R.id.navList);
        navView.setAdapter(navDrawerAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {


            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Drawer Item click listeners
        navView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id) {
                    //search events
                    case 0: {
                        displayedFragment = new SearchFragment();

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainContent, displayedFragment)
                                .commit();
                        mDrawerLayout.closeDrawers();
                        //do nothing
                        return;
                    }
                    //create event
                    case 1: {
                        Fragment fragment = new CreateEventFragment();

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.mainContent, fragment)
                                .commit();
                        mDrawerLayout.closeDrawers();
                        //do nothing
                        return;
                    }
                    //my events
                    case 2: {
                        return;
                    }
                    //my account
                    case 3: {
                        return;
                    }
                    //logout
                    case 4: {
                        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(
                                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean(getString(R.string.logged_in_key), false);
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        return;
                    }

                }
                Log.d("onItemClickListener", id + "");
            }
        });
    }

    @Override
    public void onResume() {

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean loggedIn = sharedPref.getBoolean(getString(R.string.logged_in_key), false);
        Log.d("MainActivity.onResume", "logged in is " + loggedIn);
        if(!loggedIn) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        super.onResume();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch(item.getItemId()) {
            case R.id.action_settings: {
                return true;
            }
            case R.id.create_event: {
                Intent intent = new Intent(this, CreateEventActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.logout: {
                SharedPreferences sharedPref = this.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.logged_in_key), false);
                editor.commit();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            }

        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
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
                textView.setText("sorry, error!" + error);
            }
        });
        requestQueue.add(request);
    }


    //for now, this function simply adds an item to the list
    //however, the commented out code shows how to actually get all events
    public void sendGetAllEventsRequest(View view) {
        displayedFragment.sendGetAllEventsRequest(view);

    }




}
