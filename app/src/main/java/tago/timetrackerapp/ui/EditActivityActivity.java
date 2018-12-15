package tago.timetrackerapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import tago.timetrackerapp.R;
import tago.timetrackerapp.ui.managers.LocaleManager;
import top.defaults.colorpicker.ColorPickerPopup;

// Uses open source library for coloring: https://github.com/duanhong169/ColorPicker

public class EditActivityActivity extends AppCompatActivity {

    private static final String TAG = "EditTask";

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Changes language to match settings, must be done first
        LocaleManager.setLocale(this);
        setContentView(R.layout.activity_edit_task);

        // Add ColorPicker
        final ImageView colorView = findViewById(R.id.color);
        colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new ColorPickerPopup.Builder(context)
                        .initialColor(Color.RED) // Set initial color
                        .enableBrightness(false) // Enable brightness slider or not
                        .enableAlpha(false)     // Enable alpha slider or not
                        .okTitle(getString(android.R.string.yes))
                        .cancelTitle(getString(android.R.string.no))
                        .showIndicator(true)
                        .showValue(false)
                        .build()
                        .show(v, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                //model.setColor(color);
                                //paintColor();
                            }

                            @Override
                            public void onColor(int color, boolean fromUser) {
                            }
                        });
            }
        });

    }
}
