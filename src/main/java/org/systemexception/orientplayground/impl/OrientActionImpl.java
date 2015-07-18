/**
 * @author leo
 * @date 01/03/2015 19:17
 */
package org.systemexception.orientplayground.impl;

import com.orientechnologies.common.util.OCallable;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import org.apache.commons.csv.CSVRecord;
import org.systemexception.logger.api.Logger;
import org.systemexception.logger.impl.LoggerImpl;
import org.systemexception.orientplayground.api.Action;
import org.systemexception.orientplayground.enums.CsvHeaders;
import org.systemexception.orientplayground.enums.OrientConfiguration;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.exception.TerritoriesException;
import org.systemexception.orientplayground.pojo.CsvParser;
import org.systemexception.orientplayground.pojo.Territories;
import org.systemexception.orientplayground.pojo.Territory;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class OrientActionImpl implements Action {

	private static final Logger logger = LoggerImpl.getFor(OrientActionImpl.class);
	private OrientGraph graph;
	private Territories territories;
	private Index<Vertex> index;

	@Override
	public void initialSetup(String dbName) {
		String dbPath = System.getProperty("user.dir") + File.separator + dbName;
		File dbFolder = new File(dbPath);
		deleteFolder(dbFolder);
		OrientGraphFactory orientGraphFactory = new OrientGraphFactory(OrientConfiguration.DB_STORAGE_DISK.toString
				() + dbPath, "admin", "admin");
		graph = orientGraphFactory.getTx();
		graph.executeOutsideTx(new OCallable<Object, OrientBaseGraph>() {
			@Override
			public Object call(OrientBaseGraph arg0) {
				index = graph.createIndex(OrientConfiguration.VERTEX_INDEX.toString(), Vertex.class);
				return null;
			}
		});
	}

	public OrientGraph getGraph() {
		return graph;
	}

	@Override
	public void addTerritories(String fileName) throws CsvParserException, TerritoriesException {
		readCsvTerritories(fileName);
		// Create all nodes
		for (Territory territory : territories.getTerritories()) {
			Vertex territoryVertex = graph.addVertex(OrientConfiguration.VERTEX_TERRITORY_CLASS.toString());
			territoryVertex.setProperty(OrientConfiguration.NODE_ID.toString(), territory.getNodeId());
			territoryVertex.setProperty(OrientConfiguration.NODE_DESC.toString(), territory.getNodeDescr());
			territoryVertex.setProperty(OrientConfiguration.NODE_TYPE.toString(), territory.getNodeType());
			index.put(OrientConfiguration.NODE_ID.toString(), territory.getNodeId(), territoryVertex);
			index.put(OrientConfiguration.NODE_DESC.toString(), territory.getNodeDescr(), territoryVertex);
			logger.info("Adding territory: " + territory.getNodeId() + ", " + territory.getNodeDescr());
		}
		// Add edges
		for (Territory territory : territories.getTerritories()) {
			String territoryNodeId = territory.getNodeId();
			String territoryParentNodeId = territory.getParentId();
			Vertex sourceVertex = getVertexByNodeId(territoryNodeId);
			Vertex destinationVertex = getVertexByNodeId(territoryParentNodeId);
			if (sourceVertex == null || destinationVertex == null) {
				logger.info("Node " + territoryNodeId + " has no parent");
			} else {
				Edge reportingEdge = graph.addEdge(null, destinationVertex, sourceVertex, OrientConfiguration
						.REPORTS_TO.toString());
				// add a property otherwise you'll get no edge, check orient docs
				reportingEdge.setProperty(OrientConfiguration.EDGE_TYPE.toString(), OrientConfiguration.REPORTS_TO
						.toString());
				reportingEdge.setProperty(OrientConfiguration.EDGE_SOURCE_NODE.toString(), territoryNodeId);
				reportingEdge.setProperty(OrientConfiguration.EDGE_DESTINATION_NODE.toString(), territoryParentNodeId);
				logger.info("Added edge from " + territoryNodeId + " to " + territoryParentNodeId);
			}
		}
	}

	/**
	 * Returns the vertex given the nodeId
	 *
	 * @param nodeId the node id to retrieve
	 * @return a vertex object
	 */
	public Vertex getVertexByNodeId(String nodeId) {
		Iterator<Vertex> vertexIterator = index.get(OrientConfiguration.NODE_ID.toString(), nodeId).iterator();
		if (vertexIterator.hasNext()) {
			return vertexIterator.next();
		} else {
			return null;
		}
	}

	/**
	 * Reads the territories file and creates the necessary structure
	 *
	 * @param fileName the file containing the structure
	 * @throws CsvParserException
	 * @throws TerritoriesException
	 */
	private void readCsvTerritories(String fileName) throws CsvParserException, TerritoriesException {
		CsvParser csvParser = new CsvParser(fileName);
		List<CSVRecord> csvRecords = csvParser.readCsvContents();
		territories = new Territories();
		for (CSVRecord csvRecord : csvRecords) {
			String parentId = csvRecord.get(CsvHeaders.PARENT_ID.toString());
			String nodeId = csvRecord.get(CsvHeaders.NODE_ID.toString());
			String description = csvRecord.get(CsvHeaders.DESCRIPTION.toString());
			String nodeType = csvRecord.get(CsvHeaders.TYPE.toString());
			Territory territory = new Territory(parentId, nodeId, description, nodeType);
			territories.addTerritory(territory);
		}
	}

	/**
	 * Removes folder, it needs to be empty before deletion
	 *
	 * @param dbFolder the folder to remove
	 */
	private void deleteFolder(File dbFolder) {
		File[] files = dbFolder.listFiles();
		if (files != null) { //some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		dbFolder.delete();
	}

	@Override
	public void drop() {
		graph.drop();
	}
}
