/**
 *
 * @author leo
 * @date 01/03/2015 19:17
 *
 */
package org.systemexception.orientplayground.impl;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import java.io.File;
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

	private static final Logger log = Logger.getLogger(OrientActionImpl.class.getCanonicalName());
	private String dbPath;
	private CsvParser csvParser;
	private OrientGraphFactory orientGraphFactory;
	private OrientGraphNoTx orientGraph;
	private Territories territories;

	@Override
	public void initialSetup(String dbName) {
		dbPath = "target/" + dbName;
		File dbFolder = new File(dbPath);
		deleteFolder(dbFolder);
		orientGraphFactory = new OrientGraphFactory("plocal:" + dbPath, "admin", "admin");
		orientGraph = orientGraphFactory.getNoTx();
	}

	@Override
	public void addTerritories(String fileName) throws CsvParserException, TerritoriesException {
		readCsvTerritories(fileName);
		// Create all nodes
		for (Territory territory : territories.getTerritories()) {
			Vertex territoryVertex = orientGraph.addVertex(null);
			territoryVertex.setProperty("nodeId", territory.getNodeId());
			territoryVertex.setProperty("nodeDesc", territory.getNodeDescr());
			territoryVertex.setProperty("nodeType", territory.getNodeType());
			log.log(Level.INFO, "Adding territory: {0}, {1}", new Object[]{territory.getNodeId(), territory.getNodeDescr()});
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
