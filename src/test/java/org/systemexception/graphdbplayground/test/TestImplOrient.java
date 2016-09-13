/**
 * @author leo
 * @date 02/03/2015 23:38
 */
package org.systemexception.graphdbplayground.test;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.junit.After;
import org.junit.Test;
import org.systemexception.graphdbplayground.api.DatabaseApi;
import org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration;
import org.systemexception.graphdbplayground.exception.CsvParserException;
import org.systemexception.graphdbplayground.exception.TerritoriesException;
import org.systemexception.graphdbplayground.impl.DatabaseImplOrient;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration.NODE_ID;
import static org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration.REPORTS_TO;
import static org.systemexception.graphdbplayground.test.TestCsvParser.*;

public class TestImplOrient {

	private DatabaseApi sut;
	private final static String dbName = "target/database_orient_italy",
			dbDiskStorageType = GraphDatabaseConfiguration.DB_ORIENT_STORAGE_DISK.toString(),
			dbMemoryStorageType = GraphDatabaseConfiguration.DB_STORAGE_MEMORY.toString(),
			exportFileName = "target/database_orient_export", backupFileName = "target/database_orient_backup.zip";
	private File backupFile, exportFile;

	@After
	public void tearDown() throws IOException {
		sut.drop();
	}

	@Test
	public void verify_luino_has_parent_varese() throws TerritoriesException, CsvParserException, URISyntaxException {
		getSut(dbMemoryStorageType);
		Vertex vertexLuino = sut.getVertexByNodeId(NODE_LUINO_NODEID);
		assertTrue(vertexLuino.getProperty(NODE_ID.toString()).equals(NODE_LUINO_NODEID));
		Iterator<Edge> edgeIterator = vertexLuino.getEdges(Direction.IN, REPORTS_TO.toString()).iterator();
		assertTrue(edgeIterator.hasNext());
		while (edgeIterator.hasNext()) {
			assertTrue(edgeIterator.next().getVertex(Direction.OUT).getProperty(NODE_ID.toString())
					.equals(NODE_VARESE_NODEID));
		}
	}

	@Test
	public void verify_varese_has_parent_lombardia() throws TerritoriesException, CsvParserException,
			URISyntaxException {
		getSut(dbMemoryStorageType);
		Vertex vertexVarese = sut.getVertexByNodeId(NODE_VARESE_NODEID);
		assertTrue(vertexVarese.getProperty(NODE_ID.toString()).equals(NODE_VARESE_NODEID));
		Iterator<Edge> edgeIterator = vertexVarese.getEdges(Direction.IN, REPORTS_TO.toString()).iterator();
		assertTrue(edgeIterator.hasNext());
		while (edgeIterator.hasNext()) {
			assertTrue(edgeIterator.next().getVertex(Direction.OUT).getProperty(NODE_ID.toString())
					.equals(NODE_LOMBARDIA_NODEID));
		}
	}

	@Test
	public void verify_varese_has_childs() throws TerritoriesException, CsvParserException, URISyntaxException {
		getSut(dbMemoryStorageType);
		List<Vertex> vertexVareseChilds = sut.getChildNodesOf(NODE_VARESE_NODEID);
		ArrayList<String> childNodes = new ArrayList<>();
		for (Vertex vertex : vertexVareseChilds) {
			childNodes.add(vertex.getProperty(GraphDatabaseConfiguration.NODE_DESC.toString()).toString());
		}
		assertTrue(childNodes.contains(NODE_LUINO_DESCRIPTION));
		assertTrue(childNodes.contains(NODE_LAVENA_DESCRIPTION));
		assertTrue(childNodes.contains(NODE_MACCAGNO_DESCRIPTION));
	}

	@Test
	public void verify_luino_has_parent_varese_by_method() throws TerritoriesException, CsvParserException,
			URISyntaxException {
		getSut(dbMemoryStorageType);
		Vertex vertexLuino = sut.getVertexByNodeId(NODE_LUINO_NODEID);
		Vertex vertexParent = sut.getParentNodeOf(vertexLuino.getProperty(GraphDatabaseConfiguration.NODE_ID
				.toString())
				.toString());
		String vertexParentDesc = vertexParent.getProperty(GraphDatabaseConfiguration.NODE_DESC.toString());
		assertTrue(vertexParentDesc.contains(NODE_VARESE_DESCRIPTION));
	}

	@Test
	public void verify_luino_has_no_childs() throws TerritoriesException, CsvParserException, URISyntaxException {
		getSut(dbMemoryStorageType);
		Vertex vertexLuino = sut.getVertexByNodeId(NODE_LUINO_NODEID);
		List<Vertex> childNodesOfLuino = sut.getChildNodesOf(vertexLuino.getProperty(GraphDatabaseConfiguration.NODE_ID
				.toString())
				.toString());
		assertTrue(0 == childNodesOfLuino.size());
	}

	@Test
	public void export_the_database() throws TerritoriesException, CsvParserException, URISyntaxException,
			IOException {
		getSut(dbDiskStorageType);
		sut.exportDatabase(exportFileName);
		assertTrue(exportFile.exists());
	}

	@Test
	public void backup_the_database() throws TerritoriesException, CsvParserException, URISyntaxException,
			IOException {
		getSut(dbDiskStorageType);
		if (dbDiskStorageType.equals(GraphDatabaseConfiguration.DB_ORIENT_STORAGE_DISK.toString())) {
			sut.backupDatabase(backupFileName);
			assertTrue(backupFile.exists());
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void dont_backup_database_in_memory() throws TerritoriesException, CsvParserException, URISyntaxException,
			IOException {
		getSut(dbMemoryStorageType);
		sut.backupDatabase(backupFileName);
		assertFalse(backupFile.exists());
	}

	private void getSut(final String storageType) throws URISyntaxException, CsvParserException, TerritoriesException {
		URL myTestURL = ClassLoader.getSystemResource(TEST_FILE_PATH);
		File myFile = new File(myTestURL.toURI());
		sut = new DatabaseImplOrient();
		sut.initialSetup(dbName, storageType);
		sut.addTerritories(myFile.getAbsolutePath());
		exportFile = new File(exportFileName + ".json.gz");
		backupFile = new File(backupFileName);
	}
}
