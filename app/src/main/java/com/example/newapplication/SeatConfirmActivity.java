package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newapplication.model.Teachers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class SeatConfirmActivity extends AppCompatActivity {

    private EditText teachersName, departmentName, seatNumber, passcodeNo;
    TextView busNameTV,seatNumberTV;
    private Button seatBookedButton;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DocumentReference documentReference;
    String userID;

    private String busId,seatId,busName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_confirm);

        getPassValues();

        getSupportActionBar().setTitle("Confirm Bus Seat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        teachersName = findViewById(R.id.teacherNameEditTextId);
        departmentName = findViewById(R.id.departmentNameEditTextId);
        busNameTV = findViewById(R.id.text_bus_name);
        seatNumberTV = findViewById(R.id.text_seat_number);
        passcodeNo = findViewById(R.id.passcodeEditTextId);
        seatBookedButton = findViewById(R.id.seatBookedButtonId);

        documentReference = FirebaseFirestore.getInstance().collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                teachersName.setText(value.getString("name"));
                departmentName.setText(value.getString("department"));
            }
        });

        seatBookedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                String confirmpasscodeNo = "Ahmed123";
                String userpasscodeNo = passcodeNo.getText().toString();

                if (confirmpasscodeNo.equalsIgnoreCase(userpasscodeNo)) {
                    bookedData();
                    addBookedBy();
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Seat is booked successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SeatConfirmActivity.this, "Enter correct Passcode No", Toast.LENGTH_SHORT).show();
                    passcodeNo.requestFocus();
                }
            }

            private void bookedData() {

                String name = teachersName.getText().toString().trim();
                String department = departmentName.getText().toString().trim();

                String key = databaseReference.child("Teachers").push().getKey();

                if (key == null){
                    Toast.makeText(SeatConfirmActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }

                Teachers teachers = new Teachers();
                teachers.setId(key);
                teachers.setBusId(busId);
                teachers.setSeatId(seatId);
                teachers.setName(name);
                teachers.setDepartment(department);
                teachers.setUserId(userID);

                databaseReference.child("Teachers").child(key).setValue(teachers);
            }
        });

        setData();

    }

    private void addBookedBy() {
        databaseReference.child("Buses").child(busId).child("seat").addListenerForSingleValueEvent(valueEventListener);
    }

    private void setData() {
        busNameTV.setText(busName);
        seatNumberTV.setText(seatId);
    }

    private void getPassValues() {
        if (getIntent().hasExtra("bus_id"))
            busId = getIntent().getStringExtra("bus_id");
        if (getIntent().hasExtra("seat_id"))
            seatId = getIntent().getStringExtra("seat_id");
        if (getIntent().hasExtra("bus_name"))
            busName = getIntent().getStringExtra("bus_name");

        Log.e("app_db1", "getPassValues: bus_id: "+busId );
        Log.e("app_db1", "getPassValues: seat_id: "+seatId );
        Log.e("app_db1", "getPassValues: bus_name: "+busName );
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                for (DataSnapshot snp: snapshot.getChildren()){
                    String serverSeatId = Objects.requireNonNull(snp.child("id").getValue()).toString();
                    if (serverSeatId.equals(seatId)){
                        databaseReference.child("Buses").child(busId).child("seat")
                                .child(Objects.requireNonNull(snp.getKey()))
                                .child("booked_by").setValue(userID);
                        break;
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}