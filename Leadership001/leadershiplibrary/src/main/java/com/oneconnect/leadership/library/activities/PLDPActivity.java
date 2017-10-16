package com.oneconnect.leadership.library.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;

public class PLDPActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pldp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        draw();
    }
    public void resetAnswers(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.resetHighConfirmTitle))
                .setMessage(getString(R.string.resetHighConfirmTitle))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();

                        int total = getResources().obtainTypedArray(R.array.questions).length();
                        for (int i=1; i<=total; ++i) {
                            RadioGroup r = (RadioGroup) findViewById(R.id.options);
                            editor.remove(String.format("q%d",i));
                        }

                        editor.apply();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public void takeQuiz(View view) {

        // set up the data to send
        Bundle data = new Bundle();

        // send the question number as the question to view first
        //  this allows for future features like automatically jumping to
        //  the last question the user was on previously
        data.putInt("questionNumber", 1);

        // make the intent
        Intent quiz = new Intent(this, QuizQuestion.class);

        // add the data and start the activity
        quiz.putExtras(data);
        startActivity(quiz);
    }

    /**
     * display information about the app + links, etc.
     * @param view
     */
    public void about(View view) {
        // new intent for the webpage viewer activity
        Intent about = new Intent(this, webpageViewer.class);

        // add the url to load
        Bundle data = new Bundle();
        data.putString("url", getString(R.string.aboutFile));
        about.putExtras(data);

        // start!
        startActivity(about);
    }
    private void draw() {
        // display highscore here
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        double highscore = settings.getFloat("highscore", 0);
        String textScore = String.format(getString(R.string.highscore), highscore);
        ((TextView) findViewById(R.id.score)).setText(textScore);
    }
    /**
     * resets the highscore back to zero
     * @param view
     */
    public void resetHighscore(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.resetHighConfirmTitle))
                .setMessage(getString(R.string.resetHighConfirmMessage))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putFloat("highscore", 0);
                        editor.apply();

                        // redraw the highscore since it may have changed
                        draw();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    /**
     * Override back button so we don't have strange results
     * - back should return to android home screen
     */
    @Override
    public void onBackPressed() {

    }
}
