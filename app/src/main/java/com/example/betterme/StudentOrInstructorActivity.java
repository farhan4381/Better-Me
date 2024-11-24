package com.example.betterme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StudentOrInstructorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_or_instructor);
    }

    public void onSigninClick(View view) {
        Intent intent = new Intent(StudentOrInstructorActivity.this,LoginActivity.class);
        intent.putExtra("type",0);
        startActivity(intent);

    }

    public void onCreateAccClick(View view) {
        Intent intent = new Intent(StudentOrInstructorActivity.this,LoginActivity.class);
        intent.putExtra("type",1);
        startActivity(intent);
    }
}