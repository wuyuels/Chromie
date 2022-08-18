package org.chromie.util;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

/**
 * 时间工具
 * @author liushuai7
 */
public class DateUtil {

    private static final String DATE_F = "yyMMdd";
    public static long  getTodayRelativeSeconds(){
        try {
            String date = DateFormatUtils.format(new Date(), DATE_F);
            long dateStart = DateUtils.parseDate(date, new String[]{DATE_F}).getTime();
            return (System.currentTimeMillis()- dateStart)/1000;
        } catch (Exception e) {}
        return 0;
    }

    public static String getToday() {
        return DateFormatUtils.format(new Date(), DATE_F);
    }
}
