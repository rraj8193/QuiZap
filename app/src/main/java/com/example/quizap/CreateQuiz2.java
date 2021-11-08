package com.example.quizap;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.quizap.model.QuestionModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class CreateQuiz2 extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText typeQuestion,option1,option2,option3,option4,questionNo;
    RadioButton option1Btn,option2Btn,option3Btn,option4Btn;

    Toolbar toolbar4;
    FirebaseDatabase database;
    FirebaseAuth fauth = FirebaseAuth.getInstance();
    DatabaseReference db;
    String quizName;
    boolean valid = true;
    ImageButton imageUploadButton;
    ImageView imageview;
    Uri mImageUri;
    UploadTask uploadTask;
    private StorageReference storageReference;
    String image = "";
    byte[] data;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz2);

        toolbar4 = findViewById(R.id.createQuizId1);
        setSupportActionBar(toolbar4);
        questionNo = findViewById(R.id.serialno);
        typeQuestion = findViewById(R.id.questiontext1);
        option1 = findViewById(R.id.option1field1);
        option2= findViewById(R.id.option2field1);
        option3 = findViewById(R.id.option3field1);
        option4 = findViewById(R.id.option4field1);
        option1Btn = findViewById(R.id.option1btn1);
        option2Btn = findViewById(R.id.option2btn1);
        option3Btn = findViewById(R.id.option3btn1);
        option4Btn = findViewById(R.id.option4btn1);

        quizName = getIntent().getStringExtra("quizName");
        Log.d("Tag","kk"+quizName);

        imageUploadButton = findViewById(R.id.imageUploadButton);
        imageview = findViewById(R.id.imageView);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        option1Btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    option2Btn.setChecked(false);
                    option3Btn.setChecked(false);
                    option4Btn.setChecked(false);
                }
            }
        });
        option2Btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    option1Btn.setChecked(false);
                    option3Btn.setChecked(false);
                    option4Btn.setChecked(false);
                }
            }
        });
        option3Btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    option2Btn.setChecked(false);
                    option1Btn.setChecked(false);
                    option4Btn.setChecked(false);
                }
            }
        });
        option4Btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    option2Btn.setChecked(false);
                    option3Btn.setChecked(false);
                    option1Btn.setChecked(false);
                }
            }
        });

        imageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }
    void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            mImageUri = data.getData();
            DocumentReference df = FirebaseFirestore.getInstance().collection("Quizes").document(quizName).collection("Questions").document();
            int k = 1;
            int isViewAdded = 1;
            if(mImageUri != null) {
                progressDialog = new ProgressDialog(this);
                StorageReference sr = storageReference.child(System.currentTimeMillis()+ "." + getFileExtension(mImageUri));
                sr.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(CreateQuiz2.this).load(uri).into(imageview);
                                Log.d("MNN",uri.toString());
                                image = uri.toString();
                                progressDialog.dismiss();
                            }
                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                        progressDialog.setMessage("Uploading Image");
                        progressDialog.show();
                    }
                });
            }
            else{
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_question_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.save){

            checkData();

            if(valid){
                saveQuestion();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    void checkData(){
        if(typeQuestion.getText().toString().isEmpty()){
            valid = false;
            typeQuestion.setError("Question Field Required");
            return;
        }
        if(option1.getText().toString().isEmpty()){
            valid = false;
            option1.setError("Option 1 Required");
            return;
        }
        if(option2.getText().toString().isEmpty()){
            valid = false;
            option2.setError("Option 1 Required");
            return;
        }
        if(option3.getText().toString().isEmpty()){
            valid = false;
            option3.setError("Option 1 Required");
            return;
        }
        if(option4.getText().toString().isEmpty()){
            valid = false;
            option4.setError("Option 1 Required");
            return;
        }
        if(!(option1Btn.isChecked() || option2Btn.isChecked() || option3Btn.isChecked() || option4Btn.isChecked())){
            Toast.makeText(this, "Please select correct answer", Toast.LENGTH_SHORT).show();
            valid = false;
            return;
        }
    }

    void saveQuestion(){
        String serial = questionNo.getText().toString();
        String question = typeQuestion.getText().toString();
        String optiona = option1.getText().toString();
        String optionb = option2.getText().toString();
        String optionc = option3.getText().toString();
        String optiond = option4.getText().toString();
        String answerKey = "";

        if(option1Btn.isChecked()){
            answerKey = "a";
        }
        if(option2Btn.isChecked()){
            answerKey = "b";
        }
        if(option3Btn.isChecked()){
            answerKey = "c";
        }
        if(option4Btn.isChecked()){
            answerKey = "d";
        }

        DocumentReference df = FirebaseFirestore.getInstance().collection("Quizes").document(quizName).collection("Questions").document();
        int isViewAdded = 1;
       QuestionModel questionModel = new QuestionModel(question, optiona, optionb, optionc, optiond, answerKey, image, isViewAdded,serial);
        df.set(questionModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateQuiz2.this, "Data Saved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(CreateQuiz2.this, "Failed to save data", Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }
}