package com.example.quizap.model;

public class QuestionDisplayModel {
    String questionNo,questionText,optionOne,optionTwo,optionThree,optionFour;
    String image;
    String answerText;

    public QuestionDisplayModel(String questionNo, String questionText, String optionOne, String optionTwo, String optionThree, String optionFour, String image,String answerText) {
        this.questionNo = questionNo;
        this.questionText = questionText;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;
        this.image = image;
        this.answerText = answerText;
    }
    public QuestionDisplayModel(){}

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
