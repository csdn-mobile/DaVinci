package net.csdn.davinci.utils;

public class TimeUtils {

    public static String formatMillisecond(int millisecond) {
        int seconds = millisecond / 1000;
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        String formattedMinutes = String.format("%02d", minutes);
        String formattedSeconds = String.format("%02d", remainingSeconds);

        return formattedMinutes + ":" + formattedSeconds;
    }
}
