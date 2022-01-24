package com.example.newapplication.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newapplication.Common;
import com.example.newapplication.ItemClickListener;
import com.example.newapplication.R;
import com.example.newapplication.model.SeatPlanModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<SeatPlanModel> seatList;
    private ItemClickListener itemClickListener;
    String userID;
    private Boolean isMySeatBooked = false;

    public MyAdapter(Context context, ArrayList<SeatPlanModel> seatList,ItemClickListener itemClickListener) {
        this.context = context;
        this.seatList = seatList;
        this.itemClickListener = itemClickListener;
        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context). inflate(R.layout.item_seat, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SeatPlanModel model = seatList.get(position);
        holder.seatNo.setText(model.getId());

        if (model.getId().equals(Common.SELECTED_SEAT_NO) && !isMySeatBooked){
            holder.seatNo.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.seatNo.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.seatNo.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.seatNo.setTextColor(context.getResources().getColor(R.color.black));
        }

        if (!model.getBooked_by().equals("")){
            holder.seatNo.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.seatNo.setTextColor(context.getResources().getColor(R.color.white));
        }

        if (model.getBooked_by().equals(userID)){
            holder.seatNo.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.seatNo.setTextColor(context.getResources().getColor(R.color.white));
            isMySeatBooked = true;
        }


        holder.seatNo.setOnClickListener(view -> {
            Log.d("ripon_db", "onBindViewHolder: booked_by_id value: "+model.getBooked_by());
            Log.d("ripon_db", "onBindViewHolder: my_id value: "+userID);
            if (model.getBooked_by().equals(userID)){
                showAlertDialog(position,holder.seatNo);
            }
            else if (!model.getBooked_by().equals("")){
                Toast.makeText(context, "Already booked", Toast.LENGTH_SHORT).show();
            }
            else if (isMySeatBooked){
                Toast.makeText(context, "You already booked a seat", Toast.LENGTH_SHORT).show();
            }
            else {
                itemClickListener.itemClicked(position);
            }

        });


    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView seatNo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            seatNo = itemView.findViewById(R.id.textId);
        }
    }

    public void showAlertDialog(Integer position,TextView seatTV){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage("Are you sure, you want to unselect your seat?");
        builder.setNeutralButton("Cancel",null);
        builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //seatTV.setBackgroundColor(context.getResources().getColor(R.color.white));
                //seatTV.setTextColor(context.getResources().getColor(R.color.black));
                itemClickListener.updateSeat(position);
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
}
