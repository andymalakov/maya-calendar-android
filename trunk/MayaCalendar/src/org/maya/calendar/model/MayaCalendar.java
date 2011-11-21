package org.maya.calendar.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/** 
 * Translates Gregorian <=> Maya calendar based on correlation point specified in constructor. 
 * Example:
 * <pre>
 * MayaCalendar mc = MayaCalendar.create (MayaCalendar.GMT_CORRELATION);
 * mc.set (7,16,6,16,18);
 * String gregorianDate = mc.toGregorianDate();
 * </pre>
 */ 
public class MayaCalendar {
	
	/** 
	 * GMT (Goodman - Martinez - Thompson) correlation.
	 * Most commonly accepted according to Wikipedia. 
	 */
	public static final int GMT_CORRELATION = 584283;
	
	/** GMT+2 correlation (AKA Thompson - Lounsbury).
	 * most commonly accepted according to M.Coe */
	public static final int THOMPSON_LOUNSBURY = 584285; // 
	
	
	private static final long MILLISECONDS_PER_DAY = 24L *60*60*1000;
	
	private final SimpleDateFormat format = new SimpleDateFormat ("d MMM, yyyy G");
	private final Calendar greatCycleStartDate;
	
	private int daysSinceGreatCycle;
	
//	/** @return Maya calendar correlated using Goodman-Martinez-Thompson (GMT+0) date */
//	public static MayaCalendar createThompson() {
//		Calendar c = createCalendar();  
//		c.set(Calendar.ERA, GregorianCalendar.BC);
//		c.set(Calendar.YEAR, 3114);
//		c.set(Calendar.MONTH, Calendar.AUGUST);
//		c.set(Calendar.DAY_OF_MONTH, 11);
//		c.set(Calendar.HOUR_OF_DAY, 0);
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
//		c.set(Calendar.MILLISECOND, 1);
//		
//		return new MayaCalendar(c);
//	}
//
//	/** @return Maya calendar correlated using Goodman-Martinez-Thompson + 2 (GMT+2) date */
//	public static MayaCalendar createThompson2() {
//		Calendar c = createCalendar();  
//		c.set(Calendar.ERA, GregorianCalendar.BC);
//		c.set(Calendar.YEAR, 3114);
//		c.set(Calendar.MONTH, Calendar.AUGUST);
//		c.set(Calendar.DAY_OF_MONTH, 13);
//		c.set(Calendar.HOUR_OF_DAY, 0);
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
//		c.set(Calendar.MILLISECOND, 1);
//		
//		return new MayaCalendar(c);
//	}
//	private static Calendar createCalendar () {
//	TimeZone mx = TimeZone.getTimeZone("MX"); // Mexico/Yucatan
//	GregorianCalendar result = (GregorianCalendar) Calendar.getInstance(mx);
//	//TODO: result.setGregorianChange (new Date(Long.MIN_VALUE)); // switching to pure Gregorian mode (not Julian)
//	return result;
//}
	
	/** Constructs an instance of MayaCalendar based on given correlation number. */
	public static MayaCalendar create(int correlationNumber) {
		Calendar c = getJulianDate();
		
		c.add (Calendar.DAY_OF_YEAR, correlationNumber);
		
		return new MayaCalendar(c);
	}

