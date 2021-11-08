package com.example.quizap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StudentInstruction extends AppCompatActivity {

    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    TextView dateData, timeData, termData, durationData;
    CheckBox termsCheckBox;
    Button startTest;
    int start = 0;

    String date, today, currentTime, time, duration;
    Calendar calendar;
    SimpleDateFormat dateFormat, timeFormat;
    String[] timeArray;
    int durationHour,durationMinute;
    int endTimeHour,endTimeMinute;
    String endTime;



    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_instruction);
        String quizName = getIntent().getStringExtra("keyQuizName");
        Log.d("Tag", "name " + quizName);

        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();

        dateData = findViewById(R.id.dateData);
        timeData = findViewById(R.id.timeData);
        termData = findViewById(R.id.termData);
        durationData = findViewById(R.id.durationData);
        termsCheckBox = findViewById(R.id.termscheckBox);
        startTest = findViewById(R.id.startTest);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        today = dateFormat.format(calendar.getTime());
        timeFormat = new SimpleDateFormat("HH:mm");
        currentTime = timeFormat.format(calendar.getTime());
        Log.d("mn", currentTime);
        Log.d("mn", today);

        startTest.setVisibility(View.INVISIBLE);

        DocumentReference qd = fstore.collection("Quizes").document(String.valueOf(quizName));
        qd.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                termData.setText(documentSnapshot.getString("quizDescription"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(StudentInstruction.this, "No Description Found", Toast.LENGTH_SHORT).show();
            }
        });

        DocumentReference df = fstore.collection("Quizes").document(String.valueOf(quizName)).collection("Info").document("Date");
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                date = documentSnapshot.getString("date");
                dateData.setText(documentSnapshot.getString("date"));
            }
        });
        DocumentReference tf = fstore.collection("Quizes").document(String.valueOf(quizName)).collection("Info").document("Time");
        tf.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                time = documentSnapshot.getString("time");
                timeData.setText(documentSnapshot.getString("time"));
                timeArray = time.split(":");
                Log.d("mn",timeArray[0]+" "+timeArray[1]);
            }
        });
        DocumentReference duf = fstore.collection("Quizes").document(String.valueOf(quizName)).collection("Info").document("Duration");
        duf.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                duration = documentSnapshot.getString("duration");
                durationData.setText(documentSnapshot.getString("duration"));
                duration = duration.replaceAll("[^\\d]", " ");
                duration = duration.trim();
                duration = duration.replaceAll(" +", " ");
                Log.d("mn", duration);
            }
        });
        termsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() && today.equals(date) && matchTime()) {
                    startTest.setVisibility(View.VISIBLE);
                    start = 1;
                } else {
                    startTest.setVisibility(View.INVISIBLE);
                }
            }
        });
        Log.d("mn", today.equals(date) + "");
        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentInstruction.this, MainActivity.class);
                intent.putExtra("quizName", quizName);
                intent.putExtra("duration", duration);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean matchTime() {
        String stringEndTimeHour;
        if (currentTime.equals(time)) {
            return true;
        }
        durationHour = (Integer.parseInt(duration)/60);
        durationMinute = (Integer.parseInt(duration)%60);
        endTimeHour = Integer.parseInt(timeArray[0]) + durationHour;
        endTimeMinute = Integer.parseInt(timeArray[1]) + durationMinute;
        if(endTimeHour<10){
            stringEndTimeHour = "0"+String.valueOf(endTimeHour);
        }
        else{
            stringEndTimeHour = String.valueOf(endTimeHour);
        }
        endTime = stringEndTimeHour +":"+String.valueOf(endTimeMinute);
        Log.d("mn",endTime);
        if ((currentTime.compareTo(time) > 0) && (currentTime.compareTo(endTime) < 0)){
            return true;
        }
        return false;
    }
}