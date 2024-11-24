package com.example.betterme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.betterme.Adapter.CategoryAdapter;
import com.example.betterme.Interface.RecyclerViewOnItemClickListener;
import com.example.betterme.Shared.Shared;

import java.util.ArrayList;

public class EducationCategoryActivity extends AppCompatActivity implements RecyclerViewOnItemClickListener {


    RecyclerView recyclerView;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_category);

        recyclerView = findViewById(R.id.education_cat_rv);
        list.add("Maths");
        list.add("Programming");
        list.add("English");
        list.add("Stats");
        list.add("Physics");
        list.add("Primary Tuition");

        setAdapter();

    }

    private void setAdapter() {
        CategoryAdapter adapter = new CategoryAdapter(list,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void onBackClick(View view) {
        finish();
    }

    @Override
    public void onItemClickListener(int position) {
        Shared.getInstance().selectedCategory = list.get(position);
        Intent intent = new Intent(EducationCategoryActivity.this, InstructorsListActivity.class);
        startActivity(intent);
    }
}