package com.li.blog.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author li
 * @version 1.0
 * @since 18-9-17 上午10:02
 **/
public class DateUtil {
    public static Date getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        return calendar.getTime();
    }
}
