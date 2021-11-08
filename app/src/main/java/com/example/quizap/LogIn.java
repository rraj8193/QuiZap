package com.example.quizap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogIn extends AppCompatActivity {
    EditText collegeid,email,password;
    Button login;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        collegeid = findViewById(R.id.collegeid);
        email = findViewById(R.id.emailid);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collegeid.getText().toString().isEmpty()){
                    collegeid.setError("Please Enter College Id");
                    return;
                }
                if(email.getText().toString().isEmpty()){
                    email.setError("Please Enter Email Id");
                    return;
                }
                if(password.getText().toString().isEmpty()){
                    password.setError("Please Enter Password");
                    return;
                }
                fAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = fAuth.getCurrentUser();
                        DocumentReference df = fstore.collection("Users").document(user.getUid());
                        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.getString("CollegeID").equals(collegeid.getText().toString())){
                                    if(documentSnapshot.getString("isAdmin")!=null){
                                        startActivity(new Intent(getApplicationContext(),Faculty.class));
                                        finish();
                                    }
                                    else if (documentSnapshot.getString("isUser")!=null){
                                        startActivity(new Intent(getApplicationContext(),Student.class));
                                        finish();
                                    }
                                }
                                else {
                                    Toast.makeText(LogIn.this, "College Id not found", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    return;
                                }
                            }
                        });
                    }
                });
            }
        });
    }
    public void Signin(View view){
        startActivity(new Intent(getApplicationContext(),RegistrationForm.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() !=null){
            FirebaseUser user = fAuth.getCurrentUser();
            DocumentReference df = fstore.collection("Users").document(user.getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.getString("isAdmin")!=null){
                        startActivity(new Intent(getApplicationContext(),Faculty.class));
                        finish();
                    }
                    else if (documentSnapshot.getString("isUser")!=null){
                        startActivity(new Intent(getApplicationContext(),Student.class));
                        finish();
                    }
                }
            });
        }
    }
}