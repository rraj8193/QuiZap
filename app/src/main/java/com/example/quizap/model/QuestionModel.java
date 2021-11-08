package com.example.quizap.model;

public class QuestionModel {
    String questionText,optionOne,optionTwo,optionThree,optionFour,answerText;
    String image;
    String questionNo;

    public QuestionModel(String questionText, String optionOne, String optionTwo, String optionThree, String optionFour, String answerText, String image, int isViewAdded,String questionNo) {
        this.questionText = questionText;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;
        this.answerText = answerText;
        this.image = image;
        this.isViewAdded = isViewAdded;
        this.questionNo = questionNo;
    }

    int isViewAdded; // added = 1 , deleted = 0

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOptionOne() {
        return optionOne;
    }

    public void setOptionOne(String optionOne) {
        this.optionOne = optionOne;
    }

    public String getOptionTwo() {
        return optionTwo;
    }

    public void setOptionTwo(String optionTwo) {
        this.optionTwo = optionTwo;
    }

    public String getOptionThree() {
        return optionThree;
    }

    public void setOptionThree(String optionThree) {
        this.optionThree = optionThree;
    }

    public String getOptionFour() {
        return optionFour;
    }

    public void setOptionFour(String optionFour) {
        this.optionFour = optionFour;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIsViewAdded() {
        return isViewAdded;
    }

    public void setIsViewAdded(int isViewAdded) {
        this.isViewAdded = isViewAdded;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }
}