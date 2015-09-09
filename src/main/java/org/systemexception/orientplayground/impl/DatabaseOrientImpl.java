/**
 * @author leo
 * @date 01/03/2015 19:17
 */
package org.systemexception.orientplayground.impl;

import com.orientechnologies.common.util.OCallable;
import com.orientechnologies.orient.core.command.OCommandOutputListener;
import com.orientechnologies.orient.core.db.tool.ODatabaseExport;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import org.apache.commons.csv.CSVRecord;
import org.systemexception.logger.api.Logger;
import org.systemexception.logger.impl.LoggerImpl;
import org.systemexception.orientplayground.api.DatabaseApi;
import org.systemexception.orientplayground.enums.CsvHeaders;
import org.systemexception.orientplayground.enums.ErrorCodes;
import org.systemexception.orientplayground.enums.OrientConfiguration;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.exception.TerritoriesException;
import org.systemexception.orientplayground.pojo.CsvParser;
import org.systemexception.orientplayground.pojo.Territories;
import org.systemexception.orientplayground.pojo.Territory;
import org.systemexception.orientplayground.pojo.Timer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatabaseOrientImpl implements DatabaseApi {

	private static final Logger logger = LoggerImpl.getFor(DatabaseOrientImpl.class);
	private OrientGraphNoTx graph;
	private Territories territories;
	private Index<Vertex> index;
	private final Timer timer = new Timer();

	@Override
	public void initialSetup(String dbName, String dbStorageType) {
		String dbPath = System.getProperty("user.dir") + File.separator + dbName;
		File dbFolder = new File(dbPath);
		deleteFolder(dbFolder);
		OrientGraphFactory orientGraphFactory = new OrientGraphFactory(dbStorageType + ":" + dbPath, "admin", "admin");
		graph = orientGraphFactory.getNoTx();
		graph.executeOutsideTx(new OCallable<Object, OrientBaseGraph>() {
			@Override
			public Object call(OrientBaseGraph arg0) {
				index = graph.createIndex(OrientConfiguration.VERTEX_INDEX.toString(), Vertex.class);
				return null;
			}
		});
	}

	@Override
	public void addTerritories(String fileName) throws CsvParserException, TerritoriesException {
		readCsvTerritories(fileName);
		timer.start();
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
	 *
	 * @param exportFileName
	 */
	@Override
	public void exportDatabase(String exportFileName) {
		try {
			OCommandOutputListener listener = new OCommandOutputListener() {
				@Override
				public void onMessage(String iText) {
					System.out.print(iText);
				}
			};
			ODatabaseExport export = new ODatabaseExport(graph.getRawGraph(), exportFileName, listener);
			export.exportDatabase();
			export.close();
		} catch (IOException e) {
			logger.error("Export database error", e);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param backupFileName
	 */
	@Override
	public void backupDatabase(String backupFileName) {
		try {
			if (graph.getRawGraph().getStorage().getType().equals(OrientConfiguration.DB_STORAGE_MEMORY.toString())) {
				logger.info("Operation not supported");
				return;
			}
			OCommandOutputListener listener = new OCommandOutputListener() {
				@Override
				public void onMessage(String iText) {
					System.out.print(iText);
				}
			};
			OutputStream out = new FileOutputStream(backupFileName);
			graph.getRawGraph().backup(out, null, null, listener, 9, 2048);
		} catch (IOException e) {
			logger.error("Backup database error", e);
		}
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
