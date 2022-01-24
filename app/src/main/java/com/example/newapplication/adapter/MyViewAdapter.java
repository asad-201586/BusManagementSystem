package com.example.newapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newapplication.R;

import java.util.List;

public class MyViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<String> department;

    public MyViewAdapter(Context context, List<String> department) {
        this.context = context;
        this.department = department;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.department_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textView.setText(department.get(position));
    }

    @Override
    public int getItemCount() {
        return department.size();
    }
}
