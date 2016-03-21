/**
 * @author leo
 * @date 01/03/2015 19:12
 */
package org.systemexception.graphdbplayground.api;

import com.tinkerpop.blueprints.Vertex;

import org.systemexception.graphdbplayground.exception.CsvParserException;
import org.systemexception.graphdbplayground.exception.TerritoriesException;

import java.io.IOException;
import java.util.List;

public interface DatabaseApi {

	/**
	 * Sets up the database on the specified folder
	 *
	 * @param dbName the database name
	 */
	void initialSetup(String dbName, String storageType);

	/**
	 * Reads all lines from a csv file and creates all nodes
	 *
	 * @param fileName the csv file containing the structure
	 * @throws org.systemexception.graphdbplayground.exception.CsvParserException
	 * @throws org.systemexception.graphdbplayground.exception.TerritoriesException
	 */
	void addTerritories(String fileName) throws CsvParserException, TerritoriesException;

	/**
	 * Returns the vertex given the nodeId
	 *
	 * @param nodeId the node id to retrieve
	 * @return a vertex object
	 */
	Vertex getVertexByNodeId(String nodeId);

	/**
	 * Returns all child nodes belonging to a node
	 *
	 * @param nodeId the parent node
	 * @return a vertex list with all child nodes
	 */
	List<Vertex> getChildNodesOf(String nodeId);

	/**
	 * Returns the parent node of a node
	 *
	 * @param nodeId the child node
	 * @return the parent vertex
	 */
	Vertex getParentNodeOf(String nodeId);

	/**
	 * Exports the database
	 * WARNING: Export doesn't lock your database, but browses it. This means that concurrent operation can be
	 * executed during the export
	 *
	 * @param exportFileName the file name of the export
	 */
	void exportDatabase(String exportFileName) throws IOException;

	/**
	 * Creates a database snapshot
	 *
	 * @param backupFileName the file name of the backup
	 */
	void backupDatabase(String backupFileName) throws IOException;

	/**
	 * Drops the database
	 */
	void drop() throws IOException;
}
