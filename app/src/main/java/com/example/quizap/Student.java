package com.example.quizap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class Student extends AppCompatActivity {

    Toolbar toolbarTwo;

    EditText searchQuiz;
    Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        toolbarTwo = findViewById(R.id.studentToolBar);
        setSupportActionBar(toolbarTwo);

        searchQuiz = findViewById(R.id.searchQuiz);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchQuiz.getText().toString().isEmpty()){
                    searchQuiz.setError("Please Enter Quiz Name");
                    return;
                }
                else {
                    String quizName = searchQuiz.getText().toString();
                    Intent intent = new Intent(Student.this,StudentInstruction.class);
                    Log.d("Tag", "rrrrrrrrrrrr " +quizName);
                    intent.putExtra("keyQuizName",quizName);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LogIn.class));
                finish();
        }
        return true;
    }
}