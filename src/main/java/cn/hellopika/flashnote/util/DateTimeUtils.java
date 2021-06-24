package cn.hellopika.flashnote.util;

import org.joda.time.DateTime;

/**
 * @date: 2021/6/16
 * @author: pikachu
 * @description: 日期，时间相关工具类
 **/
public class DateTimeUtils {

    public static String getNowString(){
        return DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
    }

}
