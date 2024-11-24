package com.example.betterme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterme.Interface.RecyclerViewOnItemClickListener;
import com.example.betterme.Models.Booking;
import com.example.betterme.Models.BookingHistory;
import com.example.betterme.R;

import java.util.ArrayList;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {

    Context context;
    ArrayList<Booking> list;
    RecyclerViewOnItemClickListener onItemClickListener;


    public BookingHistoryAdapter(Context context, ArrayList<Booking> list, RecyclerViewOnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_history, parent,false);
        BookingHistoryAdapter.ViewHolder holder = new BookingHistoryAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {

        Booking bookingHistory = list.get(position);
        holder.bookingId.setText("Booking ID: " + bookingHistory.getId());
        holder.category.setText("Category Name: " + bookingHistory.getCategory());
        holder.bookingDate.setText("Booking Date: " + bookingHistory.getDate());
        holder.bookingTime.setText("Booking Time: " +bookingHistory.getTime());
        holder.status.setText("Booking Status: " +bookingHistory.getStatus());

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
    public int getItemCount()  {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView bookingId,instructorName,category,bookingDate,bookingTime,status;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            bookingId = itemView.findViewById(R.id.booking_id_tv);
            instructorName = itemView.findViewById(R.id.instructor_name_tv);
            category = itemView.findViewById(R.id.category_tv);
            bookingDate = itemView.findViewById(R.id.booking_date_tv);
            bookingTime = itemView.findViewById(R.id.booking_time_tv);
            status = itemView.findViewById(R.id.status_tv);


        }
    }
}

