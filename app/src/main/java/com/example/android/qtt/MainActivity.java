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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.android.qtt.Question.*;
import com.example.android.qtt.QuizDbHelper.*;

import static com.example.android.qtt.QuestionType.CHECKBOX;
import static com.example.android.qtt.QuestionType.RADIO;
import static com.example.android.qtt.QuestionType.TEXTENTRY;

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
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()){
                        checkAnswer();
                    }
                    // not quite sure how to see if this is unchecked or compare to available valid answers
                    if  (cb1.isChecked() && cb2.isChecked()) {
                        checkAnswer();
                        cb1.setChecked(true); // show correct answer
                        cb2.setChecked(true); // show correct answer
                    }
                    //big question, how to verify if text contains "in rainbows"
                    if (typeAnswer != null){
                        checkAnswer();
                    }

                    else {
                        Toast.makeText(MainActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });

        // prevent keyboard from covering UI when quiz is launched -- not sure I'll need this
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
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);

        typeAnswer.setTextColor(textColorDefaultRb);
        typeAnswer.getText().clear();

        if (questionCounter < getQuestionCounter){
            currentQuestion = questionList.get(questionCounter);

            question.setText(currentQuestion.getQuestion());

            // I should be able to call these here, I think
            // radio options
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());

            // checkbox options
            cb1.setText(currentQuestion.getOption1());
            cb2.setText(currentQuestion.getOption2());
            cb3.setText(currentQuestion.getOption3());

            // input text
            String answerValue = typeAnswer.getText().toString();

            // switch question formats
            // the idea is to bring everything from my Array: getAllQuestions
            // but then I receive: 'void cannot be dereferenced' confused as what should I be referencing to to get my type
            // Question.type can't be resolved
            // should I use: getClass(Question).questionType ?
            // **** Pseudo code*** :    while we have questions to show,
            //                          update views

            QuestionType type = (QuestionType)

            question.setText(currentQuestion.getQuestion().getAllQuestions.questionType);
            switch (type) {
                // incompatible types: required int
                // using RADIO because: 'qualified names of the enum values should not be used in case labels
                case RADIO:
                    showRadioGroup();
                    break;
                case CHECKBOX:
                    showCheckboxes();
                    break;
                case TEXTENTRY:
                    showTypeAnswer();
                    break;
            }
            questionCounter++;
            questionCountDown.setText("Question: " + questionCounter + "/" + getQuestionCounter);
            answered = false;
            buttonConfirmNext.setText("Confirm");
        } else {
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


        // this is a tricky one, how to verify that typeAnswer contains "in rainbows"???
        // or how to check that all possible checkboxes are selected
        // if I try using QuestionType: 'expression expected' unable to call Question.type 'cose type 'has private access in Question'
        if (answerNumber == currentQuestion.getAnswerNumber() && QuestionType == RADIO ||
                answerNumber == currentQuestion.getAnswerNumber() && QuestionType == CHECKBOX) {
            score++;
            scoreView.setText("Score: " + score);
        } if (typeAnswer.getText().toString().equals("in rainbows")) {
            score++;
            scoreView.setText("Score: " + score);
        }

        showSolution();
    }

    // compare answers to valid answer
    private void showSolution(){
        //ideally this dims u=invalid answers and blends them into background
        rb1.setTextColor(Color.GRAY);
        rb2.setTextColor(Color.GRAY);
        rb3.setTextColor(Color.GRAY);
        cb1.setTextColor(Color.GRAY);
        cb2.setTextColor(Color.GRAY);
        cb3.setTextColor(Color.GRAY);

        typeAnswer.setText("in rainbows");

        // Pseudo code get all Qs w/ correct answers - I could do w.out it at this point

        // error: package does not exist if I use 'getAllQuestions' - not sure why the compiler understands this as a package
        // then I tried using 'ArrayList' then i get this error: 'cannot find symbol variable get'
        // and 'ArrayList.type' can't resolve 'type'
        if (ArrayList.get.type == RADIO) {

            // thinking that here I should add an if statement
            // if radiogroup then use this switch statement, else use the next one for cb1, cb2, cb3
            switch (currentQuestion.getAnswerNumber()) {
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
        } if (getAllQuestions.get.questionType == CHECKBOX) {

            // thinking that here I should add an if statement
            // if radiogroup then use this switch statement, else use the next one for cb1, cb2, cb3
            switch (currentQuestion.getAnswerNumber()) {
                case 1:
                    cb1.setTextColor(Color.WHITE);
                    question.setText("Answer a) is correct");
                    break;
                case 2:
                    cb2.setTextColor(Color.WHITE);
                    question.setText("Answer b) is correct");
                    break;
                case 3:
                    cb3.setTextColor(Color.WHITE);
                    question.setText("Answer c) is correct");
                    break;
            }
        } if (getAllQuestions.get.questionType == TEXTENTRY) {
            typeAnswer.setText("In Rainbows");
        }

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
