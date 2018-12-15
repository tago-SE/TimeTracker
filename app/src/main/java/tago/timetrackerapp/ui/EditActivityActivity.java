package tago.timetrackerapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import tago.timetrackerapp.R;
import tago.timetrackerapp.model.Categories;
import tago.timetrackerapp.model.Category;
import tago.timetrackerapp.ui.managers.LocaleManager;
import top.defaults.colorpicker.ColorPickerPopup;

// Uses open source library for coloring: https://github.com/duanhong169/ColorPicker

public class EditActivityActivity extends AppCompatActivity {

    private static final String TAG = "EditTask";

    private final Context context = this;

    private final Categories categories = Categories.instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Changes language to match settings, must be done first
        LocaleManager.setLocale(this);
        setContentView(R.layout.activity_edit_task);

        // Category picker
        final ImageView categorySelection = findViewById(R.id.categorySelection);
        categorySelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                builderSingle.setTitle("Select One Name:-");

                final ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<>(context, android.R.layout.select_dialog_singlechoice);
                for (Category c : categories.load()) {
                    arrayAdapter.add(c.getName());
                }
                builderSingle.setNegativeButton(getString(android.R.string.cancel),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();

            }
        });

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
