/**
 *
 * @author leo
 * @date 02/03/2015 23:38
 *
 */
package org.systemexception.orientplayground.test;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.exception.TerritoriesException;
import org.systemexception.orientplayground.impl.OrientActionImpl;

import java.util.Iterator;

import static org.junit.Assert.assertTrue;

public class TestFullItalyTerritory {

	private static OrientActionImpl sut;
	private final static String dbName = "test_database_italy_territories", fileName = "src/test/resources/geonames_it.csv";

	@BeforeClass
	public static void setUp() throws CsvParserException, TerritoriesException {
		sut = new OrientActionImpl();
		sut.initialSetup(dbName);
		sut.addTerritories(fileName);
	}

	@AfterClass
	public static void tearDown() {
		sut.drop();
	}

	@Test
	public void verify_luino_has_parent_varese() throws CsvParserException, TerritoriesException {
		Vertex vertexLuino = sut.getVertexByNodeId("6540157");
		assertTrue(vertexLuino.getProperty("nodeId").equals("6540157"));
		Iterator<Edge> edgeIterator = vertexLuino.getEdges(Direction.IN, "reportsTo").iterator();
		assertTrue(edgeIterator.hasNext());
		while (edgeIterator.hasNext()) {
			assertTrue(edgeIterator.next().getVertex(Direction.OUT).getProperty("nodeId").equals("3164697"));
		}
	}

	@Test
	public void verify_varese_has_parent_lombardia() throws CsvParserException, TerritoriesException {
		Vertex vertexVarese = sut.getVertexByNodeId("3164697");
		assertTrue(vertexVarese.getProperty("nodeId").equals("3164697"));
		Iterator<Edge> edgeIterator = vertexVarese.getEdges(Direction.IN, "reportsTo").iterator();
		assertTrue(edgeIterator.hasNext());
		while (edgeIterator.hasNext()) {
			assertTrue(edgeIterator.next().getVertex(Direction.OUT).getProperty("nodeId").equals("3174618"));
		}
	}
}
