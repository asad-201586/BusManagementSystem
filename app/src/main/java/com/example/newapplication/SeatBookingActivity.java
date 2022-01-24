package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.newapplication.adapter.BusAdapter;
import com.example.newapplication.model.Bus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeatBookingActivity extends AppCompatActivity {

    private ListView listView;
    private List<Bus> busList;
    private BusAdapter busAdapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_booking);

        Log.d("app_db", "onCreate: seatBooking activity found");

        getSupportActionBar().setTitle("Buses List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listviewId);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        busList = new ArrayList<>();
        busAdapter = new BusAdapter(SeatBookingActivity.this, busList);

    }

    @Override
    protected void onStart() {
        databaseReference.child("Buses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                busList.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Bus bus = dataSnapshot1.getValue(Bus.class);
                    busList.add(bus);
                }
                listView.setAdapter(busAdapter);

                listView.setOnItemClickListener((parent, view, position, id) -> {
                    goToSelectingSeatActivity(position);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onStart();
    }

    private void verifySeat(int position) {

    }

    private void goToSelectingSeatActivity(int position) {
        Intent intent = new Intent(getApplicationContext(), SelectingSeatActivity.class);
        intent.putExtra("bus_id",busList.get(position).getId());
        intent.putExtra("bus_name",busList.get(position).getName());
        startActivity(intent);
    }
}
