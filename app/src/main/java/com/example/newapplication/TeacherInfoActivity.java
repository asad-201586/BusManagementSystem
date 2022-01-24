package com.example.newapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.newapplication.adapter.MyViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeacherInfoActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);

        getSupportActionBar().setTitle("Teacher's Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewId);

        //Department Name
        List<String> name = new ArrayList<>();
        name.add("Computer Science and Telecommunication Engineering (CSTE)");
        name.add("Applied Chemistry and Chemical Engineering (ACCE)");
        name.add("Information and Communication Engineering (ICE)");
        name.add(" Electrical and Electronic Engineering (EEE) ");
        name.add("Fisheries & Marine Science (FIMS)");
        name.add("Department of Pharmacy");
        name.add("Department of Microbiology (MBG)");
        name.add("Department of Applied Mathematics");
        name.add("Department of Statistics");
        name.add("Food Technology and Nutrition Science (FTNS)");
        name.add("Environmental Science and Disaster Management (ESDM)");
        name.add("Department of Agriculture");
        name.add("Biotechnology and Genetic Engineering (BGE)");
        name.add("Department of Oceanography");
        name.add(" Biochemistry and Molecular Biology (BMB) ");
        name.add("Department of Zoology");
        name.add("Department of Economics");
        name.add("Bangladesh and Liberation War Studies");
        name.add("Department of Sociology");
        name.add("Department of English");
        name.add("Department of Bangla");
        name.add("Department of Social Work");
        name.add("Department of Business Administration (DBA)");
        name.add("Department of tourism and Hospitality Management (THM)");
        name.add("Department of Management Information Systems (MIS)");
        name.add("Department of Education");
        name.add("Department of Education Administration");
        name.add("Department of Law");
        name.add(" Institute of Information Science (IIS)");
        name.add(" Institute of Information Technology (IIT)");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyViewAdapter adapter = new MyViewAdapter(this, name);
        recyclerView.setAdapter(adapter);

    }
}

