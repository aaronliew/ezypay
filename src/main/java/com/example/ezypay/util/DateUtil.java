package com.example.ezypay.util;

import com.example.ezypay.exception.InvalidArgumentException;
import com.example.ezypay.exception.InvalidDateRangeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    public static String KL_TIME_ZONE = "Asia/Kuala_Lumpur";
    public static String STANDARD_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static long BUSINESS_MONTH = 30;
    private static final SimpleDateFormat standardDateFormat = new SimpleDateFormat(STANDARD_DATE_FORMAT);
    private static final TimeZone malaysianTimeZone = TimeZone.getTimeZone(KL_TIME_ZONE);

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);



    public static String convertToStandardDateStringFormat(long timestamp) {
        standardDateFormat.setTimeZone(malaysianTimeZone);
        Date dateObj = new Date(timestamp);
        return standardDateFormat.format(dateObj.getTime());
    }

    public static long calculateDurationBetweenDates(String from, String to) {
        try {
            Date fromDate = dateFormat.parse(from);
            Date toDate = dateFormat.parse(to);
            if (fromDate.getTime() > toDate.getTime()) {
                throw new InvalidDateRangeException();
            }

            long diffInMillies = Math.abs(toDate.getTime() - fromDate.getTime());

            return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        } catch (ParseException e){
            throw  new InvalidArgumentException();
        }
    }
}
