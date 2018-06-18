package com.cooperbaird.pacecalculator;

/**
 * The minutes and seconds component of a runner's pace
 * 
 * @author cooperbaird
 */
public class Pace extends AbstractTime {
	/**
	 * @param minutes the minutes component of the pace
	 * @param seconds the seconds component of the pace
	 */
	public Pace(int minutes, double seconds) {
		super(minutes, seconds);
	}

	@Override
	public double getAbstractTimeInSeconds() {
		return getMinutes()*60 + getSeconds();
	}
}
