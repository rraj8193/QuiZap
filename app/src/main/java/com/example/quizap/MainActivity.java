package com.example.quizap;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizap.model.QuestionDisplayModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<QuestionDisplayModel> dataList;
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView textView,timer;
    Button submitTestBtn;
    String quizName,duration;
    Button nextQuestionButton,previousQuestionButton;
    CountDownTimer countDownTimer;
    boolean isTimerRunning;
    long dur;
    long timeLeftInMillis;

    TextView questionNo,questionText,optionOne,optionTwo,optionThree,optionFour;
    RadioButton optionOneBtn,optionTwoBtn,optionThreeBtn,optionFourBtn;
    ImageView image;
    int questionIndex = 0;
    String optionSelected = null,answerKey,alreadySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quizName = getIntent().getStringExtra("quizName");
        duration = getIntent().getStringExtra("duration");
        Log.d("Tag",quizName);
        dur = Long.parseLong(duration);
        dur = dur * 1000 * 60;
        timeLeftInMillis = dur;

        textView = findViewById(R.id.quizNameSet);
        timer = findViewById(R.id.timerId);
        textView.setText(quizName);
        nextQuestionButton = findViewById(R.id.nextQuestionButton);
        previousQuestionButton = findViewById(R.id.previousQuestionButton);
        submitTestBtn = findViewById(R.id.submitTestBtn);

        questionNo = findViewById(R.id.questionNo);
        questionText = findViewById(R.id.questionText);
        optionOne = findViewById(R.id.optionOne);
        optionTwo = findViewById(R.id.optionTwo);
        optionThree = findViewById(R.id.optionThree);
        optionFour = findViewById(R.id.optionFour);
        optionOneBtn = findViewById(R.id.option1Btn);
        optionTwoBtn = findViewById(R.id.option2Btn);
        optionThreeBtn = findViewById(R.id.option3Btn);
        optionFourBtn = findViewById(R.id.option4Btn);
        image = findViewById(R.id.questionImage);

        previousQuestionButton.setVisibility(View.GONE);

        dataList = new ArrayList<>();
        CollectionReference cf = fstore.collection("Quizes").document(quizName).collection("Questions");
        cf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d : list){
                    QuestionDisplayModel questionDisplayModel = d.toObject(QuestionDisplayModel.class);
                    dataList.add(questionDisplayModel);
                    Log.d("mn",dataList.get(0).getQuestionText().toString());
                }
                setQuestionDetails(questionIndex);
            }
        });
        startTimer();
        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { previousQuestionButton.setVisibility(View.VISIBLE);
               questionIndex++;
                if(questionIndex == dataList.size()-1){
                    nextQuestionButton.setVisibility(View.INVISIBLE);
                }
                else{
                    nextQuestionButton.setVisibility(View.VISIBLE);
                }
                previousQuestionButton.setVisibility(View.VISIBLE);
                setQuestionDetails(questionIndex);
            }
        });

        previousQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionIndex--;
                if(questionIndex == 0){
                    previousQuestionButton.setVisibility(View.INVISIBLE);
                }
                else{
                    previousQuestionButton.setVisibility(View.VISIBLE);
                }
                nextQuestionButton.setVisibility(View.VISIBLE);
                setQuestionDetails(questionIndex);
            }
        });

        submitTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countScore();
            }
        });
        updateCountDownText();
    }
    public void setQuestionDetails(int questionIndex){
        optionOneBtn.setChecked(false);
        optionTwoBtn.setChecked(false);
        optionThreeBtn.setChecked(false);
        optionFourBtn.setChecked(false);
        DocumentReference df = fstore.collection("Answers").document(quizName).collection(user.getEmail()).document(dataList.get(questionIndex).getQuestionText());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                alreadySelected = documentSnapshot.getString("optionSelected");
                if(alreadySelected == null){
                    optionOneBtn.setChecked(false);
                    optionTwoBtn.setChecked(false);
                    optionThreeBtn.setChecked(false);
                    optionFourBtn.setChecked(false);
                }
                if("a".equals(alreadySelected)){
                    optionOneBtn.setChecked(true);
                    optionTwoBtn.setChecked(false);
                    optionThreeBtn.setChecked(false);
                    optionFourBtn.setChecked(false);
                }
                if("b".equals(alreadySelected)){
                    optionOneBtn.setChecked(false);
                    optionTwoBtn.setChecked(true);
                    optionThreeBtn.setChecked(false);
                    optionFourBtn.setChecked(false);
                }
                if("c".equals(alreadySelected)){
                    optionOneBtn.setChecked(false);
                    optionTwoBtn.setChecked(false);
                    optionThreeBtn.setChecked(true);
                    optionFourBtn.setChecked(false);
                }
                if("d".equals(alreadySelected)){
                    optionOneBtn.setChecked(false);
                    optionTwoBtn.setChecked(false);
                    optionThreeBtn.setChecked(false);
                    optionFourBtn.setChecked(true);
                }

            }
        });
        Map<String,Object> answerDetail = new HashMap<>();
        questionNo.setText(dataList.get(questionIndex).getQuestionNo());
        questionText.setText(dataList.get(questionIndex).getQuestionText());
        optionOneBtn.setText(dataList.get(questionIndex).getOptionOne());
        optionTwoBtn.setText(dataList.get(questionIndex).getOptionTwo());
        optionThreeBtn.setText(dataList.get(questionIndex).getOptionThree());
        optionFourBtn.setText(dataList.get(questionIndex).getOptionFour());
        if(!dataList.get(questionIndex).getImage().isEmpty()){
            image.setVisibility(View.VISIBLE);
            Picasso.with(image.getContext()).load(dataList.get(questionIndex).getImage())
                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                    .into(image);
        }
        else{
            image.setVisibility(View.GONE);
        }

        answerKey = dataList.get(questionIndex).getAnswerText();
        Log.d("mn",answerKey);

        optionOneBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    optionTwoBtn.setChecked(false);
                    optionThreeBtn.setChecked(false);
                    optionFourBtn.setChecked(false);
                    optionSelected = "a";
                    answerDetail.put("questionText",dataList.get(questionIndex).getQuestionText());
                    answerDetail.put("optionSelected",optionSelected);
                    answerDetail.put("answerkey",answerKey);
                    df.set(answerDetail);

                }
            }
        });
        optionTwoBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    optionOneBtn.setChecked(false);
                    optionThreeBtn.setChecked(false);
                    optionFourBtn.setChecked(false);
                    optionSelected = "b";
                    answerDetail.put("questionText",dataList.get(questionIndex).getQuestionText());
                    answerDetail.put("optionSelected",optionSelected);
                    answerDetail.put("answerkey",answerKey);
                    df.set(answerDetail);
                }
            }
        });
        optionThreeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    optionTwoBtn.setChecked(false);
                    optionOneBtn.setChecked(false);
                    optionFourBtn.setChecked(false);
                    optionSelected = "c";
                    answerDetail.put("questionText",dataList.get(questionIndex).getQuestionText());
                    answerDetail.put("optionSelected",optionSelected);
                    answerDetail.put("answerkey",answerKey);
                    df.set(answerDetail);
                }
            }
        });
        optionFourBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    optionTwoBtn.setChecked(false);
                    optionThreeBtn.setChecked(false);
                    optionOneBtn.setChecked(false);
                    optionSelected = "d";
                    answerDetail.put("questionText",dataList.get(questionIndex).getQuestionText());
                    answerDetail.put("optionSelected",optionSelected);
                    answerDetail.put("answerkey",answerKey);
                    df.set(answerDetail);
                }
            }
        });


    }

    void countScore(){
        Intent intent = new Intent(this,Score.class);
        intent.putExtra("quizName",quizName);
        startActivity(intent);
        finish();
    }
    private void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                countScore();
            }
        }.start();
        isTimerRunning = true;
    }
    private void updateCountDownText(){
        int minutes = (int) (timeLeftInMillis / 1000) / 60 ;
        int second = (int) (timeLeftInMillis / 1000) % 60 ;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,second);
        timer.setText(timeLeftFormatted);
    }
}