package tago.timetrackerapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import tago.timetrackerapp.R;
import tago.timetrackerapp.ui.Adapter.TimeLogsAdapter;
import tago.timetrackerapp.viewmodels.LogTime;

public class LogTimeActivity extends AppCompatActivity {

    //private TimeLogsAdapter timeLogsAdapter;
    private LogTime model = LogTime.instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_time);
        System.out.println("MODEL: " + model.getTimeLogs());
        System.out.println(model.getTimeLogs());
        RecyclerView recycleView = findViewById(R.id.edit_time_logs);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(llm);
        recycleView.setAdapter(new TimeLogsAdapter(this, model.getTimeLogs()));
    }


}
