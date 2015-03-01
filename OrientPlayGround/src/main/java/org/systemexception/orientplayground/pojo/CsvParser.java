/**
 *
 * @author leo
 * @date 01/03/2015 14:59
 *
 */
package org.systemexception.orientplayground.pojo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.systemexception.orientplayground.exception.CsvParserException;

public class CsvParser {

	private CSVParser csvParser = null;
	private Reader reader = null;
	private Iterable<CSVRecord> records = null;

	public CsvParser(String fileName) throws CsvParserException {
		// TODO catch specific CsvParserException
		URL url;
		try {
			url = new File(fileName).toURI().toURL();
			reader = new InputStreamReader(url.openStream(), "UTF-8");
			csvParser = new CSVParser(reader, CSVFormat.RFC4180);
			records = CSVFormat.RFC4180.parse(reader);
		} catch (IOException ex) {
			throw new CsvParserException("Malformed URL");
		}
	}

	public String readCsvLine() {
		// TODO implement this
//		records.iterator().next().ge;
		return null;
	}

}
