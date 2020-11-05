package com.lin.missyou.util;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Calendar;

public class OrderUtil {

    private static String[] yearCodes;

    // 将配置文件的值赋给静态变量
    @Value("${missyou.year-codes}")
    public void setYearCodes(String yearCodes){
        OrderUtil.yearCodes = yearCodes.split(",");
    }

    public static String makeOrderNo(){
        StringBuffer joiner = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        String mills = String.valueOf(calendar.getTimeInMillis());
        String micro = LocalDateTime.now().toString();
        String random = String.valueOf(Math.random()*1000).substring(0,2);
        joiner.append(OrderUtil.yearCodes[calendar.get(Calendar.YEAR) - 2019])
                .append(Integer.toHexString(calendar.get(Calendar.MONTH)+1).toUpperCase())
                .append(calendar.get(Calendar.DAY_OF_MONTH))
                .append(mills.substring(mills.length()-5, mills.length()))
                .append(micro.substring(micro.length()-3, micro.length()))
                .append(random);
        return joiner.toString();
    }
}
