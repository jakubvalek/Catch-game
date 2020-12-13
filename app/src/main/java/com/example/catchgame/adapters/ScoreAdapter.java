package com.example.catchgame.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.catchgame.R;
import com.example.catchgame.models.Score;

import java.util.List;

public class ScoreAdapter extends ArrayAdapter<Score> {

    private static class ViewHolder{
        TextView difficulty;
        TextView score;
    }

    public ScoreAdapter(@NonNull Context context, @NonNull List<Score> data) {
        super(context, R.layout.custom_list_item, data);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Score score = getItem(position);
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_list_item, parent, false);
            holder.difficulty = (TextView) convertView.findViewById(R.id.textView_difficulty);
            holder.score = (TextView) convertView.findViewById(R.id.textView_score);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(score != null) {
            holder.difficulty.setText(score.difficulty);
            holder.score.setText(Integer.toString(score.score));
        }
        return convertView;
    }
}
