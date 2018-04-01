package com.example.android.qtt;

import java.sql.Types;

/**
 * Created by moana on 3/31/2018.
 */
enum QuestionType
{RADIO,CHECKBOX, TEXTENTRY

    // not sure if I need this to assign int values to enums
    // i wanted to used the indexation instead
/*
{RADIO(0), CHECKBOX(1), TEXTENTRY(2);

    private int type;

    QuestionType(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }*/
}

public class Question {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private int answerNumber;
    private QuestionType type;

    public Question(){}

    public Question(String question, String option1, String option2, String option3, int answerNumber,
                    QuestionType type) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answerNumber = answerNumber;
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }
}
