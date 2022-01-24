package com.example.newapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newapplication.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    String[] busName, busFrom, busTo, busTime;
    int[] images;

    public CustomAdapter(Context context, String[] busName, String[] busFrom, String[] busTo, String[] busTime, int[] images) {
        this.context = context;
        this.busName = busName;
        this.busFrom = busFrom;
        this.busTo = busTo;
        this.busTime = busTime;
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.bus_adapter, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textviewBusName.setText(busName[position]);
        holder.textviewBusFrom.setText(busFrom[position]);
        holder.textviewBusTo.setText(busTo[position]);
        holder.textviewBusTime.setText(busTime[position]);
        holder.busImageView.setImageResource(images[0]);
    }

    @Override
    public int getItemCount() {
        return busName.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textviewBusName, textviewBusFrom, textviewBusTo, textviewBusTime;
        ImageView busImageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textviewBusName = itemView.findViewById(R.id.busNameId);
            textviewBusFrom = itemView.findViewById(R.id.busFromId);
            textviewBusTo = itemView.findViewById(R.id.busToId);
            textviewBusTime = itemView.findViewById(R.id.busTimeId);
            busImageView = itemView.findViewById(R.id.busImageId);
        }
    }
}
