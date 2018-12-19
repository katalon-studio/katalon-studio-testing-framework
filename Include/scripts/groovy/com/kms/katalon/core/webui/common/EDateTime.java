package com.kms.katalon.core.webui.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EDateTime {

	public EDateTime() {

	}

	/**
	 * Get addDaysToDate and return in specified format
	 * 
	 * @param int - amount after/before current date
	 * @param String
	 *            - date
	 * @param string
	 *            - formatted date
	 * @param int - calendarField
	 * @return string formatted addDaysToDate
	 */
	public Result addToDate(int amount, String date, String format,
			int calendarField) {

		String defaultFormat = "MM/dd/yyyy";
		try {
			if (format == null || format.isEmpty()) {
				format = defaultFormat;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date currentDate = sdf.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			calendar.add(calendarField, amount);
			return Result.reportHappyCase(sdf.format(calendar.getTime()));
		} catch (Exception e) {
			return Result.reportUnHappyCase(false, true, "INT261",
					e.getMessage());
		}
	}
}
