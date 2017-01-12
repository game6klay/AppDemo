package com.example.patja2r.test1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by patja2r on 6/1/2016.
 */
public class ErrorDialogue {

    public void showErrorText(String errorText, Context context){
        AlertDialog.Builder alertDialogue = new AlertDialog.Builder(context);

        // set title
        alertDialogue.setTitle("Error !!");
        // set Dialogue message
        alertDialogue.setMessage(errorText).setCancelable(false).setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }
}
