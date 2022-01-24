package com.example.newapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AboutApplicationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);

        getSupportActionBar().setTitle("About Application");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}