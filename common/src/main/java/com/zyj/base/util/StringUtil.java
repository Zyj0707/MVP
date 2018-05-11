package com.zyj.base.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by heyong on 16/7/19.
 */
public class StringUtil {
    public static String Pattern = "yyyy-MM-dd HH:mm:ss";
    public static String Pattern2 = "yyyyMMddHHmmss";
    public static String Pattern3 = "yyMMddHHmm";
    public static String Pattern4 = "yyyyMMdd";
    public static String Pattern5 = "yyyy-MM-dd HH:mm";
    public static String Pattern6 = "yyMMddHHmmss";
    public static String Pattern7 = "yy-MM-dd HH:mm";
    public static String Pattern8 = "yyyy/MM/dd";
    public static String Pattern9 = "HH:mm";
    private static String imei = "pos0001";
    private static DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    public static boolean isEqual(String str, String str2) {
        boolean equal = false;

        if (!isEmpty(str) && !isEmpty(str2)) {
            equal = str.equals(str2);
        }
        return equal;
    }

    public static int length(String str) {
        return isEmpty(str) ? 0 : str.length();
    }

    public static String substring(String str, int start, int end) {
        if (start >= 0 && end < str.length() && start <= end) {
            return str.substring(start, end);
        }
        else {
            return "";
        }
    }

    public static String parseString(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return str;
    }

    public static int getInt(String str, int defInt) {
        int i = defInt;
        try {
            i = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
            i = defInt;
        }
        return i;
    }

    public static long getLong(String str, long defLong) {
        long i = defLong;
        try {
            i = Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
            i = defLong;
        }
        return i;
    }

    public static boolean isDoubleZero(String s) {
        BigDecimal bigDecimal = new BigDecimal(s);

        return bigDecimal.compareTo(BigDecimal.ZERO) == 0;
    }

    public static double parseDouble(String str) {
        double i = 0;

        try {
            i = Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
            i = 0;
        }
        return i;
    }

    public static String formatDouble(double ret) {
        try {
            return decimalFormat.format(ret);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static double scaleDouble(final double ret, final int scale) {
        try {
            BigDecimal bg = new BigDecimal(String.valueOf(ret));
            return bg.setScale(scale, BigDecimal.ROUND_CEILING).doubleValue();
        } catch (Exception e) {
            return  0;
        }
    }

    /**
     * 输出32位唯一UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 输出IMEI
     */
    public static String getIMEI(Context context) {
        if (isEmpty(imei)) {
            TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
            String deviceId = TelephonyMgr.getDeviceId();

            if (!isEmpty(deviceId)) {
                imei = deviceId;
            }
        }

        return imei;
    }

    /**
     * 把字符串转换成相应位数
     */
    public static String str2Len(String str, int len) {
        if (str.length() < len) {
            int cha = len - str.length();
            for (int i = 0; i < cha; i++) {
                str = "0" + str;
            }
        } else if (str.length() > len) {
            str = str.substring(0, len);
        }

        return str;
    }

    public static String generateRandom(final int len) {
        java.util.Random random = new java.util.Random();
        String str = "";

        for(int i = 0; i < len; i++){
            str += random.nextInt(9);
        }

        return str;
    }

    /**
     * 输出当前时间戳
     */
    public static long getTimestamp() {
        Date date = new Date();
        return date.getTime();
    }
    /**
     * 输出当前时间戳
     */
    public static String getTimestampStr() {
        Date date = new Date();
        return String.valueOf(date.getTime());
    }
    /**
     * 格式化时间
     * @param time
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formatPayTime(String time) {
        String display = "";

        if (time != null) {
            try {
                if (time.length() == 14) {
                    Date tDate = new SimpleDateFormat(Pattern2, Locale.getDefault()).parse(time);// 格式化输入的时间
                    display = new SimpleDateFormat(Pattern, Locale.getDefault()).format(tDate);
                }
                else if (time.length() == 12) {
                    Date tDate = new SimpleDateFormat(Pattern6, Locale.getDefault()).parse(time);// 格式化输入的时间
                    display = new SimpleDateFormat(Pattern7, Locale.getDefault()).format(tDate);
                }
            } catch (Exception e) {
                e.printStackTrace();
                display = "未知";
            }
        }
        return display;
    }

    /**
     * 格式化时间
     * @param startTime
     * @param endTime
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String calculateTimeCost(String startTime, String endTime) {
        String display = "";

        if (!isEmpty(startTime) && !isEmpty(endTime)) {
            try {
                Date startDate = new SimpleDateFormat(Pattern2, Locale.getDefault()).parse(startTime);// 格式化输入的时间
                Date endDate = new SimpleDateFormat(Pattern2, Locale.getDefault()).parse(endTime);// 格式化输入的时间

                long diff = startDate.getTime() - endDate.getTime();//这样得到的差值是微秒级别
                long days = diff / (1000 * 60 * 60 * 24);
                long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);

                if (days > 0) {
                    display = days + "d";
                }
                if (hours > 0) {
                    display += hours + "h";
                }
                if (minutes > 0) {
                    display += minutes + "min";
                }
            } catch (Exception e) {
                e.printStackTrace();
                display = "0min";
            }
        }
        else {
            display = "0min";
        }

        return display;
    }

    /**
     * 格式化时间（输出当前时间）
     *
     * @param pattern
     *            输入参数time的时间格式 如:"yyyy-MM-dd HH:mm:ss"
     *            <p/>
     *            如果为空则默认使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formatDisplayTime(String pattern) {
        String display = "";

        try {
            display = new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date());// 格式化输入的时间
        } catch (Exception e) {
            e.printStackTrace();
        }
        return display;
    }

    /**
     * 格式化时间（输出类似于今天、 昨天这样的时间）
     *
     * @param time
     *            需要格式化的时间 如"2014-07-14 19:01:45"
     * @param pattern
     *            输入参数time的时间格式 如:"yyyy-MM-dd HH:mm:ss"
     *            <p/>
     *            如果为空则默认使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formatDisplayTime(String time, String pattern) {
        String display = "";
        int tMin = 60 * 1000;
        int tHour = 60 * tMin;
        int tDay = 24 * tHour;

        if (time != null) {
            try {
                Date tDate = new SimpleDateFormat(pattern, Locale.getDefault()).parse(time);// 格式化输入的时间
                Date today = new Date();
                SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy", Locale.getDefault());// 年
                SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());// 年月日
                Date thisYear = new Date(thisYearDf.parse(
                        thisYearDf.format(today)).getTime());// 得到年
                Date yesterday = new Date(todayDf.parse(todayDf.format(today))
                        .getTime());// 得到年月日
                // Date beforeYes = new Date(yesterday.getTime() - tDay);
                if (tDate != null) {
                    // SimpleDateFormat halfDf = new SimpleDateFormat("MM月dd日");
                    long dTime = today.getTime() - tDate.getTime();// 当前时间和给定时间的时间差
                    if (tDate.before(thisYear)) {// 不是同一年
                        display = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault())
                                .format(tDate);
                    } else {// 是同一年
                        if (dTime < tDay && tDate.after(yesterday)) {// 判断给定时间是不是今天
                            display = "今天"
                                    + new SimpleDateFormat(" HH:mm", Locale.getDefault())
                                    .format(tDate);
                        } else {
                            display = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault())
                                    .format(tDate);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return display;
    }

}
