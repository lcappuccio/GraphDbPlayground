/**
 *
 * @author leo
 * @date 01/03/2015 19:08
 *
 */
package org.systemexception.orientplayground.test;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.Iterator;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.exception.TerritoriesException;
import org.systemexception.orientplayground.impl.OrientActionImpl;

public class TestItalyTerritory {

	private static OrientActionImpl sut;
	private final static String dbName = "test_database_lombardia_territories", fileName = "src/test/resources/test_territories.csv";
	
	@BeforeClass
	public static void setUp() throws CsvParserException, TerritoriesException {
		sut = new OrientActionImpl();
		sut.initialSetup(dbName);
		sut.addTerritories(fileName);
	}
	
	@AfterClass
	public static void tearDown() {
		
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
