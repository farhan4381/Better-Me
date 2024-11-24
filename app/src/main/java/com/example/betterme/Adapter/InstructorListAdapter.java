package com.example.betterme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.betterme.Interface.RecyclerViewOnItemClickListener;
import com.example.betterme.Models.Instructor;
import com.example.betterme.R;
import com.example.betterme.Models.User;
import com.example.betterme.Shared.Shared;

import java.util.ArrayList;

public class InstructorListAdapter extends RecyclerView.Adapter<InstructorListAdapter.ViewHolder>{

    Context context;
    ArrayList<Instructor> list;
    RecyclerViewOnItemClickListener onItemClickListener;

    public InstructorListAdapter(Context context, ArrayList<Instructor> list, RecyclerViewOnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instructors_list_item, parent,false);
        InstructorListAdapter.ViewHolder holder = new InstructorListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
         Instructor instructor = list.get(position);
         holder.name.setText(instructor.getName());
         holder.age.setText("Age: "+instructor.getAge());
         holder.experties.setText("Expertise: " + instructor.getExperties());


        if (instructor.getImage() != null){
            Glide.with(context).load(instructor.getImage()).into(holder.image);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,experties,age;
        ImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.instructorname_tv);
            experties = itemView.findViewById(R.id.experties_tv);
            age = itemView.findViewById(R.id.age_tv);
            image = itemView.findViewById(R.id.profile_pic);

        }
    }
}
