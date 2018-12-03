package tago.timetrackerapp.ui.managers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class EmailManager {

    public static void sendEmail(Context c, String recipient, String subject, String text){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",recipient, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        c.startActivity(Intent.createChooser(intent, "Choose an Email client"));
    }
}
