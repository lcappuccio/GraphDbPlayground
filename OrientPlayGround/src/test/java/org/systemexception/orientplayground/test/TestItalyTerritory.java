/**
 *
 * @author leo
 * @date 01/03/2015 19:08
 *
 */
package org.systemexception.orientplayground.test;

import org.junit.Test;
import org.systemexception.orientplayground.api.Action;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.exception.TerritoriesException;
import org.systemexception.orientplayground.impl.OrientActionImpl;

public class TestItalyTerritory {

	private Action sut;
	private final String dbName = "geonames_it", fileName = "target/territories/geonames_it.csv";

	@Test
	public void setup_database() {
		sut = new OrientActionImpl();
		sut.initialSetup(dbName);
	}

	@Test
	public void add_territories() throws CsvParserException, TerritoriesException {
		sut = new OrientActionImpl();
		sut.initialSetup(dbName);
//		sut.addTerritories(fileName);
	}

}
