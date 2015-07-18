package org.systemexception.orientplayground.pojo;

/**
 * @author leo
 * @date 18/07/15 22:19
 */
public class Timer {

	private double start;
	private double end;

	/**
	 * New timer
	 */
	public Timer() {
		reset();
	}

	/**
	 * Start timer
	 */
	public void start() {
		start = System.currentTimeMillis();
	}

	/**
	 * Stop timer
	 */
	public void end() {
		end = System.currentTimeMillis();
	}

	/**
	 * Calculate total time
	 *
	 * @return elapsed time
	 */
	public double duration() {
		return (end - start);
	}

	public double durantionInSeconds() {
		return (end - start) / 1000;
	}

	/**
	 * Reset current timer
	 */
	private void reset() {
		start = 0;
		end = 0;
	}
}
