package com.example.android.qtt;

/**
 * Created by moana on 3/31/2018.
 * Inspired by previous experience implementing a web survey at my last job I wanted to use something similar to SQL.
 * During my research I found the Coding In Flow Tutorials by Florian Walther.
 * see https://codinginflow.com/tutorials/android/quiz-app-with-sqlite
 */

/**
 * to avoid referencing another table enums where implemented.
 */
enum QuestionType
{RADIO,CHECKBOX, TEXTENTRY}

/**
 * thanks to my friend Leslie D. who corrected the way I was setting my enum obj.
 */
public class Question  {
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

    /**
     * @return getters and setters
     */

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