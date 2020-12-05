package com.example.catchgame.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.example.catchgame.R;
import com.example.catchgame.views.GameView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            getWindow().getInsetsController().hide(WindowInsets.Type.statusBars());
        }
        else
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        gameView = new GameView(this, Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels);
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


}