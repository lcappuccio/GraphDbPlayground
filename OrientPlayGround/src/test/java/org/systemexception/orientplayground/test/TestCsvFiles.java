/**
 *
 * @author leo
 * @date 01/03/2015 14:46
 *
 */
package org.systemexception.orientplayground.test;

import java.io.File;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.pojo.CsvParser;

public class TestCsvFiles {

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
	public void parse_test_territories() throws CsvParserException {
		sut = new CsvParser(testFile);
	}
}
