package tago.timetrackerapp.ui.managers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateManager {

    private static final String fixedFormat = "yyyy-MM-dd HH:mm:ss";

    public static String formatTime(long time) {
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
            sb.append(elapsedDays + " d");
        }
        if (elapsedHours > 0 || elapsedDays > 0) {
            if (elapsedDays > 0)
                sb.append(" ");
            sb.append(elapsedHours + " h");
        }
        if (elapsedDays > 0 || elapsedHours > 0)
            sb.append(" ");
        sb.append(elapsedMinutes + " min");
        return sb.toString();
    }

    public static long millisecondsBetweenDates(Date d1, Date d2) {
        return d2.getTime() - d1.getTime();
    }

    public static long millisecondsBetweenDates(String date1, String date2, String dateFormat) {
        if (date1 == null || date2 == null)
            return 0;
        try {
            Date d1 = new SimpleDateFormat(fixedFormat).parse(date1);
            Date d2 = new SimpleDateFormat(fixedFormat).parse(date2);
            return millisecondsBetweenDates(d1, d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String reformatDate(String date, String prevformat, String newFormat) {
        if (date == null || prevformat == null || newFormat == null)
            return "";
        SimpleDateFormat simpleDateFormatPrev = new SimpleDateFormat(prevformat);
        SimpleDateFormat simpleDateFormatNew = new SimpleDateFormat(newFormat);
        try {
            return simpleDateFormatNew.format(simpleDateFormatPrev.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(fixedFormat);
        return dateFormat.format(date);
    }

    /**
     * Reformats a date to format (e.g. yyyy-MM-dd) and 'Today' or 'Yesterday' if the provided d
     * dates are any of those.
     * @param date
     * @return
     */
    public static String reformatSimpleDate(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static Date stringToDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int numDaysBetweenDates(String date1, String date2) {
        long milliseconds = 0; millisecondsBetweenDates(
                stringToDate(date1, fixedFormat), stringToDate(date2, fixedFormat));
        return (int) TimeUnit.MILLISECONDS.toDays(milliseconds);
    }
}
