package com.example.android.qtt;

import android.provider.BaseColumns;


/**
 * Created by moana on 3/31/2018.
 * Based on https://developer.android.com/training/data-storage/sqlite.html#java
 * and tutorials from Coding in Flow - Florian Walther
 */

public final class QuizContract {

    private QuizContract (){}

    public static class QuestionsTable implements BaseColumns{
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_OPTION1 = "OPTION1";
        public static final String COLUMN_OPTION2 = "OPTION2";
        public static final String COLUMN_OPTION3 = "OPTION3";
        public static final String COLUMN_ANSWERNUMBER = "answer_number";
        public static final String COLUMN_TYPE = "type";
    }
}