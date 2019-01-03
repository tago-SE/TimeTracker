package tago.timetrackerapp.ui.managers;

public class DateManager {

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
}
