package tago.timetrackerapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import tago.timetrackerapp.R;
import top.defaults.colorpicker.ColorPickerPopup;

public class EditCategoryActivity extends AppCompatActivity {

    private final static String TAG = "EditCategory";
    public static final String STATE = "state";
    public static final String STATE_EDIT = "edit";
    public static final String STATE_ADD = "add";

    private final Context context = this;

    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        state =  getIntent().getExtras().getString(STATE);
        if (state == null || state.equals(""))
            throw new IllegalStateException("State must either start as " + STATE_EDIT + " or " +
                    STATE_ADD);


        Button btnColor = findViewById(R.id.btnColor);
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new ColorPickerPopup.Builder(context)
                        .initialColor(Color.RED) // Set initial color
                        .enableBrightness(false) // Enable brightness slider or not
                        .enableAlpha(false)     // Enable alpha slider or not
                        .okTitle(getResources().getString(R.string.choose))
                        .cancelTitle(getResources().getString(R.string.cancel))
                        .showIndicator(true)
                        .showValue(false)
                        .build()
                        .show(v, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                v.setBackgroundColor(color);
                                Log.d(TAG, "color picked: " + color);
                            }

                            @Override
                            public void onColor(int color, boolean fromUser) {

                            }
                        });
            }
        });


    }
}
