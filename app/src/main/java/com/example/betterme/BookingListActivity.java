package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.betterme.Adapter.BookingListAdapter;
import com.example.betterme.Interface.RecyclerViewOnItemClickListener;
import com.example.betterme.Models.Booking;
import com.example.betterme.Models.Instructor;
import com.example.betterme.Shared.Shared;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingListActivity extends AppCompatActivity implements RecyclerViewOnItemClickListener {

    RecyclerView recyclerView;
    DatabaseReference database;
    Button settings_btn, profile_btn;
    ArrayList<Booking> list = new ArrayList<>();
    BookingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);
        recyclerView = findViewById(R.id.booking_history_rv);
        settings_btn = findViewById(R.id.settings_btn);
        profile_btn= findViewById(R.id.profile_btn);

        database = FirebaseDatabase.getInstance().getReference("Booking");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setAdapter();


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Booking booking = dataSnapshot.getValue(Booking.class);

                    if(booking.getInstructorId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        list.add(booking);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        setAdapter();

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingListActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingListActivity.this, InstructorProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setAdapter() {
        adapter = new BookingListAdapter(this,list,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClickListener(int position) {
        Shared.getInstance().selectedBooking = list.get(position);
        Intent intent = new Intent(BookingListActivity.this, BookingDetailsActivity.class);
        startActivity(intent);
    }
}