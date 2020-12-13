package com.example.catchgame.views.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.catchgame.R;
import com.example.catchgame.adapters.ScoreAdapter;
import com.example.catchgame.models.Score;
import com.example.catchgame.viewmodels.ScoreViewModel;

import java.util.List;

public class ScoresFragment extends Fragment {

    private ListView listView;
    private List<Score> scores;
    private Drawable buttonDrawable, pressedDrawable;
    private ScoreViewModel viewModel;
    Button buttonTop, buttonHistory;

    public ScoresFragment() {
        // Required empty public constructor
    }

    public static ScoresFragment newInstance() {
        ScoresFragment fragment = new ScoresFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scores, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ScoreViewModel.class);
        listView = view.findViewById(R.id.listView);
        buttonTop = view.findViewById(R.id.button_top);
        buttonHistory = view.findViewById(R.id.button_history);
        buttonDrawable = buttonTop.getBackground();
        pressedDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_background_pressed, null);
        viewModel.Init(requireContext());
        buttonAnimation(false);
        viewModel.getScoreLiveData().observe(requireActivity(), new Observer<List<Score>>() {
                    @Override
                    public void onChanged(List<Score> scores) {
                        ArrayAdapter<Score> adapter = new ScoreAdapter(getContext(), scores);
                        listView.setAdapter(adapter);
                    }
                });
        buttonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.topDataFromPreferences();
                if(!viewModel.topIsPressed)
                    buttonAnimation(true);
            }
        });
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.historyData();
                if(viewModel.topIsPressed)
                    buttonAnimation(true);
            }
        });

        return view;
    }

    private void buttonAnimation(boolean clicked){
        if(clicked)
            viewModel.topIsPressed = !viewModel.topIsPressed;
        if(!viewModel.topIsPressed){
            //viewModel.topIsPressed = true;
            buttonTop.setBackground(buttonDrawable);
            buttonHistory.setBackground(pressedDrawable);
        }
        else {
            //viewModel.topIsPressed = false;
            buttonHistory.setBackground(buttonDrawable);
            buttonTop.setBackground(pressedDrawable);
        }
    }

}