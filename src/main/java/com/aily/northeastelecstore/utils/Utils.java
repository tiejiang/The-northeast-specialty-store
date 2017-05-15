package com.aily.northeastelecstore.utils;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 系统时间
 *
 * @author tiejiang
 */
public class Utils {
    public static String primitiveUrl = "http://192.168.10.241/web/php/";
    //获得系统当前时间座位订单号一部分
    public static String obtainCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
