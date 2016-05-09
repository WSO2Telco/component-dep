package com.wso2telco.analyticscommonfunctions;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class DateTimeUDF {

    private final Calendar cal = Calendar.getInstance();
    
    public Integer getYear(Long timeStamp) throws ParseException {
        synchronized (cal) {
            cal.setTimeInMillis(timeStamp);
            return cal.get(Calendar.YEAR);
        }
    }

    public Integer getMonth(Long timeStamp) throws ParseException {
        synchronized (cal) {
            cal.setTimeInMillis(timeStamp);
            return cal.get(Calendar.MONTH) + 1;
        }
    }

    public Integer getDay(Long timeStamp) throws ParseException {
        synchronized (cal) {
            cal.setTimeInMillis(timeStamp);
            return cal.get(Calendar.DAY_OF_MONTH);
        }
    }

    public Integer getHour(Long timeStamp) throws ParseException {
        synchronized (cal) {
            cal.setTimeInMillis(timeStamp);
            return cal.get(Calendar.HOUR_OF_DAY);
        }
    }

    public Integer getMinute(Long timeStamp) throws ParseException {
        synchronized (cal) {
            cal.setTimeInMillis(timeStamp);
            return cal.get(Calendar.MINUTE);
        }
    }

    public Integer getSeconds(Long timeStamp) throws ParseException {
        synchronized (cal) {
            cal.setTimeInMillis(timeStamp);
            return cal.get(Calendar.SECOND);
        }
    }

    public String getMonthStartingTime(Integer year, Integer month) {
        synchronized (cal) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return String.valueOf(cal.getTimeInMillis());
        }
    }

    public String getDateStartingTime(Integer year, Integer month, Integer date) {
        synchronized (cal) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return String.valueOf(cal.getTimeInMillis());
        }
    }

    public String getHourStartingTime(Integer year, Integer month, Integer date, Integer hour) {
        synchronized (cal) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, date);
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return String.valueOf(cal.getTimeInMillis());
        }
    }

    public String getMinuteStartingTime(Integer year, Integer month, Integer date, Integer hour, Integer minute) {
        synchronized (cal) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, date);
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return String.valueOf(cal.getTimeInMillis());
        }
    }

    public String getSecondStartingTime(Integer year, Integer month, Integer date, Integer hour, Integer minute,
                                        Integer second) {
        synchronized (cal) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, date);
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, second);
            cal.set(Calendar.MILLISECOND, 0);
            return String.valueOf(cal.getTimeInMillis());
        }
    }
    
    public String getFormattedDate(Long time, String format){
    	Timestamp timeStamp = new Timestamp(time);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(timeStamp);

    }
}