/**
 * @author leo
 * @date 01/03/2015 14:46
 */
package org.systemexception.graphdbplayground.test;

import org.apache.commons.csv.CSVRecord;
import org.junit.BeforeClass;
import org.junit.Test;
import org.systemexception.graphdbplayground.exception.CsvParserException;
import org.systemexception.graphdbplayground.util.CsvParser;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

public class TestCsvParser {

	private CsvParser sut;
	private static final String testFile = "geonames_it.csv";
	private static File resourceFile;

	@BeforeClass
	public static void setUp() throws URISyntaxException {
		URL myTestURL = ClassLoader.getSystemResource(testFile);
		resourceFile = new File(myTestURL.toURI());
	}

	@Test
	public void open_test_territories_file() {
		assertTrue(resourceFile.exists());
	}

	@Test(expected = CsvParserException.class)
	public void throw_exception_for_nonexisting_file() throws CsvParserException {
		sut = new CsvParser("nonexistingfile.txt");
	}

	@Test
	public void reads_territories_test_file() throws CsvParserException {
		sut = new CsvParser(resourceFile.getAbsolutePath());
		assertTrue(sut.readCsvContents().size() > 0);
	}

	@Test
	public void parse_correctly_luino_record() throws CsvParserException {
		sut = new CsvParser(resourceFile.getAbsolutePath());
		List<CSVRecord> records = sut.readCsvContents();
		for (CSVRecord territory : records) {
			if (territory.get("DESCRIPTION").toLowerCase(Locale.getDefault()).equals("luino")) {
				assertTrue(territory.get("DESCRIPTION").equals("Luino"));
				assertTrue(territory.get("NODE_ID").equals("6540157"));
			}
		}
	}
}
