/**
 * com.lodbrok.jmx package contains the JMX support classes and interfaces
 */
package com.lodbrok.jmx;

/**
 * Ragnarok interface represents the JMX interface that JMX shall cumply
 * 
 * @author Fabio Riberto
 *
 */
public interface RagnarokMBean {

	/**
	 * threadCount setter method
	 * 
	 * @param noOfThreads
	 *
	 */
	public void setThreadCount(int noOfThreads);

	/**
	 * threadCount getter method
	 *
	 */
	public int getThreadCount();

	/**
	 * The doConfig method implements the actions that shall be inject into the
	 * application from JMX
	 *
	 */
	public String doConfig();
}
