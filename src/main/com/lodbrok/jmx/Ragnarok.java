/**
 * com.lodbrok.jmx package contains the JMX support classes and interfaces
 */
package com.lodbrok.jmx;

/**
 * Ragnarok class represents the JMX link to the application
 * 
 * @author Fabio Riberto
 *
 */
public class Ragnarok implements RagnarokMBean {

	/**
	 * threadCount member
	 *
	 */
	private int threadCount;

	/**
	 * Ragnarok constructor
	 * 
	 * @param Ragnarok
	 */
	public Ragnarok(int numThreads) {
		this.threadCount = numThreads;
	}

	@Override
	public void setThreadCount(int noOfThreads) {
	}

	@Override
	public int getThreadCount() {
		return 0;
	}

	@Override
	public String doConfig() {
		return "Number of Threads: ".concat(String.valueOf(this.threadCount));
	}

}
