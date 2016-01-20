package com.fitflo.fitflo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NavDrawer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        String[] osArray = new String[] {"OSX","Linux","Windows","Android","iOS"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,osArray);
        ListView listView = (ListView) findViewById(R.id.navList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
