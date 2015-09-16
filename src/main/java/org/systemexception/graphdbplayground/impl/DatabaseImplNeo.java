/**
 * @author leo
 * @date 16/09/15 15:53
 */
package org.systemexception.graphdbplayground.impl;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import org.apache.commons.csv.CSVRecord;
import org.neo4j.index.impl.lucene.LowerCaseKeywordAnalyzer;
import org.systemexception.graphdbplayground.api.DatabaseApi;
import org.systemexception.graphdbplayground.enums.CsvHeaders;
import org.systemexception.graphdbplayground.enums.ErrorCodes;
import org.systemexception.graphdbplayground.enums.OrientConfiguration;
import org.systemexception.graphdbplayground.exception.CsvParserException;
import org.systemexception.graphdbplayground.exception.TerritoriesException;
import org.systemexception.graphdbplayground.pojo.CsvParser;
import org.systemexception.graphdbplayground.pojo.Territories;
import org.systemexception.graphdbplayground.pojo.Territory;
import org.systemexception.graphdbplayground.pojo.Timer;
import org.systemexception.logger.api.Logger;
import org.systemexception.logger.impl.LoggerImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatabaseImplNeo implements DatabaseApi {

	private static final Logger logger = LoggerImpl.getFor(DatabaseImplNeo.class);
	private Neo4jGraph graph;
	private Territories territories;
	private Index<Vertex> index;
	private final Timer timer = new Timer();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialSetup(String dbName, String storageType) {
		graph = new Neo4jGraph(dbName);
		index = graph.createIndex(OrientConfiguration.VERTEX_INDEX.toString(), Vertex.class, new Parameter("analyzer",
				LowerCaseKeywordAnalyzer.class.getName()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTerritories(String fileName) throws CsvParserException, TerritoriesException {
		readCsvTerritories(fileName);
		timer.start();
		// Create nodes
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
		timer.end();
		logger.info("Database loaded: " + timer.durantionInSeconds() + " seconds");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vertex getVertexByNodeId(String nodeId) {
		Iterator<Vertex> vertexIterator = index.get(OrientConfiguration.NODE_ID.toString(), nodeId).iterator();
		if (vertexIterator.hasNext()) {
			return vertexIterator.next();
		} else {
			logger.info(ErrorCodes.NODE_DOES_NOT_EXIST.toString());
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Vertex> getChildNodesOf(String nodeId) {
		List<Vertex> childNodes = new ArrayList<>();
		Vertex parentNode = getVertexByNodeId(nodeId);
		for (Edge edge : parentNode.getEdges(Direction.OUT)) {
			childNodes.add(edge.getVertex(Direction.IN));
		}
		return childNodes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vertex getParentNodeOf(String nodeId) {
		Vertex node = getVertexByNodeId(nodeId);
		String parentNode = node.getEdges(Direction.IN).iterator().next().getVertex(Direction.OUT).getProperty
				(OrientConfiguration.NODE_ID.toString());
		return getVertexByNodeId(parentNode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportDatabase(String exportFileName) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void backupDatabase(String backupFileName) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drop() {
		graph.shutdown();
	}

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
}
