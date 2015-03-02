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
	private final String dbName = "test_database_lombardia_territories", fileName = "src/test/resources/test_territories.csv";

	@Test
	public void add_territories() throws CsvParserException, TerritoriesException {
		sut = new OrientActionImpl();
		sut.initialSetup(dbName);
		sut.addTerritories(fileName);
	}

}
