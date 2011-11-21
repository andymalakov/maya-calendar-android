package org.maya.calendar.model.test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.maya.calendar.model.*;

import org.junit.Test;


public class TestMayaCalendar {

	
	@Test
	public void testZero () {
		MayaCalendar mc = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
		mc.set(0,0,0,0,0);
		assertEquals (0, mc.getDaysSinceGreatCycle());
		assertEquals ("6 Sep, 3114 BC", mc.toGregorianString());
	}
	
	/** http://en.wikipedia.org/wiki/Tres_Zapotes */
	@Test
	public void testStellaC_TresZapotes () {
		MayaCalendar mc = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
		mc.parseLongCountDate("7.16.6.16.18");
		assertEquals ("3 Sep, 0032 BC", mc.toGregorianString());

		GregorianCalendar gc = new GregorianCalendar ();
		gc.set(Calendar.ERA, GregorianCalendar.BC);
		gc.set(Calendar.YEAR, 32);
		gc.set(Calendar.MONTH, Calendar.SEPTEMBER);
		gc.set(Calendar.DAY_OF_MONTH, 3);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 1);
		mc.set(gc);
		assertEquals ("7.16.6.16.18", mc.toLongCountString());
	}

	/**  */
	@Test
	public void testDoomsDay () {
		MayaCalendar mc = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
		mc.set(new GregorianCalendar (2012, Calendar.DECEMBER, 20));
		assertEquals ("12.19.19.17.19", mc.toLongCountString());

		mc.set(new GregorianCalendar (2012, Calendar.DECEMBER, 21));
		assertEquals ("13.0.0.0.0", mc.toLongCountString());
		
		assertEquals ("21 Dec, 2012 AD", mc.toGregorianString());

	}
	
	
	@Test
	public void testToday () {
		GregorianCalendar gc = new GregorianCalendar (2010, Calendar.SEPTEMBER, 28);
		MayaCalendar mc = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
		mc.set (gc);
		assertEquals ("12.19.17.13.5", mc.toLongCountString());
		assertEquals ("28 Sep, 2010 AD", mc.toGregorianString());
		assertEquals ("6 Sep, 3114 BC + 1871185", mc.toString());
	}
	
	@Test
	public void testToday2 () {
		// Today, 18:01, Saturday October 9, 2010 (UTC), in the Long Count is 12.19.17.13.16 (GMT correlation).
		
		GregorianCalendar gc = new GregorianCalendar ();
		//gc.set(Calendar.ERA, GregorianCalendar.AD);
		gc.set(Calendar.YEAR, 2010);
		gc.set(Calendar.MONTH, Calendar.OCTOBER);
		gc.set(Calendar.DAY_OF_MONTH, 9);
		gc.set(Calendar.HOUR_OF_DAY, 18);
		gc.set(Calendar.MINUTE, 1);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		
		MayaCalendar mc = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
		mc.set(gc);
		assertEquals ("12.19.17.13.16", mc.toLongCountString());
	}
	
	@Test
	public void tesGMT() {
		MayaCalendar thompson = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
		thompson.set (0, 0, 0, 0, 0);
		assertEquals ("6 Sep, 3114 BC", thompson.toGregorianString()); //Julian?
		assertEquals ("6 Sep, 3114 BC + 0", thompson.toString());
	}

	@Test
	public void testGMT2 () {
		MayaCalendar thompson = MayaCalendar.create(MayaCalendar.THOMPSON_LOUNSBURY);
		thompson.set (0, 0, 0, 0, 0);
		assertEquals ("8 Sep, 3114 BC", thompson.toGregorianString());
		assertEquals ("8 Sep, 3114 BC + 0", thompson.toString());
	}
	
	@Test
	public void testWikiDates () {
		MayaCalendar mc = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
		
		assertDates (mc, "7.16.3.2.13",  "5 Dec, 36 BC");
		assertDates (mc, "7.16.6.16.18", "1 Sept, 32 BC");
		assertDates (mc, "7.19.15.7.12", "2 Mar, 34 AD");
		assertDates (mc, "8.3.2.10.15",  "19 May, 103 AD");
		assertDates (mc, "8.4.5.17.11",  "3 Jun, 126 AD");
		assertDates (mc, "8.5.3.3.5",    "19 May, 193 AD");
		assertDates (mc, "8.5.16.9.7",   "11 Jul, 156 AD");
		assertDates (mc, "8.6.2.4.17",   "12 Mar, 162 AD");
		
		
		
	}
	
	@Test
	public void testFAMSI () {
		MayaCalendar mc = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
		assertDates (mc, "12.19.17.13.17", "10 Oct, 2010 AD");
		assertHaab (mc, "10 Yax");
		assertTzolkin (mc, "7 Caban");
	}

	/// Tzolkin
	
	@Test
	public void testTzolkinBase () {
		assertEquals (20, Tzolkin.DAYS.length);
		
		MayaCalendar mc = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
		mc.set (0,0,0,0,0);
		
		// Cycle begins at 4 Ajaw
		assertEquals (Tzolkin.Ahau, mc.toTzolkinDayName());
		assertEquals (4, mc.toTzolkinDayNumber());
	}
	
	
	/// Haab
	
	@Test
	public void testHaabBase () {
		assertEquals (19, Haab.DAYS.length);
		
		MayaCalendar mc = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
		mc.set (0,0,0,0,0);
		
		assertEquals (Haab.Pop, mc.toHaabDayName());
		assertEquals (0, mc.toHaabDayNumber());
	}
	
	@Test
	public void testHaabTzolkinDate () {
		MayaCalendar mc = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
		
		assertHaab    (mc, "9.12.2.0.16", "14 Yaxkin");
		assertTzolkin (mc, "9.12.2.0.16", "5 Cib");
		
		
		assertHaab    (mc, "12.19.17.13.17", "10 Yax");
		assertTzolkin (mc, "12.19.17.13.17", "7 Caban");
	}

	@Test
	public void testCorrelationNumbers () {
		MayaCalendar mc = MayaCalendar.create(MayaCalendar.GMT_CORRELATION);
		assertEquals (MayaCalendar.GMT_CORRELATION, mc.getCorrelationNumber());
		
		mc = MayaCalendar.create(MayaCalendar.THOMPSON_LOUNSBURY);
		assertEquals (MayaCalendar.THOMPSON_LOUNSBURY, mc.getCorrelationNumber());
	}
	
	private static void assertTzolkin (MayaCalendar mc, String longCountDate, String expectedTzolkin) {
		mc.parseLongCountDate (longCountDate);
		String actualTzolkin = String.valueOf(mc.toTzolkinDayNumber()) + ' ' + mc.toTzolkinDayName().name();
		assertEquals ("Tzolkin date", expectedTzolkin, actualTzolkin);
	}
	
	private static void assertTzolkin (MayaCalendar mc, String expectedTzolkin) {
		String actualTzolkin = String.valueOf(mc.toTzolkinDayNumber()) + ' ' + mc.toTzolkinDayName().name();
		assertEquals ("Tzolkin date", expectedTzolkin, actualTzolkin);
	}

	private static void assertHaab (MayaCalendar mc, String longCountDate, String expectedHaab) {
		mc.parseLongCountDate (longCountDate);
		String actualHaab = String.valueOf(mc.toHaabDayNumber()) + ' ' + mc.toHaabDayName().name();
		assertEquals ("Haab date", expectedHaab, actualHaab);
	}

	private static void assertHaab (MayaCalendar mc, String expectedHaab) {
		String actualHaab = String.valueOf(mc.toHaabDayNumber()) + ' ' + mc.toHaabDayName().name();
		assertEquals ("Haab date", expectedHaab, actualHaab);
	}
	
	private static void assertDates (MayaCalendar mc, String longCountDate, String gregorianDate) {
		mc.parseLongCountDate (longCountDate);
		assertEquals ("Long count date " + longCountDate, gregorianDate, mc.toGregorianString());
	}
	
//	/** http://www.mesoweb.com/resources/handbook/WH2004.pdf */
//	@Test
//	public void t2 () {
//		mc.parseLongCountDate("9.15.6.14.6");
//		assertEquals (1406446, mc.getDaysSinceGreatCycle());
//		assertEquals ("2 May, 738  AD", mc.toGregorianString());
//		
//		long daysSinceJulianDay = 584285 + mc.getDaysSinceGreatCycle();
//		
//
//		
//	}
}
