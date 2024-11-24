package com.example.betterme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {


    Button profile_btn ;
    Button settings_btn;
    Button sports_btn;
    Button education_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profile_btn = findViewById(R.id.profile_btn);
        settings_btn = findViewById(R.id.settings_btn);
        sports_btn = findViewById(R.id.sports_btn);
        education_btn = findViewById(R.id.education_btn);


        onProfileClicks();
        onSettingsClicks();
        onsportsClicks();
        oneducationClicks();
    }

    private void onProfileClicks (){
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onSettingsClicks (){
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });


    }

    private void onsportsClicks(){
        sports_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SportsCategoryActivity.class);
                startActivity(intent);
            }
        });
    }


    private void oneducationClicks(){
        education_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EducationCategoryActivity.class);
                startActivity(intent);
            }
        });
    }
}