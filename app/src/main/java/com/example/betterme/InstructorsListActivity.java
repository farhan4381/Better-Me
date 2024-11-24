package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.betterme.Adapter.InstructorListAdapter;
import com.example.betterme.Interface.RecyclerViewOnItemClickListener;
import com.example.betterme.Models.Instructor;
import com.example.betterme.Models.User;
import com.example.betterme.Shared.Shared;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InstructorsListActivity extends AppCompatActivity implements RecyclerViewOnItemClickListener {

    RecyclerView recyclerView;
    DatabaseReference database;
    InstructorListAdapter instructorListAdapter;
    ArrayList<Instructor>list = new ArrayList<>();
    Button backBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructors_list);
        recyclerView = findViewById(R.id.instructor_list_rv);
        database = FirebaseDatabase.getInstance().getReference("Instructors");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setAdapter();

//        instructorListAdapter = new InstructorListAdapter(this,list);
//        recyclerView.setAdapter(instructorListAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Instructor instructor = dataSnapshot.getValue(Instructor.class);

                    if (Shared.getInstance().selectedCategory.equals(instructor.getExperties())){
                        list.add(instructor);
                    }
                }

                instructorListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        backBtn = findViewById(R.id.btn_back);
        onbackClicks();

        }

    private void onbackClicks() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setAdapter(){
        instructorListAdapter = new InstructorListAdapter(this,list,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(instructorListAdapter);
    }

    @Override
    public void onItemClickListener(int position) {
        Shared.getInstance().selectedInstructor = list.get(position);

        Intent intent = new Intent(InstructorsListActivity.this, InstructorDetailActivity.class);
        startActivity(intent);
    }

}