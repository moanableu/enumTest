package com.example.android.qtt;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final long COUNTDOWN_IN_MILLIS = 31000;

    private TextView scoreView, questionCountDown, countdown;
    private TextView question;
    private RadioGroup rbGroup;
    private RadioButton rb1, rb2, rb3;
    private CheckBox cb1, cb2, cb3;
    private EditText typeAnswer;
    private Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int getQuestionCounter;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private long backPressTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreView = findViewById(R.id.score_view);
        questionCountDown = findViewById(R.id.question_count_view);
        countdown = findViewById(R.id.countdown);
        question = findViewById(R.id.question);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.button_1);
        rb2 = findViewById(R.id.button_2);
        rb3 = findViewById(R.id.button_3);
        cb1 = findViewById(R.id.checkbox_1);
        cb2 = findViewById(R.id.checkbox_2);
        cb3 = findViewById(R.id.checkbox_3);
        typeAnswer = findViewById(R.id.song_text);
        buttonConfirmNext = findViewById(R.id.button_continue);

        textColorDefaultRb = rb1.getTextColors(); // get default color

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = (ArrayList <Question>) dbHelper.getAllQuestions();
        getQuestionCounter = questionList.size();
        Collections.shuffle(questionList);

        hideViews();

        showNextQuestion();

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    }

                    else if (cb1.isChecked() || cb2.isChecked() || cb3.isChecked()) {
                        checkAnswer();
                    }

                    else if (!typeAnswer.getText().toString().isEmpty()) {
                        checkAnswer();
                    }

                    else {
                        Toast toast = Toast.makeText(MainActivity.this, "Please select an option", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 260);
                        toast.show();
                    }
                }

                else {
                    showNextQuestion();
                }
            }
        });

        // hide keyboard after input
        typeAnswer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(typeAnswer.getWindowToken(),0);
                    return true;
                }
                return false;
            }
        });
    }

    private void hideViews() {
        rbGroup.setVisibility(View.INVISIBLE);
        cb1.setVisibility(View.INVISIBLE);
        cb2.setVisibility(View.INVISIBLE);
        cb3.setVisibility(View.INVISIBLE);
        typeAnswer.setVisibility(View.INVISIBLE);
    }

    // display RadioGroup
    private void showRadioGroup() {
        rbGroup.setVisibility(View.VISIBLE);
    }

    // display checkboxes
    private void showCheckboxes() {
        cb1.setVisibility(View.VISIBLE);
        cb2.setVisibility(View.VISIBLE);
        cb3.setVisibility(View.VISIBLE);
    }

    // display EditText
    private void showTypeAnswer() {
        typeAnswer.setVisibility(View.VISIBLE);
    }

    /**
     * display next question
     * Causaelity R.S. implementation enum call, match question to corresponding switch statement
     */
    private void showNextQuestion() {
        rbGroup.clearCheck();

        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);

        answered = false;

        typeAnswer.getText().clear();

        if (questionCounter < getQuestionCounter) {
            currentQuestion = questionList.get(questionCounter);

            question.setText(currentQuestion.getQuestion());

            hideViews();

            switch (currentQuestion.getType()) {
                case RADIO:
                    showRadioGroup();
                    rb1.setText(currentQuestion.getOption1());
                    rb2.setText(currentQuestion.getOption2());
                    rb3.setText(currentQuestion.getOption3());
                    break;

                case CHECKBOX:
                    showCheckboxes();
                    cb1.setText(currentQuestion.getOption1());
                    cb2.setText(currentQuestion.getOption2());
                    cb3.setText(currentQuestion.getOption3());
                    break;

                case TEXTENTRY:
                    showTypeAnswer();
                    break;
            }

            questionCounter++;
            questionCountDown.setText("Question: " + questionCounter + "/" + getQuestionCounter);

            buttonConfirmNext.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            closingToast();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    /**
     * update timer
     */
    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        countdown.setText(timeFormatted);
    }

    /**
     * calculate score
     * Causaelity implemented boolean answeredCorrectly and checkbox score logic
     */
    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
        boolean answeredCorrectly = false;

        int answerNumber = 0;

        switch (currentQuestion.getType()) {
            case RADIO:
                RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
                answerNumber = rbGroup.indexOfChild(rbSelected) + 1;
                if (answerNumber == currentQuestion.getAnswerNumber()) {
                    score++;
                    answeredCorrectly = true;
                }
                break;

            case CHECKBOX:
                if (cb1.isChecked()) { answerNumber += 1; }
                if (cb2.isChecked()) { answerNumber += 2; }
                if (cb3.isChecked()) { answerNumber += 4; }

                if (answerNumber == currentQuestion.getAnswerNumber()) {
                    score++;
                    answeredCorrectly = true;
                }
                break;

            case TEXTENTRY:
                if (typeAnswer.getText().toString().equalsIgnoreCase("in rainbows")) {
                    score++;
                    answeredCorrectly = true;
                }
                break;
        }

        scoreView.setText("Score: " + score);

        if (!answeredCorrectly) {
            showSolution();
        }

    }

    /**
     * display solution from string.xml resources: checkbox and textEntry
     * Causaelity R.S. implementation call to enums
     */
    private void showSolution() {
        if (currentQuestion.getType() == QuestionType.RADIO) {

            switch (currentQuestion.getAnswerNumber()) {
                case 1:
                    question.setText("Answer a) is correct");
                    break;
                case 2:
                    question.setText("Answer b) is correct");
                    break;
                case 3:
                    question.setText("Answer c) is correct");
                    break;
            }
        }
       else if (currentQuestion.getType() == QuestionType.CHECKBOX) {
            question.setText(R.string.answerCheckbox);
        }
        else if (currentQuestion.getType() == QuestionType.TEXTENTRY) {
            question.setText(R.string.answerTextEntry);
        }
        hideViews();

        if (questionCounter < getQuestionCounter) {
            buttonConfirmNext.setText("Next");
        }
        else {
            buttonConfirmNext.setText("Exit Quiz");
        }
    }

    public void closingToast() {
        Toast toast = Toast.makeText(MainActivity.this, "Congratulations you scored: " + score + " points" , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 260);
        toast.show();
    }

    /**
     * double back press required te exit app
     */
    @Override
    public void onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Please press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressTime = System.currentTimeMillis();
    }

    /**
     * kill timer to prevent memory leaks
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void finishQuiz() {
        finish();
    }
}
