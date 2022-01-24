package com.example.newapplication.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newapplication.R;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    TextView name, post, phone;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.contactNameId);
        post = itemView.findViewById(R.id.contactPostId);
        phone = itemView.findViewById(R.id.contactMobileTextId);
    }
}
