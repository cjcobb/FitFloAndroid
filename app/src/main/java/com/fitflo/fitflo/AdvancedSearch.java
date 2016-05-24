package com.fitflo.fitflo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;


/*
This activity is meant to be a screen where users can
create a more complex search query, such as searching only by
instructor name or changing the radius, or giving a price range,
etc.
 */
public class AdvancedSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.advanced_search_menu,menu);
        return true;
    }

}
