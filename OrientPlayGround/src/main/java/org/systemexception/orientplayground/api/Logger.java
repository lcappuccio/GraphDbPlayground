/**
 * $Id$
 *
 * @author lcappuccio
 * @date 25/03/2015 17:21
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package org.systemexception.orientplayground.api;

public interface Logger {
	
	/**
	 * 
	 * @param message 
	 */
	public void info(String message);
	
	/**
	 * 
	 * @param message 
	 */
	public void debug(String message);
	
	/**
	 * 
	 * @param message
	 * @param exception 
	 */
	public void error(String message, Exception exception);

}
