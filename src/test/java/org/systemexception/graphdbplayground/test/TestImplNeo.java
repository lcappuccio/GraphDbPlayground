/**
 * @author leo
 * @date 16/09/15 18:42
 */
package org.systemexception.graphdbplayground.test;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.systemexception.graphdbplayground.api.DatabaseApi;
import org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration;
import org.systemexception.graphdbplayground.exception.CsvParserException;
import org.systemexception.graphdbplayground.exception.TerritoriesException;
import org.systemexception.graphdbplayground.impl.DatabaseImplNeo;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestImplNeo {

	private static DatabaseApi sut;
	private final static String dbName = "target/database_neo_italy", dbStorageType = GraphDatabaseConfiguration
			.ORIENT_DB_STORAGE_MEMORY.toString(), exportFileName = "target/database_neo_export.csv", backupFileName =
			"target/database_neo_backup.csv";
	private static File backupFile, exportFile;

	@BeforeClass
	public static void setUp() throws CsvParserException, TerritoriesException, URISyntaxException {
		URL myTestURL = ClassLoader.getSystemResource("geonames_it.csv");
		File myFile = new File(myTestURL.toURI());
		sut = new DatabaseImplNeo();
		sut.initialSetup(dbName, dbStorageType);
		sut.addTerritories(myFile.getAbsolutePath());
		exportFile = new File(exportFileName);
		backupFile = new File(backupFileName);
	}

	@AfterClass
	public static void tearDown() {
		sut.drop();
	}

	@Test
	public void verify_luino_has_parent_varese() {
		Vertex vertexLuino = sut.getVertexByNodeId("6540157");
		assertTrue(vertexLuino.getProperty("nodeId").equals("6540157"));
		Iterator<Edge> edgeIterator = vertexLuino.getEdges(Direction.IN, "reportsTo").iterator();
		assertTrue(edgeIterator.hasNext());
		while (edgeIterator.hasNext()) {
			assertTrue(edgeIterator.next().getVertex(Direction.OUT).getProperty("nodeId").equals("3164697"));
		}
	}

	@Test
	public void verify_varese_has_parent_lombardia() {
		Vertex vertexVarese = sut.getVertexByNodeId("3164697");
		assertTrue(vertexVarese.getProperty("nodeId").equals("3164697"));
		Iterator<Edge> edgeIterator = vertexVarese.getEdges(Direction.IN, "reportsTo").iterator();
		assertTrue(edgeIterator.hasNext());
		while (edgeIterator.hasNext()) {
			assertTrue(edgeIterator.next().getVertex(Direction.OUT).getProperty("nodeId").equals("3174618"));
		}
	}

	@Test
	public void verify_varese_has_childs() {
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
	public void verify_luino_has_parent_varese_by_method() {
		Vertex vertexLuino = sut.getVertexByNodeId("6540157");
		Vertex vertexParent = sut.getParentNodeOf(vertexLuino.getProperty(GraphDatabaseConfiguration.NODE_ID
				.toString())
				.toString());
		String vertexParentDesc = vertexParent.getProperty(GraphDatabaseConfiguration.NODE_DESC.toString());
		assertTrue(vertexParentDesc.contains("Varese"));
	}

	@Test
	public void export_the_database() {
		sut.exportDatabase(exportFileName);
		assertTrue(exportFile.exists());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void backup_the_database() {
		sut.backupDatabase(backupFileName);
		assertTrue(backupFile.exists());
	}
}
