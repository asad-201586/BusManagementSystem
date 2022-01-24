package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newapplication.adapter.BusAdapter;
import com.example.newapplication.model.Bus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BusListActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference databaseReference;
    private List<Bus> busList;
    private BusAdapter busAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        getSupportActionBar().setTitle("Bus List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listviewId);
        databaseReference = FirebaseDatabase.getInstance().getReference("Buses");

        busList = new ArrayList<>();
        busAdapter = new BusAdapter(BusListActivity.this, busList);

    }

    @Override
    protected void onStart() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                busList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Bus bus = dataSnapshot1.getValue(Bus.class);
                    busList.add(bus);
                }
                listView.setAdapter(busAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onStart();

        listView.setOnItemClickListener((parent, view, position, id) -> {

            Bus bus = busList.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(BusListActivity.this);

            builder.setTitle("Delete Bus")
                    .setMessage("Are You sure to delete this Bus")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Buses");
                        String busId = busList.get(position).getId();
                        reference.child(busId).removeValue();
                    }).setNegativeButton("No", (dialog, which) ->
                    Toast.makeText(BusListActivity.this, " ", Toast.LENGTH_SHORT).show()).show();
        });
    }
}