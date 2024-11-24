package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.betterme.Models.Instructor;
import com.example.betterme.Models.User;
import com.example.betterme.Shared.Shared;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {


    Button changepassword_btn;
    Button back_btn;
    DatabaseReference database;
    User user;
    Button profile_edit_btn;
    TextView tv_user_name,tv_user_email,tv_user_mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        changepassword_btn = findViewById(R.id.btn_change_pass);
        back_btn = findViewById(R.id.btn_back);
        profile_edit_btn = findViewById(R.id.editprofile_btn);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_email = findViewById(R.id.tv_user_email);
        tv_user_mobile = findViewById(R.id.tv_user_mobile);
        database = FirebaseDatabase.getInstance().getReference("Users");

        profile_edit_btn.setVisibility(View.GONE);



        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    User us = dataSnapshot.getValue(User.class);

                    if (us.id.equals( FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        user = us;
                        updateView();

                        profile_edit_btn.setVisibility(View.VISIBLE);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        onchangepasswordClicks();
        onbackclicks();
        oneditprofileclicks();

    }

    private void onchangepasswordClicks (){
        changepassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }


    private void onbackclicks(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }


    private void oneditprofileclicks(){
        profile_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shared.getInstance().myUser =  user;
                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateView(){

        if (user.getName() != null){
            tv_user_name.setText(user.getName());
        }

        if (user.getEmail() != null){
            tv_user_email.setText(user.getEmail());
        }

        if (user.getMobile() != null){
            tv_user_mobile.setText(user.getMobile());
        }

    }

}
