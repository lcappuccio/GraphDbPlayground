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
import static org.systemexception.graphdbplayground.enums.CsvHeaders.DESCRIPTION;
import static org.systemexception.graphdbplayground.enums.CsvHeaders.NODE_ID;

public class TestCsvParser {

	private CsvParser sut;
	static final String TEST_FILE_PATH = "geonames_it_SMALL.csv";
	static final String NODE_LUINO_DESCRIPTION = "Luino", NODE_LAVENA_DESCRIPTION = "Lavena Ponte Tresa",
			NODE_MACCAGNO_DESCRIPTION = "Maccagno", NODE_VARESE_DESCRIPTION = "Varese";
	static final String NODE_LUINO_NODEID = "6540157", NODE_VARESE_NODEID = "3164697",
			NODE_LOMBARDIA_NODEID = "3174618";
	private static File resourceFile;

	@BeforeClass
	public static void setUp() throws URISyntaxException {
		URL myTestURL = ClassLoader.getSystemResource(TEST_FILE_PATH);
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
			if (territory.get(DESCRIPTION.toString()).toLowerCase(Locale.getDefault()).equals("luino")) {
				assertTrue(territory.get(DESCRIPTION.toString()).equals(NODE_LUINO_DESCRIPTION));
				assertTrue(territory.get(NODE_ID.toString()).equals(NODE_LUINO_NODEID));
			}
		}
	}
}
