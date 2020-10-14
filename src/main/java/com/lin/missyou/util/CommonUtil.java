package com.lin.missyou.util;

import com.lin.missyou.bo.PageCounter;

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
        if(time >= startTime && time <= endTime){
            return true;
        }
        return false;
    }
}
