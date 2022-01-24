package com.example.newapplication.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newapplication.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView textView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.departmentTextviewId);

    }
}
