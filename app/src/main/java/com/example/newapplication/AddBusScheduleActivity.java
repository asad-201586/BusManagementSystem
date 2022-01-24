package com.example.newapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.newapplication.model.BusSchedule;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class AddBusScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText busName;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private TextView setTime;

    private TimePicker timePicker;
    private int timeHour, timeMinute;
    private Button submitData, loadData;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus_schedule);

        getSupportActionBar().setTitle("Add Bus Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        busName = findViewById(R.id.busNameEditTextId);
        spinnerFrom = findViewById(R.id.spinnerFromId);
        spinnerTo = findViewById(R.id.spinnerToId);
        setTime = findViewById(R.id.setTimeTextViewId);

        submitData = findViewById(R.id.submitDataButtonId);
        loadData = findViewById(R.id.loadDataButtonId);

        databaseReference = FirebaseDatabase.getInstance().getReference("Schedule");

        //From Location
        Spinner spinnerFrom = findViewById(R.id.spinnerFromId);
        String[] itemsFrom = new String[]{"FROM", "Chourasta", "Maijdee", "Dotter Haat", "Sonapur", "Campus"};
        ArrayAdapter<String> adapterFrom = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsFrom);
        spinnerFrom.setAdapter(adapterFrom);

        //To Location
        Spinner spinnerTo = findViewById(R.id.spinnerToId);
        String[] itemsTo = new String[]{"TO", "Campus", "Sonapur", "Dotter Haat", "Maijdee", "Chourasta"};
        ArrayAdapter<String> adapterTo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsTo);
        spinnerTo.setAdapter(adapterTo);

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Initialize time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddBusScheduleActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Initialize hour And minute
                                timeHour = hourOfDay;
                                timeMinute = minute;
                                //Store hour and minute in String
                                String time = timeHour + ":" + timeMinute;
                                //Initialize 24 hours time format
                                SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24Hours.parse(time);
                                    //Initilize 12Hours time format
                                    SimpleDateFormat f12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    //Set Selected time on text view
                                    setTime.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12, 0, false
                );
                //Set transparent background
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //Display previous Selected time
                timePickerDialog.updateTime(timeHour, timeMinute);
                //Show Dialog
                timePickerDialog.show();
            }
        });

        submitData.setOnClickListener(this);

        loadData.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ScheduleListActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onClick(View v) {
        submitData();
    }

    public void submitData() {

        String name = busName.getText().toString().trim();
        String from = spinnerFrom.getSelectedItem().toString().trim();
        String to = spinnerTo.getSelectedItem().toString().trim();
        String time = setTime.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            //Bus Name is empty
            Toast.makeText(this, "Enter Bus Name", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.equals(from, "From")) {
            //Location From is empty
            Toast.makeText(this, "Select a Location", Toast.LENGTH_SHORT).show();

        }

        else if (TextUtils.equals(to, "To")) {
            //Location To  is empty
            Toast.makeText(this, "Select a Location", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(time)) {
            //password is empty
            Toast.makeText(this, "Enter Bus Schedule", Toast.LENGTH_SHORT).show();
        }
        else{
            String id = databaseReference.push().getKey();
            BusSchedule busSchedule = new BusSchedule(name, from, to, time,id);
            databaseReference.child(Objects.requireNonNull(id)).setValue(busSchedule);
            Toast.makeText(getApplicationContext(), "New Bus is Add", Toast.LENGTH_SHORT).show();
        }
        busName.setText(" ");
        setTime.setText("Select Time");
    }
}