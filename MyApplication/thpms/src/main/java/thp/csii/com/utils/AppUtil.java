package thp.csii.com.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import thp.csii.com.MyApp;
import thp.csii.com.TianHongPayMentUtil;

@SuppressLint("SimpleDateFormat")
public class AppUtil {
	public static final String FORMAT_DATE = "yyyy-MM-dd";

	public static final String FORMAT_DATE_MONTH_DAT = "M月dd日";

	public static final String FORMAT_DATE_CHARACTER = "yyyy年MM月dd日";

	public static final String FORMAT_DATE_MONTHE_NO_ZERO = "yyyy年M月dd日";

	public static final String FORMAT_DATE_MONTHE_DAY_HOURS = "yyyy年M月dd日HH时";

	public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	public static final String FORMAT_DATE_TIME_TWO = "yyyy-MM-dd HH:mm";

	private static final long ONE_SECOND = 1000;

	private static final long ONE_MINUTE = ONE_SECOND * 60;

	private static final long ONE_HOUR = ONE_MINUTE * 60;

	private static final long ONE_DAY = ONE_HOUR * 24;

	/**
	 * 获取当前版本号（内部使用）
	 *
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		PackageManager pm;
		PackageInfo pi;
		try {
			pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	/**
	 * 获取当前版本名称
	 *
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		PackageManager pm;
		PackageInfo pi;
		try {
			pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			return "-";
		}
	}

	/**
	 * 获取手机IMEI号码
	 *
	 * @param context
	 * @return
	 */
	public static String getImei(Context context) {
		try {
			return ((TelephonyManager) context
					.getSystemService(Activity.TELEPHONY_SERVICE))
					.getDeviceId();
		} catch (Exception e) {
			return "-";
		}
	}

	/**
	 * 获取安卓SDK版本
	 *
	 * @return
	 */
	public static int getAndroidSDKVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 用于获取状态栏的高度，使用Resource对象获取（推荐这种方式）
	 *
	 * @return 返回状态栏高度的像素值。
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 用于获取屏幕宽度，
	 *
	 * @return 返回屏幕宽度的像素值。
	 */
	@SuppressWarnings("deprecation")
	public static int getStatusBarWidth() {
		int width = 0;
		WindowManager wm = (WindowManager) TianHongPayMentUtil.CurrentContext
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		return width;
	}

	/**
	 * 用于获取屏幕高度，
	 *
	 * @return 返回屏幕高度的像素值。
	 */
	@SuppressWarnings("deprecation")
	public static int getStatusBarHeight() {
		int width = 0;
		WindowManager wm = (WindowManager) TianHongPayMentUtil.CurrentContext
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getHeight();
		return width;
	}

	/**
	 * 定义函数动态控制listView的高度
	 *
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
		listView.setLayoutParams(params);
	}


	public static int getHTime(Date format) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
		String currentTime = sdf.format(format);
		return Integer.parseInt(currentTime);
	}

	
	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}

	/**
	 * 精度加法
	 *
	 * @param b
	 * @return
	 */
	public static BigDecimal add(BigDecimal... b) {
		BigDecimal rtn = b[0];
		for (int i = 1; i < b.length; i++) {
			rtn = add(rtn, b[i]);
		}
		return rtn;
	}

