package tago.timetrackerapp.viewmodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tago.timetrackerapp.repo.db.TimeLogDBHelper;
import tago.timetrackerapp.repo.entities.TimeLog;

public class Statistics {

    public static final Statistics instance = new Statistics();

    private final String patternFormat ="yyyy-MM-dd";;

    private List<TimeLog> dataSet;
    private String label;

    private Option option;

    private String startDate;
    private String endDate;
    private Date currentDate;

    private boolean canMoveLeft = true; // Currently no limit to left movement
    private boolean canMoveRight;

    private enum Option {
        Day, Week, Month, Year;
    }

    private Statistics() {
        toggleDay();
    }

    public String getLabel() {
        return label;
    }

    public String currentToggle() {
        return option.toString();
    }

    public boolean canMoveLeft() {
        return canMoveLeft;
    }

    public boolean canMoveRight() {
        return canMoveRight;
    }

    public List<TimeLog> load() {
        TimeLogDBHelper helper = TimeLogDBHelper.getInstance();
        dataSet = helper.getSumRange(startDate, endDate);
        if (dataSet == null)
            dataSet = new ArrayList<>();
        return dataSet;
    }

    public void toggleDay() {
        option = Option.Day;
        canMoveLeft = true;
        canMoveRight = false;
        setDayCurrentDate(new Date());
        System.out.println("start: " + startDate);
        System.out.println("end: " + endDate);
    }

    public void toggleWeek() {
        option = Option.Week;
        canMoveLeft = false;
        canMoveRight = false;
        Calendar c = Calendar.getInstance();
        clearCalendarTime(c);
        currentDate = c.getTime();
        SimpleDateFormat  simpleDateFormat = new SimpleDateFormat(patternFormat);
        startDate = simpleDateFormat.format(currentDate);
        endDate = simpleDateFormat.format(getDateSince(currentDate, 7));
        label = "" + c.get(Calendar.WEEK_OF_YEAR) + " " + Calendar.getInstance().get(Calendar.YEAR);
        System.out.println("start: " + startDate);
        System.out.println("end: " + endDate);
    }

    public void toggleMonth() {
        option = Option.Month;
        canMoveLeft = false;
        canMoveRight = false;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        clearCalendarTime(c);
        currentDate = c.getTime();
        SimpleDateFormat  simpleDateFormat = new SimpleDateFormat(patternFormat);
        startDate = simpleDateFormat.format(currentDate);
        int daysOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        endDate = simpleDateFormat.format(getDateSince(currentDate, daysOfMonth));
        label = "" + c.get(Calendar.MONTH) + " " + Calendar.getInstance().get(Calendar.YEAR);
        System.out.println("start: " + startDate);
        System.out.println("end: " + endDate);
    }

    public void toggleYear() {
        option = Option.Year;
        canMoveLeft = false;
        canMoveRight = false;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        clearCalendarTime(c);
        currentDate = c.getTime();
        SimpleDateFormat  simpleDateFormat = new SimpleDateFormat(patternFormat);
        startDate = simpleDateFormat.format(currentDate);
        int daysOfYear = c.getActualMaximum(Calendar.DAY_OF_YEAR);
        endDate = simpleDateFormat.format(getDateSince(currentDate, daysOfYear));
        label = "" + Calendar.getInstance().get(Calendar.YEAR);
        System.out.println("start: " + startDate);
        System.out.println("end: " + endDate);
    }

    private void clearCalendarTime(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }

    public void moveLeft() {
        switch (option) {
            case Day:
                setDayCurrentDate(getDateSince(currentDate, -1));
                return;
            case Week:

                return;
        }
    }

    public void moveRight() {
        switch (option) {
            case Day:
                setDayCurrentDate(getDateSince(currentDate, 1));
                return;
            case Week:

                return;
        }
    }

    private Date getDateSince(Date date, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, amount);
        return c.getTime();
    }

    private void setDayCurrentDate(Date currentDate) {
        SimpleDateFormat  simpleDateFormat = new SimpleDateFormat(patternFormat);
        this.currentDate = currentDate;
        startDate = simpleDateFormat.format(currentDate);
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 1);
        Date tomorrowDate = c.getTime();
        endDate = simpleDateFormat.format(tomorrowDate);
        canMoveRight = !isCurrentDay(currentDate, new Date());
        label = startDate;
    }

    private void setWeekCurrentDate(Date currentDate) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(currentDate);
        int today = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DAY_OF_WEEK, -today+Calendar.MONDAY);
        System.out.println("Date:" + c.getTime());


        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        this.currentDate = c.getTime();


        SimpleDateFormat  simpleDateFormat = new SimpleDateFormat(patternFormat);
        startDate = simpleDateFormat.format(currentDate);
        endDate = simpleDateFormat.format(getDateSince(currentDate, 7));
        label = "" + c.get(Calendar.WEEK_OF_YEAR) + " " + Calendar.getInstance().get(Calendar.YEAR);
        System.out.println("START:       " + startDate );
        System.out.println("END:       " + endDate);
        System.out.println("currDate: " + currentDate);
    }

    private boolean isCurrentWeek(Date d1, Date d2) {
        return false;
    }

    private boolean isCurrentDay(Date d1, Date d2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

}
