package tago.timetrackerapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tago.timetrackerapp.R;
import tago.timetrackerapp.viewmodels.EditCategory;
import tago.timetrackerapp.ui.managers.LocaleManager;
import tago.timetrackerapp.ui.util.TextChangedListener;
import top.defaults.colorpicker.ColorPickerPopup;

public class EditCategoryActivity extends AppCompatActivity {

    private final static String TAG = "EditCategory";

    private final Context context = this;

    private final EditCategory modelCategory = EditCategory.instance;


    /* @TODO Fix WindowLeak related to Color Picker */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Changes language to match settings, must be done first
        LocaleManager.setLocale(this);
        // Inflate view
        setContentView(R.layout.activity_edit_category);

        // Add ColorPicker
        final ImageView colorView = findViewById(R.id.categoryColor);
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
                                modelCategory.setColor(color);
                                paintColor();
                            }

                            @Override
                            public void onColor(int color, boolean fromUser) {
                            }
                        });
            }
        });
        // Save
        FloatingActionButton saveButton = findViewById(R.id.done);
        saveButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onSave();
                return false;
            }
        });

        // Setup name field
        EditText name = findViewById(R.id.categoryNameField);
        String s = modelCategory.getName();
        name.setText(s);
        name.setSelection(s.length());
        name.addTextChangedListener(new TextChangedListener<EditText>(name) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                modelCategory.setName(s.toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        paintColor();
    }

    private void paintColor() {
        final ImageView colorView = findViewById(R.id.categoryColor);
        colorView.setColorFilter(modelCategory.getColor());
        colorView.invalidate();
    }

    /**
     * Sets up the action bar depending on if the model is in "Add Category" or "Edit Category"
     * state.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu: adds the icons at the acton bar, if its present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (modelCategory.isAddState()) {
                actionBar.setTitle(getResources().getString(R.string.add_category));
                // Hide delete button in add state
                MenuItem menuItem = menu.findItem(R.id.action_delete);
                menuItem.setVisible(false);
            } else {
                actionBar.setTitle(getResources().getString(R.string.edit_category));
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Back button pressed
                onBack();
                return true;
            case R.id.action_delete:
                onDelete();
                return true;
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Not yet implemented, might be needed to prevent WindowLeakage...
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onSave() {
        modelCategory.save()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "save:onSubscribe " + d);
                    }
                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "save:onNext " + integer);
                        finish(); // End activity
                        Toast.makeText(context, getString(R.string.category_saved), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        int response = Integer.parseInt(e.getMessage());
                        if (response == EditCategory.SAVE_NO_NAME) {
                            AlertDialog alertDialog= new AlertDialog.Builder(context).create();
                            alertDialog.setMessage(getString(R.string.err_save_no_name_desc));
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                                    getString(android.R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }



    private void onBack() {
        // If any changes has been made the user is asked if they want to discard them or not.
        if (modelCategory.hasChanged()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(getString(R.string.q_discard_changes))
                    .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(getString(android.R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            finish();
        }
    }

    public void onDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getString(R.string.q_delete_category))
                .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modelCategory.delete();
                        dialog.dismiss();
                        finish();
                        Toast.makeText(context, getString(R.string.category_deleted), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getString(android.R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

}
