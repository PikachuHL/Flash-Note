package cn.hellopika.flashnote.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @date: 2021/6/16
 * @author: pikachu
 * @description: 日期，时间相关工具类
 **/
public class DateTimeUtils {

    /**
     * 获取当前时间的字符串
     * @return
     */
    public static String getNowString(){
        return DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
    }


    public static int getDaysUntilNow(String createTime){
        DateTime now = DateTime.now();
        // 把日期字符串转换为日期类型
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime createDate = DateTime.parse(createTime, dateTimeFormatter);

        return Days.daysBetween(createDate, now).getDays()+1;
    }

}
