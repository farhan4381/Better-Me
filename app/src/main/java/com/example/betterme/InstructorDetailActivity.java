package com.example.betterme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.betterme.Models.Booking;
import com.example.betterme.Shared.Shared;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InstructorDetailActivity extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    EditText dateEditText, timeEditText;
    Button backBtn,bookhim_btn;
    TextView name_tv, email_tv,experties_tv,mobile_tv, certificate_tv;
    ImageView instructor_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_detail);

        dateEditText= findViewById(R.id.et_date);
        timeEditText= findViewById(R.id.et_time);
        backBtn= findViewById(R.id.btn_back);
        bookhim_btn= findViewById(R.id.bookhim_btn);
        name_tv= findViewById(R.id.instructor_name);
        email_tv= findViewById(R.id.contact_email_tv);
        experties_tv= findViewById(R.id.instructor_experties);
        certificate_tv= findViewById(R.id.certificate_1_tv);
        mobile_tv = findViewById(R.id.contact_number_tv);
        instructor_image = findViewById(R.id.instructor_image);



        name_tv.setText(Shared.getInstance().selectedInstructor.name);
        email_tv.setText(Shared.getInstance().selectedInstructor.email);
        experties_tv.setText(Shared.getInstance().selectedInstructor.experties);
        mobile_tv.setText(Shared.getInstance().selectedInstructor.mobile);
        certificate_tv.setText(Shared.getInstance().selectedInstructor.certificate);


        if (Shared.getInstance().selectedInstructor.getImage() != null){
            Glide.with(this).load(Shared.getInstance().selectedInstructor.getImage()).into(instructor_image);
        }



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bookhim_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateEditText.getText().toString().equals("") || timeEditText.getText().toString().equals("")){
                    Toast.makeText(InstructorDetailActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }else {

                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();
                    Booking booking = new Booking(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            Shared.getInstance().selectedInstructor.id, dateEditText.getText().toString(),
                            timeEditText.getText().toString(),Shared.getInstance().selectedInstructor.experties, ts,"Pending");

                    FirebaseDatabase.getInstance().getReference("Booking")
                            .child(ts)
                            .setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){


                                Intent intent = new Intent(InstructorDetailActivity.this, SplashActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                ActivityCompat.finishAffinity(InstructorDetailActivity.this);

                                Toast.makeText(InstructorDetailActivity.this, "Booked Successfully", Toast.LENGTH_SHORT).show();

                            }else {

                                Toast.makeText(InstructorDetailActivity.this, "Booking not success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }

        };



        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(InstructorDetailActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, arg1);
                        myCalendar.set(Calendar.MONTH, arg2);
                        myCalendar.set(Calendar.DAY_OF_MONTH, arg3);
                        updateLabel();
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
//                new DatePickerDialog(InstructorDetailActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(InstructorDetailActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeEditText.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }



    private void updateLabel(){
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateEditText.setText(dateFormat.format(myCalendar.getTime()));

    }
}