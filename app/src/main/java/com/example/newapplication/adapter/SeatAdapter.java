package com.example.newapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newapplication.R;
import com.example.newapplication.SelectingSeatActivity;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.MyViewHolder> {

    Context context;
    String[] busName, busFrom, busTo, busTime;
    int[] images;

    public SeatAdapter(Context context, String[] busName, String[] busFrom, String[] busTo, String[] busTime, int[] images) {
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

        holder.textBusName.setText(busName[position]);
        holder.textBusFrom.setText(busFrom[position]);
        holder.textBusTo.setText(busTo[position]);
        holder.textBusTime.setText(busTime[position]);
        holder.busImageView.setImageResource(images[0]);
    }

    @Override
    public int getItemCount() {
        return busName.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textBusName, textBusFrom, textBusTo, textBusTime;
        ImageView busImageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textBusName = itemView.findViewById(R.id.busNameId);
            textBusFrom = itemView.findViewById(R.id.busFromId);
            textBusTo = itemView.findViewById(R.id.busToId);
            textBusTime = itemView.findViewById(R.id.busTimeId);
            busImageView = itemView.findViewById(R.id.busImageId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Toast.makeText(context,"NO: "+(position+1)+" Bus Selected",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, SelectingSeatActivity.class);
            context.startActivity(intent);
        }
    }
}
