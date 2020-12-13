package com.example.catchgame.models;

import android.provider.BaseColumns;

import java.io.Serializable;

public class Score implements Serializable {
    public String difficulty;
    public int score;

    public Score(String difficulty, int score) {
        this.difficulty = difficulty;
        this.score = score;
    }

    public static class ScoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "score";
        public static final String COLUMN_NAME_DIFFICULTY = "difficulty";
        public static final String COLUMN_NAME_SCORE = "score";
    }

}
