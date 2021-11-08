package com.example.quizap.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizap.R;
import com.example.quizap.model.QuizListModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.jetbrains.annotations.NotNull;

public class QuizListAdapter extends FirestoreRecyclerAdapter<QuizListModel,QuizListAdapter.QuizListHolder> {


    public QuizListAdapter(@NonNull @NotNull FirestoreRecyclerOptions<QuizListModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull QuizListAdapter.QuizListHolder holder, int position, @NonNull @NotNull QuizListModel model) {
        holder.quizName.setText(model.getQuizName());

    }
    public void deleteQuestion(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    @NonNull
    @NotNull
    @Override
    public QuizListHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quizlist,parent,false);
        return new QuizListHolder(v);
    }

    class QuizListHolder extends RecyclerView.ViewHolder{
        TextView quizName,quizDate,quizTime;

        public QuizListHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.quizname);
        }
    }
}
