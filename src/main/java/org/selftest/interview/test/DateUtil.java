/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.selftest.interview.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author wangweiheng
 * @date 2018-08-17
 */
public class DateUtil {
    /**
     * 日期格式，年月日，例如：20181001 20180102
     */
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

    /**
     * 获取YYYY格式
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 获取YYYY格式
     */
    public static String getYear(Date date) {
        return formatDate(date, "yyyy");
    }

    /**
     * 获取YYYY-MM-DD格式
     */
    public static String getDay() {
        return formatDate(new Date(), "yyyy-MM-dd");
    }

    /**
     * 获取YYYY-MM-DD格式
     */
    public static String getDay(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
     * 获取YYYY-MM格式
     */
    public static String getMonth() {
        return formatDate(new Date(), "yyyy-MM");
    }

    /**
     * 获取YYYY-MM格式
     */
    public static String getMonth(Date date) {
        return formatDate(date, "yyyy-MM");
    }

    /**
     * 获取YYYYMMDD格式
     */
    public static String getDays() {
        return formatDate(new Date(), "yyyyMMdd");
    }

    /**
     * 获取YYYYMMDD格式
     */
    public static String getDays(Date date) {
        return formatDate(date, "yyyyMMdd");
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     */
    public static String getTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss.SSS格式
     */
    public static String getMsTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * 获取YYYYMMDDHHmmss格式
     */
    public static String getAllTime() {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     */
    public static String getTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDate(Date date, String pattern) {
        String formatDate = null;
        if (StringUtils.isNotBlank(pattern)) {
            formatDate = DateFormatUtils.format(date, pattern);
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 日期比较，如果s>=e 返回true 否则返回false)
     *
     * @author luguosui
     */
    public static boolean compareDate(String s, String e) {
        if (parseDate(s) == null || parseDate(e) == null) {
            return false;
        }
        return parseDate(s).getTime() >= parseDate(e).getTime();
    }

    /**
     * 格式化日期
     */
    public static Date parseDate(String date) {
        return parse(date, "yyyy-MM-dd");
    }

    /**
     * 格式化日期
     */
    public static Date parseTimeMinutes(String date) {
        return parse(date, "yyyy-MM-dd HH:mm");
    }

    /**
     * 格式化日期
     */
    public static Date parseTime(String date) {
        return parse(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期
     */
    public static Date parse(String date, String pattern) {
        try {
            return DateUtils.parseDate(date, pattern);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期
     */
    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 把日期转换为Timestamp
     */
    public static Timestamp format(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 校验日期是否合法
     */
    public static boolean isValidDate(String s) {
        return parse(s, "yyyy-MM-dd HH:mm:ss") != null;
    }

    /**
     * 校验日期是否合法
     */
    public static boolean isValidDate(String s, String pattern) {
        return parse(s, pattern) != null;
    }

    public static int getDiffYear(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(
                    startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * <li>功能描述：时间相减得到天数
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;

        try {
            beginDate = format.parse(beginDateStr);
            endDate = format.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        // System.out.println("相隔的天数="+day);

        return day;
    }

    /**
     * 得到n天之后的日期
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n天之后的日期
     */
    public static Date getAfterDay(Integer days) {

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        return date;
    }

    /**
     * 得到n天之后是周几
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }

    /**
     * 获取当前时间秒
     *
     * @return
     */
    public static Long getCurrentTimeSecond() {
        Date date = new Date();
        return date.getTime() / 1000;

    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Long getCurrentTime() {
        Date date = new Date();
        return date.getTime();

    }
    public static Date getCurrentDate() {
        Date date = new Date();
        return date;

    }

    /**
     * Description:计算所在月的第一天
     *
     * @return
     */
    public static Date getFirstDateOfMonth() {
        Calendar c1 = Calendar.getInstance(Locale.CHINA);
        c1.setTime(new Date());
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.DAY_OF_MONTH, 1);
        return c1.getTime();
    }

    /**
     * Description:计算某月的第一天
     *
     * @return
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar c1 = Calendar.getInstance(Locale.CHINA);
        c1.setTime(date);
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.DAY_OF_MONTH, 1);
        return c1.getTime();
    }

    /**
     * Description:获得某月的最后一天
     *
     * @return
     */
    public static Date getLastDayOfMonth() {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * Description:获得某月的最后一天
     *
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 获取N天前日期或N天后日期（但为N天前时N为负数）
     *
     * @param day
     * @return
     */
    public static Date getbBforDayOfDate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 获取某日期N天前日期或N天后日期（但为N天前时N为负数）
     *
     * @param day ，date
     * @return
     */
    public static String getBeforDayOfDate(int day, String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(date));
        calendar.add(Calendar.DATE, day);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取两个日期相差几个月
     */
    public static long getMonthSub(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    public static String getLastDayOfMonth(String date, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.parse(date, pattern));
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    /**
     * 功能描述:
     *
     * @param: 获取时间yyyy-MM-dd
     * @return:
     * @auther: Jifei•Xia
     * @date: 2018/11/16 3:03 PM
     */
    public static Date getTimeyyyyMMdd(Date date) {
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdfd.format(date);
        return parse(dateStr, "yyyy-MM-dd");

    }

    /**
     * 功能描述:
     *
     * @param: 获取当前时间yyyy-MM-dd
     * @return:
     * @auther: Jifei•Xia
     * @date: 2018/11/2 3:03 PM
     */
    public static Date getNowTimeyyyyMMdd() {
        Date now = new Date();
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdfd.format(now);
        return parse(dateStr, "yyyy-MM-dd");

    }


    /**
     * 功能描述: 获取上一年的日期yyyy-MM-dd 日
     *
     * @param:
     * @return:
     * @auther: Jifei•Xia
     * @date: 2018/11/16 11:37 AM
     */
    public static Date getLastYearDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -1);
        Date y = c.getTime();
        String lastYearDate = format.format(y);
        return parse(lastYearDate, "yyyy-MM-dd");
    }

    /**
     * 功能描述: 获取上一年的月yyyy-MM 月
     *
     * @param:Date date
     * @return:
     * @auther: Jifei•Xia
     * @date: 2018/11/16 11:37 AM
     */
    public static Date getLastYearMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -1);
        Date y = c.getTime();
        String lastYearDate = format.format(y);
        return parse(lastYearDate, "yyyy-MM");
    }

    /**
     * 功能描述:
     *
     * @param:
     * @return:
     * @auther: Jifei•Xia
     * @date: 2018/11/16 2:35 PM
     */
    public static Date getLastYear(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -1);
        Date y = c.getTime();
        String lastYear = format.format(y);
        return parse(lastYear, "yyyy");
    }

    /**
     * 功能描述: 获取上一年的日期yyyy-MM-dd 周 flag: START：开始  END：结束
     *
     * @param:
     * @return:
     * @auther: Jifei•Xia
     * @date: 2018/11/16 11:37 AM
     */
    public static Date getLastYearWeek(Date date, String flag) {

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);//周日开始
        calendar.setTime(date);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1); // 2016年
        cal.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR)); // 设置为2016年的第10周
        if ("START".equals(flag)) {//
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        } else {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

        }
        Date weekDay = getTimeyyyyMMdd(cal.getTime());
        return weekDay;
    }

    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }

    public static Date getUserDefined(Date start, Date end, String flag) {
        int dayNum = differentDays(start, end);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        if ("START".equals(flag)) {
            c.setTime(start);
        } else {
            c.setTime(end);
        }
        c.add(Calendar.DATE, -dayNum);
        Date y = c.getTime();
        String lastMonthDate = format.format(y);
        return parse(lastMonthDate, "yyyy-MM-dd");
    }

    /**
     * 功能描述: 获取前一日
     *
     * @param:
     * @return:
     * @auther: Jifei•Xia
     * @date: 2018/11/16 12:01 PM
     */
    public static Date getYesterday(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -1);
        return c.getTime();
    }

    /**
     * 功能描述: 获取上一个月
     *
     * @param:
     * @return:
     * @auther: Jifei•Xia
     * @date: 2018/11/16 12:05 PM
     */
    public static Date getLastMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        Date y = c.getTime();
        String lastMonth = format.format(y);
        return parse(lastMonth, "yyyy-MM");
    }

    /**
     * 功能描述: 获取上周日期
     *
     * @param:
     * @return:
     * @auther: Jifei•Xia
     * @date: 2018/11/16 2:40 PM
     */
    public static Date getLastWeek(Date date, String flag) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);//周日开始
        calendar.setTime(date);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        cal.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - 1);
        if ("START".equals(flag)) {//
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        } else {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

        }
        Date weekDay = getTimeyyyyMMdd(cal.getTime());
        return weekDay;
    }


    /**
     * 指定日期多少天后 的日期
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getDateAfterDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 指定日期多少天后 的日期
     *
     * @param date
     * @param days
     * @return
     */
    public static String getDateAfterDays(String date, int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        calendar.add(Calendar.DATE, days);
        return sdf.format(calendar.getTime());
    }


    public final static class RangeDate {
        private Date startDate;
        private Date endDate;

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public String getStartDateFormatDay() {
            if (startDate == null) {
                return null;
            }
            return getDay(startDate);
        }

        public String getEndDateFormatDay() {
            if (endDate == null) {
                return null;
            }
            return getDay(endDate);
        }

        @Override
        public String toString() {
            return "RingDate{" +
                    "startDate=" + startDate +
                    ", endDate=" + endDate +
                    '}';
        }
    }


    /**
     * 获取N个月后的日期
     *
     * @param number
     * @return 返回date
     */
    public static Date getAfterMonth(int number) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, number);
        return c.getTime();
    }


    /**
     * 获取N个月后的日期
     *
     * @param number
     * @return 返回string
     */
    public static String getAfterMonthString(int number) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(getAfterMonth(number).getTime());
        return strDate;
    }


    /**
     * 获取N年后的日期
     *
     * @param number
     * @return 返回date
     */
    public static Date getAfterYear(int number) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, number);
        return c.getTime();
    }


    /**
     * 获取N年后的日期
     *
     * @param number
     * @return 返回String
     */
    public static String getAfterYearString(int number) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(getAfterYear(number).getTime());
        return strDate;
    }

    public static boolean isThisWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }

    //判断选择的日期是否是今天
    public static boolean isToday(long time)
    {
        return isThisTime(time,"yyyy-MM-dd");
    }
    //判断选择的日期是否是本月
    public static boolean isThisMonth(long time)
    {
        return isThisTime(time,"yyyy-MM");
    }

    //判断选择的日期是否是本年
    public static boolean isThisYear(long time)
    {
        return isThisTime(time,"yyyy");
    }
    private static boolean isThisTime(long time,String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if(param.equals(now)){
            return true;
        }
        return false;
    }

    public static boolean isSameBirthDay(final Date date1, final Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        final Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        final Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameBirthDay(cal1, cal2);
    }


    public static boolean isSameBirthDayWithInEachMonth(final Date date1, final Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        final Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        final Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameBirthDayWithInEachMonth(cal1, cal2);
    }

    public static boolean isSameBirthDay(final Calendar cal1, final Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH));
    }

    public static boolean isSameBirthDayWithInEachMonth(final Calendar cal1, final Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    /**
     * 输入日期获取日期的00时00分00秒000毫秒
     * @param date 输入的日期   如入参为空， 即返回当前的日期
     * @param day 调整天数  入参为：正整数即返回之后多少天    负整数即返回之前多少天
     * 例子：date:2010-07-21 15:55:12.557  day:5  返回参数:2010-07-26 00:00:00.000
     * @return
     */
    public static Date dayFirstDateWithAdjustDay(Date date, Integer day) {
    	if (null == date) {
			date = new Date();
		}
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    if (null != day && day != 0) {			
	    	calendar.add(Calendar.DATE, day);
		}
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}

    /**
     * 输入日期获取日期的23时59分59秒毫秒
     * @param date 输入的日期   如入参为空， 即返回当前的日期
     * @param day 调整天数  入参为：正整数即返回之后多少天    负整数即返回之前多少天
     * 例子：date:2010-07-21 15:55:12.557  day:-5  返回参数:2010-07-16 23:59:59.999
     * @return
     */
    public static Date dayLastDateWithAdjustDay(Date date, Integer day) {
    	if (null == date) {
			date = new Date();
		}
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    if (null != day && day != 0) {			
	    	calendar.add(Calendar.DATE, day);
		}
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	    return calendar.getTime();
	}

    public static void main(String[] args) throws ParseException {
//        //同比
//        System.out.println("日:" + getLastYearDate(new Date()));
//        System.out.println("月:" + getLastYearMonth(new Date()));
//        System.out.println("年:" + getLastYear(new Date()));
//        System.out.println("周 start:" + getLastYearWeek(new Date(), "START"));
//        System.out.println("周 end:" + getLastYearWeek(new Date(), "END"));
//        //环比
//        System.out.println("日 yesterday:" + getYesterday(new Date()));
//        System.out.println("月:" + getLastMonth(new Date()));
//        System.out.println("周 start:" + getLastWeek(new Date(), "START"));
//        System.out.println("------");
//        System.out.println(getYearOnYearDate(new Date(), new Date(), "WEEK"));
//        System.out.println(getRingDate(new Date(), new Date(), "USER_DEFINED").getStartDate());
//        System.out.println(getRingDate(new Date(), new Date(), "USER_DEFINED").getEndDate());
//
//
//        System.out.println("N个月后的日期::" + getAfterMonth(2));
//        System.out.println("N个月后的日期::" + getAfterMonthString(2));
//        System.out.println("------>");
//        Long ti = new Date().getTime();
//        System.out.println(isThisWeek(ti));
//        System.out.println(isThisMonth(ti));
//        System.out.println(isThisYear(ti));
//        long tii = getLastYearDate(new Date()).getTime();
//        System.out.println(isThisYear(tii));
//
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date curr = simpleDateFormat.parse("2019-11-09");
        Date curr2 = simpleDateFormat.parse("1988-11-09");

        /*System.out.println(curr);
        System.out.println(curr2);*/


        System.out.println(isSameBirthDay(curr,curr2));
        //System.out.println(isSameBirthDayWithInEachMonth(curr,curr2));
    }

}
