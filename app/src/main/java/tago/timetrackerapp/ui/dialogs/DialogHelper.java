package tago.timetrackerapp.ui.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class DialogHelper {

    public static void showAlert(Context c, String title, String description, String buttonText) {
        AlertDialog alertDialog= new AlertDialog.Builder(c).create();
            alertDialog.setMessage(description);
            alertDialog.setTitle(title);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttonText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
    }
}
