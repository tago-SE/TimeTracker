package tago.timetrackerapp.ui.managers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class EmailManager {

    public static Intent createSendEmailIntent(String title, String recipient, String subject, String text){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",recipient, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return Intent.createChooser(intent, title);
    }
}
