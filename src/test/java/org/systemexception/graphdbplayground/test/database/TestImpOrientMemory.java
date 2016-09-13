package org.systemexception.graphdbplayground.test.database;

import org.junit.Before;
import org.junit.Test;
import org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration;
import org.systemexception.graphdbplayground.exception.CsvParserException;
import org.systemexception.graphdbplayground.exception.TerritoriesException;
import org.systemexception.graphdbplayground.impl.DatabaseImplOrient;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.systemexception.graphdbplayground.test.TestCsvParser.TEST_FILE_PATH;

public class TestImpOrientMemory extends DatabaseImplTest {

	@Before
	public void setUp() throws CsvParserException, TerritoriesException, URISyntaxException {
		dbStorageType = GraphDatabaseConfiguration.DB_STORAGE_MEMORY.toString();
		exportFileName = "target/database_orient_memory_export";
		backupFileName = "target/database_orient_memory_backup.zip";
		URL myTestURL = ClassLoader.getSystemResource(TEST_FILE_PATH);
		File myFile = new File(myTestURL.toURI());
		sut = new DatabaseImplOrient();
		sut.initialSetup(dbName, dbStorageType);
		sut.addTerritories(myFile.getAbsolutePath());
		exportFile = new File(exportFileName);
		backupFile = new File(backupFileName);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void dont_backup_database_in_memory() throws TerritoriesException, CsvParserException, URISyntaxException,
			IOException {
		sut.backupDatabase(backupFileName);
		assertFalse(backupFile.exists());
	}
}
