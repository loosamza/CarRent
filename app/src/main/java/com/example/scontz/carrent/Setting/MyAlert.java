package com.example.scontz.carrent.Setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.scontz.carrent.R;

/**
 * Created by scOnTz on 16/11/2559.
 */
public class MyAlert {
    public void myDialog(Context context,
                         String strTitle,
                         String strMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.danger);
        builder.setTitle(strTitle);
        builder.setMessage(strMessage);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        builder.show();
    }


}
