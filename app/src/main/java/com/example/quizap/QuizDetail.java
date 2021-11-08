package com.example.quizap;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class QuizDetail extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    Spinner durationspinner;
    TextView displaytime, displaydate;
    EditText quizName,quizDescription;
    String name,item,description;
    FirebaseFirestore fstore;
    FirebaseAuth fauth;
    FirebaseUser user;
    Button next;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        user = fauth.getCurrentUser();
        email = user.getEmail();

        quizName = findViewById(R.id.quizName);
        quizDescription = findViewById(R.id.quizDescription);
        next = findViewById(R.id.nextButton);

        durationspinner = findViewById(R.id.duration_menu);
        String[] duration = getResources().getStringArray(R.array.duration);
        ArrayAdapter durationadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, duration);
        durationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationspinner.setAdapter(durationadapter);
        durationspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose Duration")){
                    item = "None";
                }
                else{
                    if(quizName.getText().toString().isEmpty()){
                        quizName.setError("Enter Quiz Name");
                        return;
                    }
                    item = parent.getItemAtPosition(position).toString();
                    DocumentReference df = fstore.collection("Quizes").document(quizName.getText().toString()).collection("Info").document("Duration");
                    Map<String,Object> durationInfo = new HashMap<>();
                    durationInfo.put("duration",item);
                    df.set(durationInfo);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        displaytime = findViewById(R.id.displaytime);
        displaydate = findViewById(R.id.displaydate);
        displaytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quizName.getText().toString().isEmpty()){
                    quizName.setError("Enter Quiz Name");
                    return;
                }
                DialogFragment timepicker = new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(), "time picker");
            }
        });


        displaydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quizName.getText().toString().isEmpty()){
                    quizName.setError("Enter Quiz Name");
                    return;
                }
                DialogFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quizName.getText().toString().isEmpty()){
                    quizName.setError("Enter Quiz Name");
                    return;
                }
                setQuizDetail();
            }
        });
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String min = "";
        if(minute < 10){
            min = "0"+String.valueOf(minute);
        }
        else{
            min = String.valueOf(minute);
        }
        displaytime.setText(hourOfDay + ":" + min);
        DocumentReference df = fstore.collection("Quizes").document(quizName.getText().toString()).collection("Info").document("Time");
        Map<String,Object> quesInfo = new HashMap<>();
        quesInfo.put("time",String.valueOf(hourOfDay) + ":" + min);
        df.set(quesInfo);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String mon = "";
        month = month + 1;
        if(month < 10) {
            mon = "0" + String.valueOf(month);
        }
        else {
            mon = String.valueOf(month);
        }
        displaydate.setText(dayOfMonth + "/" + mon + "/" + year);
        DocumentReference df = fstore.collection("Quizes").document(quizName.getText().toString()).collection("Info").document("Date");
        Map<String,Object> quesInfo = new HashMap<>();
        quesInfo.put("date",String.valueOf(dayOfMonth) + "/" + mon + "/" + String.valueOf(year));
        df.set(quesInfo);
    }
    public void setQuizDetail(){
        DocumentReference quizinforef = fstore.collection("Quizes").document(quizName.getText().toString());
        Map<String,Object> quizinfo = new HashMap<>();
        quizinfo.put("quizName",quizName.getText().toString());
        quizinfo.put("quizDescription",quizDescription.getText().toString());
        quizinfo.put("email",email);
        quizinforef.set(quizinfo);
        Intent intent = new Intent(getApplicationContext(),CreateQuiz.class);
        Log.d("TAG", "onCreate: "+quizName.getText().toString());
        intent.putExtra("quizName",quizName.getText().toString());
        intent.putExtra("quizDescription",quizDescription.getText().toString());
        startActivity(intent);
        finish();
    }
}