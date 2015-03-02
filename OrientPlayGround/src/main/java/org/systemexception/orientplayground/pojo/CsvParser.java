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
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.systemexception.orientplayground.exception.CsvParserException;

public class CsvParser {

	private CSVParser csvParser;
	private CSVFormat csvFormat;
	private List<CSVRecord> records;
	private final String[] headerMapping = new String[]{"PARENT_ID", "NODE_ID", "DESCRIPTION", "TYPE"};

	public CsvParser(String fileName) throws CsvParserException {
		csvFormat = CSVFormat.RFC4180.withHeader(headerMapping).withSkipHeaderRecord(true);
		try {
			URL csvUrl = new File(fileName).toURI().toURL();
			Reader csvReader = new InputStreamReader(csvUrl.openStream(), "UTF-8");
			csvParser = new CSVParser(csvReader, csvFormat);
			records = csvParser.getRecords();
		} catch (IOException ex) {
			throw new CsvParserException("Malformed URL\n" + ex.getMessage());
		}
	}

	public List readCsvContents() {
		return records;
	}

	public String[] getMapping() {
		String[] tempArray = headerMapping;
		return tempArray;
	}

}
