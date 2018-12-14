package tago.timetrackerapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;

import tago.timetrackerapp.R;
import tago.timetrackerapp.repo.model.Category;
import tago.timetrackerapp.ui.managers.LocaleManager;
import tago.timetrackerapp.ui.util.Colorizer;
import tago.timetrackerapp.ui.util.TextChangedListener;
import tago.timetrackerapp.ui.viewmodel.EditCategoryVM;
import top.defaults.colorpicker.ColorPickerPopup;

public class EditCategoryActivity extends AppCompatActivity {

    private final static String TAG = "EditCategory";

    public static final String RANDOM_COLOR_KEY     = "randomColor";
    public static final String CATEGORY_KEY         = "category";
    public static final String STATE_KEY            = "state";
    public static final String STATE_EDIT           = "edit";
    public static final String STATE_ADD            = "add";

    private final Context context = this;
    private String state;
    private EditCategoryVM viewModel;

    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;


    /* @TODO Fix WindowLeak related to Color Picker */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Changes language to match settings, must be done first
        LocaleManager.setLocale(this);
        // Inflate view
        setContentView(R.layout.activity_edit_category);

        // Check if proper state argument was passed
        state =  getIntent().getExtras().getString(STATE_KEY);
        if (state == null || state.equals(""))
            throw new IllegalStateException("State must either start as " + STATE_EDIT + " or " +
                    STATE_ADD);

        // Add back button to ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (state.equals(STATE_ADD)) {
            actionBar.setTitle(getResources().getString(R.string.add_category));
        } else {
            actionBar.setTitle(getResources().getString(R.string.edit_category));
        }

        // Create ViewModel
        viewModel = ViewModelProviders.of(this).get(EditCategoryVM.class);

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
                                viewModel.setColor(color);
                            }

                            @Override
                            public void onColor(int color, boolean fromUser) {

                            }
                        });
            }
        });
        // Update color listener
        viewModel.colorLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer color) {
                colorView.setColorFilter(color);
            }
        });

        EditText text = findViewById(R.id.categoryNameField);
        text.addTextChangedListener(new TextChangedListener<EditText>(text) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                viewModel.setName(s.toString());
            }
        });

        String jsonObject = getIntent().getExtras().getString(CATEGORY_KEY);
        if (jsonObject != null) {
            Category category = new Gson().fromJson(jsonObject, Category.class);
            viewModel.setStartingCategory(category);
            String categoryName = category.getName();
            text.setText(categoryName);
            text.setSelection(categoryName.length());
        } else {
            // Setup a random starting color if first time then remove the intent
            Intent intent = getIntent();
            if (intent.getExtras().getBoolean(RANDOM_COLOR_KEY)) {
                intent.removeExtra(RANDOM_COLOR_KEY); // removes the intent
                viewModel.setColor(Colorizer.getRandomColor());
            }
        }

        // Handle save response
        viewModel.saveLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer response) {
                AlertDialog alertDialog;
                switch (response) {
                    case EditCategoryVM.SAVE_NO_NAME:
                        alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setMessage(getString(R.string.err_save_no_name_desc));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(android.R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        return;
                    case EditCategoryVM.SAVE_NO_COLOR:
                        alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setMessage(getString(R.string.err_save_no_color_desc));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(android.R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        return;
                    case EditCategoryVM.SAVE_OK:
                        finish(); // End activity
                    default:
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu: adds the icons at the acton bar, if its present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        // Hide delete item if in Add Categoryu state
        if (state.equals(STATE_ADD)) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Back button pressed
                onBack();
                return true;
            case R.id.action_save:
                viewModel.save();
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

    private void onBack() {
        // If any changes has been made the user is asked if they want to discard them or not.
        if (viewModel.hasChanged()) {
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
                        viewModel.delete();
                        dialog.dismiss();
                        finish();
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
