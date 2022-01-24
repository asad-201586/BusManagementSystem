package com.example.newapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.newapplication.adapter.ContactAdapter;

public class ContactsActivity extends AppCompatActivity {

    String [] name, post, phone;
    RecyclerView recyclerView;
    ContactAdapter ContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        getSupportActionBar().setTitle("Transport Section");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = getResources().getStringArray(R.array.contact_name);
        post = getResources().getStringArray(R.array.contact_post);
        phone = getResources().getStringArray(R.array.contact_phone);
        recyclerView = findViewById(R.id.contactRecyclerviewId);

        ContactAdapter = new ContactAdapter(this, name, post, phone);
        recyclerView.setAdapter(ContactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}