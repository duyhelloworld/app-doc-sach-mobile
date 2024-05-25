package huce.edu.vn.appdocsach.utils;

import android.annotation.SuppressLint;

import java.time.Duration;
import java.time.LocalDateTime;

public final class DatetimeUtils {

    @SuppressLint("DefaultLocale")
    public static String countTimeCostedUpToNow(LocalDateTime before) {
        if (before == null) {
            return "";
        }
        Duration duration = Duration.between(before, LocalDateTime.now());

        int minutes = (int) duration.toMinutes();
        int hours = (int) duration.toHours();
        int seconds = (int) duration.getSeconds();

        String unit;
        int value;
        if (seconds < 60) {
            value = (seconds);
            unit = "giây";
        } else if (minutes < 60) {
            value = minutes;
            unit = "phút";
        } else if (hours < 24) {
            value = hours;
            unit = "giờ";
        } else {
            value = (hours / 24);
            unit = "ngày";
        }

        return String.format("%d %s trước", value, unit);
    }
}
