/*
 *   casmi
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 *  casmi is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package casmi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Date utility class.
 * 
 * @author T. Takeuchi
 * 
 */
public class DateUtil {

    /**
     * Returns the current time in milliseconds.
     * 
     * @return
     *         The difference, measured in milliseconds, between the current time
     *         and midnight, January 1, 1970 UTC.
     */
    public static int millis() {

        return (int)System.currentTimeMillis();
    }

    /**
     * Returns the current second as a value from 0 - 59.
     * 
     * @return
     *         The second as a int value from 0 - 59.
     */
    public static int second() {

        return new GregorianCalendar().get(Calendar.SECOND);
    }

    /**
     * Returns the current minute as a value from 0 - 59.
     * 
     * @return
     *         The minute as a int value from 0 - 59.
     */
    public static int minute() {

        return new GregorianCalendar().get(Calendar.MINUTE);
    }

    /**
     * Returns the current hour as a value from 0 - 23.
     * 
     * @return
     *         The hour as a int value from 0 - 23.
     */
    public static int hour() {

        return new GregorianCalendar().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Returns the current day as a value from 0 - 31.
     * 
     * @return
     *         The day as a int value from 0 - 31.
     */
    public static int day() {

        return new GregorianCalendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Returns the current month as a value from 1 - 12.
     * 
     * @return
     *         The month as a int value from 1 - 12.
     */
    public static int month() {

        return new GregorianCalendar().get(Calendar.MONTH) + 1;
    }

    /**
     * Returns the current year as a value (2011, 2012, 2013, etc).
     * 
     * @return
     *         The year as a int value.
     */
    public static int year() {

        return new GregorianCalendar().get(Calendar.YEAR);
    }
    
    public static final int secondToMillis(int second) {
        
        return second * 1000;
    }
    
    public static final int minuteToMillis(int minute) {
        
        return secondToMillis(minute * 60);
    }
    
    public static final int hourToMillis(int hour) {
        
        return minuteToMillis(hour * 60);
    }
    
    public static final int dayToMillis(int day) {
        
        return hourToMillis(day * 24);
    }

    /**
     * Formats a Date into a date/time string.
     * 
     * @param date
     *            The time value to be formatted into a time string.
     * @param pattern
     *            The pattern describing the date and time format.
     * 
     * @return
     *         The formatted time string.
     */
    public static String format(Date date, String pattern) {

        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * Parses text from the beginning of the given string to produce a date.
     * 
     * @param source
     *            A String whose beginning should be parsed.
     * @param pattern
     *            The pattern describing the date and time format.
     * 
     * @return
     *         A Date parsed from the string.
     * 
     * @throws ParseException
     *             If the beginning of the specified string cannot be parsed.
     */
    public static Date parse(String source, String pattern) throws ParseException {

        return new SimpleDateFormat(pattern).parse(source);
    }

    /**
     * Convert a java.util.Date object to a java.sql.Date object.
     * 
     * @param date A java.util.Date object.
     * @return A java.sql.Date object.
     */
    public static java.sql.Date toSqlDate(java.util.Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new java.sql.Date(cal.getTimeInMillis());
    }

    /**
     * Convert a java.sql.Date object to a java.util.Date object.
     * This method is equal to simple upcast.
     * 
     * @param date A java.sql.Date object.
     * @return A java.util.Date object.
     */
    public static java.util.Date toUtilDate(java.sql.Date date) {

        return (java.util.Date)date;
    }
}
