package com.cooperbaird.pacecalculator;

/**
 * A runner's split for a certain interval
 * 
 * @author cooperbaird
 */
public class Split {
	private final int lap;
	private final Time time;
	
	/**
	 * @param lap the lap number
	 * @param t the time for that lap
	 */
	public Split(int lap, Time t) {
		this.lap = lap;
		time = t;
	}
	
	/**
	 * @return the split's lap number
	 */
	public int getLap() {
		return lap;
	}

    /**
     * @return the split's lap number as a string
     */
	public String getLapAsString() {
	    return Integer.toString(lap);
    }

	/**
	 * @return the split's time
	 */
	public Time getTime() {
		return time;
	}

	@Override
	public String toString() {
		String secs = Double.toString(time.getRoundedSeconds(3));
		if(time.getSeconds() < 10)
			secs = "0" + secs;

		String mins = Integer.toString(time.getMinutes());
		if(time.getMinutes() < 10)
			mins = "0" + mins;

		String hrs = Integer.toString(time.getHours()) + ":";
		if(time.getHours() == 0)
			hrs = "";
		else if(time.getHours() < 10)
			hrs = "0" + hrs;
		
		return hrs + mins + ":" + secs;
	}
}
