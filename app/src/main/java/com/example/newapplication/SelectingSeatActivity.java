package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newapplication.adapter.MyAdapter;
import com.example.newapplication.model.SeatPlanModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SelectingSeatActivity extends AppCompatActivity implements ItemClickListener{

    private RecyclerView recyclerView;
    private ArrayList<SeatPlanModel> seatList;
    private MyAdapter adapter;

    private Button buttonBook;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String busId;
    private String busName;
    private TextView selectedSeatNoTV;
    private String userID;
    private LinearLayout layoutBottom;
    private Boolean isMySeatBooked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecting_seat);

        getSupportActionBar().setTitle("Select Your Favorite Seat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        initRecycler();
        getPassValue();
        getData();

        buttonBook.setOnClickListener(v -> {
            if (!Common.SELECTED_SEAT_NO.equals("")){
                Intent intent = new Intent(getApplicationContext(), SeatConfirmActivity.class);
                intent.putExtra("bus_id",busId);
                intent.putExtra("seat_id",Common.SELECTED_SEAT_NO);
                intent.putExtra("bus_name",busName);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select your seat first", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getPassValue() {
        if (getIntent().hasExtra("bus_id"))
            busId = getIntent().getStringExtra("bus_id");
        else busId = "";

        if (getIntent().hasExtra("bus_name"))
            busName = getIntent().getStringExtra("bus_name");
        else busName = "";

    }

    private void getData() {
        seatList.clear();
        if (busId.equals("")){
            Toast.makeText(this, "No bus id found", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("Buses").child(busId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snpSeat: snapshot.child("seat").getChildren()){
                        SeatPlanModel model;
                        model = snpSeat.getValue(SeatPlanModel.class);

                        if (!isMySeatBooked)
                            isMySeatBooked(Objects.requireNonNull(model).getBooked_by());

                        seatList.add(model);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isMySeatBooked(String booked_by) {
        if (booked_by.equals(userID)){
            layoutBottom.setVisibility(View.GONE);
            isMySeatBooked = true;
        }
    }

    private void initRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MyAdapter(SelectingSeatActivity.this,seatList,this);
        recyclerView.setAdapter(adapter);
    }

    private void init() {
        Common.SELECTED_SEAT_NO = "";
        databaseReference = FirebaseDatabase.getInstance().getReference();
        seatList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewId);
        buttonBook = findViewById(R.id.buttonBookId);
        selectedSeatNoTV = findViewById(R.id.text_seat_no_id);
        layoutBottom = findViewById(R.id.layout_bottom);
        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void itemClicked(int position) {
        Common.SELECTED_SEAT_NO = seatList.get(position).getId();
        selectedSeatNoTV.setText(Common.SELECTED_SEAT_NO);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateSeat(int position) {
        databaseReference
                .child("Buses")
                .child(busId)
                .child("seat")
                .child(String.valueOf(position))
                .child("booked_by")
                .setValue("").addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        getData();
                        Toast.makeText(SelectingSeatActivity.this, "Seat unselected", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(SelectingSeatActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                });

    }
}