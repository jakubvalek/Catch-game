package com.example.catchgame.other;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.catchgame.models.Score;

import java.util.ArrayList;
import java.util.List;

public class SqliteHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Score.db";
    public static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Score.ScoreEntry.TABLE_NAME + " (" +
            "ROWID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            Score.ScoreEntry.COLUMN_NAME_DIFFICULTY + " TEXT," +
            Score.ScoreEntry.COLUMN_NAME_SCORE + " TEXT)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Score.ScoreEntry.TABLE_NAME;


    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        removeTable(db);
        onCreate(db);
    }

    public boolean insertItem(String difficulty, int score)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Score.ScoreEntry.COLUMN_NAME_DIFFICULTY, difficulty);
        contentValues.put(Score.ScoreEntry.COLUMN_NAME_SCORE, score);
        return db.insert(Score.ScoreEntry.TABLE_NAME, null, contentValues) != -1;
    }
    public boolean insertItem(Score score)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Score.ScoreEntry.COLUMN_NAME_DIFFICULTY, score.difficulty);
        contentValues.put(Score.ScoreEntry.COLUMN_NAME_SCORE, score.score);
        return db.insert(Score.ScoreEntry.TABLE_NAME, null, contentValues) != -1;
    }

    public List<Score> getScoreList()
    {
        List<Score> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + Score.ScoreEntry.TABLE_NAME, null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            String difficulty = res.getString(res.getColumnIndex(Score.ScoreEntry.COLUMN_NAME_DIFFICULTY));
            int score = res.getInt(res.getColumnIndex(Score.ScoreEntry.COLUMN_NAME_SCORE));
            scores.add(0, new Score(difficulty, score));
            res.moveToNext();
        }
        res.close();
        return scores;
    }

    public void removeTable(@Nullable SQLiteDatabase db)
    {
        if(db == null)
            db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
    }
}