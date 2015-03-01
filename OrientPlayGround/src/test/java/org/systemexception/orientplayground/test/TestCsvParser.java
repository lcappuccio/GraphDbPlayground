/**
 *
 * @author leo
 * @date 01/03/2015 14:46
 *
 */
package org.systemexception.orientplayground.test;

import java.io.File;
import java.util.List;
import org.apache.commons.csv.CSVRecord;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.pojo.CsvParser;

public class TestCsvParser {

	private CsvParser sut;
	private static final String testFile = "src/test/resources/test_territories.csv";

	@Test
	public void open_test_territories_file() {
		File testTerritories = new File(testFile);
		assertTrue(testTerritories.exists());
	}

	@Test(expected = CsvParserException.class)
	public void throw_exception_for_nonexisting_file() throws CsvParserException {
		sut = new CsvParser("nonexistingfile.txt");
	}

	@Test
	public void reads_territories_test_file() throws CsvParserException {
		sut = new CsvParser(testFile);
		assertTrue(sut.readCsvLine().size() > 0);
	}

	@Test
	public void parse_correctly_luino_record() throws CsvParserException {
		sut = new CsvParser(testFile);
		List<CSVRecord> records = sut.readCsvLine();
		for (int i = 0; i < records.size(); i++) {
			CSVRecord territory = records.get(i);
			if (territory.get("DESCRIPTION").toLowerCase().equals("luino")) {
				assertTrue(territory.get("DESCRIPTION").equals("Luino"));
				assertTrue(territory.get("NODE_ID").equals("6540157"));
			}
		}
	}
}
