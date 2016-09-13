/**
 * @author leo
 * @date 16/09/15 18:42
 */
package org.systemexception.graphdbplayground.test;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.systemexception.graphdbplayground.api.DatabaseApi;
import org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration;
import org.systemexception.graphdbplayground.exception.CsvParserException;
import org.systemexception.graphdbplayground.exception.TerritoriesException;
import org.systemexception.graphdbplayground.impl.DatabaseImplNeo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration.NODE_ID;
import static org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration.REPORTS_TO;
import static org.systemexception.graphdbplayground.test.TestCsvParser.*;

public class TestImplNeo {

	private DatabaseApi sut;
	private final static String dbName = "target/database_neo_italy",
			dbStorageType = GraphDatabaseConfiguration.DB_STORAGE_MEMORY.toString(),
			exportFileName = "target/database_neo_export.csv", backupFileName = "target/database_neo_backup.csv";
	private File backupFile, exportFile;

	@Before
	public void setUp() throws CsvParserException, TerritoriesException, URISyntaxException {
		URL myTestURL = ClassLoader.getSystemResource(TEST_FILE_PATH);
		File myFile = new File(myTestURL.toURI());
		sut = new DatabaseImplNeo();
		sut.initialSetup(dbName, dbStorageType);
		sut.addTerritories(myFile.getAbsolutePath());
		exportFile = new File(exportFileName);
		backupFile = new File(backupFileName);
	}

	@After
	public void tearDown() throws IOException {
		sut.drop();
	}

	@Test
	public void verify_luino_has_parent_varese() {
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
	public void verify_varese_has_parent_lombardia() {
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
	public void verify_varese_has_childs() {
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
	public void verify_luino_has_parent_varese_by_method() {
		Vertex vertexLuino = sut.getVertexByNodeId(NODE_LUINO_NODEID);
		Vertex vertexParent = sut.getParentNodeOf(vertexLuino
				.getProperty(NODE_ID.toString()).toString());
		String vertexParentDesc = vertexParent.getProperty(GraphDatabaseConfiguration.NODE_DESC.toString());
		assertTrue(vertexParentDesc.contains(NODE_VARESE_DESCRIPTION));
	}

	@Test
	public void verify_luino_has_no_childs() {
		Vertex vertexLuino = sut.getVertexByNodeId(NODE_LUINO_NODEID);
		List<Vertex> childNodesOfLuino = sut.getChildNodesOf(vertexLuino
				.getProperty(NODE_ID.toString()).toString());
		assertTrue(0 == childNodesOfLuino.size());
	}

	@Test
	public void export_the_database() throws IOException {
		sut.exportDatabase(exportFileName);
		assertTrue(exportFile.exists());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void backup_the_database() throws IOException {
		sut.backupDatabase(backupFileName);
		assertTrue(backupFile.exists());
	}
}