	private static Calendar getJulianDate() {
		//http://en.wikipedia.org/wiki/Julian_day_number => 12:00 January 1, 4713 BC, Monday
		TimeZone mx = TimeZone.getTimeZone("MX"); // Mexico/Yucatan
		GregorianCalendar c = (GregorianCalendar) Calendar.getInstance(mx);
		
		// Switching to proleptic Gregorian calendar used by most Maya scholars (extend Georgian calendar prior to 1581)
		//c.setGregorianChange(new Date(Long.MIN_VALUE)); 
		c.set(Calendar.ERA, GregorianCalendar.BC);
		c.set(Calendar.YEAR, 4713);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 1);
		return c;
	}

	/**
	 * @param correlationPoint Correlation point, beginning of the last Great Cycle in Maya calendar
	 * 
	 * @see #create(int)
	 * 
	 */
	public MayaCalendar (Calendar correlationDate) {
		this.greatCycleStartDate = correlationDate;
		
		format.setTimeZone(TimeZone.getTimeZone("MX"));
	}
	
	public int getDaysSinceGreatCycle() {
		return daysSinceGreatCycle;
	}

	/** parses Date specified in Long Count format, e.g. "12.19.19.17.19" */ 
	public void parseLongCountDate (String longCountDate) {
		String [] components = longCountDate.split("\\.");
		try {
			if (components.length != 5)
				throw new Exception("Expecting 5 numbers separated by dots");
			int baktuns = Integer.valueOf(components[0]);
			int katuns = Integer.valueOf(components[1]);
			int tuns = Integer.valueOf(components[2]);
			int winals = Integer.valueOf(components[3]);
			int kins = Integer.valueOf(components[4]);
			set (baktuns, katuns, tuns, winals, kins);
		} catch (Throwable e) {
			throw new IllegalArgumentException("Invalid long count date format: " + e.getMessage());
		}
	}
	
	/** Set date to given long count date */
	public void set (int baktuns, int katuns, int tuns, int uinals, int kins) {
		assert MayaTimeUnit.Kin.toDays (1) == 1;
		daysSinceGreatCycle =
			MayaTimeUnit.Baktun.toDays (baktuns) +
			MayaTimeUnit.Katun.toDays(katuns) +
			MayaTimeUnit.Tun.toDays(tuns) +
			MayaTimeUnit.Winal.toDays(uinals) +
			kins;
	}
	
	/** Set date to a given Gregorian/Julian calendar date */
	public void set (Calendar c) {
		//long nDays = TimeUnit.MILLISECONDS.toDays(c.getTimeInMillis() - correlationPoint.getTimeInMillis());
		long nDays = (c.getTimeInMillis() - greatCycleStartDate.getTimeInMillis()) / MILLISECONDS_PER_DAY;

		if (nDays < 0)
			throw new RuntimeException("Date is too small " + c.getTime());
		if (nDays > Integer.MAX_VALUE)
			throw new RuntimeException("Date is too large " + c.getTime());
		
		daysSinceGreatCycle = (int) nDays; 
	}
	
	/** @return Long count format int[5] = { baktun, katun, tun, winal, tin } */
	public int[] toLongCount () {
		
		int [] result = new int [5];
		int date = daysSinceGreatCycle;
		result [MayaTimeUnit.Baktun.ordinal()] = date / MayaTimeUnit.Baktun.numberOfDays; // baktuns 
		date = date % MayaTimeUnit.Baktun.numberOfDays;
		
		result [MayaTimeUnit.Katun.ordinal()] = date / MayaTimeUnit.Katun.numberOfDays; // katun
		date = date % MayaTimeUnit.Katun.numberOfDays;
		
		result [MayaTimeUnit.Tun.ordinal()] = date / MayaTimeUnit.Tun.numberOfDays; // tun
		date = date % MayaTimeUnit.Tun.numberOfDays;

		result[MayaTimeUnit.Winal.ordinal()] = date / MayaTimeUnit.Winal.numberOfDays; // winal
		result[MayaTimeUnit.Kin.ordinal()] = date % MayaTimeUnit.Winal.numberOfDays; // kin
		
		return result;
	}
	
	/** @return day name number in Tzolk'in calendar, e.g. it returns 0 (Ajaw) for the day "4 Ajaw" */
	public Tzolkin toTzolkinDayName () {
		// The Tzolk'in date is counted forward from 4 Ajaw.
		return Tzolkin.DAYS[(daysSinceGreatCycle + 19) % 20]; // relative to Ajaw
	}
	
	/** @return day number in Tzolk'in calendar, e.g. it returns 4 for the day "4 Ajaw" */
	public int toTzolkinDayNumber () {
		// The Tzolk'in date is counted forward from 4 Ajaw.
		return (daysSinceGreatCycle + 4) % 13;
	}

	
	/** @return day name number in Haab calendar, e.g. it returns Yaxkin (5) for the day "14 Yaxk'in" */
	public Haab toHaabDayName () {
		int d = (daysSinceGreatCycle + 349) % 365;
		return Haab.DAYS[d / 20]; 
	}
	
	/** @return day number in Haab calendar, e.g. it returns 14 for the day "14 Yaxk'in" */
	public int toHaabDayNumber () {
		int d = (daysSinceGreatCycle + 349) % 365;
		return d % 20 - 1;
	}
	
	/** @return Long count representation of currently set date (e.g. "7.16.6.16.18") */
	public String toLongCountString () {
		int [] lc = toLongCount();
		return String.valueOf(lc[0]) + '.' + lc[1] + '.' + lc[2] + '.' + lc[3] + '.' + lc[4];
	}

	/** @return Gregorian calendar representation of currently set date  */
	public String toGregorianString () {
		Calendar c = toGregorianDate ();
		return format.format(c.getTime());
	}

	/** @return Converts currently defined date into Gregorian calendar */
	public Calendar toGregorianDate () {
		Calendar c = (Calendar)greatCycleStartDate.clone();
		c.add(Calendar.DAY_OF_YEAR, daysSinceGreatCycle);
		return c;
	}
	
	public int getCorrelationNumber () {
		Calendar julianDate = getJulianDate();
		Calendar date = toGregorianDate();
		return (int) (( date.getTimeInMillis() - julianDate.getTimeInMillis()) / MILLISECONDS_PER_DAY );
	}
	
	public String toString () {
		return format.format(greatCycleStartDate.getTime()) + " + " + String.valueOf(daysSinceGreatCycle);
	}

	
}
