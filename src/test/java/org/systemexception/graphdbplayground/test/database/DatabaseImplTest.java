package org.systemexception.graphdbplayground.test.database;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.junit.After;
import org.junit.Test;
import org.systemexception.graphdbplayground.api.DatabaseApi;
import org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration;
import org.systemexception.graphdbplayground.exception.CsvParserException;
import org.systemexception.graphdbplayground.exception.TerritoriesException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration.NODE_ID;
import static org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration.REPORTS_TO;
import static org.systemexception.graphdbplayground.test.TestCsvParser.*;

/**
 * @author leo
 * @date 13/09/16 20:31
 */
abstract class DatabaseImplTest {

	DatabaseApi sut;
	File backupFile, exportFile;
	String dbStorageType, dbName, exportFileName, backupFileName;

	@After
	public void tearDown() throws IOException {
		sut.drop();
	}

	@Test
	public void verify_luino_has_parent_varese() {
		assertTrue(verifyNodeHasParent(NODE_LUINO_NODEID, NODE_VARESE_NODEID));
	}

	@Test
	public void verify_varese_has_parent_lombardia() {
		assertTrue(verifyNodeHasParent(NODE_VARESE_NODEID, NODE_LOMBARDIA_NODEID));
	}

	@Test
	public void verify_varese_has_childs() throws TerritoriesException, CsvParserException, URISyntaxException {
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
	public void verify_luino_has_no_childs() throws TerritoriesException, CsvParserException, URISyntaxException {
		Vertex vertexLuino = sut.getVertexByNodeId(NODE_LUINO_NODEID);
		List<Vertex> childNodesOfLuino = sut.getChildNodesOf(vertexLuino.getProperty(GraphDatabaseConfiguration.NODE_ID
				.toString())
				.toString());
		assertTrue(0 == childNodesOfLuino.size());
	}

	private boolean verifyNodeHasParent(final String nodeId, final String parentNodeId) {
		Vertex vertexVarese = sut.getVertexByNodeId(nodeId);
		assertTrue(vertexVarese.getProperty(NODE_ID.toString()).equals(nodeId));
		Iterator<Edge> edgeIterator = vertexVarese.getEdges(Direction.IN, REPORTS_TO.toString()).iterator();
		assertTrue(edgeIterator.hasNext());
		return (edgeIterator.next().getVertex(Direction.OUT).getProperty(NODE_ID.toString()).equals(parentNodeId));
	}
}
