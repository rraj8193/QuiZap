package com.example.quizap.model;

public class QuizListModel {

    String quizName;
    String quizDate;
    String quizTime;

    public QuizListModel(String quizName, String share) {
        this.quizName = quizName;
        this.share = share;
    }
    public QuizListModel(){}

    String share;

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }
}
