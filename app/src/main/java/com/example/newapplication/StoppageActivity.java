package com.example.newapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StoppageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoppage);

        getSupportActionBar().setTitle("Bus Stoppage");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}