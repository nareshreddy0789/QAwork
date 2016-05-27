/**
 * 
 */
package com.apple.carnival.qa.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author harisha
 *
 */
public class DateUtil {

	private static DateFormat dateFormat = null;
	private static Date date = new Date();

	public static String getDate(String format) {
		dateFormat = new SimpleDateFormat(format);
		String scheduledDate = dateFormat.format(date);

		return scheduledDate;

	}
}
