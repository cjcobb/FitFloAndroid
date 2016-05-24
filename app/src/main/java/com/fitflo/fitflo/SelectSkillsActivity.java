package com.fitflo.fitflo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;
/*
utility activity to select skills
could use this when an instructor creates a profile, they
can select skills they specialize in, and users can search those.
 */
public class SelectSkillsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_skills);
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


    /*
    all the skills are hardcoded right now
    i dont think we can have users input their own skills
    i think we need a predetermined set of skills
    that users can select
    we could have an other section that they fill in themselves
    and then we can see what skills we need to add to
    the set of standard skills
     */
    protected void onDestroy() {
        super.onDestroy();
        Intent skillsIntent = new Intent();
        ArrayList<String> skills = new ArrayList<>();

        CheckBox cb = (CheckBox) findViewById(R.id.takedownsCheckbox);
        if(cb.isChecked()) {
            skills.add(cb.getText().toString());
        }
        cb = (CheckBox) findViewById(R.id.legRidingCheckbox);
        if(cb.isChecked()) {
            skills.add(cb.getText().toString());
        }
        cb = (CheckBox) findViewById(R.id.topCheckbox);
        if(cb.isChecked()) {
            skills.add(cb.getText().toString());
        }
        cb = (CheckBox) findViewById(R.id.handfightingCheckbox);
        if(cb.isChecked()) {
            skills.add(cb.getText().toString());
        }
        cb = (CheckBox) findViewById(R.id.bottomCheckbox);
        if(cb.isChecked()) {
            skills.add(cb.getText().toString());
        }
        cb = (CheckBox) findViewById(R.id.freestyleCheckbox);
        if(cb.isChecked()) {
            skills.add(cb.getText().toString());
        }
        cb = (CheckBox) findViewById(R.id.grecoCheckbox);
        if(cb.isChecked()) {
            skills.add(cb.getText().toString());
        }
        //passing back an array of strings that represent
        //selected skills
        skillsIntent.putExtra("skills",skills.toArray());
        setResult(RESULT_OK,skillsIntent);
    }

}
