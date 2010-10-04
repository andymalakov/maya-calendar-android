package org.sf.maya.calendar.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/** Translates Gregorian <=> Maya calendar based on correlation point specified in constructor */ 
public class MayaCalendar {
	
	private final SimpleDateFormat format = new SimpleDateFormat ("d MMM, yyyy G");
	private final Calendar correlationPoint;
	
	private int daysSinceGreatCycle;
	
	/** @return Maya calendar correlated using Goodman-Martinez-Thompson (GMT+0) date */
	public static MayaCalendar getThompson() {
		Calendar c = createCalendar();  
		c.set(Calendar.ERA, GregorianCalendar.BC);
		c.set(Calendar.YEAR, 3114);
		c.set(Calendar.MONTH, Calendar.AUGUST);
		c.set(Calendar.DAY_OF_MONTH, 11);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 1);
		
		return new MayaCalendar(c);
	}

	/** @return Maya calendar correlated using Goodman-Martinez-Thompson + 2 (GMT+2) date */
	public static MayaCalendar getThompson2() {
		Calendar c = createCalendar();  
		c.set(Calendar.ERA, GregorianCalendar.BC);
		c.set(Calendar.YEAR, 3114);
		c.set(Calendar.MONTH, Calendar.AUGUST);
		c.set(Calendar.DAY_OF_MONTH, 13);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 1);
		
		return new MayaCalendar(c);
	}

	/**
	 * @param correlationPoint Correlation point, beginning of the last Great Cycle in Maya calendar
	 * 
	 * @see #getThompson()
	 * @see #getThompson2()
	 * 
	 */
	public MayaCalendar (Calendar correlationPoint) {
		this.correlationPoint = correlationPoint;
		
		format.setTimeZone(TimeZone.getTimeZone("MX"));
	}
	
	public int getDaysSinceGreatCycle() {
		return daysSinceGreatCycle;
	}

	private static Calendar createCalendar () {
		TimeZone mx = TimeZone.getTimeZone("MX"); // Mexico/Yucatan
		GregorianCalendar result = (GregorianCalendar) Calendar.getInstance(mx);
		result.setGregorianChange (new Date(Long.MIN_VALUE)); // switching to pure Gregorian mode (not Julian)
		//result.setLenient(true);
		return result;
	}
	
	
	/** parses Date specified in Long Count format, e.g. "12.19.19.17.19" */ 
	public void parseLongCountDate (String longCountDate) {
		String [] components = longCountDate.split("\\.");
		try {
			int baktuns = Integer.valueOf(components[0]);
			int katuns = Integer.valueOf(components[1]);
			int tuns = Integer.valueOf(components[2]);
			int winals = Integer.valueOf(components[3]);
			int kins = Integer.valueOf(components[4]);
			set (baktuns, katuns, tuns, winals, kins);
		} catch (Throwable e) {
			throw new RuntimeException("Invalid long count date format", e);
		}
	}
	
	public void set (int baktuns, int katuns, int tuns, int uinals, int kins) {
		assert MayaTimeUnit.Kin.toDays (1) == 1;
		daysSinceGreatCycle =
			MayaTimeUnit.Baktun.toDays (baktuns) +
			MayaTimeUnit.Katun.toDays(katuns) +
			MayaTimeUnit.Tun.toDays(tuns) +
			MayaTimeUnit.Winal.toDays(uinals) +
			kins;
	}
	
	private static final long MILLISECONDS_PER_DAY = 24L *60*60*1000;
	
	public void set (Calendar c) {
		//long nDays = TimeUnit.MILLISECONDS.toDays(c.getTimeInMillis() - correlationPoint.getTimeInMillis());
		long nDays = (c.getTimeInMillis() - correlationPoint.getTimeInMillis()) / MILLISECONDS_PER_DAY;

		if (nDays < 0)
			throw new RuntimeException("Date is too small " + c.getTime());
		if (nDays > Integer.MAX_VALUE)
			throw new RuntimeException("Date is too large " + c.getTime());
		
		daysSinceGreatCycle = (int) nDays; 
	}
	
	
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
	
	public String toLongCountString () {
		
		int [] lc = toLongCount();
		return String.valueOf(lc[0]) + '.' + lc[1] + '.' + lc[2] + '.' + lc[3] + '.' + lc[4];
		
	}

	public String toString () {
		return format.format(correlationPoint.getTime()) + " + " + String.valueOf(daysSinceGreatCycle);
	}
	
	
	public String toGregorianString () {
		Calendar c = (Calendar)correlationPoint.clone();
		c.add(Calendar.DAY_OF_YEAR, daysSinceGreatCycle);
		return format.format(c.getTime());
	}


//	public void setGregorian (String dmy) {
//	try {
//		Date date = format.parse(dmy);
//		Calendar c = (Calendar) correlationPoint.clone();
//		c.setTime(date);
//		set (c);
//	} catch (Throwable e) {
//		throw new RuntimeException("Invalid date format", e);
//	}
//}

	
}
