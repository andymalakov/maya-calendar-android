package org.maya.calendar.model;

public enum MayaTimeUnit {
	
	Baktun(144000),
	Katun (7200),
	Tun   (360),
	Winal (20),
	Kin   (1);
	

	public final int numberOfDays;
	private MayaTimeUnit (int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	
	public int toDays (int units) {
		return numberOfDays * units;
	}

}
