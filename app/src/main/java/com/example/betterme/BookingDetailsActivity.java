package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.betterme.Models.Booking;
import com.example.betterme.Models.Instructor;
import com.example.betterme.Models.User;
import com.example.betterme.Shared.Shared;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingDetailsActivity extends AppCompatActivity {

    DatabaseReference database;
    User user;
    Button back_btn,booking_accept_btn,booking_decline_btn;
    TextView student_name_tv, booking_date_tv,booking_time_tv,category_tv,mobile_tv,email_tv, status_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        student_name_tv = findViewById(R.id.student_name_tv);
        booking_date_tv = findViewById(R.id.booking_date_tv);
        booking_accept_btn = findViewById(R.id.booking_accept_btn);
        booking_decline_btn = findViewById(R.id.booking_decline_btn);
        booking_time_tv = findViewById(R.id.booking_time_tv);
        category_tv = findViewById(R.id.category_name_tv);
        mobile_tv = findViewById(R.id.mobile_number_tv);
        email_tv = findViewById(R.id.student_email_tv);
        status_tv = findViewById(R.id.status_tv);
        back_btn = findViewById(R.id.btn_back);

        onBackClicks();

        if (Shared.getInstance().selectedBooking.getStatus() != null){
            if (!Shared.getInstance().selectedBooking.getStatus().equals("Pending")){
                booking_accept_btn.setVisibility(View.GONE);
                booking_decline_btn.setVisibility(View.GONE);
            }
            status_tv.setText(Shared.getInstance().selectedBooking.getStatus());
        }

        database = FirebaseDatabase.getInstance().getReference("Users");
        database.child(Shared.getInstance().selectedBooking.userId).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                           user = snapshot.getValue(User.class);
                           setView();
                        }else {

                            Toast.makeText(BookingDetailsActivity.this, "Some Error Occured",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void setView(){
        student_name_tv.setText(user.getName());
        booking_date_tv.setText(Shared.getInstance().selectedBooking.getDate());
        booking_time_tv.setText(Shared.getInstance().selectedBooking.getTime());
        category_tv.setText(Shared.getInstance().selectedBooking.getCategory());
        email_tv.setText(user.getEmail());
        mobile_tv.setText(user.getMobile());


    }


    private void onBackClicks() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

        booking_accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Booking booking = new Booking(Shared.getInstance().selectedBooking.getUserId(),
                        Shared.getInstance().selectedBooking.getInstructorId(), Shared.getInstance().selectedBooking.getDate(),
                        Shared.getInstance().selectedBooking.getTime(),Shared.getInstance().selectedBooking.getCategory(), Shared.getInstance().selectedBooking.getId(),
                        "Accepted");
                FirebaseDatabase.getInstance().getReference("Booking")
                        .child(Shared.getInstance().selectedBooking.getId())
                        .setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            Intent intent = new Intent(BookingDetailsActivity.this, SplashActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            ActivityCompat.finishAffinity(BookingDetailsActivity.this);

                            Toast.makeText(BookingDetailsActivity.this, "Accepted Successfully", Toast.LENGTH_SHORT).show();

                        }else {

                            Toast.makeText(BookingDetailsActivity.this, "Accepted not success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        booking_decline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Booking booking = new Booking(Shared.getInstance().selectedBooking.getUserId(),
                        Shared.getInstance().selectedBooking.getInstructorId(), Shared.getInstance().selectedBooking.getDate(),
                        Shared.getInstance().selectedBooking.getTime(),Shared.getInstance().selectedBooking.getCategory(), Shared.getInstance().selectedBooking.getId(),
                        "Decline");
                FirebaseDatabase.getInstance().getReference("Booking")
                        .child(Shared.getInstance().selectedBooking.getId())
                        .setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            Intent intent = new Intent(BookingDetailsActivity.this, SplashActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            ActivityCompat.finishAffinity(BookingDetailsActivity.this);

                            Toast.makeText(BookingDetailsActivity.this, "Declined Successfully", Toast.LENGTH_SHORT).show();

                        }else {

                            Toast.makeText(BookingDetailsActivity.this, "Declined not success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}