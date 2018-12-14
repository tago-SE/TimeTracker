package tago.timetrackerapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tago.timetrackerapp.R;

// Uses open source library for coloring: https://github.com/duanhong169/ColorPicker

public class EditTaskActivity extends AppCompatActivity {

    private static final String TAG = "EditTask";

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);


    }

}
