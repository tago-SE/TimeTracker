package tago.timetrackerapp.viewmodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import tago.timetrackerapp.repo.db.TimeLogDBHelper;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.repo.entities.TimeLog;

// Rename to EditTime?
public class LogTime {

    public static final LogTime instance = new LogTime();

    private Date localDate;

    private List<TimeLog> timeLogs = new ArrayList<>();
    private long totalMilliseconds;
    public static final int MAX_PROGRESS = 100;
    public static final int SAVE_OK = 3;

    private LogTime() {
        // Required empty constructor
    }

    public void start(List<Activity> activities, Date startDate, long timeMilliseconds) {
        timeLogs.clear();
        for (Activity activity : activities) {
            timeLogs.add(new TimeLog(activity));
        }
        this.totalMilliseconds = timeMilliseconds;
        this.localDate = startDate;
    }

    public List<TimeLog> getTimeLogs() {
        return timeLogs;
    }

    public long getTotalMilliseconds() {
        return totalMilliseconds;
    }

    public long getUnspentMilliseconds() {
        int totalProgress = 0;
        for (TimeLog timeLog : timeLogs)
            totalProgress += timeLog.progress;
        double factor = 1.0 - ((double) totalProgress/MAX_PROGRESS);
        return (long) (factor*totalMilliseconds);
    }

    public Observable<Integer> save() {
        return Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                //return Observable.error(new Exception("" + ERR_PARTIAL_SAVE));

                for (TimeLog timeLog : timeLogs) {
                    if (timeLog.progress > 0) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        timeLog.start = dateFormat.format(localDate);
                        localDate.setTime(localDate.getTime() + timeLog.milliseconds);
                        timeLog.stop = dateFormat.format(localDate);
                        TimeLogDBHelper.getInstance().insert(timeLog);
                    }
                }
                return Observable.just(SAVE_OK);
            }
        });
    }

    public boolean hasChanged() {
        return totalProgress() != 0;
    }

    public int totalProgress() {
        int totalProgress = 0;
        for (TimeLog timeLog : timeLogs)
            totalProgress += timeLog.progress;
        return totalProgress;
    }

}
