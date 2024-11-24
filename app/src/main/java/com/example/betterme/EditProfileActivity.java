package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.betterme.Models.Instructor;
import com.example.betterme.Models.User;
import com.example.betterme.Shared.Shared;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    Button back_btn;
    Button edit_btn;

    EditText change_name_et,change_phone_number_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        back_btn = findViewById(R.id.btn_back);
        edit_btn = findViewById(R.id.confirm_profile_btn);
        change_name_et = findViewById(R.id.change_name_et);
        change_phone_number_et = findViewById(R.id.change_phone_number_et);

        change_phone_number_et.setText(Shared.getInstance().myUser.getMobile());
        change_name_et.setText(Shared.getInstance().myUser.getName());

        onBackbtnclicks();
        oneditbtnclicks();

    }
    public void onBackbtnclicks(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void oneditbtnclicks(){
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!change_name_et.getText().toString().isEmpty() && !change_phone_number_et.getText().toString().isEmpty() ){

                    editProfile();
                }else{
                    Toast.makeText(getApplicationContext(), "Please fill all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void editProfile(){

        User user = new User(change_name_et.getText().toString(),change_phone_number_et.getText().toString(),
                Shared.getInstance().myUser.getEmail(),Shared.getInstance().myUser.getPassword(),0,
                Shared.getInstance().myUser.getId());

        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                    editor.putInt("type", 0);
                    editor.apply();

                    Intent intent = new Intent(EditProfileActivity.this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    ActivityCompat.finishAffinity(EditProfileActivity.this);

                    Toast.makeText(EditProfileActivity.this, "Edit Successfully", Toast.LENGTH_SHORT).show();

                }else {

                    Toast.makeText(EditProfileActivity.this, "Edit not success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
