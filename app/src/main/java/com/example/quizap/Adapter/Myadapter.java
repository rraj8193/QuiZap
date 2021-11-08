package com.example.quizap.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizap.R;
import com.example.quizap.model.QuestionDisplayModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class Myadapter extends FirestoreRecyclerAdapter<QuestionDisplayModel, Myadapter.MyViewHolder> {

    public Myadapter(@NonNull @NotNull FirestoreRecyclerOptions<QuestionDisplayModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull Myadapter.MyViewHolder holder, int position, @NonNull @NotNull QuestionDisplayModel model) {
        holder.questionNo.setText(model.getQuestionNo());
        holder.questionText.setText(model.getQuestionText());
        holder.optionOne.setText(model.getOptionOne());
        holder.optionTwo.setText(model.getOptionTwo());
        holder.optionThree.setText(model.getOptionThree());
        holder.optionFour.setText(model.getOptionFour());
        Log.d("mnn",model.getImage());
        if(!model.getImage().toString().isEmpty()){
            holder.image.setVisibility(View.VISIBLE);
            Picasso.with(holder.image.getContext()).load(model.getImage()).into(holder.image);
        }
        else{
            holder.image.setVisibility(View.GONE);
        }
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_attempt_layout,parent,false);
        return new MyViewHolder(v);
    }

    public void deleteQuestion(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView questionNo,questionText,optionOne,optionTwo,optionThree,optionFour;
        Button optionOneBtn,optionTwoBtn,optionThreeBtn,optionFourBtn;
        ImageView image;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            questionNo = itemView.findViewById(R.id.questionNo);
            questionText = itemView.findViewById(R.id.questionText);
            optionOne = itemView.findViewById(R.id.optionOne);
            optionTwo = itemView.findViewById(R.id.optionTwo);
            optionThree = itemView.findViewById(R.id.optionThree);
            optionFour = itemView.findViewById(R.id.optionFour);
            optionOneBtn = itemView.findViewById(R.id.option1Btn);
            optionTwoBtn = itemView.findViewById(R.id.option2Btn);
            optionThreeBtn = itemView.findViewById(R.id.option3Btn);
            optionFourBtn = itemView.findViewById(R.id.option4Btn);
            image = itemView.findViewById(R.id.questionImage);
        }
    }
}
