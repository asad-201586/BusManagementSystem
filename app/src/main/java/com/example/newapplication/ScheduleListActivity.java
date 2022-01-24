package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class ScheduleListActivity extends AppCompatActivity {

    private ListView listView;
    private List<Bus> busList;
    private BusAdapter busAdapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        getSupportActionBar().setTitle("Bus Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listviewId);

        databaseReference = FirebaseDatabase.getInstance().getReference("Schedule");
        busList = new ArrayList<>();

        busAdapter = new BusAdapter(ScheduleListActivity.this, busList);


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
            AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleListActivity.this);

            builder.setTitle("Delete Bus")
                    .setMessage("Are You sure to delete this Bus")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Schedule");
                        String busId = busList.get(position).getId();
                        reference.child(busId).removeValue();
                    }).setNegativeButton("No", (dialog, which) ->
                    Toast.makeText(ScheduleListActivity.this, " ", Toast.LENGTH_SHORT).show()).show();
        });
    }
}