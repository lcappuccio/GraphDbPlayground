/**
 *
 * @author leo
 * @date 01/03/2015 19:12
 *
 */
package org.systemexception.orientplayground.api;

import com.tinkerpop.blueprints.Vertex;
import org.systemexception.orientplayground.exception.CsvParserException;
import org.systemexception.orientplayground.exception.TerritoriesException;

import java.util.List;

public interface DatabaseApi {

	/**
	 * Sets up the database on the specified folder
	 *
	 * @param dbName the database name
	 */
	void initialSetup(String dbName);

	/**
	 * Reads all lines from a csv file and creates all nodes
	 *
	 * @param fileName the csv file containing the structure
	 * @throws org.systemexception.orientplayground.exception.CsvParserException
	 * @throws org.systemexception.orientplayground.exception.TerritoriesException
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
	 * @param nodeId
	 * @return
	 */
	List<Vertex> getChildNodesOf(String nodeId);

	/**
	 * Returns the parent node of a node
	 *
	 * @param nodeId
	 * @return
	 */
	Vertex getParentNodeOf(String nodeId);

	/**
	 * Drops the database
	 */
	void drop();

}
