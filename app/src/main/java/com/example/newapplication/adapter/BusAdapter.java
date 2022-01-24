package com.example.newapplication.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.newapplication.R;
import com.example.newapplication.model.Bus;

import java.util.List;

public class BusAdapter extends ArrayAdapter<Bus> {

    private Activity context;
    private List<Bus> busList;

    public BusAdapter(Activity context, List<Bus> busList) {
        super(context, R.layout.bus_adapter, busList);
        this.context = context;
        this.busList = busList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.bus_adapter, null, true);

        TextView name = view.findViewById(R.id.busNameId);
        TextView from = view.findViewById(R.id.busFromId);
        TextView to = view.findViewById(R.id.busToId);
        TextView time = view.findViewById(R.id.busTimeId);

        Bus bus = busList.get(position);

        name.setText(bus.getName());
        from.setText("From: " + bus.getFrom());
        to.setText("To: " + bus.getTo());
        time.setText("Time: " + bus.getTime());

        return view;
    }

}
