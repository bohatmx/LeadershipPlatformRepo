package com.oneconnect.leadership.library.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;

import java.util.Stack;

public class QuizQuestion extends Activity {
    int questionNumber;
    Stack<Integer> previousQuestions = new Stack<Integer>();
    public static final String PREFS_NAME = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);

        Bundle data = this.getIntent().getExtras();
        displayQuestion(data.getInt("questionNumber"));
    }

    /**
     * displays the question for the question number
     * @param n
     */
    private void displayQuestion(int n) {
        questionNumber = n;


        Resources res = getResources();
        TypedArray questions = res.obtainTypedArray(R.array.questions);
        String[] question = res.getStringArray(questions.getResourceId(n-1, -1));

        // note - (n indexed from 1)
        // change next question button to submit answers when on last question
        if (n == questions.length()) {
            Button next = ((Button) findViewById(R.id.nextBtn));
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit(v);
                }
            });
            next.setText(getString(R.string.submitButton));

        } else {
            Button next = ((Button) findViewById(R.id.nextBtn));
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    next(v);
                }
            });
            next.setText(getString(R.string.nextButton));
        }
        // disable previous question button if on first question
        if (n == 1) {
            ((Button) findViewById(R.id.previousBtn)).setEnabled(false);
        } else {
            ((Button) findViewById(R.id.previousBtn)).setEnabled(true);
        }

        // show the question number
        TextView t = (TextView) findViewById(R.id.questionNumber);
        t.setText(String.format(getString(R.string.questionNumber), n, questions.length()));

        ((TextView) findViewById(R.id.qDescription)).setText(question[0]);

        // set text for the radio buttons
        for (int i=1; i<=4; ++i) {
            String optID = String.format("opt%d", i);
            RadioButton r = (RadioButton) findViewById(getResources().getIdentifier(optID, "id", this.getPackageName()));
            r.setText(question[i]);
            r.setChecked(false);
        }

        // check either saved option or don't know
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        RadioButton r = (RadioButton) findViewById(getResources().getIdentifier(settings.getString(String.format("q%d", n), "opt0"), "id", this.getPackageName()));
        r.setChecked(true);
    }

    /**
     * saves the selected answer to the question number given
     * @param n
     */
    private void saveAnswer(int n) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        RadioGroup r = (RadioGroup) findViewById(R.id.options);
        editor.putString(String.format("q%d",n), getResources().getResourceEntryName(r.getCheckedRadioButtonId()));
        editor.apply();
    }

    /**
     * save the answer and navigate to the previous question
     * @param view
     */
    public void previous(View view) {
        previousQuestions.push(questionNumber);
        saveAnswer(questionNumber);
        displayQuestion(questionNumber-1);
    }

    /**
     * save the answer and navigate to the next question
     * @param view
     */
    public void next(View view) {
        previousQuestions.push(questionNumber);
        saveAnswer(questionNumber);
        displayQuestion(questionNumber+1);
    }

    /**
     * submit the quiz for marking
     * @param view
     */
    public void submit(View view) {
        // must save the current answer first!
        saveAnswer(questionNumber);

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.submitConfirmTitle))
                .setMessage(getString(R.string.submitConfirmMessage))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // switch to the results activity (that also handles score tally and highscores)
                        Intent results = new Intent(QuizQuestion.this, QuizResults.class);
                        startActivity(results);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    /**
     * return to the main screen
     * @param view
     */
    public void backToMenu(View view) {
        Intent main = new Intent(this, PLDPActivity.class);
        startActivity(main);
    }

    /**
     * override back button to work with how questions are displayed
     */
    @Override
    public void onBackPressed() {
        if (!previousQuestions.empty()) {
            int previous = previousQuestions.pop();
            saveAnswer(questionNumber);
            displayQuestion(previous);
        } else {
            backToMenu(null);
        }
    }

    }
