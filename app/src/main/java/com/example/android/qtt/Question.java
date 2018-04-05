package com.example.android.qtt;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Types;

/**
 * Created by moana on 3/31/2018.
 */
enum QuestionType
{RADIO,CHECKBOX, TEXTENTRY}

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

    protected Question(Parcel in) {
        question = in.readString();
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
        answerNumber = in.readInt();
    }

 /*   @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(option1);
        dest.writeString(option2);
        dest.writeString(option3);
        dest.writeInt(answerNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }*/

/*    public static final Creator <Question> CREATOR = new Creator <Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };*/

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
//was not in Github - maybe
