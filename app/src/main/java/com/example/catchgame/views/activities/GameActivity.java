package com.example.catchgame.views.activities;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.catchgame.R;
import com.example.catchgame.views.GameView;

public class GameActivity extends AppCompatActivity implements GameView.GameViewListener {

    public static final String EXTRA_DIFFICULTY = "DIFF";
    private GameView gameView;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getInsetsController().hide(WindowInsets.Type.statusBars());
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        difficulty = getIntent().getStringExtra(EXTRA_DIFFICULTY);
        gameView = new GameView(this, Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels, difficulty, this);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.start();
    }

    @Override
    public void stopTheGame(int score) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int resId;
        switch (difficulty){
            case "Hard":
                resId = R.string.sp_score_hard;
                break;
            case "Insane":
                resId = R.string.sp_score_insane;
                break;
            default:
            case "Normal":
                resId = R.string.sp_score_normal;
                break;
        }
        int previousDiffScore = preferences.getInt(getString(resId), 0);
        if(previousDiffScore > score){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(getString(resId), score);
            editor.apply();
        }
        finish();
    }
}