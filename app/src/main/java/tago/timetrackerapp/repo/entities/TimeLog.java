package tago.timetrackerapp.repo.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class TimeLog {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "start")
    public String start;

    @ColumnInfo(name = "stop")
    public String stop;

    @Ignore
    private Activity activity;

    @ColumnInfo(name = "activity_id")
    public long activityId;

    @ColumnInfo(name = "time")
    public long milliseconds;

    @ColumnInfo(name = "c_export")
    public boolean calendarExported;

    @Ignore
    public int progress;


    public TimeLog() {
        // Required empty constructor
    }

    public TimeLog(Activity activity) {
        setActivity(activity);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        if (activity == null)
            activityId = 0;
        else
            this.activityId = activity.id;
    }

    public Activity getActivity() {
        return activity;
    }

    @Override
    public String toString() {
        return "TimeLog{" +
                "id=" + id +
                ", start='" + start + '\'' +
                ", stop='" + stop + '\'' +
                ", activity=" + activity +
                ", activityId=" + activityId +
                ", milliseconds=" + milliseconds +
                ", progress=" + progress +
                '}';
    }
}
