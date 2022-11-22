package cn.hyperchain.sealpdf;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by superlee on 2017/11/16.
 * 日期格式化
 */
public final class DateUtil {

    public final static String Format_Year_To_Day = "yyyy-MM-dd";
    public final static String Format_Year_To_Second = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_ONE = "yyyyMMddHHmmss";

    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    public static String dateToString(Date data) {
        return new SimpleDateFormat(Format_Year_To_Second).format(data);
    }

    // long类型转换为String类型
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType) {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }
    public static String longToString(long currentTime) {
        return longToString(currentTime,Format_Year_To_Second);
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 根据日期得到年(后两位)
     *
     * @param date 日期
     */
    public static String getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        return year.substring(year.length() - 2);
    }

    /**
     * 获取日期的月份（自动补零）
     *
     * @param date 日期
     * @return 返回月数
     */
    public static String getMoth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.format("%02d", cal.get(Calendar.MONTH) + 1);
    }

    /**
     * 获取日期的日期（自动补零）
     *
     * @param date 日期
     * @return 返回月数
     */
    public static String getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.format("%02d", cal.get(Calendar.DATE) + 1);
    }

    /**
     * 比较两个时间戳的日期是否相同
     *
     * @param stamp1
     * @param stamp2
     * @return
     */
    public static boolean dateEquals(long stamp1, long stamp2) {
        Date date1 = longToDate(stamp1, Format_Year_To_Second);
        Date date2 = longToDate(stamp2, Format_Year_To_Second);
        return getDate(date1).equals(getDate(date2));
    }

    /**
     * 判断两个时间是否相差指定分钟
     *
     * @param _time1 时间戳1
     * @param _time2 时间戳2
     * @param minute 指定分钟
     * @return 超过指定分钟:false;未超过:true
     */
    public static boolean isTwoTimeDeltaMinute(long _time1, long _time2, int minute) {
        long deltaSecond = Math.abs(_time1 - _time2);
        long deltaMinute = deltaSecond / (1000 * 60);
        int curMinute = new Long(deltaMinute).intValue();
        return curMinute < minute;
    }

    /**
     * 获取最近天数的时间点列表
     *
     * @param day
     * @return
     */
    public static List<Long> getLastDayPoint(int day) {
        List<Long> result = new ArrayList<>();

        long current = System.currentTimeMillis();
        result.add(current);
        // 获取今日凌晨时间戳
        long time = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        result.add(time);
        for (int i = 1; i < day; ++i) {
            time = time - (1000 * 3600 * 24);
            result.add(time);
        }
        Collections.reverse(result);
//        return Lists.reverse(result);
        return result;
    }

    /**
     * 获取最近天数的时间点列表
     *
     * @param start
     * @param end
     * @return
     */
    public static List<Long> getDayPointBetween(Long start, Long end) {
        List<Long> result = new ArrayList<>();

        long current = start;
        result.add(current);
        // 获取今日凌晨时间戳
        long time = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        while (time < end) {
            time = time + (1000 * 3600 * 24);
            result.add(time);
        }
        return result;
    }

    /**
     * 获取今天至今的时间点（每小时）
     *
     * @return
     */
    public static List<Long> getTodayHourPoint() {
        long current = System.currentTimeMillis();
        // 获取今日凌晨时间戳
        long time = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        List<Long> result = new LinkedList<>();
        int num = 24;
        while ((num--) > 0) {
            result.add(time);
            time = time + (1000 * 3600);
        }
        result.add(current);
        return result;
    }

    /**
     * 获取昨日时间点（每小时）
     *
     * @return
     */
    public static List<Long> getYestdayHourPoint() {
        long current = System.currentTimeMillis();
        // 获取今日凌晨时间戳
        long today = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
        long yesterday = today - (1000 * 3600 * 24);
        List<Long> result = new LinkedList<>();
        int num = 25;
        while ((num--) > 0) {
            result.add(yesterday);
            yesterday = yesterday + (1000 * 3600);
        }
//        result.add(yesterday - 1000);
//        result.add(yesterday - 1000);
        return result;
    }

    public static Date addDays(final Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);

        return new Date(cal.getTime().getTime());
    }

    public static Date addHours(final Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hours);

        return new Date(cal.getTime().getTime());
    }

    /**
     * 获得两个日期之间的秒数 向下取整
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public static int secondsBetweenDatesFloor(Date dateStart, Date dateEnd) {
        long millsBetweenDates = millsBetweenDates(dateStart, dateEnd);
        return (int) (millsBetweenDates / 1000);
    }

    /**
     * 计算两个日期之前的毫秒数
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public static long millsBetweenDates(Date dateStart, Date dateEnd) {
        if (dateStart == null || dateEnd == null) {
            return 0;
        }

        long dateStartTime = dateStart.getTime();
        long dateEndTime = dateEnd.getTime();
        long millsBetweenDates = dateEndTime - dateStartTime;
        millsBetweenDates = millsBetweenDates > 0 ? millsBetweenDates : 0;
        return millsBetweenDates;
    }

    /**
     * 加分钟
     *
     * @param date
     * @param mins
     * @return
     */
    public static Date addMins(final Date date, int mins) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, mins);

        return new Date(cal.getTime().getTime());
    }

    /**
     * 日期之间的分钟数 向下取整
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public static int minutesBetweenDatesFloor(Date dateStart, Date dateEnd) {
        long millsBetweenDates = millsBetweenDates(dateStart, dateEnd);
        return new BigDecimal(millsBetweenDates).divide(new BigDecimal(1000 * 60), 0, RoundingMode.DOWN).intValue();
    }

    /**
     * 日期之间的分钟数 向下取整
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public static int minutesBetweenDatesCeil(Date dateStart, Date dateEnd) {
        long millsBetweenDates = millsBetweenDates(dateStart, dateEnd);
        return new BigDecimal(millsBetweenDates).divide(new BigDecimal(1000 * 60), 0, RoundingMode.CEILING).intValue();
    }

    /**
     * 参数允许为null,如果是null则返回null
     *
     * @param strTime
     * @return
     */
    public static Date stringToDateOrNull(String strTime) {
        if (strTime != null) {
            return stringToDate(strTime, Format_Year_To_Second);
        }
        return null;
    }

    /**
     * 获取历史时间获取
     * 共有三个参数,startTime为空的时候，用createTime补上，有endTime则是两段式的时间间隔，否则则是只有startTime格式化后的时间点
     *
     * @return yyyy-MM-dd HH:mm:ss格式的字符串
     */
    public static String buildForTime(Date createTime, Date startTime, Date endTime) {
        if (startTime == null) {
            startTime = createTime;
        }
        return buildForTime(startTime, endTime);
    }

    /**
     * 获取实际取证时间点或时间段的字符串
     *
     * @return yyyy-MM-dd HH:mm:ss格式的字符串
     */
    public static String buildForTime(Date startTime, Date endTime) {
        String startTimeFormat = "{0,date,yyyy-MM-dd HH:mm:ss}";
        String allTimeFormat = "{0,date,yyyy-MM-dd HH:mm:ss} ~ {1,date,yyyy-MM-dd HH:mm:ss}";
        if (startTime == null) {
            return "";
        }
        String forTime = MessageFormat.format(startTimeFormat, startTime);
        if (endTime != null) {
            forTime = MessageFormat.format(allTimeFormat, startTime, endTime);
        }

        return forTime;
    }

}
