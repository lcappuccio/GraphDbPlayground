/**
 *
 * @author leo
 * @date 01/03/2015 14:59
 *
 */
package org.systemexception.orientplayground.pojo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.systemexception.orientplayground.enums.CsvHeaders;
import org.systemexception.orientplayground.exception.CsvParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

public class CsvParser {

	private List<CSVRecord> records;
	private final String[] headerMapping = new String[]{CsvHeaders.PARENT_ID.toString(), CsvHeaders.NODE_ID.toString(),
		CsvHeaders.DESCRIPTION.toString(), CsvHeaders.TYPE.toString()};

	public CsvParser(String fileName) throws CsvParserException {
		CSVFormat csvFormat = CSVFormat.RFC4180.withHeader(headerMapping).withSkipHeaderRecord(true);
		try {
			URL csvUrl = new File(fileName).toURI().toURL();
			Reader csvReader = new InputStreamReader(csvUrl.openStream(), "UTF-8");
			CSVParser csvParser = new CSVParser(csvReader, csvFormat);
			records = csvParser.getRecords();
		} catch (IOException ex) {
			throw new CsvParserException("Malformed URL\n" + ex.getMessage());
		}
	}

	public List readCsvContents() {
		return records;
	}

	public String[] getMapping() {
		return headerMapping;
	}
}
