package com.example.newapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newapplication.R;
import com.example.newapplication.SelectingSeatActivity;
import com.example.newapplication.model.Bus;

import java.util.ArrayList;

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<Bus> busList;

    public BusListAdapter(Context context, ArrayList<Bus> busList) {
        this.context = context;
        this.busList = busList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.bus_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Bus model = busList.get(position);
        setData(holder,model);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SelectingSeatActivity.class);
            Log.d("app_db", "onBindViewHolder: bus id in adapter : "+model.getId());
            intent.putExtra("bus_id",model.getId());
            intent.putExtra("bus_name",model.getName());

            context.startActivity(intent);
        });
    }

    private void setData(MyViewHolder holder, Bus model) {
        holder.from.setText(model.getFrom());
        holder.to.setText(model.getTo());
        holder.name.setText(model.getName());
        holder.time.setText(model.getTime());
    }

    @Override
    public int getItemCount() {
        if (busList.size() != 0) return busList.size();
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView from;
        TextView to;
        TextView time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.busNameId);
            from = itemView.findViewById(R.id.busFromId);
            to = itemView.findViewById(R.id.busToId);
            time = itemView.findViewById(R.id.busTimeId);
        }
    }
}
