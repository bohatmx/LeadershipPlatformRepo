package com.oneconnect.leadership.library.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;

import java.util.Objects;

public class QuizResults extends AppCompatActivity {
    public static final String PREFS_NAME = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);
        displayResults();
    }

    protected void displayResults() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Resources res = getResources();


        TypedArray questions = res.obtainTypedArray(R.array.questions);

        double total = questions.length();
        double score = 0;

        LinearLayout container = (LinearLayout) findViewById(R.id.resultsContainer);

        // iterate over questions and process each
        for (int i=0; i<total; ++i) {
            String answered = settings.getString(String.format("q%d", i+1),"opt0");
            String[] question = res.getStringArray(questions.getResourceId(i, -1));

            // question title
            TextView title = new TextView(this);
            title.setPadding(0, 20, 0, 10);
            title.setText(String.format(getString(R.string.questionNumberSingle), i+1));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                title.setTextAppearance(android.R.style.TextAppearance_Large);
            }
            container.addView(title);

            // question itself
            TextView desc = new TextView(this);
            desc.setText(question[0]);
            container.addView(desc);

            // your answer
            TextView yours = new TextView(this);
            int index = Integer.parseInt(answered.substring(3));
            String answerText;
            if (index == 0) {
                answerText = getString(R.string.dontKnow);
            } else {
                answerText = question[index];
            }
            yours.setText(String.format(getString(R.string.youAnswered), answerText));
            container.addView(yours);

            // add either the correct answer or a message saying it was correct
            if (Objects.equals(question[5], answered)) {
                TextView correct = new TextView(this);
                correct.setText(getString(R.string.correct));
                container.addView(correct);

                // add to score if correct
                ++score;
            } else {
                TextView incorrect = new TextView(this);
                index = Integer.parseInt(question[5].substring(3));
                incorrect.setText(String.format(getString(R.string.incorrect), question[index]));
                container.addView(incorrect);
            }
        }

        // convert to percent score
        score = (score/total) * 100;

        // display the score in the title
        TextView t = (TextView) findViewById(R.id.resultsTitle);
        t.setText(String.format(getString(R.string.resultsTitle), score ));

        // save the score
        if (score > settings.getFloat("highscore", 0)) {
            // display highscore message in UI
            TextView highscoreText = new TextView(this);
            highscoreText.setPadding(0, 20, 0, 10);
            highscoreText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            highscoreText.setText(getString(R.string.newHighscore));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                highscoreText.setTextAppearance(android.R.style.TextAppearance_Large);
            }
            container.addView(highscoreText, 0);

            // save highscore
            SharedPreferences.Editor editor = settings.edit();
            editor.putFloat("highscore", (float) score);
            editor.apply();
        }

    }

    /**
     * override back button to go directly back to main activity
     * (don't want ability to just jump back to the previous question)
     */
    @Override
    public void onBackPressed() {
        finish();
        backToMenu(null);
    }

    /**
     * return to the main screen
     * @param view
     */
    public void backToMenu(View view) {
        Intent main = new Intent(this, PLDPActivity.class);
        startActivity(main);
    }
}
