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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestImplOrient {

	private DatabaseApi sut;
	private final static String dbName = "target/database_orient_italy",
			dbDiskStorageType = GraphDatabaseConfiguration.ORIENT_DB_STORAGE_DISK.toString(),
			dbMemoryStorageType = GraphDatabaseConfiguration.ORIENT_DB_STORAGE_MEMORY.toString(),
			exportFileName = "target/database_orient_export", backupFileName = "target/database_orient_backup.zip";
	private File backupFile, exportFile;

	@After
	public void tearDown() {
		sut.drop();
	}

	@Test
	public void verify_luino_has_parent_varese() throws TerritoriesException, CsvParserException, URISyntaxException {
		getSut(dbMemoryStorageType);
		Vertex vertexLuino = sut.getVertexByNodeId("6540157");
		assertTrue(vertexLuino.getProperty("nodeId").equals("6540157"));
		Iterator<Edge> edgeIterator = vertexLuino.getEdges(Direction.IN, "reportsTo").iterator();
		assertTrue(edgeIterator.hasNext());
		while (edgeIterator.hasNext()) {
			assertTrue(edgeIterator.next().getVertex(Direction.OUT).getProperty("nodeId").equals("3164697"));
		}
	}

	@Test
	public void verify_varese_has_parent_lombardia() throws TerritoriesException, CsvParserException,
			URISyntaxException {
		getSut(dbMemoryStorageType);
		Vertex vertexVarese = sut.getVertexByNodeId("3164697");
		assertTrue(vertexVarese.getProperty("nodeId").equals("3164697"));
		Iterator<Edge> edgeIterator = vertexVarese.getEdges(Direction.IN, "reportsTo").iterator();
		assertTrue(edgeIterator.hasNext());
		while (edgeIterator.hasNext()) {
			assertTrue(edgeIterator.next().getVertex(Direction.OUT).getProperty("nodeId").equals("3174618"));
		}
	}

	@Test
	public void verify_varese_has_childs() throws TerritoriesException, CsvParserException, URISyntaxException {
		getSut(dbMemoryStorageType);
		List<Vertex> vertexVareseChilds = sut.getChildNodesOf("3164697");
		ArrayList<String> childNodes = new ArrayList<>();
		for (Vertex vertex : vertexVareseChilds) {
			childNodes.add(vertex.getProperty(GraphDatabaseConfiguration.NODE_DESC.toString()).toString());
		}
		assertTrue(childNodes.contains("Luino"));
		assertTrue(childNodes.contains("Lavena Ponte Tresa"));
		assertTrue(childNodes.contains("Maccagno"));
	}

	@Test
	public void verify_luino_has_parent_varese_by_method() throws TerritoriesException, CsvParserException,
			URISyntaxException {
		getSut(dbMemoryStorageType);
		Vertex vertexLuino = sut.getVertexByNodeId("6540157");
		Vertex vertexParent = sut.getParentNodeOf(vertexLuino.getProperty(GraphDatabaseConfiguration.NODE_ID
				.toString())
				.toString());
		String vertexParentDesc = vertexParent.getProperty(GraphDatabaseConfiguration.NODE_DESC.toString());
		assertTrue(vertexParentDesc.contains("Varese"));
	}

	@Test
	public void export_the_database() throws TerritoriesException, CsvParserException, URISyntaxException {
		getSut(dbDiskStorageType);
		sut.exportDatabase(exportFileName);
		assertTrue(exportFile.exists());
	}

	@Test
	public void backup_the_database() throws TerritoriesException, CsvParserException, URISyntaxException {
		getSut(dbDiskStorageType);
		if (dbDiskStorageType.equals(GraphDatabaseConfiguration.ORIENT_DB_STORAGE_DISK.toString())) {
			sut.backupDatabase(backupFileName);
			assertTrue(backupFile.exists());
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void dont_backup_database_in_memory() throws TerritoriesException, CsvParserException, URISyntaxException {
		getSut(dbMemoryStorageType);
		sut.backupDatabase(backupFileName);
		assertFalse(backupFile.exists());
	}

	private void getSut(final String storageType) throws URISyntaxException, CsvParserException, TerritoriesException {
		URL myTestURL = ClassLoader.getSystemResource("geonames_it_SMALL.csv");
		File myFile = new File(myTestURL.toURI());
		sut = new DatabaseImplOrient();
		sut.initialSetup(dbName, storageType);
		sut.addTerritories(myFile.getAbsolutePath());
		exportFile = new File(exportFileName + ".json.gz");
		backupFile = new File(backupFileName);
	}
}
