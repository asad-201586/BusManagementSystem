package com.example.newapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AboutNstuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_nstu);


        getSupportActionBar().setTitle("About NSTU");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}