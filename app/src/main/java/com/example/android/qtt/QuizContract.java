package com.example.android.qtt;

import android.provider.BaseColumns;


/**
 * Created by moanableu on 3/31/2018.
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
