package com.lin.missyou.util;

import com.lin.missyou.bo.PageCounter;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class CommonUtil {

    public static PageCounter convertToPageParameter(Integer start, Integer count){
        Integer pageNumber = start/count;
        return PageCounter.builder()
                .page(pageNumber)
                .count(count)
                .build();
    }

    public static Boolean isInTimeLine(Date now, Date start, Date end){
        Long time = now.getTime();
        Long startTime = start.getTime();
        Long endTime = end.getTime();
        return time >= startTime && time <= endTime;
    }

    public static Calendar addSomeSeconds(Calendar calendar, int seconds){
        calendar.add(Calendar.SECOND, seconds);
        return calendar;
    }

    public static Boolean isOutOfDate(Date startTime, Long period){
        Long now = Calendar.getInstance().getTimeInMillis();
        Long startTimeStamp = startTime.getTime();
        Long periodMillionSecond = period*1000;
        return now > (startTimeStamp+periodMillionSecond);
    }

    public static Boolean isOutOfDate(Date expiredTime){
        Long now = Calendar.getInstance().getTimeInMillis();
        Long expiredTimeStamp = expiredTime.getTime();
        return now > expiredTimeStamp;
    }

    private static String toPlain(BigDecimal p){
        return p.stripTrailingZeros().toString();
    }

    public static String yuanToFenPlainString(BigDecimal p){
        return CommonUtil.toPlain(p.multiply(new BigDecimal("100")));
    }

    public static String timestamp10(){
        long timestamp13 = Calendar.getInstance().getTimeInMillis();
        String timestamp13Str = Long.toString(timestamp13);
        return timestamp13Str.substring(0, timestamp13Str.length() - 3);
    }
}
