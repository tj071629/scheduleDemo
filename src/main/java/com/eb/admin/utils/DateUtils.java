package com.eb.admin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
* @ClassName: DateUtils
* 
* @author Jason.Y
* @date 2018年3月2日 上午9:57:54
*
 */
public class DateUtils {
	
	private static Logger log = LoggerFactory.getLogger(DateUtils.class);

	/**
	 * 默认 yyyy-MM-dd
	 * @author: yangxu
	 * @date: 2015年6月6日 上午9:57:49
	 * @return Date
	 * @throws
	 */
	public static Date parseFromStr(String src, String patten) {
		if (patten == null) {
			patten = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		try {
			return sdf.parse(src);
		} catch (ParseException e) {
			log.error("parseFromStr", e);
		}
		return null;
	}

	/**
	 * 格式化当前时间
	 * @param patten 默认 yyyy-MM-dd
	 * @return
	 */
	public static String getCurrentDate(String patten) {
		return formatDate(new Date(), patten);
	}

	/**
	 * 格式化指定时间
	 * @param patten 默认 yyyy-MM-dd
	 * @return
	 */
	public static String formatDate(Date date, String patten) {
		if (patten == null) {
			patten = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		return sdf.format(date);
	}

	public static Date addDate(Date date, int num) {
		return new Date(date.getTime() + num * (long) 24 * 3600 * 1000);
	}

	public static Date minusDate(Date date, int num) {
		return new Date(date.getTime() - num * (long) 24 * 3600 * 1000);
	}

	/**
	 * 增减日期
	 * @param date 基础日期
	 * @param field 类型 年月日时分秒
	 * @param amount 偏移量
	 * @return
	 */
	public static Date addDate(Date date, int field, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);
		return cal.getTime();
	}

	/**
	 * 增减日期(返回String)
	 * @param int addDay(增加或减少天数，负数表示往回退 addDay 天)
	 * @param String formatter (返回日期格式 比如 yyyy-MM-DD)
	 * @param amount 偏移量
	 * @return
	 */
	public static String addDate(int addDay, String formatter) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, addDay);
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		String mDateTime = sdf.format(calendar.getTime());
		return mDateTime;
	}
	
	/**   
     * 计算两个日期之间相差的天数   
     * @param date1   
     * @param date2   
     * @return   
     */    
    public static int daysBetween(Date date1,Date date2)     
    {     
        Calendar cal = Calendar.getInstance();     
        cal.setTime(date1);     
        long time1 = cal.getTimeInMillis();                  
        cal.setTime(date2);     
        long time2 = cal.getTimeInMillis();          
        long between_days=(time2-time1)/(1000*3600*24);     
             
       return Integer.parseInt(String.valueOf(between_days));            
    } 
	
	/**
	 * 根据开始日期和结束日期返回其中相差的日期
	 * @param Date beginDate
	 * @param Date endDate (返回日期格式 比如 yyyy-MM-DD)
	 * @return List<Date>
	 */
	public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
	    List<Date> lDate = new ArrayList<Date>();
	    Calendar cal = Calendar.getInstance();
	    //使用给定的 Date 设置此 Calendar 的时间
	    cal.setTime(beginDate);
	    boolean bContinue = true;
	    while (bContinue) {
	        //根据日历的规则，为给定的日历字段添加或减去指定的时间量
	        cal.add(Calendar.DAY_OF_MONTH, 1);
	        // 测试此日期是否在指定日期之后
	        if (endDate.after(cal.getTime())) {
	            lDate.add(cal.getTime());
	        } else {
	            break;
	        }
	    }
	    lDate.remove(beginDate);//把开始时间从集合中除去
	    lDate.add(endDate);//把结束时间加入到集合中
	    return lDate;
	}
	
	/**
	 * 根据开始日期和结束日期返回其中相差的日期(包括开始和结束日期)
	 * @param Date beginDate
	 * @param Date endDate (返回日期格式 比如 yyyy-MM-DD)
	 * @return List<Date>
	 */
	public static List<Date> getDatesBetweenTwoDateAll(String beginDateStr, String endDateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = null;
		Date endDate = null;
		try {
			beginDate = sdf.parse(beginDateStr);
			endDate = sdf.parse(endDateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Date> lDate = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		//使用给定的 Date 设置此 Calendar 的时间
		if(!beginDateStr.equals(endDateStr)){
			cal.setTime(beginDate);
		}
		boolean bContinue = true;
		lDate.add(beginDate);//把结束时间加入到集合中
		while (bContinue) {
			//根据日历的规则，为给定的日历字段添加或减去指定的时间量
			cal.add(Calendar.DAY_OF_MONTH, 1);
			// 测试此日期是否在指定日期之后
			if (endDate.after(cal.getTime())) {
				lDate.add(cal.getTime());
			} else {
				break;
			}
		}
		if(!beginDateStr.equals(endDateStr)){
			lDate.add(endDate);//把结束时间加入到集合中
		}
		return lDate;
	}
	
	
	/**
	 * 转换日期得到指定格式的日期字符串
	 * 
	 * @param formatString
	 *            需要把目标日期格式化什么样子的格式。例如,yyyy-MM-dd HH:mm:ss
	 * @param targetDate
	 *            目标日期
	 * @return
	 */
	public static String convertDate2String(String formatString, Date targetDate) {
		SimpleDateFormat format = null;
		String result = null;
		if (targetDate != null) {
			format = new SimpleDateFormat(formatString);
			result = format.format(targetDate);
		} else {
			return null;
		}
		return result;
	}
	
	/**
	 * 获取某一指定天的Date类型
	 * @param int day  偏移量 （天：正：从今往后推 day天，负：从今往前推day天）
	 * @return List<Date>
	 */
	public static Date getSomeDayDate(int day){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,  day);
		return cal.getTime();
		
	}

	/**
	 * 是否同一天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int isSameDay(Date date1, Date date2) {
		return formatDate(date1, null).compareTo(formatDate(date2, null));
	}

	/**
	 * 获取0点
	 * @param date
	 * @return
	 */
	public static Date getDayStartTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取最后时间
	 * @param date
	 * @return
	 */
	public static Date getDayEndTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	/**
     * 切割时间段
     * 支持每月/每天/每小时/每分钟交易金额(可分应用平台统计)
     * @param dateType 日期类型 M(每月)/D(每天)/H(每小时)/N(每分钟)
     * M：日期段应为当年月份以内 且 日期必须是01 时分秒必须是 00:00:00  例如：2016-06-01 00:00:00 2016-10-01 00:00:00
     * D: 日期段应为一月内 且 日期应当是01或31  时分秒必须是 00:00:00   例如：2016-10-01 00:00:00 2016-10-31 00:00:00
     * H：日期段应为一天内  且 时分秒必须是 00:00:00   例如：2016-10-01 00:00:00 2016-10-02 00:00:00
     * N：日期段应为一小时内  日期应相同 且 分秒必须是 xx:00:00   例如：2016-10-02 22:00:00 2016-10-02 23:00:00

     * @param dateType 交易类型 M/D/H/T -->每月/每天/每小时/每分钟
     * @param start    yyyy-MM-dd HH:mm:ss
     * @param end      yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static List<Date> cutDate(String dateType, String start, String end) {
        try {
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dBegin = sdf.parse(start);
            Date dEnd = sdf.parse(end);
            return findDates(dateType, dBegin, dEnd);
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
        return null;
    }

    public static List<Date> findDates(String dateType, Date dBegin, Date dEnd) throws Exception {
        List<Date> listDate = new ArrayList<>();
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        listDate.add(calBegin.getTime());
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        while (calEnd.after(calBegin)) {
            switch (dateType) {
                case "M":
                    calBegin.add(Calendar.MONTH, 1);
                    break;
                case "D":
                    calBegin.add(Calendar.DAY_OF_YEAR, 1);
                    break;
                case "H":
                    calBegin.add(Calendar.HOUR, 1);
                    break;
                case "T":
                    calBegin.add(Calendar.MINUTE, 10);
                    break;
                default:
                    return null;
            }
            if (calEnd.after(calBegin))
                listDate.add(calBegin.getTime());
            else {
                listDate.add(calEnd.getTime());
                break;
            }
        }
        return listDate;
    }
    
	public static void main(String[] args) {

		/*String strDate = "2008-10-19";

		System.out.println(parseFromStr("201305", null).toLocaleString());

		// 定义模板从字符串中提取数字
		String path1 = "yyyy-MM-dd";
		// 定义模板将取出来的日期转换成指定格式
		// String path2 = "yyyy年MM月dd日  HH时mm分ss秒SS毫秒";
		SimpleDateFormat sdf1 = new SimpleDateFormat(path1);
		// SimpleDateFormat sdf2 = new SimpleDateFormat(path2);
		Date d = null;
		try {
			d = sdf1.parse(strDate);// 从给定的字符串中提取出来日期

			System.out.println(d.toLocaleString());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("", e);
		}
		// System.out.println(sdf2.format(d));//将日期变为新的格式

		Calendar current = Calendar.getInstance();

		// current.setTime(date)

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(current.getTime()));
		System.out.println(current.get(Calendar.DAY_OF_MONTH));
		current.add(Calendar.MONTH, -1);
		// current.set(Calendar.DAY_OF_MONTH, 1);

		System.out.println(sdf.format(current.getTime()));
*/
		/*String begin_date = "2016-10-13";
		String end_date = "2016-10-12";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			
			System.err.println(isSameDay(sdf.parse(begin_date),sdf.parse(end_date)));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Date nowTime1 = DateUtils.parseFromStr(DateUtils.formatDate(new Date(), "yyyy-MM-dd"),
				"yyyy-MM-dd");
		Date nowTime2 = parseFromStr("2018-03-21", "yyyy-MM-dd");
		System.err.println(daysBetween(nowTime1, nowTime2));
		
	}
	

}
