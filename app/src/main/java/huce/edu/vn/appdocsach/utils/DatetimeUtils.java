package huce.edu.vn.appdocsach.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public final class DatetimeUtils {

    public static String betterFormatFromNow(LocalDateTime before) {
        Duration duration = Duration.between(before, LocalDateTime.now());
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        StringBuilder result = new StringBuilder();
        if (hours > 0) {
            result.append(hours).append(" giờ ");
        }
        if (minutes > 0) {
            result.append(minutes).append(" phút ");
        }
        return result.toString();
    }
}
