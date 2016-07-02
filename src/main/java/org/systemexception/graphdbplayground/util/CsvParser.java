/**
 * @author leo
 * @date 01/03/2015 14:59
 */
package org.systemexception.graphdbplayground.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.systemexception.graphdbplayground.enums.CsvHeaders;
import org.systemexception.graphdbplayground.exception.CsvParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

public class CsvParser {

	private static final Logger logger = LogManager.getLogger(CsvParser.class);
	private List<CSVRecord> records;

	public CsvParser(String fileName) throws CsvParserException {
		String[] headerMapping = new String[]{CsvHeaders.PARENT_ID.toString(), CsvHeaders.NODE_ID.toString(),
				CsvHeaders.DESCRIPTION.toString(), CsvHeaders.TYPE.toString()};
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
			String errorMessage = "Malformed URL\n" + ex.getMessage();
			logger.error(errorMessage);
			throw new CsvParserException(errorMessage);
		}
	}

	public List readCsvContents() {
		return records;
	}
}
