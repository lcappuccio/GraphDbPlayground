/**
 * @author leo
 * @date 16/09/15 15:53
 */
package org.systemexception.graphdbplayground.impl;

import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import org.apache.commons.csv.CSVRecord;
import org.neo4j.index.impl.lucene.LowerCaseKeywordAnalyzer;
import org.systemexception.logger.api.Logger;
import org.systemexception.logger.impl.LoggerImpl;
import org.systemexception.graphdbplayground.api.DatabaseApi;
import org.systemexception.graphdbplayground.enums.CsvHeaders;
import org.systemexception.graphdbplayground.enums.OrientConfiguration;
import org.systemexception.graphdbplayground.exception.CsvParserException;
import org.systemexception.graphdbplayground.exception.TerritoriesException;
import org.systemexception.graphdbplayground.pojo.CsvParser;
import org.systemexception.graphdbplayground.pojo.Territories;
import org.systemexception.graphdbplayground.pojo.Territory;
import org.systemexception.graphdbplayground.pojo.Timer;

import java.util.List;

public class DatabaseImplNeo implements DatabaseApi {

	private static final Logger logger = LoggerImpl.getFor(DatabaseImplNeo.class);
	private Neo4jGraph graph;
	private Territories territories;
	private Index<Vertex> index;
	private final Timer timer = new Timer();

	@Override
	public void initialSetup(String dbName, String storageType) {
		graph = new Neo4jGraph(dbName);
		index = graph.createIndex(OrientConfiguration.VERTEX_INDEX.toString(), Vertex.class, new Parameter("analyzer",
				LowerCaseKeywordAnalyzer.class.getName()));
	}

	@Override
	public void addTerritories(String fileName) throws CsvParserException, TerritoriesException {
		readCsvTerritories(fileName);
		timer.start();
		for (Territory territory : territories.getTerritories()) {

		}
	}

	@Override
	public Vertex getVertexByNodeId(String nodeId) {
		return null;
	}

	@Override
	public List<Vertex> getChildNodesOf(String nodeId) {
		return null;
	}

	@Override
	public Vertex getParentNodeOf(String nodeId) {
		return null;
	}

	@Override
	public void exportDatabase(String exportFileName) {

	}

	@Override
	public void backupDatabase(String backupFileName) {

	}

	@Override
	public void drop() {

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
