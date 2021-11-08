package com.example.quizap;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Score extends AppCompatActivity {

    TextView score,quizNameScore;
    FirebaseAuth fauth = FirebaseAuth.getInstance();
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    FirebaseUser user = fauth.getCurrentUser();
    int countScore = 0;
    String optionSelected=" ";
    String answerKey=" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        String quizName = getIntent().getStringExtra("quizName");
        quizNameScore = findViewById(R.id.quizNameScore);
        score = findViewById(R.id.scoreText);
        quizNameScore.setText(quizName);
        CollectionReference cf = fstore.collection("Answers").document(quizName).collection(user.getEmail());
        cf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    answerKey = d.getString("answerkey");
                    optionSelected = d.getString("optionSelected");
                    Log.d("mn",answerKey);
                    Log.d("mn",optionSelected);
                    if(optionSelected.equals(answerKey)){
                        countScore++;
                    }
                }
                Log.d("mn",String.valueOf(countScore));
                score.setText(String.valueOf(countScore));
            }
        });

    }
}