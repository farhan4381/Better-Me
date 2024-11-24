package com.example.betterme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    Button btn_back,booking_history_btn,btn_contact,share_btn;
    Button logout_btn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        btn_back = findViewById(R.id.btn_back);
        booking_history_btn = findViewById(R.id.booking_history_btn);
        btn_contact = findViewById(R.id.btn_contact);
        logout_btn = findViewById(R.id.logout_btn);
        share_btn = findViewById(R.id.share_btn);



        onBackClick();
        onContactUsClick();
        onlogoutclicks();
    }

    private void onBackClick(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "BetterMe App");
                share.putExtra(Intent.EXTRA_TEXT, "Hey, check out this app BetterMe");
                startActivity(Intent.createChooser(share, "Share link!"));

            }
        });

        booking_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, BookingHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onContactUsClick(){
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });
    }
    private void onlogoutclicks(){
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(SettingActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ActivityCompat.finishAffinity(SettingActivity.this);

            }
        });
    }
}