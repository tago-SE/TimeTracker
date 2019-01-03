package tago.timetrackerapp.ui.managers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManager {

    // Default date format for timestamps
    public static final SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getTimeBetweenDates(Date startDate, Date endDate) {

        long time = endDate.getTime() - startDate.getTime();

        long secondssInMilli = 1000;
        long minutesInMilli = secondssInMilli*60;
        long hoursInMilli = minutesInMilli*60;
        long daysInMilli = hoursInMilli*24;

        long elapsedDays = time/daysInMilli;
        time = time% daysInMilli;

        long elapsedHours = time/hoursInMilli;
        time = time % hoursInMilli;

        long elapsedMinutes = time/minutesInMilli;
        time = time%minutesInMilli;

        StringBuilder sb = new StringBuilder();
        if (elapsedDays > 0) {
            sb.append(elapsedDays + "days ");
        }
        if (elapsedHours > 0 || elapsedDays > 0) {
            sb.append(elapsedHours + "h ");
        }
        if (elapsedHours > 0 || elapsedDays > 0 || elapsedMinutes > 0) {
            sb.append(elapsedMinutes + "min ");
        }
        return sb.toString();
    }
}
