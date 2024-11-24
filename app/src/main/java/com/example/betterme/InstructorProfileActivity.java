package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.betterme.Models.Instructor;
import com.example.betterme.Shared.Shared;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InstructorProfileActivity extends AppCompatActivity {

    Button back_btn,changepassword_btn,editprofile_btn;
    ImageView profile_img;
    DatabaseReference database;
    Instructor instructor;
    TextView tv_user_name;
    TextView tv_user_email;
    TextView tv_user_number;
    TextView tv_user_category;
    TextView tv_user_age;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_profile);

        back_btn= findViewById(R.id.btn_back);
        changepassword_btn= findViewById(R.id.btn_change_pass);
        editprofile_btn= findViewById(R.id.editprofile_btn);
        profile_img= findViewById(R.id.profile_img);
        tv_user_name= findViewById(R.id.tv_user_name);
        tv_user_email= findViewById(R.id.tv_user_email);
        tv_user_number= findViewById(R.id.tv_user_mobile);
        tv_user_category= findViewById(R.id.tv_experties);
        tv_user_age= findViewById(R.id.age);
        database = FirebaseDatabase.getInstance().getReference("Instructors");


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Instructor in = dataSnapshot.getValue(Instructor.class);

                    if (in.id.equals( FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        instructor = in;
                        updateView();
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        onbackClick();
        onchangepasswordclick();
    }

    private void updateView(){
        if (instructor.getImage() != null){
            Glide.with(this).load(instructor.getImage()).into(profile_img);
        }

        if (instructor.getName() != null){
            tv_user_name.setText(instructor.getName());
        }

        if (instructor.getEmail() != null){
            tv_user_email.setText(instructor.getEmail());
        }

        if (instructor.getMobile() != null){
            tv_user_number.setText(instructor.getMobile());
        }

        if (instructor.getExperties() != null){
            tv_user_category.setText(instructor.getExperties());
        }

        if (instructor.getAge() != null){
            tv_user_age.setText(instructor.getAge());
        }




    }

    private void onchangepasswordclick (){
         changepassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InstructorProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

         editprofile_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Shared.getInstance().myInstructor = instructor;
                 Intent intent = new Intent(InstructorProfileActivity.this, InstructorEditProfileActivity.class);
                 startActivity(intent);
             }
         });
    }

    private void onbackClick(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



}