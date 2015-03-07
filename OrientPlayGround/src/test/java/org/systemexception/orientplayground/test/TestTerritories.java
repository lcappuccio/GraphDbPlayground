/**
 *
 * @author leo
 * @date 01/03/2015 18:30
 *
 */
package org.systemexception.orientplayground.test;

import java.util.List;
import org.apache.commons.csv.CSVRecord;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.exception.TerritoriesException;
import org.systemexception.orientplayground.pojo.CsvParser;
import org.systemexception.orientplayground.pojo.Territories;
import org.systemexception.orientplayground.pojo.Territory;

public class TestTerritories {

	private Territories sut;
	private CsvParser csvParser;
	private List<CSVRecord> csvRecords;

	@Before
	public void setUp() throws CsvParserException {
		csvParser = new CsvParser("src/test/resources/test_territories.csv");
		csvRecords = csvParser.readCsvContents();
	}

	@Test(expected = TerritoriesException.class)
	public void refuse_present_territory() throws TerritoriesException {
		sut = new Territories();
		String parentId = csvRecords.get(0).get("PARENT_ID");
		String nodeId = csvRecords.get(0).get("NODE_ID");
		String description = csvRecords.get(0).get("DESCRIPTION");
		String nodeType = csvRecords.get(0).get("TYPE");
		Territory territory1 = new Territory(parentId, nodeId, description, nodeType);
		Territory territory2 = new Territory(parentId, nodeId, description, nodeType);
		sut.addTerritory(territory1);
		sut.addTerritory(territory2);
	}

	@Test
	public void create_all_entries_in_csv_file() throws TerritoriesException {
		sut = new Territories();
		for (CSVRecord csvRecord : csvRecords) {
			String parentId = csvRecord.get("PARENT_ID");
			String nodeId = csvRecord.get("NODE_ID");
			String description = csvRecord.get("DESCRIPTION");
			String nodeType = csvRecord.get("TYPE");
			Territory territory = new Territory(parentId, nodeId, description, nodeType);
			sut.addTerritory(territory);
		}
		assertTrue(csvRecords.size() == sut.getTerritories().size());
	}
}
