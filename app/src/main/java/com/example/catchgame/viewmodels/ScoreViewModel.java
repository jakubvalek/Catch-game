package com.example.catchgame.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import com.example.catchgame.R;
import com.example.catchgame.models.Score;
import com.example.catchgame.other.SqliteHelper;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreViewModel extends ViewModel {

    public boolean topIsPressed = true;
    private MutableLiveData<List<Score>> scoreMutableLiveData;
    private List<Score> fullScoreList;
    private SqliteHelper sqliteHelper;
    private Context context;

    public void Init(Context context) {
        this.context = context;
        if(sqliteHelper == null)
            sqliteHelper = new SqliteHelper(context);
    }

    public LiveData<List<Score>> getScoreLiveData() {
        if (scoreMutableLiveData == null) {
            scoreMutableLiveData = new MutableLiveData<>();
            fullScoreList = sqliteHelper.getScoreList();
            topDataFromPreferences();
        }
        return scoreMutableLiveData;
    }

    public void topDataFromPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        List<Score> difficultyScores = new ArrayList<>();
        difficultyScores.add(new Score("Normal", preferences.getInt(context.getString(R.string.sp_score_normal), 0)));
        difficultyScores.add(new Score("Hard", preferences.getInt(context.getString(R.string.sp_score_hard), 0)));
        difficultyScores.add(new Score("Insane", preferences.getInt(context.getString(R.string.sp_score_insane), 0)));
        scoreMutableLiveData.setValue(difficultyScores);
    }

    public void topDataFilter(){
        Map<String, Integer> maxMap = new HashMap<>();
        for (int i = 0; i < fullScoreList.size(); i++) {
            Score score = fullScoreList.get(i);
            Integer currentMax = maxMap.get(score.difficulty);
            if (currentMax == null) {
                currentMax = Integer.MIN_VALUE;
            }
            maxMap.put(score.difficulty, Math.max(score.score, currentMax));
        }
        List<Score> filteredScores = new ArrayList<>();
        for (String itemId : maxMap.keySet()) {
            filteredScores.add(new Score(itemId, maxMap.get(itemId)));
        }
        scoreMutableLiveData.setValue(filteredScores);
    }

    public void insertScoreToSql(Score score){
        sqliteHelper.insertItem(score);
        addData(score);
    }

    public void historyData(){
        scoreMutableLiveData.setValue(fullScoreList);
    }

    public void addData(Score score){
        fullScoreList.add(0, score);
        if(!topIsPressed)
            scoreMutableLiveData.setValue(fullScoreList);
        else
            topDataFromPreferences();
    }

}