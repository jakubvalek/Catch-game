package com.example.catchgame.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.example.catchgame.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    public static final String CHANGE_MUSIC_VOLUME = "CHANGE_MUSIC_VOLUME";
    public static final String CHANGE_SOUND_VOLUME = "CHANGE_SOUND_VOLUME";
    public static final String PLAY_CATCH_SOUND = "PLAY_CATCH_SOUND";
    public static final String PLAY_EXPLOSION_SOUND = "PLAY_EXPLOSION_SOUND";
    public static final String MUSIC_PLAYER_PAUSE = "MUSIC_PLAYER_PAUSE";
    public static final String MUSIC_PLAYER_RESUME = "MUSIC_PLAYER_RESUME";
    private MediaPlayer musicPlayer;
    private SoundPool soundPool;
    private int explosion, caught;
    private List<Integer> playlist;
    private int songDecision;
    private float soundVolume;
    private boolean isMusicPlayerPaused = false;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MusicService.CHANGE_MUSIC_VOLUME: {
                    int volume = intent.getIntExtra(MusicService.CHANGE_MUSIC_VOLUME, 50);
                    musicPlayer.setVolume(volume / 100.0f, volume / 100.0f);
                    break;
                }
                case MusicService.CHANGE_SOUND_VOLUME: {
                    int volume = intent.getIntExtra(MusicService.CHANGE_SOUND_VOLUME, 50);
                    soundVolume = volume / 100.0f;
                    break;
                }
                case MusicService.PLAY_CATCH_SOUND:
                    soundPool.play(caught, soundVolume, soundVolume, 0, 0, 1);
                    break;
                case MusicService.PLAY_EXPLOSION_SOUND:
                    soundPool.play(explosion, soundVolume, soundVolume, 0, 0, 1);
                    break;
                case MUSIC_PLAYER_PAUSE:
                    if(musicPlayer.isPlaying()){
                        musicPlayer.pause();
                        isMusicPlayerPaused = true;
                    }
                    break;
                case MUSIC_PLAYER_RESUME:
                    if(isMusicPlayerPaused){
                        musicPlayer.start();
                        isMusicPlayerPaused = false;
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        playlist = new ArrayList<>();
        playlist.add(R.raw.music1);
        playlist.add(R.raw.music2);
        playlist.add(R.raw.music3);
        songDecision = new Random().nextInt(3);
        musicPlayer = MediaPlayer.create(this, playlist.get(songDecision));
        musicPlayer.setOnCompletionListener(this);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build();
        explosion = soundPool.load(this, R.raw.explosion, 1);
        caught = soundPool.load(this, R.raw.caught, 1);
        soundVolume = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt(getString(R.string.soundVolume_key), 0);
        Log.d("qwe", "Sound volume: " + soundVolume);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CHANGE_MUSIC_VOLUME);
        intentFilter.addAction(CHANGE_SOUND_VOLUME);
        intentFilter.addAction(PLAY_CATCH_SOUND);
        intentFilter.addAction(PLAY_EXPLOSION_SOUND);
        intentFilter.addAction(MUSIC_PLAYER_PAUSE);
        intentFilter.addAction(MUSIC_PLAYER_RESUME);
        registerReceiver(receiver, intentFilter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicPlayer.stop();
        musicPlayer.release();
        soundPool.release();
        unregisterReceiver(receiver);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        songDecision++;
        if(songDecision >= playlist.size())
            songDecision = 0;
        Log.d("qwe", "New song decision: " + songDecision + " " + playlist.get(songDecision).toString());
        musicPlayer.reset();
        musicPlayer = MediaPlayer.create(this, playlist.get(songDecision));
        musicPlayer.setOnCompletionListener(this);
        musicPlayer.start();
    }

}
