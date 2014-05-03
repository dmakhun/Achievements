package com.softserve.edu.manager;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

public class ScheduleRowsManagerTest {

	@Test
	public void findMonday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2014, 2, 20);
		ScheduleRowsManagerImplementation srm = new ScheduleRowsManagerImplementation(calendar);
		Calendar calendarTrue = Calendar.getInstance();
		calendarTrue.set(2014, 2, 17, 8, 0);
		calendar = srm.findMonday();
		assertTrue(calendarTrue.get(Calendar.MONTH) == calendar
				.get(Calendar.MONTH)
				&& calendarTrue.get(Calendar.DAY_OF_MONTH) == calendar
						.get(Calendar.DAY_OF_MONTH)
				&& calendarTrue.get(Calendar.YEAR) == calendar
						.get(Calendar.YEAR));
	}
}