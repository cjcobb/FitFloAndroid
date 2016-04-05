package com.fitflo.fitflo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CoachesProfile extends AppCompatActivity {
    public final int IMAGE_CHOOSER = 101;
    public final String isCoachKey = "isCoach";
    public final String createProfileText = "Create Coach's Profile";
    public final String updateProfileText = "Update Coach's Profile";
    public final String selectImageText = "Select Image";
    public final String changeImageText = "Change Image";
    public final String hasImageKey ="hasImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaches_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        boolean isCoach = FileUtils.getBoolean(this,isCoachKey);
        Button createOrUpdateBtn = (Button) findViewById(R.id.create_or_update_btn);
        if(isCoach) {
            createOrUpdateBtn.setText(updateProfileText);
        } else {
            createOrUpdateBtn.setText(createProfileText);
        }
        boolean hasImage = FileUtils.getBoolean(this,hasImageKey);
        Button selectOrUpdateImgBtn = (Button) findViewById(R.id.select_image_button);
        if(hasImage) {
            selectOrUpdateImgBtn.setText("Update Image");
        } else {
            selectOrUpdateImgBtn.setText("Select Image");
        }
    }

    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_CHOOSER);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case IMAGE_CHOOSER:
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        ViewGroup mainView = (ViewGroup) findViewById(R.id.mainContent);
                        ImageView imageView = new ImageView(this);
                        Bitmap imageB = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(imageB);
                        imageView.setLayoutParams(
                                new Toolbar.LayoutParams(300,300));
                        mainView.addView(imageView,2);
                        FileUtils.writeBoolean(this,hasImageKey,true);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void createOrUpdateProfile(View view) {
        boolean isCoach = FileUtils.getBoolean(this,isCoachKey);
        if(!isCoach) {
            FileUtils.writeBoolean(this,isCoachKey,true);
        }

    }

}
