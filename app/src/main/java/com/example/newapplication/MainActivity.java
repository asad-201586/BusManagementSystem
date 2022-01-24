package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.newapplication.model.SeatPlanModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView navHeaderUsername;
    TextView navHeaderEmail;
    ImageView navHeaderProImage;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    AlertDialog.Builder admin_alert;
    LayoutInflater inflater;
    View hView;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;

    String userId;

    private Button homeButton;
    private Button scheduleButton;
    private Button teacherButton;

    private Button stoppageButton;
    private Button trackingButton;
    private Button bookingButton;

    private Button contactButton;
    private Button nstuButton;
    private Button settingButton;
    private SpManager spManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seatPlan();
        iniSP();

        drawerLayout = findViewById(R.id.drawerLayoutId);
        navigationView = findViewById(R.id.navigationViewId);
        navigationView.bringToFront();

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hView = navigationView.getHeaderView(0);

        navHeaderUsername = hView.findViewById(R.id.navHeaderUserNameId);
        navHeaderEmail = hView.findViewById(R.id.navHeaderEmailId);
        navHeaderProImage = hView.findViewById(R.id.navProImageId);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                navHeaderUsername.setText(value.getString("name"));
                navHeaderEmail.setText(value.getString("email"));
            }
        });

        admin_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();

        homeButton = findViewById(R.id.homeButtonId);
        scheduleButton = findViewById(R.id.scheduleButtonId);
        teacherButton= findViewById(R.id.teacherButtonId);

        stoppageButton = findViewById(R.id.stoppageButtonId);
        trackingButton = findViewById(R.id.trackingButtonId);
        bookingButton = findViewById(R.id.bookingButtonId);

        contactButton = findViewById(R.id.contactButtonId);
        nstuButton = findViewById(R.id.aboutNSTUButtonId);
        settingButton = findViewById(R.id.settingButtonId);

        // First Row
        homeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        scheduleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        teacherButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TeacherInfoActivity.class);
                startActivity(intent);
            }
        });

        // Second Row
        stoppageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StoppageActivity.class);
                startActivity(intent);
            }
        });

        trackingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrackingActivity.class);
                startActivity(intent);
            }
        });

        bookingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SeatBookingActivity.class);
                startActivity(intent);
            }
        });

        //Third Row
        contactButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });

        nstuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutNstuActivity.class);
                startActivity(intent);
            }
        });

        settingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

    }

    private void iniSP() {
        spManager = new SpManager(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.adminmenuId) {

            if (SpManager.getBoolean(MainActivity.this,SpManager.ADMIN_LOGIN_STATUS)){
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
            }else {
                View view = inflater.inflate(R.layout.admin_layout, null);
                admin_alert.setTitle("For Admin Panel")
                        .setMessage("Enter Admin Passcode for Admin login")
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText passcode = view.findViewById(R.id.adminPopId);
                                if (passcode.getText().toString().isEmpty()) {
                                    passcode.setError("Required Filed");
                                    Toast.makeText(getApplicationContext(), "Enter Admin valid Passcode", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                String confirmpasscode = "Ahmed123";
                                String userpasscode = passcode.getText().toString();

                                if (confirmpasscode.equalsIgnoreCase(userpasscode)) {
                                    startActivity(intent);
                                    SpManager.saveBoolean(MainActivity.this,SpManager.ADMIN_LOGIN_STATUS,true);
                                    Toast.makeText(getApplicationContext(), "Admin Panel Enter Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Enter correct Admin Passcode", Toast.LENGTH_SHORT).show();
                                    passcode.requestFocus();
                                }
                            }
                        }).setNegativeButton("Cancel", null)
                        .setView(view)
                        .create().show();
            }

        }
        return super.onOptionsItemSelected(item);

    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.homeMenuId:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;

            case R.id.profileMenuId:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;

            case R.id.resetPasswordId:
                startActivity(new Intent(getApplicationContext(), ResetPassword.class));
                break;

            case R.id.logoutId:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
                break;

            case R.id.aboutMenuId:
                startActivity(new Intent(getApplicationContext(), AboutApplicationActivity.class));
                break;
        }

        return false;
    }

    public void seatPlan(){

        Log.d("my_seat", "seatPlan: called" );

        ArrayList<SeatPlanModel> seatList = new ArrayList<>();

        for (int i=0;i<8;i++){
            for (int j=0;j<4;j++){
                String value;
                value = "A"+(i+1)+(j+1);

                SeatPlanModel m1 = new SeatPlanModel();
                m1.setName(value);
                m1.setId(value);
                seatList.add(m1);
                Log.d("my_seat", "seatPlan: seat: "+value );
            }
        }

    }
}