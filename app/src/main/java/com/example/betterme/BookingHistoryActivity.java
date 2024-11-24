package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.betterme.Adapter.BookingHistoryAdapter;
import com.example.betterme.Adapter.BookingListAdapter;
import com.example.betterme.Interface.RecyclerViewOnItemClickListener;
import com.example.betterme.Models.Booking;
import com.example.betterme.Models.BookingHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingHistoryActivity extends AppCompatActivity implements RecyclerViewOnItemClickListener {


    RecyclerView recyclerView;
    DatabaseReference database;
    ArrayList<Booking> list = new ArrayList<>();
    BookingHistoryAdapter adapter;
    Button back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        recyclerView = findViewById(R.id.order_history_rv);
        database = FirebaseDatabase.getInstance().getReference("Booking");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        back_btn = findViewById(R.id.btn_back);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Booking booking = dataSnapshot.getValue(Booking.class);
                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(booking.userId)){
                        list.add(booking);
                    }

                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        onbacklick();
        setAdapter();

    }

    private void onbacklick() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setAdapter() {
        adapter = new BookingHistoryAdapter(this, list, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onItemClickListener(int position) {

    }
}