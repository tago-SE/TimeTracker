package tago.timetrackerapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import tago.timetrackerapp.R;
import tago.timetrackerapp.ui.util.Colorizer;
import tago.timetrackerapp.ui.util.TextChangedListener;
import tago.timetrackerapp.ui.viewmodel.EditCategoryVM;
import top.defaults.colorpicker.ColorPickerPopup;

public class EditCategoryActivity extends AppCompatActivity {

    private final static String TAG = "EditCategory";

    public static final String STATE = "state";
    public static final String STATE_EDIT = "edit";
    public static final String STATE_ADD = "add";

    private final Context context = this;

    private String state;

    private int selectedColor;



    /* @TODO Fix WindowLeak related to Color Picker */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        state =  getIntent().getExtras().getString(STATE);
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
        final EditCategoryVM viewModel = ViewModelProviders.of(this).get(EditCategoryVM.class);

        // Add ColorPicker
        final ImageView colorView = findViewById(R.id.categoryColor);

        colorView.setOnClickListener(new View.OnClickListener() {
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
                                viewModel.setColor(color);
                                Log.d(TAG, "color picked: " + color);
                            }

                            @Override
                            public void onColor(int color, boolean fromUser) { }
                        });
            }
        });
        // Update color
        viewModel.colorLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer color) {
                colorView.setColorFilter(color);
            }
        });
        // Start with random color
        viewModel.setColor(Colorizer.getRandomColor());

        EditText text = findViewById(R.id.categoryNameField);
        text.addTextChangedListener(new TextChangedListener<EditText>(text) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                viewModel.setName(s.toString());
            }
        });

        // Save response
        viewModel.saveLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer response) {
                switch (response) {
                    case EditCategoryVM.SAVE_NO_NAME:
                        // You need to set a name before saving.
                        return;
                    case EditCategoryVM.SAVE_NO_COLOR:

                        return;
                    default:
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu: adds the icons at the acton bar, if its present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_delete);
        menuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Back button pressed
                onBack();
                return true;
            case R.id.action_save:
                Log.d(TAG, "action:save");
                return true;
            case R.id.action_delete:
                Log.d(TAG, "action:delete");
                return true;
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void onBack() {
        finish();
    }

    private void onSave() {

    }

    private void onDelete() {

    }
}
