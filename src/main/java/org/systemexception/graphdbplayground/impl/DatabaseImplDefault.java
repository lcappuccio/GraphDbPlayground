/**
 * @author leo
 * @date 17/09/15 18:17
 */
package org.systemexception.graphdbplayground.impl;

import com.tinkerpop.blueprints.*;
import org.apache.commons.csv.CSVRecord;
import org.systemexception.graphdbplayground.api.DatabaseApi;
import org.systemexception.graphdbplayground.enums.CsvHeaders;
import org.systemexception.graphdbplayground.enums.ErrorCodes;
import org.systemexception.graphdbplayground.enums.GraphDatabaseConfiguration;
import org.systemexception.graphdbplayground.exception.CsvParserException;
import org.systemexception.graphdbplayground.exception.TerritoriesException;
import org.systemexception.graphdbplayground.util.CsvParser;
import org.systemexception.graphdbplayground.model.Territories;
import org.systemexception.graphdbplayground.model.Territory;
import org.systemexception.graphdbplayground.util.Timer;
import org.systemexception.logger.api.Logger;
import org.systemexception.logger.impl.LoggerImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class DatabaseImplDefault implements DatabaseApi {

	protected static final Logger logger = LoggerImpl.getFor(DatabaseImplOrient.class);
	protected Graph graph;
	private Territories territories;
	protected Index<Vertex> index;
	private final Timer timer = new Timer();
	private final String LOG_SEPARATOR = ", ";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTerritories(String fileName) throws CsvParserException, TerritoriesException {
		readCsvTerritories(fileName);
		timer.start();
		// Create all nodes
		for (Territory territory : territories.getTerritories()) {
			Vertex territoryVertex = graph.addVertex(GraphDatabaseConfiguration.VERTEX_TERRITORY_CLASS.toString());
			territoryVertex.setProperty(GraphDatabaseConfiguration.NODE_ID.toString(), territory.getNodeId());
			territoryVertex.setProperty(GraphDatabaseConfiguration.NODE_DESC.toString(), territory.getNodeDescr());
			territoryVertex.setProperty(GraphDatabaseConfiguration.NODE_TYPE.toString(), territory.getNodeType());
			index.put(GraphDatabaseConfiguration.NODE_ID.toString(), territory.getNodeId(), territoryVertex);
			index.put(GraphDatabaseConfiguration.NODE_DESC.toString(), territory.getNodeDescr(), territoryVertex);
			logger.info("Adding territory: " + territory.getNodeId() + LOG_SEPARATOR + territory.getNodeDescr());
		}
		// Add edges
		for (Territory territory : territories.getTerritories()) {
			String territoryNodeId = territory.getNodeId();
			String territoryParentNodeId = territory.getParentId();
			Vertex sourceVertex = getVertexByNodeId(territoryNodeId);
			Vertex destinationVertex = getVertexByNodeId(territoryParentNodeId);
			if (sourceVertex == null || destinationVertex == null) {
				logger.info(territoryNodeId + LOG_SEPARATOR + ErrorCodes.NODE_HAS_NO_PARENT);
			} else {
				Edge reportingEdge = graph.addEdge(null, destinationVertex, sourceVertex, GraphDatabaseConfiguration
						.REPORTS_TO.toString());
				// add a property otherwise you'll get no edge, check orient docs
				reportingEdge.setProperty(GraphDatabaseConfiguration.EDGE_TYPE.toString(), GraphDatabaseConfiguration
						.REPORTS_TO
						.toString());
				reportingEdge.setProperty(GraphDatabaseConfiguration.EDGE_SOURCE_NODE.toString(), territoryNodeId);
				reportingEdge.setProperty(GraphDatabaseConfiguration.EDGE_DESTINATION_NODE.toString(),
						territoryParentNodeId);
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
		Iterator<Vertex> vertexIterator = index.get(GraphDatabaseConfiguration.NODE_ID.toString(), nodeId).iterator();
		if (vertexIterator.hasNext()) {
			return vertexIterator.next();
		} else {
			logger.info(nodeId + LOG_SEPARATOR + ErrorCodes.NODE_DOES_NOT_EXIST.toString());
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
		if (childNodes.size() == 0) {
			logger.info(nodeId + LOG_SEPARATOR + ErrorCodes.NODE_HAS_NO_CHILDS);
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
				(GraphDatabaseConfiguration.NODE_ID.toString());
		return getVertexByNodeId(parentNode);
	}

	private void readCsvTerritories(String fileName) throws CsvParserException, TerritoriesException {
		logger.info("Start loading territories file");
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
		logger.info("Finished loading territories file");
	}
}
