package com.example.catchgame.views.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.catchgame.R;
import com.example.catchgame.adapters.SectionsPagerAdapter;
import com.example.catchgame.models.Score;
import com.example.catchgame.services.MusicService;
import com.example.catchgame.viewmodels.ScoreViewModel;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.SeekBarPreference;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ScoreViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(ScoreViewModel.class);
        viewModel.Init(this);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.selectTab(tabs.getTabAt(1));
        Intent music = new Intent(this, MusicService.class);
        startService(music);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                Score score = (Score)data.getSerializableExtra("extra");
                viewModel.insertScoreToSql(score);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent pauseMusic = new Intent();
        pauseMusic.setAction(MusicService.MUSIC_PLAYER_PAUSE);
        sendBroadcast(pauseMusic);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent resumeMusic = new Intent();
        resumeMusic.setAction(MusicService.MUSIC_PLAYER_RESUME);
        sendBroadcast(resumeMusic);
    }
}