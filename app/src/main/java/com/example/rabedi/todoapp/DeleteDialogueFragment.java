package com.example.rabedi.todoapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by rabedi on 11/25/15.
 */
public class DeleteDialogueFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_confirmation_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
