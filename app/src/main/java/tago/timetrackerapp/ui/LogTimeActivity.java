package tago.timetrackerapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tago.timetrackerapp.R;
import tago.timetrackerapp.ui.Adapter.TimeLogsAdapter;
import tago.timetrackerapp.ui.managers.DateManager;
import tago.timetrackerapp.ui.managers.LocaleManager;
import tago.timetrackerapp.viewmodels.LogTime;

public class LogTimeActivity extends AppCompatActivity {

    private LogTime model = LogTime.instance;
    private TextView textView;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Changes language to match settings, must be done first
        LocaleManager.setLocale(this);
        // Inflate view
        setContentView(R.layout.activity_log_time);
        // Setup adapter
        RecyclerView recycleView = findViewById(R.id.edit_time_logs);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(llm);
        TimeLogsAdapter timeLogsAdapter = new TimeLogsAdapter(this, model.getTimeLogs(), model.getTotalMilliseconds()) {
            @Override
            public void onSeekBarChanged() {
                // Update remaining time ot split info
                updateUI();
            }
        };
        recycleView.setAdapter(timeLogsAdapter);
        updateUI();
        // Setup action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.add_time));
        }
        // Save button
        FloatingActionButton saveButton = findViewById(R.id.done);
        saveButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onSave();
                return false;
            }
        });
    }

    private void updateUI() {
        if (textView == null)
            textView = findViewById(R.id.info);
        long time = model.getUnspentMilliseconds();
        textView.setText(String.format(getResources().getString(R.string.left_to_spend),
                DateManager.formatTime(time)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Back button pressed
                onBack();
                return true;
        }
        return false;
    }

    private void onSave() {
        int totalProgress = model.totalProgress();
        if (totalProgress == 0) {
            AlertDialog alertDialog= new AlertDialog.Builder(context).create();
            alertDialog.setMessage(getString(R.string.err_no_time_spent));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                    getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else if (totalProgress < model.MAX_PROGRESS) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(getString(R.string.confirm_unfinished_save))
                    .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            save();
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
            save();
        }
    }

    private void save() {
        model.save()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        finish();
    }

    private void onBack() {
        // If any changes has been made the user is asked if they want to discard them or not.
        if (model.hasChanged()) {
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

}
