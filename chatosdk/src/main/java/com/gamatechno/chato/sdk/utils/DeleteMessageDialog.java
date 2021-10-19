package com.gamatechno.chato.sdk.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class DeleteMessageDialog {
    Context context;
    OnAlertDialog onAlertDialog;
    AlertDialog.Builder alertDialog;

    public DeleteMessageDialog(Context context, OnAlertDialog onAlertDialog) {
        this.context = context;
        this.onAlertDialog = onAlertDialog;
        this.initAlert(true);
    }

    private void initAlert(boolean isCancellabel) {
        this.alertDialog = new AlertDialog.Builder(this.context);
        this.alertDialog.setMessage("Hapus pesan?");
        this.alertDialog.setPositiveButton("Hapus untuk saya", (dialog, which) ->
                DeleteMessageDialog.this.onAlertDialog.onDeleteForMeButton(dialog));

        this.alertDialog.setNegativeButton("Hapus untuk semua orang", (dialog, which) ->
                DeleteMessageDialog.this.onAlertDialog.onDeleteForAllButton(dialog));

        this.alertDialog.setOnCancelListener(DialogInterface::dismiss);


        this.alertDialog.show();
    }

    public interface OnAlertDialog {
        void onDeleteForMeButton(DialogInterface var1);
        void onDeleteForAllButton(DialogInterface var1);
    }
}
