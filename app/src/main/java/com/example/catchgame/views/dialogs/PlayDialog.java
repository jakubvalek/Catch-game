package com.example.catchgame.views.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.preference.PreferenceManager;

import com.example.catchgame.R;
import com.example.catchgame.views.activities.GameActivity;

public class PlayDialog extends AppCompatDialogFragment {

    private Spinner spinner;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_play, null);

        final Button play = view.findViewById(R.id.button_play);
        Button cancel = view.findViewById(R.id.button_cancel);
        spinner = view.findViewById(R.id.spinner_difficulty);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.difficulty_entries, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        int savedSpinnerIndex = preferences.getInt(getString(R.string.sp_difficulty_spinner_index), 0);
        spinner.setSelection(savedSpinnerIndex);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();
                editor.putInt(getString(R.string.sp_difficulty_spinner_index), spinner.getSelectedItemPosition());
                editor.apply();

                Intent playGame = new Intent(getActivity().getApplicationContext(), GameActivity.class);
                playGame.putExtra(GameActivity.EXTRA_DIFFICULTY, spinner.getSelectedItem().toString());
                getActivity().startActivityForResult(playGame, 1);
                dismiss();
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

}
