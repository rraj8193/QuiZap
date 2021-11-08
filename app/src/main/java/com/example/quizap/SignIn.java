package com.example.quizap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {
    Button collegesigninbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        collegesigninbutton = findViewById(R.id.collegesigninbtn);

        collegesigninbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollegeSignIn();
            }
        });
    }
    public void CollegeSignIn(){
        Intent collegesignin = new Intent (this, RegistrationForm.class);
        startActivity(collegesignin);
    }
}