package com.vinta.utils;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class DateUtil {


    public static String getCron(Date date){
        String formatString = "ss mm HH dd MM ? yyyy";
        return format(formatString, date);
    }

    public static String format(String pattern, Date date){
        SimpleDateFormat dateFormat= new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
}
