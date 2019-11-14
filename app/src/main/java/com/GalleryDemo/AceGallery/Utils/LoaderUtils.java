package com.GalleryDemo.AceGallery.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class LoaderUtils {

    public static String stringForTime(long time) {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder, Locale.getDefault());//Locale.getDefault 获取系统当前语言

        long seconds = time/1000%60;
        long minutes = time/1000/60%60;
        long hours = time/1000/60/60;

        stringBuilder.setLength(0);
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static String time2Date (long time) {
        Date date = time > 1000000000000L ? new Date(time) : new Date(time * 1000);
        String result = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return result;
    }

    public static boolean isSameDay (long time1, long time2) {
        Date date1 = time1 > 1000000000000L ? new Date(time1) : new Date(time1 * 1000);
        Date date2 = time2 > 1000000000000L ? new Date(time2) : new Date(time2 * 1000);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

    }
}
