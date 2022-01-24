package com.example.newapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newapplication.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    Context context;
    String [] name, post, phone;

    public ContactAdapter(Context context, String[] name, String[] post, String[] phone) {
        this.context = context;
        this.name = name;
        this.post = post;
        this.phone = phone;
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.contacts_single_row, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.name.setText(name [position]);
        holder.post.setText(post [position]);
        holder.phone.setText(phone [position]);

        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + holder.phone.getText().toString()));
                context.startActivity(intentCall);
            }
        });

    }

    @Override
    public int getItemCount() {
        return name.length;
    }

}
