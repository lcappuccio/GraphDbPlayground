/**
 * $Id$
 *
 * @author lcappuccio
 * @date 25/03/2015 17:25
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package org.systemexception.orientplayground.impl;

import org.apache.logging.log4j.LogManager;
import org.systemexception.orientplayground.api.Logger;

public class LoggerImpl implements Logger {

	private final org.apache.logging.log4j.Logger logger;

	private LoggerImpl(org.apache.logging.log4j.Logger logger) {
		this.logger = logger;
	}
	
	public static LoggerImpl getFor(Class clazz) {
		org.apache.logging.log4j.Logger logger = LogManager.getLogger(clazz);
		return new LoggerImpl(logger);
	}

	@Override
	public void info(String message) {
		this.logger.info(message);
	}

	@Override
	public void debug(String message) {
		this.logger.debug(message);
	}

	@Override
	public void error(String message, Exception exception) {
		this.logger.error("Error: "+ message + ", Exception: " + exception);
	}
}
