/**
 *
 * @author leo
 * @date 01/03/2015 14:59
 *
 */
package org.systemexception.orientplayground.pojo;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.systemexception.orientplayground.exception.CsvParserException;

public class CsvParser {

	private CSVParser csvParser = null;
	private List records = null;

	public CsvParser(String fileName) throws CsvParserException {
		try {
			URL url = new File(fileName).toURI().toURL();
			Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
			csvParser = new CSVParser(reader, CSVFormat.RFC4180);
			records = csvParser.getRecords();
		} catch (IOException ex) {
			throw new CsvParserException("Malformed URL\n" + ex.getMessage());
		}
	}

	public List readCsvLine() {
		return records;
	}

}
