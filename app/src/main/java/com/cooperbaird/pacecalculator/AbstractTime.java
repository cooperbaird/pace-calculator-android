package com.cooperbaird.pacecalculator;

/**
 * Abstract class that will be parent of both Time and Pace.
 * Needed for polymorphism.
 * 
 * @author cooperbaird
 */
public abstract class AbstractTime {
	private final int minutes;
	private final double seconds;
	
	/**
	 * @param minutes the minutes of the time
	 * @param seconds the seconds of the time
	 */
	public AbstractTime(int minutes, double seconds) {
		this.minutes = minutes;
		this.seconds = seconds;
	}

	/**
	 * @return the minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * @return the seconds
	 */
	public double getSeconds() {
		return seconds;
	}
	
	/**
	 * @param decimalPlaces the number of decimal places to round to
	 * @return the seconds rounded
	 */
	public double getRoundedSeconds(int decimalPlaces) {
		return RunningCalculations.roundToDecimal(seconds, decimalPlaces);
	}
	
	/**
	 * @return the abstract time in total seconds
	 */
	public abstract double getAbstractTimeInSeconds();
}
