package tago.timetrackerapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import tago.timetrackerapp.R;

public class CategoriesActivity extends AppCompatActivity {

    private final Context contex = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.add_category);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contex, EditCategoryActivity.class);
                intent.putExtra(EditCategoryActivity.STATE, EditCategoryActivity.STATE_ADD);
                startActivity(intent);
            }
        });
    }

}
