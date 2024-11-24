package com.example.betterme.Adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.Interface.RecyclerViewOnItemClickListener;
import com.example.betterme.Models.Booking;
import com.example.betterme.Models.Instructor;
import com.example.betterme.Models.User;
import com.example.betterme.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder>{


    Context context;
    ArrayList<Booking> list;
    RecyclerViewOnItemClickListener onItemClickListener;

    public BookingListAdapter(Context context, ArrayList<Booking> list, RecyclerViewOnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }

    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        Booking booking = list.get(position);

        holder.student_name.setText("ID: " + booking.getId());
        holder.booking_time.setText(booking.getTime());
        holder.category_name.setText(booking.getCategory());
        holder.status.setText(booking.getStatus());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView student_name;
        public TextView booking_time;
        public TextView category_name;
        public TextView status;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            student_name = (TextView) itemView.findViewById(R.id.student_name);
            booking_time = (TextView) itemView.findViewById(R.id.booking_time);
            category_name = (TextView) itemView.findViewById(R.id.category_name);
            status = (TextView) itemView.findViewById(R.id.status);

        }
    }
}
