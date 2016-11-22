package com.wxdroid.basemodule.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils
{

    /**
     * 格式化日期显示
     *
     * @param unixtime
     * @param pattern
     * @return
     */
    public static String formatTime(long unixtime, String pattern) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(pattern);
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT00:00:00"));
        return timeFormat.format(unixtime);
    }

    /**
     * 基于服务器时间，处理业务时间
     * @param time
     * @param serverTime
     * @return
     */
    public static String getCurTime(long time, long serverTime) {
        long distance = (serverTime - time * 1000) / 1000;
        if (distance < 60) {
            return "刚刚";// String.valueOf(distance)+"秒";

        } else if (distance < 60 * 60) {
            return String.valueOf(distance / 60) + "分钟前";
        } else if (distance < 60 * 60 * 24) {
            return String.valueOf(distance / 60 / 60) + "小时前";
        } else if (distance < 60 * 60 * 24 * 30) {
            return String.valueOf(distance / 60 / 60 / 24) + "天前";
        } else if (distance < 60 * 60 * 24 * 30 * 12) {
            return String.valueOf(distance / 60 / 60 / 24 / 30) + "月前";
        } else {
            return String.valueOf(distance / 60 / 60 / 24 / 30 / 12) + "年前";
        }
    }



    public static String getInterval(String createtime) { //传入的时间格式必须类似于2012-8-21 17:53:20这样的格式
        try {
            String interval = createtime;

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            Date d1 = (Date) sd.parse(createtime, pos);

            //用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
            long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒

            if (time / 1000 < 10 && time / 1000 >= 0) {
                //如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
                interval = "刚刚";

            } else if (time / 1000 < 60 && time / 1000 > 0) {
                //如果时间间隔小于60秒则显示多少秒前
                int se = (int) ((time % 60000) / 1000);
                interval = se + "秒前";

            } else if (time / 60000 < 60 && time / 60000 > 0) {
                //如果时间间隔小于60分钟则显示多少分钟前
                int m = (int) ((time % 3600000) / 60000);//得出的时间间隔的单位是分钟
                interval = m + "分钟前";

            } else if (time / 3600000 < 24 && time / 3600000 >= 0) {
                //如果时间间隔小于24小时则显示多少小时前
                int h = (int) (time / 3600000);//得出的时间间隔的单位是小时
                interval = h + "小时前";

            } else {
                //大于24小时，则显示正常的时间，但是不显示秒
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                ParsePosition pos2 = new ParsePosition(0);
                Date d2 = (Date) sdf.parse(createtime, pos2);

                interval = sdf.format(d2);
            }
            return interval;
        }catch (Exception e){
            return createtime;
        }
    }

    public static String getRewardTopicInterval(String createtime) { //传入的时间格式必须类似于2012-8-21 17:53:20这样的格式
        try {
            String interval = createtime;

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            Date d1 = (Date) sd.parse(createtime, pos);

            //用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
            long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒

            if (time / 1000 < 10 && time / 1000 >= 0) {
                //如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
                interval = "刚刚";

            } else if (time / 1000 < 60 && time / 1000 > 0) {
                //如果时间间隔小于60秒则显示多少秒前
                int se = (int) ((time % 60000) / 1000);
                interval = se + "秒前";

            } else if (time / 60000 < 60 && time / 60000 > 0) {
                //如果时间间隔小于60分钟则显示多少分钟前
                int m = (int) ((time % 3600000) / 60000);//得出的时间间隔的单位是分钟
                interval = m + "分钟前";

            } else if (time / 3600000 < 24 && time / 3600000 >= 0) {
                //如果时间间隔小于24小时则显示多少小时前
                int h = (int) (time / 3600000);//得出的时间间隔的单位是小时
                interval = h + "小时前";

            } else {
                //大于24小时，则显示天
                int h = (int) (time / 3600000 / 24);//得出的时间间隔的单位是小时
                interval = h + "天前";
            }
            return interval;
        }catch (Exception e){
            return createtime;
        }
    }

    /**
     * 一天的时间毫秒
     */
    private static final int DAY_MILLIS = 24 * 60 * 60 * 1000;

    /**
     * 一年的时间毫秒
     */
    private static final int YEAR_MILLIS = 365 * DAY_MILLIS;

    /**
     * 今天零点时间戳
     * @return
     */
     public static long getTodayZeroTimestamp(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }


    /**
     * 私信聊天时间
     * @param createTime
     * @return
     */
    public static String getMessageForTime(long createTime) {//时间戳
        if (createTime < 9999999999L) {
            createTime *= 1000;
        }

        //时间差
        long duration = createTime - getTodayZeroTimestamp();
        if (duration >= 0) {//今天
            return new SimpleDateFormat("HH:mm").format(new Date(createTime));
        } else if (- DAY_MILLIS <= duration && duration < 0) {
            return "昨天 " + new SimpleDateFormat("HH:mm").format(new Date(createTime));
        } else if (- DAY_MILLIS * 2 <= duration && duration < - DAY_MILLIS) {
            return "前天 " + new SimpleDateFormat("HH:mm").format(new Date(createTime));
        } else if (- YEAR_MILLIS <= duration && duration < - DAY_MILLIS * 2) {
            return  new SimpleDateFormat("MM-dd HH:mm").format(new Date(createTime));
        } else {
            return  new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(createTime));
        }
    }


    /**
     * 格式化聊天列表时间
     * @param createTime
     * @return
     */
    public static String getChatListForTime(long createTime) {//时间戳
        if (createTime < 9999999999L) {
            createTime *= 1000;
        }

        //时间差
        long duration = createTime - getTodayZeroTimestamp();
        if (duration >= 0) {//今天
            return new SimpleDateFormat("HH:mm").format(new Date(createTime));
        } else if (- DAY_MILLIS <= duration && duration < 0) {
            return "昨天 ";
        } else if (- DAY_MILLIS * 7 <= duration && duration < - DAY_MILLIS) {
            //获得星期
            return getWeekDay(createTime);
        } else {
            return  new SimpleDateFormat("yyyy/MM/dd").format(new Date(createTime));
        }
    }


    /**
     * 输入时间戳变星期
     *
     * @param time
     * @return
     */
    public static String getWeekDay(long time) {
        int index = 0;
        try {
            Calendar cd = Calendar.getInstance();
            cd.setTime(new Date(time));
            index = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (Throwable e) {
            e.printStackTrace();
        }
        String week = null;
        if (index == 1) {
            week = "星期日";
        } else if (index == 2) {
            week = "星期一";
        } else if (index == 3) {
            week = "星期二";
        } else if (index == 4) {
            week = "星期三";
        } else if (index == 5) {
            week = "星期四";
        } else if (index == 6) {
            week = "星期五";
        } else if (index == 7) {
            week = "星期六";
        }
        return week;

    }


    /**
     * 将时间改成时间戳
     * @param time
     * @return
     */
    public static long getTimestrapFromTimeStr(String time) {//传入的时间格式必须类似于2012-8-21 17:53:20这样的格式
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sd.parse(time).getTime();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static String formatMessageCreateTime(String createtime) { //传入的时间格式必须类似于2012-8-21 17:53:20这样的格式
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            Date d1 = (Date) sd.parse(createtime, pos);

            //用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
            long duration = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒
            duration = duration / 1000;
          /*  if (time / 1000 < 10 && time / 1000 >= 0) {
                //如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
                interval = "刚刚";

            } else if (time / 1000 < 60 && time / 1000 > 0) {
                //如果时间间隔小于60秒则显示多少秒前
//                int se = (int) ((time % 60000) / 1000);
                interval = "刚刚";

            } else if (time / 60000 < 60 && time / 60000 > 0) {
                //如果时间间隔小于60分钟则显示多少分钟前
                int m = (int) ((time % 3600000) / 60000);//得出的时间间隔的单位是分钟
                interval = m + "分钟前";

            } else if (time / 3600000 < 24 && time / 3600000 > 0) {
                //如果时间间隔小于24小时则显示多少小时前
                int h = (int) (time / 3600000);//得出的时间间隔的单位是小时
                interval = h + "小时前";

            } else if(time / 3600000 > 24*1  && time / 3600000  <  24 * 30){
                int d = (int) (time / 3600000 / 24);//得出的时间间隔的单位是小时
                interval = d + "天前";
            } else {
                //大于24小时，则显示正常的时间，但是不显示秒
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                ParsePosition pos2 = new ParsePosition(0);
                Date d2 = (Date) sdf.parse(createtime, pos2);

                interval = sdf.format(d2);
            }*/

            if (duration < 60) {
                return "刚刚";// String.valueOf(distance)+"秒";

            } else if (duration < 60 * 60) {
                return String.valueOf(duration / 60) + "分钟前";
            } else if (duration < 60 * 60 * 24) {
                return String.valueOf(duration / 60 / 60) + "小时前";
            } else if (duration < 60 * 60 * 24 * 30) {
                return String.valueOf(duration / 60 / 60 / 24) + "天前";
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                ParsePosition pos2 = new ParsePosition(0);
                Date d2 = (Date) sdf.parse(createtime, pos2);

                return sdf.format(d2);
            }
        }catch (Exception e){
            return createtime;
        }
    }

    public static String getIntervalNew(String createtime) { //传入的时间格式必须类似于2012-8-21 17:53:20这样的格式
        try {
            String interval = createtime;

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            Date d1 = (Date) sd.parse(createtime, pos);

            //用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
            long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒

            if (time / 1000 < 10 && time / 1000 >= 0) {
                //如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
                interval = "刚刚";

            } else if (time / 1000 < 60 && time / 1000 > 0) {
                //如果时间间隔小于60秒则显示多少秒前
                int se = (int) ((time % 60000) / 1000);
                interval = se + "秒前";

            } else if (time / 60000 < 60 && time / 60000 > 0) {
                //如果时间间隔小于60分钟则显示多少分钟前
                int m = (int) ((time % 3600000) / 60000);//得出的时间间隔的单位是分钟
                interval = m + "分钟前";

            } else if (time / 3600000 < 24 && time / 3600000 >= 0) {
                //如果时间间隔小于24小时则显示多少小时前
                int h = (int) (time / 3600000);//得出的时间间隔的单位是小时
                interval = h + "小时前";

            } else if(time / 3600000 >= 24  && time / 3600000  <  24 * 30){
                int d = (int) (time / 3600000 / 24);//得出的时间间隔的单位是小时
                interval = d + "天前";
            } else {
                //大于24小时，则显示正常的时间，但是不显示秒
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                ParsePosition pos2 = new ParsePosition(0);
                Date d2 = (Date) sdf.parse(createtime, pos2);

                interval = sdf.format(d2);
            }
            return interval;
        }catch (Exception e){
            return createtime;
        }
    }

    public static String getIntervalTimeStr(long lastTime, long currentTime){
        long time = currentTime - lastTime;
        String interval = "";
        if(time/1000 < 10 && time/1000 >= 0) {
            //如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
            interval ="刚刚";

        } else if(time/1000 < 60 && time/1000 > 0) {
            //如果时间间隔小于60秒则显示多少秒前
            int se = (int) ((time%60000)/1000);
            interval = se + "秒前";

        } else if(time/60000 < 60 && time/60000 > 0) {
            //如果时间间隔小于60分钟则显示多少分钟前
            int m = (int) ((time%3600000)/60000);//得出的时间间隔的单位是分钟
            interval = m + "分钟前";

        }else if(time/3600000 < 24 && time/3600000 >= 0) {
            //如果时间间隔小于24小时则显示多少小时前
            int h = (int) (time/3600000);//得出的时间间隔的单位是小时
            interval = h + "小时前";

        } else {
            //大于24小时，则显示正常的时间，但是不显示秒
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            ParsePosition pos2 = new ParsePosition(0);
            if(lastTime == 0){
                Date d2 = new Date(currentTime);
                interval = sdf.format(d2);
            }else{
                Date d2 = new Date(lastTime);
                interval = sdf.format(d2);
            }
        }

        return interval;
    }

    public static String getHHMMSSDurationByLong(long duration){
        int min = (int)(duration / 60 % 60);
        int hour = (int)(duration / 3600);
        StringBuffer timeStr = new StringBuffer();
        if(hour < 10){
            timeStr.append("0");
        }
        timeStr.append(hour);
        timeStr.append(":");
        if(min < 10){
            timeStr.append("0");
        }
        timeStr.append(min);
        timeStr.append(":");

        int second = (int)(duration % 60);
        if(second < 10){
            timeStr.append("0");
        }
        timeStr.append(second);
        return timeStr.toString();
    }

    public static String getMMSSDurationByInt(int duration){
        int min = duration / 60 % 60;
        StringBuffer timeStr = new StringBuffer();
        /*if(min < 10){
            timeStr.append("0");
        }*/
        timeStr.append(min);
        timeStr.append(":");

        int second = duration % 60;
        if(second < 10){
            timeStr.append("0");
        }
        timeStr.append(second);
        return timeStr.toString();
    }

    public static String getHMSDurationStrByStr(String createtime){
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            Date d1 = (Date) sd.parse(createtime, pos);

            long duration = System.currentTimeMillis() - d1.getTime();

            return getSecondByStr(duration/1000);
        }catch(Exception e){
            return createtime;
        }

    }

    public static String getSecondByStr(long duration) {
        long lastTime = duration;
        int hour = (int) (lastTime / 3600);
        int min = (int) (lastTime / 60 % 60);
        StringBuffer timeStr = new StringBuffer();
        if (hour > 0) {
            timeStr.append(hour);
            timeStr.append("小时");
            timeStr.append(min);
            timeStr.append("分");
            return timeStr.toString();
        }

        if (min > 0) {
            timeStr.append(min);
            timeStr.append("分");
        }

        int second = (int) (lastTime % 60);
        if (second >= 0) {
            timeStr.append(second);
            timeStr.append("秒");
        }
        return timeStr.toString();
    }


    public static String getHHMMSSDurationStrByLong(long duration){
        int min = (int)(duration / 60 % 60);
        int hour = (int)(duration / 3600);
        StringBuffer timeStr = new StringBuffer();
        if(hour < 10){
            timeStr.append("0");
        }
        timeStr.append(hour);
        timeStr.append(":");
        if(min < 10){
            timeStr.append("0");
        }
        timeStr.append(min);
        timeStr.append(":");

        int second = (int)(duration % 60);
        if(second < 10){
            timeStr.append("0");
        }
        timeStr.append(second);
        return timeStr.toString();
    }

    public static long getCountDownSTime(String endTimeStr, long curTime){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date d1 = (Date) sd.parse(endTimeStr, pos);
        long remainTime = d1.getTime()/1000 - curTime;
        if(remainTime < 0){
            remainTime = 0;
        }
        return remainTime;
    }

    public static String getCountDownDHMTime(long remainTime){
        try {
            if(remainTime <= 0){
                return "";
            }
            long lastTime = remainTime;
            int day = (int)(lastTime / 3600 / 24);
            StringBuffer timeStr = new StringBuffer();
            timeStr.append(day);
            timeStr.append("天");
            int hour = (int)(lastTime / 3600 % 24);
            timeStr.append(hour);
            timeStr.append("小时");
            int min = (int)(lastTime / 60 % 60);
            timeStr.append(min);
            timeStr.append("分");
//			int second = (int)(lastTime % 60);
//			timeStr.append(second);
//			timeStr.append("秒");
            return timeStr.toString();
        }catch(Exception e){

        }

        return "";
    }

    /**
     * 计算时间 时分秒 使用 splite进行分割
     * @param remainTime
     * @param splite
     * @return
     */
    public static String getTimeBySplite(long remainTime, String splite){
        try {
            if(remainTime <= 0){
                return "0";
            }
            long lastTime = remainTime;
//            int day = (int)(lastTime / 3600 / 24);
            StringBuffer timeStr = new StringBuffer();
//            timeStr.append(day);
//            timeStr.append("天");
            int hour = (int)(lastTime / 3600);
            if(hour > 0) {
                timeStr.append(hour);
                timeStr.append(splite);
            }
            int min = (int)(lastTime / 60 % 60);
//            if(min > 0 || hour > 0) {
                if(min < 10) {
                    timeStr.append("0"+min);
                } else {
                    timeStr.append(min);
                }
                timeStr.append(splite);
//            }
			int second = (int)(lastTime % 60);
            if(second > 0 || min > 0 || hour > 0) {
                if(second < 10) {
                    timeStr.append("0"+second);
                } else {
                    timeStr.append(second);
                }
            }
//			timeStr.append("秒");
            return timeStr.toString();
        }catch(Exception e){

        }

        return "0";
    }


    public static String getFormatTimeStr(String createtime){
        String formatTimeStr = createtime;
        try{
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sd2 = new SimpleDateFormat("yyyy年MM月dd日HH时");
            ParsePosition pos = new ParsePosition(0);
            Date d1 = (Date) sd.parse(createtime, pos);
            formatTimeStr = sd2.format(d1);

        }catch (Exception e){

        }
        return formatTimeStr;
    }

    public static String getSimleLastTimeDuation(long lastTime){
        int min = (int)(lastTime / 60 % 60);
        int hour = (int)(lastTime / 3600);
        StringBuffer timeStr = new StringBuffer();
        if(hour < 10){
            timeStr.append("0");
        }
        timeStr.append(hour);
        timeStr.append(":");
        if(min < 10){
            timeStr.append("0");
        }
        timeStr.append(min);
        timeStr.append(":");

        int second = (int)(lastTime % 60);
        if(second < 10){
            timeStr.append("0");
        }
        timeStr.append(second);
        return timeStr.toString();
    }


    /**
     * 获取时间戳
     * @param time
     * @param format
     * @return
     */
    public static long getTime(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT00:00:00"));
        try {
            return sdf.parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * @param time
     * @return
     */
    public static String getFormatTime(long time) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return sd.format(date);
    }

    public static boolean shouldRefresh(long lastTime, long currentTime) {
        return currentTime - lastTime > 1000 * 60 * 3;
    }

    public static String getCountDownYMDHMTime(long remainTime){
        try {
//            if(remainTime <= 0){
//                return "";
//            }
            long lastTime = remainTime;
            StringBuffer timeStr = new StringBuffer();
            int year = (int)(lastTime / 3600 / 24 / 30 / 12);
            if(year > 0){
                timeStr.append(year);
                timeStr.append("年");
            }
            int month = (int)(lastTime / 3600 / 24 / 30);
            if(month > 0){
                timeStr.append(month);
                timeStr.append("月");
            }
            int day = (int)(lastTime / 3600 / 24);
            if(day > 0) {
                timeStr.append(day);
                timeStr.append("天");
            }
            int hour = (int)(lastTime / 3600 % 24);
            if(hour > 0){
                timeStr.append(hour);
                timeStr.append("小时");
            }
            int min = (int)(lastTime / 60 % 60);
            timeStr.append(min<1?1:min);
            timeStr.append("分");
//			int second = (int)(lastTime % 60);
//			timeStr.append(second);
//			timeStr.append("秒");
            return timeStr.toString();
        }catch(Exception e){

        }

        return "";
    }
    public static String formatVideoAddTime(String addTime) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            Date date = (Date) sd.parse(addTime, pos);
            sd = new SimpleDateFormat("MM月dd日 HH:mm");
            return sd.format(date);
        } catch (Exception e) {
            return addTime;
        }
    }


    public static String getCurrentTimeZone()
    {
        TimeZone tz = TimeZone.getDefault();
        return createGmtOffsetString(true,true,tz.getRawOffset());
    }
    public static String createGmtOffsetString(boolean includeGmt,
                                               boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        appendNumber(builder, 2, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
        appendNumber(builder, 2, offsetMinutes % 60);
        return builder.toString();
    }

    private static void appendNumber(StringBuilder builder, int count, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < count - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
    }
}
