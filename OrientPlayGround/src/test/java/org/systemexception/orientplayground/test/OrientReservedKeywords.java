/**
 * $Id$
 *
 * @author lcappuccio
 * @date 26/03/2015 17:03
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package org.systemexception.orientplayground.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.systemexception.orientplayground.api.Action;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.exception.TerritoriesException;
import org.systemexception.orientplayground.impl.OrientActionImpl;

public class OrientReservedKeywords {

	private static Action sut;
	private final static String dbName = "test_keywords";

	@BeforeClass
	public static void setUp() throws CsvParserException, TerritoriesException {
		sut = new OrientActionImpl();
		sut.initialSetup(dbName);
	}

	@AfterClass
	public static void tearDown() {
	}

}
