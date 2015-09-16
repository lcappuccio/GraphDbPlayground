/**
 * @author leo
 * @date 16/09/15 15:53
 */
package org.systemexception.orientplayground.impl;

import com.tinkerpop.blueprints.Vertex;
import org.systemexception.orientplayground.api.DatabaseApi;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.exception.TerritoriesException;

import java.util.List;

public class DatabaseImplNeo implements DatabaseApi {

	@Override
	public void initialSetup(String dbName, String storageType) {

	}

	@Override
	public void addTerritories(String fileName) throws CsvParserException, TerritoriesException {

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
}
