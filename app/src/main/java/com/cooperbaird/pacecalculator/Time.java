package com.cooperbaird.pacecalculator;

/**
 * The hours, minutes, and seconds components of a runner's time
 * 
 * @author cooperbaird
 */
public class Time extends AbstractTime {
	private final int hours;
	
	/**
	 * @param hours the hours component of the time
	 * @param minutes the minutes component of the time
	 * @param seconds the seconds component of the time
	 */
	public Time(int hours, int minutes, double seconds) {
		super(minutes, seconds);
		this.hours = hours;
	}

	/**
	 * @return the hours
	 */
	public int getHours() {
		return hours;
	}

	@Override
	public double getAbstractTimeInSeconds() {
		return getHours()*3600 + getMinutes()*60 + getSeconds();
	}
}
