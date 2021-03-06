package com.example.android.qtt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.qtt.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moana on 3/31/2018. Create db using SQL Helper to leverage APIs that will help with DB management.
 * This can be understood as a data handler
 */

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final  String DATABASE_NAME = "MyQuizDB";
    private static final int DATABASE_VERSION = 4;

    //reference to db
    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
        public void onCreate(SQLiteDatabase db) {
            this.db = db;

            final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                    QuestionsTable.TABLE_NAME + " (" +
                    QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                    QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                    QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                    QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                    QuestionsTable.COLUMN_ANSWERNUMBER + " INTEGER, " +
                    QuestionsTable.COLUMN_TYPE + " TEXT" + //not INTEGER!
                    ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        // the above line can potentially be replaced by a switch statement where DB versions will update as needed
        onCreate(db);
    }

    /**
     * questions, if a new question is added here we will need to update database version, a manual process for the time being
     */
    private void fillQuestionsTable(){
        Question q1 = new Question
                ("Song that was not part of the ''In Rainbows'' tour?",
                        "All I Need","Reckoner","House of Cards",2,
                        QuestionType.RADIO);
        addQuestion(q1);
        Question q2 = new Question
                ("Artwork designer for ''OK Computer''?",
                        "Stanley Donwood","Dale Atkinson","Galen Cheney",1, QuestionType.RADIO);
        addQuestion(q2);
        Question q3 = new Question
                ("James Bond movie that rejected a Radiohead song?",
                        "Skyfall","Spectre","Quantum of Solace",2, QuestionType.RADIO);
        addQuestion(q3);
        Question q4 = new Question
                ("Which album was released for a pay-what-you-want price?",
                        "In Rainbows", "Hail to the Thief", "OK Computer",1, QuestionType.TEXTENTRY);
        addQuestion(q4);
        Question q5 = new Question
                ("Select the first album released by Radiohead",
                        "OK Computer","The Bends","Pablo Honey",3, QuestionType.RADIO);
        addQuestion(q5);
        Question q6 = new Question
                ("Who in this list has covered Creep?",
                        "Damien Rice","Amy Winehouse","Moby",5, QuestionType.CHECKBOX);
        addQuestion(q6);
        Question q7 = new Question
                ("Which song is Radiohead''s most famous song?",
                        "House of Cards","Street Spirit","Creep",3,QuestionType.RADIO);
        addQuestion(q7);
    }

    /**
     *
     * @param question
     */
    private void addQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWERNUMBER, question.getAnswerNumber());
        //set and get enum as string:
        cv.put(QuestionsTable.COLUMN_TYPE, String.valueOf(question.getType()));
        db.insert(QuestionsTable.TABLE_NAME,null, cv);
    }

    /**
     * @return using a Cursor interface to iterate thru questions
     */
    public List<Question> getAllQuestions(){
        List<Question> questionList = new ArrayList <>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()){
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNumber(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWERNUMBER)));
                //set and get enum as string:
                question.setType(QuestionType.valueOf(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_TYPE))));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}
