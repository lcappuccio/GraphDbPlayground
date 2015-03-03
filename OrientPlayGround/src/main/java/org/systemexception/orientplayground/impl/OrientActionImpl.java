/**
 *
 * @author leo
 * @date 01/03/2015 19:17
 *
 */
package org.systemexception.orientplayground.impl;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.IndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVRecord;
import org.systemexception.orientplayground.api.Action;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.exception.TerritoriesException;
import org.systemexception.orientplayground.pojo.CsvParser;
import org.systemexception.orientplayground.pojo.Territories;
import org.systemexception.orientplayground.pojo.Territory;

public class OrientActionImpl implements Action {

	private static final Logger logger = Logger.getLogger(OrientActionImpl.class.getCanonicalName());
	private String dbPath;
	private CsvParser csvParser;
	private OrientGraphFactory orientGraphFactory;
	private IndexableGraph graph;
	private Territories territories;
	Index<Vertex> index;

	@Override
	public void initialSetup(String dbName) {
		dbPath = "target/" + dbName;
		File dbFolder = new File(dbPath);
		deleteFolder(dbFolder);
		orientGraphFactory = new OrientGraphFactory("plocal:" + dbPath, "admin", "admin");
		graph = orientGraphFactory.getNoTx();
		index = graph.createIndex("vertexIndex", Vertex.class);
	}

	@Override
	public void addTerritories(String fileName) throws CsvParserException, TerritoriesException {
		readCsvTerritories(fileName);
		// Create all nodes
		for (Territory territory : territories.getTerritories()) {
			// TODO investigate classes and their attributes
			Vertex territoryVertex = graph.addVertex("class:Territory");
			territoryVertex.setProperty("nodeId", territory.getNodeId());
			territoryVertex.setProperty("nodeDesc", territory.getNodeDescr());
			territoryVertex.setProperty("nodeType", territory.getNodeType());
			index.put("nodeId", territory.getNodeId(), territoryVertex);
			index.put("nodeDesc", territory.getNodeDescr(), territoryVertex);
			logger.log(Level.INFO, "Adding territory: {0}, {1}", new Object[]{territory.getNodeId(), territory.getNodeDescr()});
		}
		// Add edges
		for (Territory territory : territories.getTerritories()) {
			String territoryNodeId = territory.getNodeId();
			String territotyParentNodeId = territory.getParentId();
			Vertex sourceVertex = getVertexByNodeId(territoryNodeId);
			Vertex destinationVertex = getVertexByNodeId(territotyParentNodeId);
			if (sourceVertex == null || destinationVertex == null) {
				logger.log(Level.INFO, "Node {0} has no parent", territoryNodeId);
			} else {
				// TODO investigate edge classes and attributes
				Edge reportingEdge = graph.addEdge(null, destinationVertex, sourceVertex, "reportsTo");
				// add a property otherwise you'll get no edge, check orient docs
				reportingEdge.setProperty("type", "reportsTo");
				reportingEdge.setProperty("sourceNode", territoryNodeId);
				reportingEdge.setProperty("destinationNode", territotyParentNodeId);
				logger.log(Level.INFO, "Added edge from {0} to {1}", new Object[]{territoryNodeId, territotyParentNodeId});
			}
		}
	}

	/**
	 * Returns the vertex given the nodeId
	 *
	 * @param nodeId
	 * @return
	 */
	public Vertex getVertexByNodeId(String nodeId) {
		// TODO Should throw exception if more than one item, otherwise implement unique keys
		Iterator<Vertex> vertexIterator = index.get("nodeId", nodeId).iterator();
		if (vertexIterator.hasNext()) {
			return vertexIterator.next();
		} else {
			return null;
		}
	}

	/**
	 * Reads the territories file and creates the necessary structure
	 *
	 * @param fileName
	 * @throws CsvParserException
	 * @throws TerritoriesException
	 */
	private void readCsvTerritories(String fileName) throws CsvParserException, TerritoriesException {
		csvParser = new CsvParser(fileName);
		List<CSVRecord> csvRecords = csvParser.readCsvContents();
		territories = new Territories();
		for (CSVRecord csvRecord : csvRecords) {
			String parentId = csvRecord.get("PARENT_ID");
			String nodeId = csvRecord.get("NODE_ID");
			String description = csvRecord.get("DESCRIPTION");
			String nodeType = csvRecord.get("TYPE");
			Territory territory = new Territory(parentId, nodeId, description, nodeType);
			territories.addTerritory(territory);
		}
	}

	/**
	 * Removes folder, it needs to be empty before deletion
	 *
	 * @param dbFolder
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
}