	/**
	 * 精度加法
	 *
	 * @param b
	 * @param i
	 * @param scale
	 * @return
	 */
	public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
		if (b1 == null)
			return b2;
		if (b2 == null)
			return b1;
		return b1.add(b2);
	}

	/**
	 * 加法
	 *
	 * @param
	 * @param
	 * @param
	 * @return
	 */
	public static Integer add(Integer i1, Integer i2) {
		if (i1 == null)
			return i2;
		if (i2 == null)
			return i1;
		return i1 + i2;
	}

	public static String timeString(String string) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		long time = Integer.parseInt(string);
		String date = sdf.format(new Date(time * 1000));
		return date;
	}

	public static long stringToDate(String string) {
		long time = Integer.parseInt(string);
		return time * 1000;
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * * 获取指定日期是星期几 参数为null时表示获取当前日期是星期几
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekOfDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekOfDays[w];
	}

	/**
	 * 日期计算（日）
	 *
	 * @param d
	 * @param i
	 * @return
	 */
	public static Date addDay(Date d, int i) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + i);
		return c.getTime();
	}

	/**
	 * 日期计算（月）
	 *
	 * @param d
	 * @param i
	 * @return
	 */
	public static Date addMonth(Date d, int i) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) + i);
		return c.getTime();
	}

	/**
	 * 日期计算（小时）
	 *
	 * @param d
	 * @param i
	 * @return
	 */
	public static Date addHour(Date d, int i) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR, c.get(Calendar.HOUR) + i);
		return c.getTime();
	}

	/**
	 * 日期计算（分钟）
	 *
	 * @param d
	 * @param i
	 * @return
	 */
	public static Date addMinute(Date d, int i) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + i);
		return c.getTime();
	}

	/**
	 * 转换为数组
	 *
	 * @param in
	 * @return
	 */
	public static String[] asArray(List<String> in) {
		if (in == null || in.size() == 0) {
			return null;
		}
		String[] rtn = new String[in.size()];
		for (int i = 0; i < in.size(); i++) {
			rtn[i] = in.get(i);
		}
		return rtn;
	}

	/**
	 * 数字金额转换为大写金额
	 *
	 * @return
	 */
	public static String asBigCurrency(double val) {
		String fraction[] = { "角", "分" };
		String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String unit[][] = { { "元", "万", "亿" }, { "", "拾", "佰", "仟" } };
		String head = val < 0 ? "负" : "";
		val = Math.abs(val);
		String s = "";
		for (int i = 0; i < fraction.length; i++) {
			s += (digit[(int) (Math.floor(val * 10 * Math.pow(10, i)) % 10)] + fraction[i])
					.replaceAll("(零.)+", "");
		}
		if (s.length() < 1) {
			s = "整";
		}
		int integerPart = (int) Math.floor(val);
		for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
			String p = "";
			for (int j = 0; j < unit[1].length && val > 0; j++) {
				p = digit[integerPart % 10] + unit[1][j] + p;
				integerPart = integerPart / 10;
			}
			s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i]
					+ s;
		}
		return head
				+ s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "")
						.replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
	}

	/**
	 * 格式化为金额
	 *
	 * @param i
	 * @return
	 */
	public static String asCurrencyNoDecimal(int i) {
		String rtn = asCurrency(i);
		if (rtn.endsWith(".00")) {
			return rtn.substring(0, rtn.length() - 3);
		} else {
			return rtn;
		}
	}

	/**
	 * 格式化为金额
	 *
	 * @param i
	 * @return
	 */
	public static String asCurrency(int i) {
		BigDecimal b = new BigDecimal(i);
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.CHINA);
		return n.format(b.doubleValue());
	}

	/**
	 * 格式化为金额
	 *
	 * @param b
	 * @return
	 */
	public static String asCurrency(BigDecimal b) {
		if (b == null) {
			b = new BigDecimal(0);
		}
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.CHINA);
		return n.format(b.doubleValue());
	}

	/**
	 * 格式化为金额(并自动去掉小数点后面的无效0)
	 *
	 * @param s
	 * @return
	 */
	public static String asCurrency(String s) {
		if (s != null && s.length() > 0 && s.indexOf(".") > 0) {
			// 正则表达
			s = s.replaceAll("0+?$", "");// 去掉后面无用的零
			s = s.replaceAll("[.]$", "");// 如小数点后面全是零则去掉小数点
		} else {
			return "";
		}
		return s;
	}

	/**
	 * 格式化为金额
	 *
	 * @param b
	 * @return
	 */
	public static String asCurrency(Double d) {
		if (d == null) {
			return "";
		}
		DecimalFormat fmt = new DecimalFormat("##,###,###,###,##0.00");
		return fmt.format(d);
	}

	/**
	 * 日期格式化
	 *
	 * @param in
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date asDate(String in) throws ParseException {
		return asDate(in, FORMAT_DATE);
	}

	/**
	 * 日期格式化
	 *
	 * @param in
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date asDate(String in, String format) throws ParseException {
		if (isNull(in)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(in);
	}

	/**
	 * 转换为数值
	 *
	 * @param s
	 * @return
	 */
	public static BigDecimal asDecimal(String s) {
		return asDecimal(s, true);
	}

	/**
	 * 转换为数值
	 *
	 * @param s
	 * @param nullable
	 * @return
	 */
	public static BigDecimal asDecimal(String s, boolean nullable) {
		BigDecimal rtn = null;
		if (!AppUtil.isNull(s) && AppUtil.isDecimal(s)) {
			rtn = new BigDecimal(s);
		}
		if (!nullable && rtn == null) {
			rtn = new BigDecimal(0);
		}
		return rtn;
	}

	/**
	 * 转换为数值
	 *
	 * @param in
	 * @return
	 */
	public static double asDouble(BigDecimal in) {
		if (in == null)
			return 0.0d;
		return in.doubleValue();
	}

	/**
	 * @param s
	 * @return
	 */
	public static int asInt(BigDecimal b) {
		Double d = asDouble(b);
		return d.intValue();
	}

	/**
	 * @param i
	 * @return
	 */
	public static int asInt(Integer i) {
		if (i == null)
			return 0;
		return i.intValue();
	}

	/**
	 * @param i
	 * @return
	 */
	public static int asInt(Long l) {
		if (l == null)
			return 0;
		return asInt(l.toString());
	}

	/**
	 * @param s
	 * @return
	 */
	public static int asInt(String s) {
		if (isNull(s)) {
			return 0;
		}
		return Integer.parseInt(s.trim());
	}

	/**
	 * 数组转换为List
	 *
	 * @param ss
	 * @return
	 */
	public static List<String> asList(String[] ss) {
		List<String> rtn = new ArrayList<String>();
		if (ss != null && ss.length > 0) {
			for (String s : ss) {
				rtn.add(s);
			}
		}
		return rtn;
	}

	/**
	 * 格式化为百分比
	 *
	 * @param b
	 * @return
	 */
	public static String asPercent(BigDecimal b) {
		if (b == null)
			return "";
		return round(multiply(b, 100), 2).toPlainString() + "%";
	}

	/**
	 * 保留小数位
	 *
	 * @param b
	 * @param scale
	 * @return
	 */
	public static String asString(BigDecimal b, int scale) {
		return asString(b, scale, false);
	}

	/**
	 * 保留小数位
	 *
	 * @param b
	 * @param scale
	 * @return
	 */
	public static String asString(BigDecimal b, int scale,
								  boolean strpTrailingZeros) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		if (b == null)
			return "0";
		if (strpTrailingZeros) {
			if (asDouble(b) == 0.0d)
				return "0";
			else
				return b.setScale(scale, BigDecimal.ROUND_HALF_UP)
						.stripTrailingZeros().toPlainString();
		} else {
			return b.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
		}
	}

	/**
	 * 日期格式化
	 *
	 * @param in
	 * @param format
	 * @return
	 */
	public static String asString(Date in, String format) {
		if (in == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(in);
	}

	/**
	 * 整型转字符型
	 *
	 * @param i
	 * @return
	 */
	public static String asString(Integer i) {
		if (i == null)
			return "";
		return String.valueOf(i);
	}

	/**
	 * @param l
	 * @return
	 */
	public static String asString(Long l) {
		if (l == null)
			return "";
		return String.valueOf(l);
	}

	/**
	 * 转换为String
	 *
	 * @param s
	 * @return
	 */
	public static String asString(String s) {
		if (s == null)
			return "";
		return s;
	}

	/**
	 * 转换为String
	 *
	 * @param s
	 * @return
	 */
	public static String asString(CharSequence s) {
		if (s == null)
			return "";
		return s.toString();
	}

	/**
	 * 以友好的方式显示过去时间
	 *
	 * @param dt
	 * @param now
	 * @return
	 */
	public static String asPastTimeFriendly(Date dt, Date now) {
		if (dt == null || now == null || dt.after(now)) {
			return "";
		}
		long dtTime = dt.getTime();
		long nowTime = now.getTime();
		int days = (int) (nowTime / 86400000 - dtTime / 86400000); // 间隔天数
		if (days == 0) {
			int hour = (int) ((nowTime - dtTime) / 3600000);// 间隔小时数
			if (hour == 0) {
				return Math.max((nowTime - dtTime) / 60000, 1) + "分钟前";
			} else {
				return hour + "小时前";
			}
		} else if (days == 1) {
			return "昨天" + asString(dt, "M") + "时";
		} else if (days == 2) {
			return "前天" + asString(dt, "M") + "时";
		} else {
			return asString(dt, "M月d日H时");
		}
	}

	public static Date formatDate(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat(AppUtil.FORMAT_DATE_TIME,
				Locale.ENGLISH);
		Date d = null;
		try {
			d = sdf.parse(str);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * 以友好的方式显示过去时间，若不是在当前年的数据则显示年份
	 *
	 * @param dt
	 * @param now
	 * @return
	 */
	public static String displayPastTimeFriendly(long ms) {
		Date date = new Date(ms);
		if (date == null) {
			return "未知";
		}
		Date baseDate = new Date();
		if (baseDate.before(date)) {
			return "未知";
		}
		int year = getYearDiff(baseDate, date);
		int month = getMonthDiff(baseDate, date);
		int day = (int) getDayDiff(baseDate, date);
		
	
		
		if (year >= 1) {
			return year + "年前";
		} else if (month >= 1) {
			return month + "月前";
		}		
		if (day > 0) {
			if (day > 2) {
				return day + "天前";
			} else if (day == 2) {
				return "前天";
			} else if (day == 1) {
				return "昨天";
			}
		}
		if (!isSameDay(baseDate, date)) {
			return "昨天";
		}		
		int hour = AppUtil.getHTime(baseDate)-AppUtil.getHTime(date);
		if (hour > 6) {
			return "今天";
		} else if (hour > 0) {
			return hour + "小时前";
		}else{
			int minute = (int) ((baseDate.getTime() - date.getTime()) / (1 * 60 * 1000));
			if (minute < 2) {
				return "刚刚";
			} else if (minute < 30) {
				return minute + "分钟前";
			} else if (minute > 30) {
				return "半个小时以前";
			}	
		}				
		return "未知";
	}

	// 2个日期相差多少天
	public static long getDayDiff(Date startDate, Date endDate) {
		long t1 = startDate.getTime();
		long t2 = endDate.getTime();
		long count = (t2 - t1) / (24L * 60 * 60 * 1000);
		return Math.abs(count);
	}

	// 判断2个日期是否在同一天
	public static boolean isSameDay(Date date, Date date2) {
		String str = formatDate(date, "yyyy-MM-dd");
		String str2 = formatDate(date2, "yyyy-MM-dd");
		return str.equals(str2);
	}

	public static String formatDate(Date date, String pattern) {
		String returnValue = "";
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			returnValue = df.format(date);
		}
		return returnValue;
	}

	// 2个日期相差多少年
	public static int getYearDiff(Date minDate, Date maxDate) {
		if (minDate.after(maxDate)) {
			Date tmp = minDate;
			minDate = new Date(maxDate.getTime());
			maxDate = new Date(tmp.getTime());
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(minDate);
		int year1 = calendar.get(Calendar.YEAR);
		int month1 = calendar.get(Calendar.MONTH);
		int day1 = calendar.get(Calendar.DATE);

		calendar.setTime(maxDate);
		int year2 = calendar.get(Calendar.YEAR);
		int month2 = calendar.get(Calendar.MONTH);
		int day2 = calendar.get(Calendar.DATE);
		int result = year2 - year1;
		if (month2 < month1) {
			result--;
		} else if (month2 == month1 && day2 < day1) {
			result--;
		}
		return result;
	}

	// 2个日期相差多少月
	public static int getMonthDiff(Date minDate, Date maxDate) {
		if (minDate.after(maxDate)) {
			Date tmp = minDate;
			minDate = new Date(maxDate.getTime());
			maxDate = new Date(tmp.getTime());
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(minDate);
		int year1 = calendar.get(Calendar.YEAR);
		int month1 = calendar.get(Calendar.MONTH);
		int day1 = calendar.get(Calendar.DATE);

		calendar.setTime(maxDate);
		int year2 = calendar.get(Calendar.YEAR);
		int month2 = calendar.get(Calendar.MONTH);
		int day2 = calendar.get(Calendar.DATE);

		int months = 0;
		if (day2 >= day1) {
			months = month2 - month1;
		} else {
			months = month2 - month1 - 1;
		}
		return (year2 - year1) * 12 + months;
	}

	/**
	 * 以友好的方式显示未来时间
	 *
	 * @param dt
	 * @param now
	 * @return
	 */
	public static String asFutureTimeFriendly(Date dt, Date now) {
		if (dt == null || now == null || dt.before(now)) {
			return "";
		}
		long dtTime = dt.getTime();
		long nowTime = now.getTime();
		int days = (int) ((dtTime - nowTime) / 86400000); // 间隔天数
		if (days == 0) {
			int hour = (int) ((dtTime - nowTime) / 3600000);// 间隔小时数
			if (hour == 0) {
				int minutes = (int) (dtTime - nowTime) / 60000; // 间隔分钟数
				if (minutes == 0) {
					return Math.max((dtTime - nowTime) / 1000, 1) + "秒钟后";
				} else {
					return Math.max((dtTime - nowTime) / 60000, 1) + "分钟后";
				}
			} else {
				return hour + "小时后";
			}
		} else if (days == 1) {
			return "明天" + asString(dt, "M") + "时";
		} else if (days == 2) {
			return "后天" + asString(dt, "M") + "时";
		} else {
			return asString(dt, "M月d日H时");
		}
	}

	/**
	 * 以友好的方式显示剩余时间
	 *
	 * @param dt
	 *            未来时间
	 * @param now
	 *            当前时间
	 * @return
	 */
	public static String asRemainingTimeFriendly(Date dt, Date now) {
		if (dt == null || now == null || dt.before(now)) {
			return "";
		}
		long times = dt.getTime() - now.getTime();
		long d = times / (1000 * 60 * 60 * 24);
		times = times % (1000 * 60 * 60 * 24);
		long h = times / (1000 * 60 * 60);
		times = times % (1000 * 60 * 60);
		long m = times / (1000 * 60);
		times = times % (1000 * 60);
		long s = times / 1000;
		return (d + "天" + h + "小时" + m + "分钟" + s + "秒");
	}

	/**
	 * 以友好的方式显示剩余时间
	 *
	 * @param date
	 * @return
	 */
	public static String asRemainingTimeFriendly(Date date) {
		if (date == null) {
			return "";
		}
		Date curDate = new Date();
		long splitTime = curDate.getTime() - date.getTime();
		if (splitTime < (30 * ONE_DAY)) {
			if (splitTime < ONE_MINUTE) {
				return "刚刚";
			}
			if (splitTime < ONE_HOUR) {
				return String.format("%d分钟前", splitTime / ONE_MINUTE);
			}

			if (splitTime < ONE_DAY) {
				return String.format("%d小时前", splitTime / ONE_HOUR);
			}

			return String.format("%d天前", splitTime / ONE_DAY);
		}
		String result;
		result = "M月d日";
		return (new SimpleDateFormat(result, Locale.CHINA)).format(date);
	}

	/**
	 * 日期格式化(yyyy-MM-dd)
	 *
	 * @param in
	 * @return
	 */
	public static String asStringDate(Date in) {
		return asString(in, FORMAT_DATE);
	}

	/**
	 * 日期格式化(yyyy-MM-dd)
	 *
	 * @param in
	 * @return
	 */
	public static String asStringDatetime(Date in) {
		return asString(in, FORMAT_DATE_TIME);
	}

	/**
	 * @param b
	 * @param scale
	 * @return
	 */
	public static String asStringNoZero(BigDecimal b, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		if (b == null || b.doubleValue() == 0.0)
			return "";
		return b.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
	}

	/**
	 * 格式化手机号码
	 *
	 * @param b
	 * @param scale
	 * @return
	 */
	public static String asStringMobile(String mobilePhone) {
		if (!isMobilePhone(mobilePhone)) {
			return mobilePhone;
		}
		return mobilePhone.substring(0, 3) + "-" + mobilePhone.substring(3, 7)
				+ "-" + mobilePhone.substring(7);
	}

	/**
	 * 格式化手机号码(部分不可见)
	 *
	 * @param b
	 * @param scale
	 * @return
	 */
	public static String asInvisibleMobile(String mobilePhone) {
		if (!isMobilePhone(mobilePhone)) {
			return mobilePhone;
		}
		return mobilePhone.substring(0, 3) + "****" + mobilePhone.substring(7);
	}

	/**
	 * 格式化卡号(部分不可见)
	 *
	 * @param b
	 * @param scale
	 * @return
	 */
	public static String asInvisibleAccountNo(String accountNo) {
		if (accountNo == null || "".equals(accountNo.trim())) {
			return "";
		}
		return accountNo.substring(0, 4)
				+ " **** **** "
				+ accountNo.substring(accountNo.length() - 4,
						accountNo.length());
	}

	/**
	 * 精度除法
	 *
	 * @param b1
	 * @param b2
	 * @param scale
	 * @return
	 */
	public static BigDecimal divide(BigDecimal b1, BigDecimal b2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		if (b1 == null || b1.doubleValue() == 0.0) {
			return null;
		}
		if (b2 == null || b2.doubleValue() == 0.0) {
			return null;
		}
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 精度除法
	 *
	 * @param b
	 * @param i
	 * @param scale
	 * @return
	 */
	public static BigDecimal divide(BigDecimal b, int i, int scale) {
		return divide(b, new BigDecimal(i), scale);
	}

	/**
	 * 精度除法
	 *
	 * @param b
	 * @param i
	 * @param scale
	 * @return
	 */
	public static BigDecimal divide(int i1, int i2, int scale) {
		BigDecimal b = new BigDecimal(i1);
		return divide(b, new BigDecimal(i2), scale);
	}

	/**
	 * 取得字符串的字节数
	 *
	 * @param in
	 * @return
	 */
	public static int getByteLen(String in) {
		return in.getBytes().length;
	}

	/**
	 * 取得文件的扩展名
	 *
	 * @param fileName
	 * @return
	 */
	public static String getExtensionName(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	/**
	 * 从数组中取得特定元素
	 *
	 * @param ary
	 * @param idx
	 * @return
	 */
	public static String getFromArray(String[] ary, int idx) {
		if (ary == null || ary.length <= idx) {
			return "";
		}
		return ary[idx];
	}

	/**
	 * 从数组中取得特定元素<br/>
	 * 根据内容获得索引index
	 *
	 * @param ary
	 * @param idx
	 * @return
	 */
	public static int getFromArray(String[] ary, String s) {
		if (ary == null || s == null) {
			return 0;
		}
		for (int i = 0; i < ary.length; i++) {
			if (ary[i].equals(s)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * 取月份的最后一天
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth(String year, String month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		return lPad(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2, '0');
	}

	/**
	 * 取得字符串所在的索引位置
	 *
	 * @param s
	 * @param ss
	 * @return
	 */
	public static int getStringIndex(String s, String[] ss) {
		if (AppUtil.isNull(s) || ss == null || ss.length == 0) {
			return -1;
		}
		for (int i = 0; i < ss.length; i++) {
			if (s.equalsIgnoreCase(ss[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 取得模板文件夹的根路径
	 *
	 * @return
	 */
	public static String getTemplatePath() {
		String rtn = getWebRootPath();
		rtn += "template/";
		return rtn;
	}

	/**
	 * 取得Web系统的根路径
	 *
	 * @return
	 */
	public static String getWebRootPath() {
		String rtn = null;
		try {
			String cp = (AppUtil.class.getClassLoader().getResource("").toURI())
					.getPath();
			File cpf = new File(cp);
			rtn = cpf.getParent();
			if (!rtn.endsWith(File.separator)) {
				rtn += File.separator;
			}
			return rtn;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 是否为标准日期型
	 *
	 * @param in
	 * @return
	 */
	public static boolean isDate(String in) {
		return isDate(in, FORMAT_DATE);
	}

	/**
	 * 是否为标准日期型
	 *
	 * @param in
	 * @param format
	 * @return
	 */
	public static boolean isDate(String in, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdf.parse(in);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否为数字
	 *
	 * @param num
	 * @return
	 */
	public static boolean isDecimal(String num) {
		try {
			new BigDecimal(num);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 第一参数是否大于第二参数
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean isGreater(BigDecimal b1, BigDecimal b2) {
		double d1 = asDouble(b1);
		double d2 = asDouble(b2);
		return d1 > d2;
	}

	/**
	 * 第一参数是否大于或等于第二参数
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean isGreaterOrEquals(BigDecimal b1, BigDecimal b2) {
		double d1 = asDouble(b1);
		double d2 = asDouble(b2);
		return d1 >= d2;
	}

	/**
	 * 判断字符串是否为半角
	 *
	 * @param in
	 * @return
	 */
	public static boolean isHalf(String in) {
		if (isNull(in))
			return false;
		return (in.length() == getByteLen(in));
	}

	/**
	 * 判断文件是否为图片文件
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean isImage(String fileName) {
		String ext = getExtensionName(fileName);
		return "GIF".equalsIgnoreCase(ext) || "JPG".equalsIgnoreCase(ext)
				|| "JPEG".equalsIgnoreCase(ext) || "PNG".equalsIgnoreCase(ext)
				|| "BMP".equalsIgnoreCase(ext);
	}

	/**
	 * 判断是否为整数
	 *
	 * @param num
	 * @return
	 */
	public static boolean isInt(String num) {
		try {
			Integer.parseInt(num);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否为邮件地址
	 *
	 * @param mail
	 * @return
	 */
	public static boolean isMail(String mail) {
		// String reg =
		// "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		// 验证是否电子邮箱是否有效的校验规则
		String reg = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		return matcher(reg, mail);
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param in
	 * @return
	 */
	public static boolean isNull(String in) {
		return in == null || "".equals(in.trim());
	}

	/**
	 * 是否为手机号码
	 *
	 * @param in
	 * @return
	 */
	public static boolean isMobilePhone(String in) {
		if (isNull(in)) {
			return false;
		}
		return in.length() == 11;
	}

	/**
	 * 是否为有效的手机号码
	 *
	 * @param mobile
	 * @return
	 */
	public static boolean isMobileValid(String mobile) {
		if (isNull(mobile)) {
			return false;
		}
		// 验证是否手机号是否有效的校验规则
		String reg = "^((13[0-9])|14[5, 7]|(15[^4, \\D])|17[0, 6-8]|(18[0-9]))\\d{8}$";
		return matcher(reg, mobile) != false;
	}

	/**
	 * 是否为身份证号码
	 *
	 * @param in
	 *            身份证号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isIdCard(String in) {
		if (isNull(in)) {
			return false;
		}
		// 身份证号长度为15或18位
		return !(in.length() != 18 && in.length() != 15);
	}

	/**
	 * 是否为有效的身份证号码
	 *
	 * @param in
	 *            身份证号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isIdCardValid(String in) throws ParseException {
		String[] valCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] parityBit = { "7", "9", "10", "5", "8", "4", "2", "1", "6",
				"3", "7", "9", "10", "5", "8", "4", "2" };
		String str = "";
		if (isNull(in)) {
			return false;
		}
		// ================ 除最后一位都为数字 ================
		if (in.length() == 18) {
			// 18位号码除最后一位外，都应为数字
			str = in.substring(0, 17);
		} else if (in.length() == 15) {
			// 身份证15位号码都应为数字
			str = in.substring(0, 6) + "19" + in.substring(6, 15);
		}
		String regNum = "^[0-9]*$";// 判断字符串是否为数字
		if (matcher(regNum, str) == false) {
			return false;
		}

		// ================ 出生年月是否有效 ================
		String strYear = str.substring(6, 10);// 年份
		String strMonth = str.substring(10, 12);// 月份
		String strDay = str.substring(12, 14);// 月份
		String regDate = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";// 校验日期的正则表达式
		if (matcher(regDate, strYear + "-" + strMonth + "-" + strDay) == false) {
			// 身份证生日无效
			return false;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
				|| (gc.getTime().getTime() - s.parse(
						strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
			// 身份证生日不在有效范围
			return false;
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			// 身份证月份无效
			return false;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			// 身份证日期无效
			return false;
		}

		// ================ 地区码时候有效 ================
		Map h = getAreaCode();
		if (h.get(str.substring(0, 2)) == null) {
			// 身份证地区编码错误
			return false;
		}

		// ================ 判断最后一位的值 ================
		int totalmul = 0;
		for (int i = 0; i < 17; i++) {
			totalmul = totalmul
					+ Integer.parseInt(String.valueOf(str.charAt(i)))
					* Integer.parseInt(parityBit[i]);
		}
		int modValue = totalmul % 11;
		String strVerifyCode = valCodeArr[modValue];
		str = str + strVerifyCode;

		if (in.length() == 18) {
			if (str.equals(in) == false) {
				// 不是有效的身份证号码
				return false;
			}
		}
		return true;
	}

	/**
	 * 使用正则表达式匹配
	 *
	 * @param reg
	 *            匹配规则
	 * @param str
	 *            匹配内容
	 * @return
	 */
	public static boolean matcher(String reg, String str) {
		boolean tem = false;
		if (reg == null || str == null) {
			return false;
		}
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		tem = m.matches();
		return tem;
	}

	/**
	 * 获取地区编码
	 *
	 * @return Map对象
	 */
	@SuppressWarnings("unchecked")
	private static Map getAreaCode() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("11", "北京");
		map.put("12", "天津");
		map.put("13", "河北");
		map.put("14", "山西");
		map.put("15", "内蒙古");
		map.put("21", "辽宁");
		map.put("22", "吉林");
		map.put("23", "黑龙江");
		map.put("31", "上海");
		map.put("32", "江苏");
		map.put("33", "浙江");
		map.put("34", "安徽");
		map.put("35", "福建");
		map.put("36", "江西");
		map.put("37", "山东");
		map.put("41", "河南");
		map.put("42", "湖北");
		map.put("43", "湖南");
		map.put("44", "广东");
		map.put("45", "广西");
		map.put("46", "海南");
		map.put("50", "重庆");
		map.put("51", "四川");
		map.put("52", "贵州");
		map.put("53", "云南");
		map.put("54", "西藏");
		map.put("61", "陕西");
		map.put("62", "甘肃");
		map.put("63", "青海");
		map.put("64", "宁夏");
		map.put("65", "新疆");
		map.put("71", "台湾");
		map.put("81", "香港");
		map.put("82", "澳门");
		map.put("91", "国外");
		return map;
	}

	/**
	 * 验证日期是否处于from及to中间
	 *
	 * @param in
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean isBetween(Date in, Date from, Date to) {
		if (in == null) {
			return false;
		}
		if (from != null) {
			if (in.compareTo(from) < 0) {
				return false;
			}
		}
		Calendar cto = Calendar.getInstance();
		cto.setTime(to);
		cto.set(Calendar.HOUR_OF_DAY, 23);
		cto.set(Calendar.MINUTE, 59);
		cto.set(Calendar.SECOND, 59);
		if (to != null) {
			if (cto.getTime().compareTo(in) < 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 是否为今天
	 *
	 * @param dt
	 * @param today
	 * @return
	 */
	public static boolean isToday(Date dt, Date today) {
		if (dt != null && today != null) {
			String ymd1 = asString(dt, "yyyyMMdd");
			String ymd2 = asString(today, "yyyyMMdd");
			return ymd1.equals(ymd2);
		}
		return false;
	}

	/**
	 * 字符串连接
	 *
	 * @param in
	 * @return
	 */
	public static String join(String joiner, Collection<String> c) {
		StringBuffer sb = new StringBuffer();
		if (c != null) {
			for (String s : c) {
				if (!isNull(s)) {
					sb.append(s);
					sb.append(joiner);
				}
			}
			if (sb.length() > joiner.length()) {
				sb = sb.delete(sb.length() - joiner.length(), sb.length());
			}
		}
		return sb.toString();
	}

	/**
	 * 字符串连接
	 *
	 * @param in
	 * @return
	 */
	public static String join(String joiner, List<Integer> in) {
		StringBuffer sb = new StringBuffer();
		for (Integer s : in) {
			if (s != null) {
				sb.append(asString(s));
				sb.append(joiner);
			}
		}
		if (sb.length() > joiner.length()) {
			sb = sb.delete(sb.length() - joiner.length(), sb.length());
		}
		return sb.toString();
	}

	/**
	 * 字符串连接
	 *
	 * @param in
	 * @return
	 */
	public static String join(String joiner, String... in) {
		StringBuffer sb = new StringBuffer();
		for (String s : in) {
			if (!isNull(s)) {
				sb.append(s);
				sb.append(joiner);
			}
		}
		if (sb.length() > joiner.length()) {
			sb = sb.delete(sb.length() - joiner.length(), sb.length());
		}
		return sb.toString();
	}

	/**
	 * 取左边指定长度的字符串
	 *
	 * @param s
	 * @param len
	 * @return
	 */
	public static String left(String s, int len) {
		if (isNull(s)) {
			return s;
		}
		if (len >= s.length()) {
			return s;
		}
		return s.substring(0, len) + "...";
	}

	/**
	 * 左补齐
	 *
	 * @param in
	 * @param length
	 * @param c
	 * @return
	 */
	public static String lPad(String in, int length, char c) {
		for (int i = 0; i < length; i++) {
			in = c + in;
		}
		return in.substring(in.length() - length);
	}

	/**
	 * 最大值
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static BigDecimal max(BigDecimal b1, BigDecimal b2) {
		if (isGreater(b1, b2)) {
			return b1;
		} else {
			return b2;
		}
	}

	/**
	 * 最小值
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static BigDecimal min(BigDecimal b1, BigDecimal b2) {
		if (isGreater(b1, b2)) {
			return b2;
		} else {
			return b1;
		}
	}

	/**
	 * 精度减法(b1-b2)
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static BigDecimal minus(BigDecimal b1, BigDecimal b2) {
		if (b2 == null)
			return b1;
		if (b1 == null)
			return b2.negate();
		return b1.add(b2.negate());
	}

	/**
	 * 精度乘法
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
		if (b1 == null || b2 == null) {
			return null;
		} else {
			return b1.multiply(b2);
		}
	}

	/**
	 * 精度乘法
	 *
	 * @param b
	 * @param i
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal b, int i) {
		return multiply(b, new BigDecimal(i));
	}

	/**
	 * 替换NULL
	 *
	 * @param in
	 * @return
	 */
	public static String repNull(String in) {
		if (isNull(in))
			return "";
		return in;
	}

	/**
	 * @param s
	 * @param len
	 * @return
	 */
	public static String right(String s, int len) {
		if (isNull(s)) {
			return s;
		}
		if (len >= s.length()) {
			return s;
		}
		return s.substring(s.length() - len);
	}

	/**
	 * 保留小数位
	 *
	 * @param b
	 * @param scale
	 * @return
	 */
	public static BigDecimal round(BigDecimal b, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		if (b == null)
			return null;
		return b.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 保留小数位后两位
	 *
	 * @param et
	 * @param s
	 * @param i
	 *            (小数点前保留多少位)
	 * @return
	 */
	public static void asFormat(EditText et, CharSequence s, int i) {
		if (et == null || s == null) {
			return;
		}
		/* 输入小数 */
		if (s.toString().contains(".")) {
			if (s.length() - 1 - s.toString().indexOf(".") > 2) {
				s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
				et.setText(s);
				// 控制光标焦点位置
				et.setSelection(s.length());
			}
		}
		// 例如:输入.
		if (s.toString().trim().substring(0).equals(".")) {
			s = "0" + s;
			et.setText(s);
			// 控制光标焦点位置
			et.setSelection(2);
		}

		/* 输入整数 */
		// 例如:输入01
		if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
			if (!s.toString().substring(1, 2).equals(".")) {
				et.setText(s.subSequence(0, 1));
				// 控制光标焦点位置
				et.setSelection(1);
			}
		}
		if (!s.toString().contains(".")) {
			if (s.length() > i) {
				s = s.toString().subSequence(0, s.toString().length() - 1);
				et.setText(s);
				// 控制光标焦点位置
				et.setSelection(s.length());
			}
		}
	}

	/**
	 * 计算两个日期之间的天数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(Date date1, Date date2) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		date1 = sdf.parse(sdf.format(date1));
		date2 = sdf.parse(sdf.format(date2));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Math.abs(Integer.parseInt(String.valueOf(between_days)));
	}
	
	/**
	 * 解决TextView异常换行问题
	 * 
	 * @param input
	 * @return
	 */
	 public static String toSBC(String str) {
		char c[] = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}
	 //如果图片大于200k  则进行压缩
	 public static File scal(Uri fileUri){
	        String path = fileUri.getPath();
	        File outputFile = new File(path);
	        long fileSize = outputFile.length();
	        final long fileMaxSize = 200 * 1024;
	         if (fileSize >= fileMaxSize) {
	                BitmapFactory.Options options = new BitmapFactory.Options();
	                options.inJustDecodeBounds = true;
	                BitmapFactory.decodeFile(path, options);
	                int height = options.outHeight;
	                int width = options.outWidth;
	 
	                double scale = Math.sqrt((float) fileSize / fileMaxSize);
	                options.outHeight = (int) (height / scale);
	                options.outWidth = (int) (width / scale);
	                options.inSampleSize = (int) (scale + 0.5);
	                options.inJustDecodeBounds = false;
	 
	                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
	                outputFile = new File(createImageFile().getPath());
	                FileOutputStream fos = null;
	                try {
	                    fos = new FileOutputStream(outputFile);
	                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	                    fos.close();
	                } catch (IOException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }              
	                if (!bitmap.isRecycled()) {
	                    bitmap.recycle();
	                }else{
	                    File tempFile = outputFile;
	                    outputFile = new File(createImageFile().getPath());
	                    copyFileUsingFileChannels(tempFile, outputFile);
	                }
	                 
	            }
	         return outputFile;         
	    }
	 
	    public static Uri createImageFile(){
	        // Create an image file name
	        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	        String imageFileName = "JPEG_" + timeStamp;
	        File storageDir = Environment.getExternalStoragePublicDirectory(
	                Environment.DIRECTORY_PICTURES);
	        File image = null;
	        try {
	            image = File.createTempFile(
	                imageFileName,  /* prefix */
	                ".jpg",         /* suffix */
	                storageDir      /* directory */
	            );
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	 
	        // Save a file: path for use with ACTION_VIEW intents
	        return Uri.fromFile(image);
	    }
	    public static void copyFileUsingFileChannels(File source, File dest){
	        FileChannel inputChannel = null;
	        FileChannel outputChannel = null;
	        try {
	            try {
	                inputChannel = new FileInputStream(source).getChannel();
	                outputChannel = new FileOutputStream(dest).getChannel();
	                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        } finally {
	            try {
	                inputChannel.close();
	                outputChannel.close();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    }


}
