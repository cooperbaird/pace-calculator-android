package com.cooperbaird.pacecalculator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Methods to calculate a runner's pace or time
 * 
 * @author cooperbaird
 */
public final class RunningCalculations {
	private static final double milesPerK = 1 / 1.609344;
	
	/**
	 * @param k the distance in kilometers
	 * @return the distance in miles
	 */
	public static double kilometersToMiles(double k) {
		return k * milesPerK;
	}
	
	/**
	 * @param m the distance in miles
	 * @return the distance in kilometers
	 */
	public static double milesToKilometers(double m) {
		return m * (1/milesPerK);
	}
	
	/**
	 * @param d the double to round
	 * @param decimalPlaces the number of decimal places to round to
	 * @return the rounded double
	 */
	public static double roundToDecimal(double d, int decimalPlaces) {
		String places = "#.";
		for(int i = 0; i < decimalPlaces; i++) places += "#";
		DecimalFormat df = new DecimalFormat(places);
		df.setRoundingMode(RoundingMode.HALF_UP);
		return Double.parseDouble(df.format(d));
	}
	
	/**
	 * @param u the distance unit (m/k)
	 * @param d the distance in miles
	 * @param t a runner's total time
	 * @return a Pace object representing the runner's pace
	 */
	public static Pace calculatePace(char u, double d, Time t) {
		if(u == 'k')
			d = kilometersToMiles(d);

		double paceInSeconds = t.getAbstractTimeInSeconds() / d;
		int paceMinutes = paceMinutes(paceInSeconds);
		double paceSeconds = paceSeconds(paceInSeconds, paceMinutes);

		return new Pace(paceMinutes, paceSeconds);
	}
	
	/**
	 * @param u the distance unit (m/k)
	 * @param d the distance in miles
	 * @param p the runner's pace
	 * @return a Time object representing the runner's time
	 */
	public static Time calculateTime(char u, double d, Pace p) {
		if(u == 'k')
			d = kilometersToMiles(d);
		double timeInSeconds = p.getAbstractTimeInSeconds() * d;	
		return convertTimeSecondsToTime(timeInSeconds);
	}
	
	/**
	 * @param u1 the distance unit (m/k)
	 * @param u2 the split unit (m/k)
	 * @param d the distance in miles
	 * @param s the distance of the split
	 * @param t the runner's time
	 * @return a list of Split objects for the runner's splits
	 */
	public static List<Split> calculateSplits(char u1, char u2, double d, double s, Time t) {
		if(u1 == 'k')
			d = kilometersToMiles(d);
		if(u2 == 'k')
			s = kilometersToMiles(s);
		
		double timeSecs = t.getAbstractTimeInSeconds();
		double splitD = d / s;
		double split = timeSecs / splitD;
		List<Split> splitList = new ArrayList<>();
		
		int splitNum = 1;
		for(double j = split; j <= timeSecs; j += split) {
			splitList.add(new Split(splitNum, convertTimeSecondsToTime(j)));
			splitNum++;
		}
		
		return splitList;
	}
	
	/**
	 * @param u the distance unit (m/k)
	 * @param p the runner's pace
	 * @param t the runner's time
	 * @return the distance run in terms of u
	 */
	public static double calculateDistance(char u, Pace p, Time t) {
		double distance = t.getAbstractTimeInSeconds() / p.getAbstractTimeInSeconds();
		if(u == 'k')
			distance = milesToKilometers(distance);
		return distance;
	}
	
	/**
	 * @param seconds the total time in seconds
	 * @return a new Time object
	 */
	private static Time convertTimeSecondsToTime(double seconds) {
		int h = timeHours(seconds);
		int m = timeMinutes(h, seconds);
		double s = timeSeconds(h, m, seconds);
		return new Time(h, m, s);
	}

	/**
	 * @param s the runner's pace in seconds
	 * @return the minutes component of the pace
	 */
	private static int paceMinutes(double s) {
		return (int) Math.floor((s/3600) * 60);
	}

	/**
	 * @param s the runner's pace in seconds
	 * @param m the minutes component of the pace
	 * @return the seconds component of the pace
	 */
	private static double paceSeconds(double s, double m) {
		double minutes = (s/3600) * 60;
		double minutesDecimal = minutes - m;
		return minutesDecimal * 60;
	}
	
	/**
	 * @param s the total time run in seconds
	 * @return the hours component of the time
	 */
	private static int timeHours(double s) {
		return (int) Math.floor(s/3600);
	}
	
	/**
	 * @param h the hours component of the time
	 * @param s the total time run in seconds
	 * @return the minutes component of the time
	 */
	private static int timeMinutes(int h, double s) {
		double seconds = s - (h*3600);
		return (int) Math.floor(seconds/60);
	}
	
	/**
	 * @param h the hours component of the time
	 * @param m the minutes component of the time
	 * @param s the total time run in seconds
	 * @return the seconds component of the time
	 */
	private static double timeSeconds(int h, int m, double s) {
		return s - (h*3600) - (m*60);
	}
}
