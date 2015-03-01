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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CsvParser {

	private final CSVParser csvParser;
	private Iterable<CSVRecord> records;

	public CsvParser(String fileName) throws UnsupportedEncodingException, MalformedURLException, IOException {
		// TODO catch specific CsvParserException
		URL url = new File(fileName).toURI().toURL();
		Reader reader = new InputStreamReader(url.openStream(), "UTF-8");
		csvParser = new CSVParser(reader, CSVFormat.RFC4180);
		records = CSVFormat.RFC4180.parse(reader);
	}

	public String readCsvLine() {
		// TODO implement this
//		records.iterator().next().ge;
		return null;
	}

}
