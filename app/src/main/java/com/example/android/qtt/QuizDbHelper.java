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
 * Created by moana on 3/31/2018.
 */

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final  String DATABASE_NAME = "MyQuizDB";
    private static final int DATABASE_VERSION = 1;

    //reference to db
    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        //define Db class source OR import class - see QuizContract import
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " (" +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWERNUMBER + " INTEGER, " +
                QuestionsTable.COLUMN_TYPE + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable(){
        Question q1 = new Question("1 is correct", "a", "b","c",1,0);
        addQuestion(q1);
        Question q2 = new Question("2 is correct", "a", "b","c",2,1);
        addQuestion(q2);
        // great question how to set this text entry as opposed to Q No. 3
        // solution would be to set this to String
        Question q3 = new Question("text entry is correct", null, null,null,3,2);
        addQuestion(q3);
    }

    private void addQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWERNUMBER, question.getAnswerNumber());
        cv.put(QuestionsTable.COLUMN_TYPE, question.getType());
        db.insert(QuestionsTable.TABLE_NAME,null, cv);
    }

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
                question.setType(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_TYPE)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}
