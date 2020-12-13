package com.example.catchgame.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.catchgame.R;
import com.example.catchgame.models.Score;
import com.example.catchgame.services.MusicService;
import com.example.catchgame.viewmodels.ScoreViewModel;
import com.example.catchgame.views.GameView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GameActivity extends AppCompatActivity implements GameView.GameViewListener {

    public static final String EXTRA_DIFFICULTY = "DIFF";
    private GameView gameView;
    private String difficulty;
    private boolean pauseDialogShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getInsetsController().hide(WindowInsets.Type.statusBars());
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        Score score = new Score(getIntent().getStringExtra(EXTRA_DIFFICULTY), 0);

        FrameLayout frameGame = findViewById(R.id.frameGame);
        gameView = new GameView(this, Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels, score, this, null);
        LinearLayout gameWidgets = new LinearLayout(this);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        params.setMargins(20,20,20,20);

        FloatingActionButton button = new FloatingActionButton(this);
        button.setLayoutParams(params);
        button.setImageResource(R.drawable.pause);
        button.setAlpha(0.4f);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseDialogShown = true;
                gameView.stop();
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setTitle("Game is paused");
                builder.setCancelable(false);
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pauseDialogShown = false;
                        gameView.start();
                    }
                });
                builder.setNegativeButton("Save & quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pauseDialogShown = false;
                        gameView.finishGame();
                    }
                });
                builder.create().show();
            }
        });

        gameWidgets.setLayoutParams(params);
        gameWidgets.setGravity(Gravity.END);
        gameWidgets.addView(button);

        frameGame.addView(gameView);
        frameGame.addView(gameWidgets);
        setContentView(frameGame);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent pauseMusic = new Intent();
        pauseMusic.setAction(MusicService.MUSIC_PLAYER_PAUSE);
        sendBroadcast(pauseMusic);
        if(!pauseDialogShown)
            gameView.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent resumeMusic = new Intent();
        resumeMusic.setAction(MusicService.MUSIC_PLAYER_RESUME);
        sendBroadcast(resumeMusic);
        if(!pauseDialogShown)
            gameView.start();
    }

    @Override
    public void stopTheGame(Score score) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int resId;
        switch (score.difficulty){
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
        if(score.score > previousDiffScore){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(getString(resId), score.score);
            editor.apply();
        }

        setResult(RESULT_OK, new Intent().putExtra("extra", score));
        finish();
    }
}