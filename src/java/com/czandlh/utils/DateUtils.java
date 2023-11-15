package com.czandlh.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public static Date addMonth() {
        try {
            Date date = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(calendar.MONTH, 1);//把日期往后增加一天.整数往后推,负数往前移动//这个时间就是日期往后推一天的结果
            return calendar.getTime();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String dateTimeToOrder(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String dateToCourseTime(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(date);
    }

    public static String dateToTime(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String dateTimeToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String timeToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static Date strToEndDate(String str) {
        try{
            str = str + " 23:59:59";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.parse(str);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String longToDateStr(long l){
      DateTimeFormatter df= DateTimeFormatter.ofPattern("YYYY-MM-dd");
      String dataStr = df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(l * 1000), ZoneId.of("Asia/Shanghai")));
      return dataStr;
    }

    public static Date strToStartDate(String str){
        try{
            str = str + " 00:00:00";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.parse(str);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

  public static Date strToDate(String str){
    try{
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return format.parse(str);
    }catch (Exception e){
      throw new RuntimeException(e);
    }
  }

    public static Long duration(Date date){
        long start = date.getTime();
        long end = System.currentTimeMillis();
        long duration = (end - start) / 60 * 1000;
        return duration;
    }

  public static void main(String[] args) {
//    longToDateStr();
  }
}
