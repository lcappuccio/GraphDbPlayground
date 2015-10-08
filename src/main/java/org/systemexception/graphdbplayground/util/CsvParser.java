/**
 *
 * @author leo
 * @date 01/03/2015 14:59
 *
 */
package org.systemexception.graphdbplayground.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.systemexception.logger.api.Logger;
import org.systemexception.logger.impl.LoggerImpl;
import org.systemexception.graphdbplayground.enums.CsvHeaders;
import org.systemexception.graphdbplayground.exception.CsvParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

public class CsvParser {

	private static final Logger logger = LoggerImpl.getFor(CsvParser.class);
	private List<CSVRecord> records;
	private final String[] headerMapping = new String[]{CsvHeaders.PARENT_ID.toString(), CsvHeaders.NODE_ID.toString(),
		CsvHeaders.DESCRIPTION.toString(), CsvHeaders.TYPE.toString()};

	public CsvParser(String fileName) throws CsvParserException {
		CSVFormat csvFormat = CSVFormat.RFC4180.withHeader(headerMapping).withSkipHeaderRecord(true);
		try {
			URL csvUrl = new File(fileName).toURI().toURL();
			Reader csvReader = new InputStreamReader(csvUrl.openStream(), "UTF-8");
			CSVParser csvParser = new CSVParser(csvReader, csvFormat);
			Timer timer = new Timer();
			timer.start();
			records = csvParser.getRecords();
			timer.end();
			logger.info("Loaded " + fileName + ": " + timer.durantionInSeconds() + " seconds");
		} catch (IOException ex) {
			throw new CsvParserException("Malformed URL\n" + ex.getMessage());
		}
	}

	public List readCsvContents() {
		return records;
	}
}
