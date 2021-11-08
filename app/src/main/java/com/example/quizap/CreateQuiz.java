package com.example.quizap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizap.Adapter.Myadapter;
import com.example.quizap.model.QuestionDisplayModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class CreateQuiz<fstore> extends BaseActivity  {

    Toolbar toolbar3;
    String item,quizName,quizDescription;
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    FirebaseAuth fauth;
    FirebaseUser user;
    EditText question, option1, option2, option3, option4;
    TextView serial;
    RecyclerView recyclerView;
    Myadapter adapter;
    FloatingActionButton addQuestion;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        toolbar3 = findViewById(R.id.createQuizId);
        setSupportActionBar(toolbar3);

        recyclerView = findViewById(R.id.recycler);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        user = fauth.getCurrentUser();
        email = user.getEmail();
        Log.d("email", email);

        quizName = getIntent().getStringExtra("quizName");
        Log.d("Tag","quizName is "+ quizName);
        quizDescription = getIntent().getStringExtra("quizDescription");

        addQuestion = findViewById(R.id.addquestionFloatingButton);

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreateQuiz2.class);
                intent.putExtra("quizName",quizName);
                startActivity(intent);
            }
        });
        setupQuestionList();
    }

    void setupQuestionList() {
        CollectionReference cf = fstore.collection("Quizes").document(String.valueOf(quizName)).collection("Questions");
        Query query = cf;
        FirestoreRecyclerOptions<QuestionDisplayModel> options = new FirestoreRecyclerOptions.Builder<QuestionDisplayModel>().setQuery(cf,QuestionDisplayModel.class).build();
        adapter = new Myadapter(options);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteQuestion(viewHolder.getAbsoluteAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}