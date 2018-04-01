package com.example.android.qtt;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import com.example.android.qtt.Question.*;

public class MainActivity extends AppCompatActivity {

    LinearLayout checkboxLayout;

    private TextView scoreView, questionCountDown, countdown;
    private TextView question;
    private RadioGroup rbGroup;
    private TextView cbGroup; // checkbox layout
    private RadioButton rb1, rb2, rb3;
    private String selectedAnswer = "";
    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    private EditText typeAnswer;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;

    //init array list - Question obj has to use same name as array!
    private List<Question> questionList;
    private int questionCounter; // q shown
    private int getQuestionCounter; // ttl q in array
    private Question currentQuestion; // displayed q

    private int score;
    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreView = findViewById(R.id.score_view);
        questionCountDown = findViewById(R.id.question_count_view);
        countdown = findViewById(R.id.countdown);
        question = findViewById(R.id.question);
        rbGroup = findViewById(R.id.radio_group);
        //cbGroup = findViewById(R.id.checkbox_layout);
        rb1 = findViewById(R.id.button_1);
        rb2 = findViewById(R.id.button_2);
        rb3 = findViewById(R.id.button_3);
        cb1 = findViewById(R.id.checkbox_1);
        cb2 = findViewById(R.id.checkbox_2);
        cb3 = findViewById(R.id.checkbox_3);
        typeAnswer = findViewById(R.id.song_text);
        buttonConfirmNext = findViewById(R.id.button_continue);

        textColorDefaultRb = rb1.getTextColors(); // get default color


        // init dbHelper
        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();
        getQuestionCounter = questionList.size(); // get count ttl
        Collections.shuffle(questionList);

        // hide views
        hideViews();

        // get Qs
        showNextQuestion();

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered){
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() ||
                            cb1.isChecked() || cb2.isChecked() || cb3.isChecked()
                            //big question, how to verify if text contains "in rainbows"
                    ){
                        checkAnswer();
                    } else {
                        Toast.makeText(MainActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });

        // prevent keyboard from covering UI when quiz is launched
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void hideViews(){
        rbGroup.setVisibility(View.INVISIBLE);
        cb1.setVisibility(View.INVISIBLE);
        cb2.setVisibility(View.INVISIBLE);
        cb3.setVisibility(View.INVISIBLE);
        typeAnswer.setVisibility(View.INVISIBLE);
    }

    // display RadioGroup
    private void showRadioGroup(){
        rbGroup.setVisibility(View.VISIBLE);
    }

    // display checkboxes
    private void showCheckboxes(){
        cb1.setVisibility(View.VISIBLE);
        cb2.setVisibility(View.VISIBLE);
        cb3.setVisibility(View.VISIBLE);
    }

    // display EditText
    private void showTypeAnswer(){
        typeAnswer.setVisibility(View.VISIBLE);
    }

    private void showNextQuestion(){
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();
        cb1.setTextColor(textColorDefaultRb);
        cb2.setTextColor(textColorDefaultRb);
        cb3.setTextColor(textColorDefaultRb);
        typeAnswer.setTextColor(textColorDefaultRb);

        if (questionCounter < getQuestionCounter){
            currentQuestion = questionList.get(questionCounter);

            question.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            cb1.setText(currentQuestion.getOption1());
            cb2.setText(currentQuestion.getOption2());
            cb3.setText(currentQuestion.getOption3());
            typeAnswer(currentQuestion.extractEditText().toString()); // doesn't like this
            String answerValue = typeAnswer.getText().toString();

            //switch question formats
            // the idea is to bring everything from my Array,
            //     can't define questionType as int here: incompatible type - confused, this is the name of my array
            // can't find getAllQuestions - if I use that
            // can't find RADIO  if I use it with questionList

            int type = (int)

            question.setText(currentQuestion.getQuestion()).questionList.get.questionType;
            switch (type) {
                // seems to like required int...
                case QuestionType.RADIO:
                    showRadioGroup();
                    break;
                case QuestionType.CHECKBOX:
                    showCheckboxes();
                    break;
                case QuestionType.TEXTENTRY:
                    showTypeAnswer();
                    break;
            }
            questionCounter++;
            questionCountDown.setText("Question: " + questionCounter + "/" + getQuestionCounter);
            answered = false;
            buttonConfirmNext.setText("Confirm");
        } else {
            hideViews();
        } if (questionCounter == 3){
            finishQuiz();
        }
    }

    // calculate score
    private void checkAnswer( ){

        // editText + checkboxes
        answered = true;

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        cb1 = findViewById(R.id.checkbox_1);
        cb2 = findViewById(R.id.checkbox_2);
        cb3 = findViewById(R.id.checkbox_3);
        typeAnswer = findViewById(R.id.song_text);

        int answerNumber = rbGroup.indexOfChild(rbSelected) +1;

        if (answerNumber == currentQuestion.getAnswerNumber()){
            score++;
            scoreView.setText("Score: " + score);
        }

        showSolution();
    }

    // compare answers to valid answer
    private void showSolution(){
        rb1.setTextColor(Color.GRAY);
        rb2.setTextColor(Color.GRAY);
        rb3.setTextColor(Color.GRAY);
        cb1.setTextColor(Color.GRAY);
        cb2.setTextColor(Color.GRAY);
        cb3.setTextColor(Color.GRAY);

        typeAnswer.setText("in rainbows");

        // not sure how to cast this to String
        String answerNumber = questionList.get(questionCounter).toString();

        int type = (int) questionList.get(questionCounter).getType();

    if ( type == Question.TEXTENTRY) {
        EditText typeAnswer = findViewById(R.id.song_text);
        String typedAnswer = typeAnswer.getText().toString();

        selectedAnswer = typedAnswer.toLowerCase();
        checkAnswer(typeAnswer); // (answerNumber.toString()); //unhappy because this is an int :(
        typeAnswer.setText("");

        //Checkboxes
    } else if ( type == Question.CHECKBOX) {
        CheckBox cb1 = findViewById(R.id.checkbox_1);
        CheckBox cb2 = findViewById(R.id.checkbox_2);
        CheckBox cb3 = findViewById(R.id.checkbox_3);

        if (cb1.isChecked()){
            cb1.setChecked(false);
        }

        if (cb2.isChecked()){
            cb2.setChecked(false);
        }

        if (cb3.isChecked()){
            cb3.setChecked(false);
        }

        selectedAnswer = " ";
        checkAnswer();

    } else if (type == Question.RADIO){

        // thinking that here I should add an if statement
        // if radiogroup then use this switch statement, else use the next one for cb1, cb2, cb3
        switch (currentQuestion.getAnswerNumber()){
            case 1:
                rb1.setTextColor(Color.WHITE);
                question.setText("Answer a) is correct");
                break;
            case 2:
                rb2.setTextColor(Color.WHITE);
                question.setText("Answer b) is correct");
                break;
            case 3:
                rb3.setTextColor(Color.WHITE);
                question.setText("Answer c) is correct");
                break;
        }
    }

        // or should the change in question type be handled here?
        //if (questionCounter < getQuestionCounter && type == CHECKBOX || TEXTENTRY)
        if (questionCounter < getQuestionCounter ){
            // switch here?
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }



    private void finishQuiz(){
        finish();
    }
}
