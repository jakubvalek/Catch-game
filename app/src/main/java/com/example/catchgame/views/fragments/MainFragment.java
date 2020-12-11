package com.example.catchgame.views.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.catchgame.R;
import com.example.catchgame.views.activities.GameActivity;
import com.example.catchgame.views.dialogs.HowToPlayDialog;
import com.example.catchgame.views.dialogs.PlayDialog;

public class MainFragment extends Fragment {

    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    //private String mParam1;
    //private String mParam2;

    TextView textView_play, textView_howToPlay;


    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        textView_play = view.findViewById(R.id.play);
        textView_howToPlay = view.findViewById(R.id.how_to_play);
        textView_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent playGame = new Intent(getActivity().getApplicationContext(), GameActivity.class);
                //startActivity(playGame);
                PlayDialog playDialog = new PlayDialog();
                playDialog.show(getActivity().getSupportFragmentManager(), null);
            }
        });
        textView_howToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HowToPlayDialog howToPlayDialog = new HowToPlayDialog();
                howToPlayDialog.show(getActivity().getSupportFragmentManager(), null);
            }
        });
        return view;
    }
}