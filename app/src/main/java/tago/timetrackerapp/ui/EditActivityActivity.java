package tago.timetrackerapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tago.timetrackerapp.R;
import tago.timetrackerapp.viewmodels.EditCategories;
import tago.timetrackerapp.viewmodels.EditActivity;
import tago.timetrackerapp.viewmodels.EditCategory;
import tago.timetrackerapp.repo.entities.Category;
import tago.timetrackerapp.ui.managers.LocaleManager;
import tago.timetrackerapp.ui.util.TextChangedListener;
import top.defaults.colorpicker.ColorPickerPopup;

// Uses open source library for coloring: https://github.com/duanhong169/ColorPicker

/* @TODO Fix WindowLeak related to Color Picker */

public class EditActivityActivity extends AppCompatActivity {

    private static final String TAG = "EditTask";

    private final Context context = this;

    private final EditActivity activityModel = EditActivity.instance;
    private final EditCategories categoriesModel = EditCategories.instance;

    private static int lastSelectedCategoryIndex;

    private ImageView colorView;
    private ImageView iconView;
    private Button categoryView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Changes language to match settings, must be done first
        LocaleManager.setLocale(this);
        setContentView(R.layout.activity_edit_task);

        // Category picker
        categoryView = findViewById(R.id.categorySelection);

        final Category category = activityModel.getCategory();
        if (category != null) {
            updateCategoryButton(category);
        }


        categoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getResources().getString(R.string.select_category));

                final ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<>(context, android.R.layout.select_dialog_singlechoice);

                final List<Category> categoryList =  categoriesModel.load();
                for (Category c : categoryList)
                    arrayAdapter.add(c.name);

               // Find current selected Category
                int checkedItem = -1; // no category selected
                if (category != null) {
                    checkedItem = categoryList.indexOf(category);
                }
                lastSelectedCategoryIndex = -1;  // reset as none
                builder.setSingleChoiceItems(arrayAdapter, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // user made a change, save item index
                        lastSelectedCategoryIndex = which;
                    }
                });
                builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (lastSelectedCategoryIndex == -1)
                            return;
                        // user clicked OK, update model and view with selected changes
                        Category selectedCategory = categoryList.get(lastSelectedCategoryIndex);
                        activityModel.setCategory(selectedCategory);
                        updateCategoryButton(selectedCategory);
                    }
                });
                builder.setNegativeButton(getString(android.R.string.cancel), null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Color picker
        colorView = findViewById(R.id.color);
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
                                // Update model and view with selected changes
                                activityModel.setColor(color);
                                paintColor();
                            }

                            @Override
                            public void onColor(int color, boolean fromUser) {
                            }
                        });
            }
        });

        // Setup name field
        EditText name = findViewById(R.id.categoryNameField);
        String s = activityModel.getName();
        name.setText(s);
        name.setSelection(s.length());
        name.addTextChangedListener(new TextChangedListener<EditText>(name) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                activityModel.setName(s.toString());
            }
        });

        // Icon picker
        iconView = findViewById(R.id.icon);
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* TODO: add icon picker */
            }
        });
    }

    private void updateCategoryButton(Category category) {
        categoryView.setText(category.name);
        categoryView.setBackgroundColor(category.color);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Back button pressed
                onBack();
                return true;
            case R.id.action_save:
                onSave();
                return true;
            case R.id.action_delete:
                onDelete();
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        paintColor();
    }

    /**
     * Sets up the action bar depending on if the model is in "Add Activity" or "Edit Activity"
     * state.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu: adds the icons at the acton bar, if its present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (activityModel.isAddState()) {
                actionBar.setTitle(getResources().getString(R.string.add_activity));
                // Hide delete button in add state
                MenuItem menuItem = menu.findItem(R.id.action_delete);
                menuItem.setVisible(false);
            } else {
                actionBar.setTitle(getResources().getString(R.string.edit_activity));
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void paintColor() {
        int color = activityModel.getColor();
        colorView.setColorFilter(color);
        colorView.invalidate();
        iconView.setColorFilter(color);
        iconView.invalidate();
    }

    private void onBack() {
        // If any changes has been made the user is asked if they want to discard them or not.
        if (activityModel.hasChanged()) {
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

    private void onSave() {
        activityModel.save()
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
                        Toast.makeText(context, getString(R.string.activity_saved), Toast.LENGTH_SHORT).show();
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

    private void onDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getString(R.string.q_delete_activity))
                .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activityModel.delete();
                        dialog.dismiss();
                        finish();
                        Toast.makeText(context, getString(R.string.activity_deleted), Toast.LENGTH_SHORT).show();
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
