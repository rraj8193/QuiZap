package com.example.quizap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationForm extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText password, confpassword;
    EditText collegeid, userid;
    CheckBox facultybox, studentbox;
    Button registerbtn;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    boolean valid = true;
    int accountType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        name = findViewById(R.id.c_namefield);
        email = findViewById(R.id.c_emailfield);
        password = findViewById(R.id.c_passwordfield);
        confpassword = findViewById((R.id.c_confpasswordfield));
        collegeid = findViewById(R.id.collegeidfield);
        facultybox = findViewById(R.id.facultyaccessbtn);
        studentbox = findViewById(R.id.studentaccessbtn);
        userid = findViewById((R.id.userid));

        registerbtn = findViewById(R.id.register);

        facultybox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundbutton, boolean isChecked) {
                if (compoundbutton.isChecked()) {
                    studentbox.setChecked(false);
                }
            }
        });
        studentbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundbutton, boolean isChecked) {
                if (compoundbutton.isChecked()) {
                    facultybox.setChecked(false);
                }
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(name);
                checkField(email);
                checkField(password);
                checkField(confpassword);
                checkField(collegeid);
                checkField(userid);
                if (!(facultybox.isChecked() || studentbox.isChecked())) {
                    Toast.makeText(RegistrationForm.this, "Select the account type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (valid && (password.getText().toString().equals(confpassword.getText().toString()))) {
                    fauth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(RegistrationForm.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = fauth.getCurrentUser();
                                DocumentReference df = fstore.collection("Users").document(user.getUid());
                                Map<String, Object> userinfo = new HashMap<>();
                                userinfo.put("FullName", name.getText().toString());
                                userinfo.put("Email", email.getText().toString());
                                userinfo.put("CollegeID", collegeid.getText().toString());
                                userinfo.put("UserId", userid.getText().toString());
                                if (facultybox.isChecked()) {
                                    userinfo.put("isAdmin", "1");
                                } else if (studentbox.isChecked()) {
                                    userinfo.put("isUser", "0");
                                }
                                df.set(userinfo);
                            if (facultybox.isChecked()) {
                               startActivity(new Intent(getApplicationContext(), Faculty.class));
                            } else if (studentbox.isChecked()) {
                                startActivity(new Intent(getApplicationContext(), Student.class));                            }
                            finish();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrationForm.this, "Error"+ e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(RegistrationForm.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void viewLoginPage(View view) {
        Intent login = new Intent(this, LogIn.class);
        startActivity(login);
        finish();
    }

    public boolean checkField(EditText textfield) {
        if (textfield.getText().toString().isEmpty()) {
            textfield.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }
}
