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

import static org.junit.Assert.assertTrue;
import static org.systemexception.graphdbplayground.test.TestCsvParser.TEST_FILE_PATH;

public class TestImplOrientDisk extends DatabaseImplTest {

	@Before
	public void setUp() throws URISyntaxException, CsvParserException, TerritoriesException {
		dbName = "target/database_orient_italy";
		dbStorageType = GraphDatabaseConfiguration.DB_ORIENT_STORAGE_DISK.toString();
		exportFileName = "target/database_orient_disk_export";
		backupFileName = "target/database_orient_disk_backup.zip";
		URL myTestURL = ClassLoader.getSystemResource(TEST_FILE_PATH);
		File myFile = new File(myTestURL.toURI());
		sut = new DatabaseImplOrient();
		sut.initialSetup(dbName, dbStorageType);
		sut.addTerritories(myFile.getAbsolutePath());
		exportFile = new File(exportFileName);
		backupFile = new File(backupFileName);
	}

	@Test
	public void export_the_database() throws IOException {
		sut.exportDatabase(exportFileName);
		assertTrue(exportFile.exists());
	}

	@Test
	public void backup_the_database() throws TerritoriesException, CsvParserException, URISyntaxException,
			IOException {
		sut.backupDatabase(backupFileName);
		assertTrue(backupFile.exists());
	}
}
